package com.uxin.hadoop.monitor.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import redis.clients.jedis.Client;

public final class IoUtil {

	/**
	 * 文件是否存在
	 * 
	 * @param filepath
	 * @return
	 */
	public static boolean FileIsExsit(String filepath) {
		File file = new File(filepath);
		return file.exists();
	}

	/**
	 * 
	 * @param filepath
	 * @return
	 */
	public static boolean FileDelete(String filepath) {
		Boolean isok = false;
		try {
			File file = new File(filepath);
			isok = file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isok;
	}

	/**
	 * 按行读取
	 * 
	 * @param filePath
	 * @param encoding
	 * @return
	 */
	public static String ReadFileByLine(String filePath, String encoding) {
		StringBuilder result = new StringBuilder();
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = "";
				while ((lineTxt = bufferedReader.readLine()) != null) {
					result.append(lineTxt);
					// result.append(System.lineSeparator()+lineTxt);
				}
				bufferedReader.close();
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 按字节读取
	 * 
	 * @param fileName
	 * @param encoding
	 * @return
	 */
	public static String ReadFileByChars(String fileName, String encoding) {
		StringBuilder result = new StringBuilder();

		try {
			File file = new File(fileName);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				Reader reader = new InputStreamReader(
						new FileInputStream(file), encoding);
				int tempchar;
				while ((tempchar = reader.read()) != -1) {
					result.append((char) tempchar);
				}
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 
	 * @param fileName
	 * @param key
	 * @param appIsExit
	 *            ,如果为空是否是否退出程序。
	 * @return
	 */
	public static String getPropertiesSingleValue(String fileName, String key) {
		Properties prop = getProperties(fileName);
		String value = null;
		if (prop != null) {
			value = prop.getProperty(key);
		}
		return value;
	}

	/***
	 * 
	 * @param filepath
	 * @return
	 */
	public static Properties getProperties(String filepath) {
		Properties prop = null;
		InputStream in = null;
		InputStream fis = null;
		try {
			prop = new Properties();
			fis = new FileInputStream(filepath);
			if (fis != null) {
				in = new BufferedInputStream(fis);
				prop.load(new InputStreamReader(in, "utf-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (fis != null)
				fis.close();
			if (in != null)
				in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}

	/***
	 * 
	 * @param fileName
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Properties getPropertiesInClassPath(String fileName) {
		Properties properties = null;
		InputStreamReader stream = null;
		try {
			stream = new InputStreamReader(Client.class.getClassLoader()
					.getResourceAsStream(fileName), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (stream != null) {
			properties = new Properties();
			try {
				properties.load(stream);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

	/**
	 * 遍历文件，返回文件路径list
	 * 
	 * @param dirPath
	 * @return
	 */
	public static List<String> GetFilesInDir(String dirPath) {
		File file = new File(dirPath);
		File[] files = file.listFiles();
		List<String> list = new ArrayList<String>();
		for (File file2 : files) {
			list.add(file2.getPath());
		}
		return list;
	}

	/**
	 * 重命名文件名
	 * 
	 * @param oldName
	 * @param newName
	 */
	public static void RenameFile(String oldName, String newName) {
		File file = new File(oldName);
		file.renameTo(new File(newName));
		// //想命名的原文件夹的路径
		// File file1 = new File("f:/A");
		// //将原文件夹更改为A，其中路径是必要的。注意
		// file1.renameTo(new File("f:/B"));
	}

	/**
	 * get class path
	 * 
	 * @return
	 */
	public static String getClassPath() {
		return Thread.currentThread().getContextClassLoader().getResource("")
				.getPath();
	}

	/***
	 * 
	 * @param rootPath
	 * @param arr
	 * @return
	 */
	public static String getFilesNotExsit(String rootPath, String[] arr) {
		StringBuffer sbBuffer = new StringBuffer();
		for (String f : arr) {
			String filepath = String.format("%s%s", rootPath, f);
			if (!FileIsExsit(filepath)) {
				sbBuffer.append(f);
				sbBuffer.append("，");
			}
		}
		return sbBuffer.toString();
	}

	/***
	 * 
	 * @param file
	 * @return
	 */
	public static String getExtension(String file) {
		if (file != null) {
			int lastd = file.lastIndexOf('.');
			if (lastd != -1 && lastd < file.length() - 1) {
				return file.substring(lastd);
			}
		}
		return "";
	}

	/***
	 * 
	 * @return
	 */
	public static String getFileName() {
		long timestamp = new Date().getTime();
		Random random = new Random(timestamp);
		return String.format("%s%s", timestamp, random.nextInt(10000));
	}
}
