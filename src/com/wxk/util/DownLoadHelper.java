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
	 * ������ϢURL
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
	 * �ӷ����������°�apk�ļ�
	 * @param apkUrl �ļ���ַ
	 * @param pd ������
	 * @return
	 * @throws Exception
	 */
	public static File getFileFromServer(String apkUrl, ProgressDialog pd) throws Exception{     
	    //�����ȵĻ���ʾ��ǰ��sdcard�������ֻ��ϲ����ǿ��õ�      
	    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){     
	        URL url = new URL(apkUrl);
	        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();     
	        conn.setConnectTimeout(5000);     
	        //��ȡ���ļ��Ĵ�С       
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
	            //��ȡ��ǰ������      
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
