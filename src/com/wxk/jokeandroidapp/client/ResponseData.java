package com.wxk.jokeandroidapp.client;

import java.io.Closeable;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class ResponseData implements Closeable {
	public final static String HTTP_ENCODED = "UTF-8";
	private Integer httpcode;
	private String responseString;

	public ResponseData() {
		this.setHttpcode(500);
	}

	public ResponseData(HttpResponse httpResponse) throws Exception {
		this.setHttpcode(httpResponse.getStatusLine().getStatusCode());
		this.setResponseString(EntityUtils.toString(httpResponse.getEntity(),
				HTTP_ENCODED));
	}

	public ResponseData(Integer httpcode, String responseString) {
		this.setHttpcode(httpcode);
		this.setResponseString(responseString);
	}

	public Integer getHttpcode() {
		return httpcode;
	}

	public void setHttpcode(Integer httpcode) {
		this.httpcode = httpcode;

	}

	public String getResponseString() {
		return responseString;
	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

	public Boolean getStatus() {
		if (this.httpcode == 200 && this.responseString != null
				&& !"".equals(this.responseString))
			return true;
		return false;
	}

	@Override
	public String toString() {
		return this.responseString;
	}

	@Override
	public void close() {
		this.responseString = null;
	}
}
