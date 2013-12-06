package com.wxk.jokeandroidapp.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class BaseClient {

	protected static final String TAG = "52lxh:HTTP_Client";

	private final Boolean isCookie = false;

	private String token;

	public String getToken() {
		// TODO
		if (null == token || "".equals(token))
			token = "";
		return token;
	}

	protected CookieStore getCookieStore() {
		// TODO
		return null;
	}

	protected void updateCookieStore(CookieStore cookieStore) {
		// TODO
	}

	protected ResponseData getUri(String uriString) throws Exception {
		return getUri(uriString, null);
	}

	/**
	 * @param uriString
	 * @return
	 * @throws Exception
	 */
	protected ResponseData getUri(String uriString, List<NameValuePair> params)
			throws Exception {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		ResponseData result;
		try {
			StringBuilder sbParams = new StringBuilder();
			if (params != null && params.size() > 0) {
				for (NameValuePair nvp : params) {
					sbParams.append("&" + nvp.getName() + "=" + nvp.getValue());
				}
				String strParams = sbParams.toString();
				if (strParams != null && !"".equals(strParams)) {
					strParams = strParams.replaceFirst("&", "?");
					uriString += strParams;
				}
			}
			Log.i(TAG, "HTTP GET: " + uriString);
			HttpGet httpGet = new HttpGet(uriString);
			// set cookieStore
			if (getCookieStore() != null) {
				httpClient.setCookieStore(getCookieStore());
			}
			HttpResponse httpResponse = httpClient.execute(httpGet);
			// update cookieStore
			updateCookieStore(httpClient.getCookieStore());

			result = new ResponseData(httpResponse);

		} catch (Exception ex) {
			throw new NetWorkException(ex);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

	protected ResponseData putUri(String uriString) throws Exception {
		return this.putUri(uriString, new ArrayList<NameValuePair>());
	}

	private void addToken(List<NameValuePair> params) {
		if (params != null) {
			// set token
		}
	}

	protected ResponseData putUri(String uriString, List<NameValuePair> params)
			throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		ResponseData responseData;
		addToken(params);
		// new http entity and set encoded
		HttpEntity entity = new UrlEncodedFormEntity(params,
				ResponseData.HTTP_ENCODED);

		// new http put object
		HttpPut httpPost = new HttpPut(uriString);
		httpPost.setEntity(entity);
		// http client

		// set cookieStore
		if (isCookie) {
			if (getCookieStore() != null) {
				httpClient.setCookieStore(getCookieStore());
			}
		}
		try {
			Log.i(TAG, "HTTP PUT: " + uriString);
			// get http response object
			HttpResponse httpResponse = httpClient.execute(httpPost);
			// update cookieStore
			if (isCookie) {
				updateCookieStore(httpClient.getCookieStore());
			}
			responseData = new ResponseData(httpResponse);
		} catch (Exception ex) {
			throw new NetWorkException(ex);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		// http request successful
		if (responseData.getStatus()) {
			Log.i(TAG, "PUT DATA: " + logParams(params));
		} else {
			Log.w(TAG, "PUT DATA: " + logParams(params));
			Log.w(TAG, "HTTP CODE: " + responseData.getHttpcode());
		}
		return responseData;
	}

	private String logParams(List<NameValuePair> params) {
		if (params != null) {
			StringBuffer sb = new StringBuffer();
			for (NameValuePair nameValuePair : params) {
				sb.append(nameValuePair.getName() + " = "
						+ nameValuePair.getValue() + "\n");
			}
			return sb.toString();
		}
		return "";
	}

	/**
	 * @param uriString
	 * @return
	 * @throws Exception
	 */
	protected ResponseData postUri(String uriString) throws Exception {

		return this.postUri(uriString, new ArrayList<NameValuePair>());
	}

	protected ResponseData postUri(String uriString, List<NameValuePair> params)
			throws Exception {

		return this.postUri(uriString, params, null, null);
	}

	protected ResponseData postUri(String uriString, File enclosureFile,
			String enclosureName) throws Exception {

		return this.postUri(uriString, new ArrayList<NameValuePair>(),
				enclosureFile, enclosureName);
	}

	/**
	 * @param uriString
	 * @param params
	 * @return
	 * @throws Exception
	 */
	protected ResponseData postUri(String uriString,
			List<NameValuePair> params, File enclosureFile, String enclosureName)
			throws Exception {
		addToken(params);

		// new http post object
		HttpPost httpPost = new HttpPost(uriString);
		if (null == enclosureFile) {
			HttpEntity entity = new UrlEncodedFormEntity(params,
					ResponseData.HTTP_ENCODED);
			httpPost.setEntity(entity);
		} else {
			MultipartEntity entity = new MultipartEntity();

			entity.addPart(enclosureName, new FileBody(enclosureFile));

			if (null != params) {
				for (NameValuePair nv : params) {
					entity.addPart(nv.getName(), new StringBody(nv.getValue()));
				}
			}
			httpPost.setEntity(entity);
		}

		// http client

		DefaultHttpClient httpClient = new DefaultHttpClient();
		ResponseData responseData;
		// set cookieStore
		if (isCookie) {
			if (getCookieStore() != null) {
				httpClient.setCookieStore(getCookieStore());
			}
		}
		try {
			Log.i(TAG, "HTTP POST: " + uriString);
			// get http response object
			HttpResponse httpResponse = httpClient.execute(httpPost);
			// update cookieStore
			if (isCookie) {
				updateCookieStore(httpClient.getCookieStore());
			}
			responseData = new ResponseData(httpResponse);
		} catch (Exception ex) {
			throw new NetWorkException(ex);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		// http request successful
		if (responseData.getStatus()) {
			Log.i(TAG, "POST DATA: " + logParams(params));
		} else {
			Log.w(TAG, "POST DATA: " + logParams(params));
			Log.w(TAG, "HTTP CODE: " + responseData.getHttpcode());
		}
		return responseData;
	}
}
