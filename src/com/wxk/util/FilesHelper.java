package com.wxk.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FilesHelper {
	private static String SDPATH = Environment.getExternalStorageDirectory()
			+ "";

	/**
	 * ��SD�����洴��Ŀ¼
	 * 
	 * @param pathName
	 *            Ŀ¼����
	 */
	private static boolean CreatePath(String pathName) {
		File path = new File(SDPATH + pathName);
		boolean res = path.mkdirs();
		return res;
	}

	/**
	 * �ж�Ŀ¼�Ƿ����
	 * 
	 * @param pathName
	 *            Ŀ¼����
	 * @return
	 */
	private static boolean IsExistsPath(String pathName) {
		File path = new File(SDPATH + pathName);
		return path.exists();
	}

	/**
	 * ��SD����ȡĿ¼
	 * @param pathName Ŀ¼����
	 * @return
	 */
	public static String GetPath(String pathName) {
		if (!IsExistsPath(pathName)) {
			CreatePath(pathName);
		}
		return SDPATH + pathName;
	}

	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @param filePath
	 *            �ļ�Ŀ¼
	 * @param fileName
	 *            �ļ�����
	 */
	public static void CreateFile(String filePath, String fileName, Bitmap bm) {
		try {
			// ���Ŀ¼�����ڴ���Ŀ¼
			if (!IsExistsPath(filePath)) {
				CreatePath(filePath);
			}
			String path = SDPATH + filePath + "/" + fileName;
			File file = new File(path);
			if (!file.exists()) {
				OutputStream os = new FileOutputStream(file);
				// BufferedOutputStream bos=new BufferedOutputStream(os);
				FileOutputStream bos = new FileOutputStream(file);
				CompressFormat format = CompressFormat.JPEG;
				if (fileName
						.substring((fileName.lastIndexOf(".") + 1),
								fileName.length() - 1).toLowerCase()
						.equals("png")) {
					format = CompressFormat.PNG;
				}
				bm.compress(format, 100, bos);
				bos.flush();
				bos.close();
			}
		} catch (Exception e) {
			// e.printStackTrace();
			// TODO: handle exception
		}
	}

	/**
	 * ��SD����ȡͼƬ
	 * 
	 * @param imgPath
	 *            ͼƬĿ¼
	 * @param imgName
	 *            ͼƬ����
	 * @return
	 */
	public static Bitmap GetImage(String imgPath, String imgName) {
		String path = SDPATH + imgPath + "/" + imgName;
		File imgFile = new File(path);
		Bitmap bm = null;
		if (imgFile.exists()) {
			bm = BitmapFactory.decodeFile(path);
		}
		return bm;
	}
}
