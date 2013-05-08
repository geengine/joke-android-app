package com.wxk.tools;

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
	 * 在SD卡上面创建目录
	 * 
	 * @param pathName
	 *            目录名字
	 */
	private static boolean CreatePath(String pathName) {
		File path = new File(SDPATH + pathName);
		boolean res = path.mkdirs();
		return res;
	}

	/**
	 * 判断目录是否存在
	 * 
	 * @param pathName
	 *            目录名字
	 * @return
	 */
	private static boolean IsExistsPath(String pathName) {
		File path = new File(SDPATH + pathName);
		return path.exists();
	}

	/**
	 * 在SD卡获取目录
	 * @param pathName 目录名字
	 * @return
	 */
	public static String GetPath(String pathName) {
		if (!IsExistsPath(pathName)) {
			CreatePath(pathName);
		}
		return SDPATH + pathName;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param filePath
	 *            文件目录
	 * @param fileName
	 *            文件名字
	 */
	public static void CreateFile(String filePath, String fileName, Bitmap bm) {
		try {
			// 如果目录不存在创建目录
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
	 * 从SD卡读取图片
	 * 
	 * @param imgPath
	 *            图片目录
	 * @param imgName
	 *            图片名称
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
