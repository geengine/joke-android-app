package com.wxk.jokeandroidapp.services;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import com.wxk.jokeandroidapp.bean.ReplyBean;
import com.wxk.jokeandroidapp.bean.core.ReplyBeanHandler;
import com.wxk.jokeandroidapp.client.ReplyClient;
import com.wxk.jokeandroidapp.client.ResponseData;
import com.wxk.jokeandroidapp.db.ReplyDb;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReplyService extends IntentService {

	public static final String GET_REPLY_DATA_INTENT = "52lxh:get_reply_data_intent";
	public static final String REFRESH_REPLY_UI_INTENT = "52lxh:refresh_reply_ui_intent";
	public static final String EXTRA_CACHED = "52lxh:extra_cached";
	public static final String EXTRA_APPEND = "52lxh:extra_append";
	public static final String EXTRA_REFRESH = "52lxh:extra_refresh";
	public static final String EXTRA_ERROR = "52lxh:extra_error";
	public static final String ARG_JOKE_ID = "52lxh:joke_id";
	public static final String ARG_PAGE = "52lxh:reply_page";
	public static final String ARG_SIZE = "52lxh:reply_size";
	public static final String ARG_REPLY_COUNT_ADD = "52lxh:replay_count_add";

	private static final String TAG = "52lxh:ReplyService";
	private static ReplyDb db;
	static {
		db = new ReplyDb();
	}

	public ReplyService() {
		super("52lxh:reply_service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "::onHandleIntent()");
		if (intent.getAction().equals(GET_REPLY_DATA_INTENT)) {
			boolean isAppend = intent.getBooleanExtra(EXTRA_APPEND, false);
			boolean isRefresh = intent.getBooleanExtra(EXTRA_REFRESH, false);
			long jokeId = intent.getLongExtra(ARG_JOKE_ID, 0);
			int page = intent.getIntExtra(ARG_PAGE, 1);
			int size = intent.getIntExtra(ARG_SIZE, 50);
			int countAdd = intent.getIntExtra(ARG_REPLY_COUNT_ADD, 0);
			List<ReplyBean> replyItems = new ArrayList<ReplyBean>();
			List<ReplyBean> cachedItems = loadReplyFromCache(this, jokeId,
					page, size);
			if (!isRefresh) {
				if (cachedItems != null) {
					final Intent refreshIntent = new Intent(
							REFRESH_REPLY_UI_INTENT + jokeId);
					refreshIntent.putExtra(ARG_JOKE_ID, jokeId);
					refreshIntent.putExtra(ARG_REPLY_COUNT_ADD, countAdd);
					refreshIntent.putExtra(ARG_PAGE, page);
					refreshIntent.putExtra(EXTRA_CACHED, true);
					refreshIntent.putExtra(EXTRA_APPEND, isAppend);
					sendBroadcast(refreshIntent);
				}
			}

			if (updateFromServer(replyItems, jokeId, page, size)) {
				final Intent refreshIntent = new Intent(REFRESH_REPLY_UI_INTENT
						+ jokeId);
				refreshIntent.putExtra(ARG_JOKE_ID, jokeId);
				refreshIntent.putExtra(ARG_REPLY_COUNT_ADD, countAdd);
				refreshIntent.putExtra(ARG_PAGE, page);
				refreshIntent.putExtra(EXTRA_CACHED, false);
				refreshIntent.putExtra(EXTRA_APPEND, isAppend);
				sendBroadcast(refreshIntent);
			} else {
				final Intent refreshIntent = new Intent(REFRESH_REPLY_UI_INTENT
						+ jokeId);
				refreshIntent.putExtra(ARG_JOKE_ID, jokeId);
				refreshIntent.putExtra(ARG_REPLY_COUNT_ADD, countAdd);
				refreshIntent.putExtra(ARG_PAGE, page);
				refreshIntent.putExtra(EXTRA_CACHED, true);
				refreshIntent.putExtra(EXTRA_APPEND, isAppend);
				sendBroadcast(refreshIntent);
			}
		}
	}

	public static List<ReplyBean> loadReplyFromCache(final Context context,
			final long jokeId, final int page, final int size) {
		Log.i(TAG, String.format(
				"::loadReplyFromCache()=> JokeID=%s,Page=%s,Size=%s", jokeId,
				page, size));
		return db.getList(jokeId, page, size);
	}

	public boolean updateFromServer(final List<ReplyBean> replyItems,
			final long jokeId, final int page, final int size) {
		Log.d(TAG, "::updateFromServer()");
		try {
			ReplyClient client = new ReplyClient();
			ResponseData responseData = client.getReplys(jokeId, page, size);
			if (responseData.getStatus()) {
				SAXParser parser = SAXParserFactory.newInstance()
						.newSAXParser();

				ReplyBeanHandler handler = new ReplyBeanHandler();
				InputSource is = new InputSource(new StringReader(
						responseData.toString()));
				parser.parse(is, handler);
				replyItems.addAll(handler.getReplyItems());

				// set jokeid
				for (ReplyBean item : replyItems) {
					item.setJokeID(jokeId);
				}
				/* sqlite cache */
				return db.addAll(replyItems);
			}
		} catch (Exception ex) {
			Log.e(TAG, "" + ex.getMessage());
			ex.printStackTrace();
		}
		return false;
	}
}
