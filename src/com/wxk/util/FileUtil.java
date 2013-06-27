package com.wxk.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class FileUtil {
	final static String TAG = "FileUtil";

	public static Boolean downLoadFile(String url, File cacheFile) {
		boolean retValue = false;
		final HttpClient httpclient = new DefaultHttpClient();
		try {
			if (!cacheFile.exists()) {
				final HttpGet httpget = new HttpGet(url);

				HttpResponse response = httpclient.execute(httpget);
				if (200 == response.getStatusLine().getStatusCode()) {
					HttpEntity entity = response.getEntity();
					if (entity != null && entity.isStreaming()) {
						byte[] respBytes = EntityUtils.toByteArray(entity);
						try {
							cacheFile.createNewFile();
							FileOutputStream fos = new FileOutputStream(
									cacheFile);
							fos.write(respBytes);
							fos.close();
							LogUtil.d(TAG,
									"Cache file save: " + cacheFile.getPath());
							retValue = true;
						} catch (IOException e) {
							Log.e(TAG, "Error writing to file cache: "
									+ cacheFile.toString(), e);
						}
						respBytes = null;
					}
					entity = null;
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
		return retValue;
	}
}
