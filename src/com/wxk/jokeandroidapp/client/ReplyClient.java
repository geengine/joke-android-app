package com.wxk.jokeandroidapp.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.wxk.jokeandroidapp.Constant;

public class ReplyClient extends BaseClient {
	final String REPLY_LIST = Constant.API_URL + "/getnewreplylist.aspx";
	final String ADD_REPLY = Constant.API_URL + "/doreply.aspx";

	public ResponseData getReplys(long jokeid, int page, int size)
			throws Exception {
		ResponseData responseData = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jokeid", "" + jokeid));
		params.add(new BasicNameValuePair("count", "" + size));
		// params.add(new BasicNameValuePair("current", "" + page));
		// params.add(new BasicNameValuePair("pagesize", "" + size));
		responseData = getUri(REPLY_LIST, params);
		return responseData;
	}

	public ResponseData doReply(long jokeid, String content) throws Exception {
		ResponseData responseData = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jokeid", "" + jokeid));
		params.add(new BasicNameValuePair("con", content));
		responseData = postUri(ADD_REPLY, params);
		return responseData;
	}

}
