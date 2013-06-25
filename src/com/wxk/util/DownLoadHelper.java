package com.wxk.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.Environment;

public class DownLoadHelper {
	/**
	 * 下载信息URL
	 */
	private URL url = null;

	public String DownLoad(String urlStr) {
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL(urlStr);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			conn.setReadTimeout(5000);
			BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line="";
			while((line=reader.readLine())!=null){
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return sb.toString();
	}
	/**
	 * 从服务器下载新版apk文件
	 * @param apkUrl 文件地址
	 * @param pd 进度条
	 * @return
	 * @throws Exception
	 */
	public static File getFileFromServer(String apkUrl, ProgressDialog pd) throws Exception{     
	    //如果相等的话表示当前的sdcard挂载在手机上并且是可用的      
	    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){     
	        URL url = new URL(apkUrl);
	        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();     
	        conn.setConnectTimeout(5000);     
	        //获取到文件的大小       
	        pd.setMax(conn.getContentLength());     
	        InputStream is = conn.getInputStream();     
	        File file = new File(FilesHelper.GetPath("/52lxh/apk"), "MainActivityNew.apk");     
	        FileOutputStream fos = new FileOutputStream(file);     
	        BufferedInputStream bis = new BufferedInputStream(is);     
	        byte[] buffer = new byte[1024];     
	        int len ;     
	        int total=0;     
	        while((len =bis.read(buffer))!=-1){     
	            fos.write(buffer, 0, len);     
	            total+= len;     
	            //获取当前下载量      
	            pd.setProgress(total);     
	        }     
	        fos.close();     
	        bis.close();     
	        is.close();     
	        return file;     
	    }     
	    else{     
	        return null;     
	    }     
	}    
}
