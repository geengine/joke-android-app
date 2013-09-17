package com.wxk.jokeandroidapp.services;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.bean.core.JokeBeanHandler;
import com.wxk.jokeandroidapp.client.JokeClient;
import com.wxk.jokeandroidapp.client.ResponseData;
import com.wxk.jokeandroidapp.db.JokeDb;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class JokeService extends IntentService {
	public static final String GET_JOKE_DATA_INTENT = "52lxh:get_joke_data_intent";
	public static final String REFRESH_JOKE_UI_INTENT = "52lxh:refresh_joke_ui_intent";

	public static final String EXTRA_CACHED = "52lxh:joke_data_cached";
	public static final String EXTRA_SERVER_ERROR = "52lxh:joke_server_error";
	public static final String EXTRA_APPEND = "52lxh:joke_data_append";
	public static final String EXTRA_REFRESH = "52lxh:joke_data_refresh";
	public static final String EXTRA_NEW_DATA = "52lxh:joke_data_new";
	public static final String ARG_JOKE_TOPIC = "52lxh:joke_topic";
	public static final String ARG_JOKE_PAGE = "52lxh:joke_page";
	public static final String ARG_JOKE_SIZE = "52lxh:joke_size";

	private static final String TAG = "52lxh:JokeService";
	private static JokeDb db;
	static {
		db = new JokeDb();
	}

	public JokeService() {
		super("52lxh:joke_service");
		Log.d(TAG, "::JokeService()");

	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "::onBind()");
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "::onHandleIntent()");
		if (intent.getAction().equals(GET_JOKE_DATA_INTENT)) {
			Log.d(TAG, "=>:action=" + GET_JOKE_DATA_INTENT);
			boolean isAppend = intent.getBooleanExtra(EXTRA_APPEND, false);
			boolean isRefresh = intent.getBooleanExtra(EXTRA_REFRESH, false);
			int jokeTopic = intent.getIntExtra(ARG_JOKE_TOPIC, 0);
			int jokePage = intent.getIntExtra(ARG_JOKE_PAGE, 1);
			int jokeSize = intent.getIntExtra(ARG_JOKE_SIZE, 10);

			List<JokeBean> jokeItems = new ArrayList<JokeBean>();
			/* Send any cached feed first */
			List<JokeBean> cachedItems;
			if (!isRefresh) {
				cachedItems = loadJokeFromCache(this, jokeTopic, jokePage);
				if (cachedItems != null) {
					final Intent refreshIntent = new Intent(
							REFRESH_JOKE_UI_INTENT);
					refreshIntent.putExtra(EXTRA_CACHED, true);

					refreshIntent.putExtra(EXTRA_APPEND, isAppend);
					sendBroadcast(refreshIntent);
				}
			}
			if (updataFeedFromServer(jokeItems, jokePage, jokeTopic, jokeSize)) {
				final Intent refreshIntent = new Intent(REFRESH_JOKE_UI_INTENT);
				refreshIntent.putExtra(EXTRA_CACHED, false);
				refreshIntent.putExtra(EXTRA_NEW_DATA, true);
				refreshIntent.putExtra(EXTRA_REFRESH, isRefresh);
				refreshIntent.putExtra(EXTRA_APPEND, isAppend);
				sendBroadcast(refreshIntent);
			} else {
				final Intent refreshIntent = new Intent(REFRESH_JOKE_UI_INTENT);
				refreshIntent.putExtra(EXTRA_CACHED, false);
				refreshIntent.putExtra(EXTRA_NEW_DATA, false);
				refreshIntent.putExtra(EXTRA_APPEND, false);
				refreshIntent.putExtra(EXTRA_REFRESH, isRefresh);
				sendBroadcast(refreshIntent);
			}
		}
	}

	public static List<JokeBean> loadJokeFromCache(final Context context,
			final int topicId, final int page) {
		return db.getList(page, 10, topicId);
	}

	public boolean updataFeedFromServer(final List<JokeBean> jokeItems,
			final int page, final int topicId, final int size) {
		Log.d(TAG, "::updataFeedFromServer()");
		try {
			JokeClient client = new JokeClient();
			ResponseData responseData = client.getJokes(page, size, topicId);

			if (responseData.getStatus()) {
				SAXParser parser = SAXParserFactory.newInstance()
						.newSAXParser();
				JokeBeanHandler handler = new JokeBeanHandler();
				InputSource is = new InputSource(new StringReader(
						responseData.toString()));
				parser.parse(is, handler);

				jokeItems.addAll(handler.getJokeItems());

				/* Set topic id */
				for (JokeBean item : jokeItems) {
					item.setTopic(topicId);
				}

				/* Now write the joke to cache */
				return db.addAll(jokeItems);
			}
		} catch (Exception e) {
			Log.e(TAG, "" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
}
