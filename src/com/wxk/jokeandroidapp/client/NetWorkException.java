package com.wxk.jokeandroidapp.client;

public class NetWorkException extends Exception {

	final String TAG = "NetWrokException";
	private String msg;

	public NetWorkException(Exception ex) {
		msg = ex.getMessage();
	}

	public NetWorkException() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return msg;
	}

}
