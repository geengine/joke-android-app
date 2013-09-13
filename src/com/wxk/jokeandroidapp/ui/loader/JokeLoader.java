package com.wxk.jokeandroidapp.ui.loader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.bean.core.JokeBeanHandler;
import com.wxk.jokeandroidapp.client.JokeClient;
import com.wxk.jokeandroidapp.client.ResponseData;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class JokeLoader extends AsyncLoader<List<JokeBean>> {

	private static final String TAG = "52lxh:JokeLoader";
	private Activity mActivity;
	private int mPage = 1;
	private int mSize = 10;
	private int mTopicID;

	public JokeLoader(Activity context, int topicID) {
		super(context);
		mActivity = context;
		mTopicID = topicID;
	}

	@Override
	public List<JokeBean> loadInBackground() {
		Log.i(TAG, "::loadInBackground()");

		try {
			JokeClient client = new JokeClient();
			ResponseData responseData = client.getJokes(mPage, mSize, mTopicID);

			if (responseData.getStatus()) {
				SAXParser parser = SAXParserFactory.newInstance()
						.newSAXParser();
				JokeBeanHandler handler = new JokeBeanHandler();
				InputSource is = new InputSource(new StringReader(responseData
						.toString()));
				parser.parse(is, handler);

				return handler.getJokeItems();
			} else {
				Log.e(TAG, "No data.");
			}
		} catch (SAXException e) {
			Log.d(TAG, "failed to parse XML");
			Log.d(TAG, Log.getStackTraceString(e));
		} catch (Exception e) {
			final String msg = e.getMessage();
			Log.d(TAG, String.format("Error, %s!", msg));
			Log.d(TAG, Log.getStackTraceString(e));
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(mActivity, "Error, " + msg,
							Toast.LENGTH_SHORT).show();
				}

			});
		}
		/* If anything, return an empty list */
		return new ArrayList<JokeBean>();
	}

}
