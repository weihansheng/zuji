package com.project.zuji.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.util.Log;

public class SDTool {
	public static final String SDPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/**
	 * 检测文件或者路径是否存在
	 * 
	 * 可以给值为Null，如果给值null,判断路径是否存在
	 */

	public static boolean isExists(String path, String fileName) {
		if (null == path && null == fileName) {
			return false;
		}
		String name;
		name = SDPATH + File.separator + path;
		File file = new File(name);
		if (!file.exists()) {
			file.mkdirs();
		}
		File fileNmae = new File(name, fileName);
		return fileNmae.exists();
	}

	public static boolean isExists(String path) {
		if (null == path) {
			return false;
		}
		String name;

		name = SDPATH + File.separator + path;

		File file = new File(name);
		if (!file.exists()) {
			file.mkdir();
		}
		return file.exists();
	}

	public static void deleteFile(String path, String fileName) {
		File file = new File(SDPATH + File.separator + path + File.separator
				+ fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	public static void deleteFile(String path) {
		File file = new File(SDPATH + File.separator + path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 检查SD卡是否可用
	 */
	public static boolean isAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	// 读取文件内容
	public static String readText(String path, String fileName) {
		if (!isAvailable()) {
			return null;
		}
		String file = SDPATH + File.separator + path + File.separator
				+ fileName;
		Log.e("file", file);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuffer buffer = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				buffer.append(line + "\n");
			}
			br.close();
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 写入文件内容
	public static boolean writeText(String path, String fileName,
			boolean isApp, String content) {
		if (!isAvailable()) {
			return false;
		}
		isExists(path, fileName);
		File file = new File(SDPATH + File.separator + path);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		try {
			FileWriter fw = new FileWriter(SDPATH + File.separator + path
					+ File.separator + fileName, isApp);
			fw.write(content);
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 读取字节文件内容
	public static InputStream readStream(String path, String fileName) {
		if (!isAvailable()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(SDPATH + File.separator
					+ path + File.separator + fileName);
			return fis;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 读取字节文件内容
	public static byte[] readStreamContent(String path, String fileName) {
		if (!isAvailable()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(SDPATH + File.separator
					+ path + File.separator + fileName);
			byte[] content = readAllStream(fis);
			fis.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean writeStream(String path, String fileName,
			boolean isApp, byte[] content) {
		if (!isAvailable()) {
			return false;
		}
		File file = new File(SDPATH + File.separator + path);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		try {
			FileOutputStream fos = new FileOutputStream(SDPATH + File.separator
					+ path + File.separator + fileName, isApp);
			fos.write(content);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 讲字节流写入sd卡
	public static boolean writeStream(String path, String fileName,
			boolean isApp, InputStream is) {
		if (!isAvailable()) {
			return false;
		}
		Log.e("isAvailable", isAvailable() + "");
		File file = new File(SDPATH + File.separator + path);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		try {
			FileOutputStream fos = new FileOutputStream(SDPATH + File.separator
					+ path + File.separator + fileName, isApp);
			byte[] content = readAllStream(is);
			if (null != content) {
				fos.write(content);
				fos.close();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 将字节流转换成byte[]数组
	public static byte[] readAllStream(InputStream is) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int size;
		try {
			while ((size = is.read(buffer, 0, 1024)) != -1) {
				bos.write(buffer, 0, size);
			}
			byte[] content = bos.toByteArray();
			bos.close();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
