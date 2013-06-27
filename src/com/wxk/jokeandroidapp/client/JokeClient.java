package com.wxk.jokeandroidapp.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.wxk.jokeandroidapp.Constant;

public class JokeClient extends BaseClient {
	// http://www.52lxh.com/appinterface/getnewdatalist.aspx?current="+ current + "&pagesize="
	// + pageSize;
	final String JOKE_LIST = Constant.BASE_URL + "getnewdatalist.aspx";

	public ResponseData getJokes(int page, int pageSize) throws Exception {
		ResponseData responseData = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("current", "" + page));
		params.add(new BasicNameValuePair("pagesize", "" + pageSize));
		responseData = getUri(JOKE_LIST, params);
		return responseData;
	}
}
