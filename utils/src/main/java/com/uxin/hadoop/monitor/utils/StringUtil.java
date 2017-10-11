package com.uxin.hadoop.monitor.utils;

import java.util.List;

/**
 * 
 * @author gaozhenyong
 * 
 */
public final class StringUtil {
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean IsNullOrEmpty(String str) {
		if (str == null || str.length() == 0)
			return true;
		return false;
	}

	/**
	 * 
	 * @param content
	 * @param prefix
	 * @return
	 */
	public static String TrimStart(String content, String prefix) {
		if (content.startsWith(prefix)) {
			int len = prefix.length();
			return content.substring(len);
		}
		return content;
	}

	/**
	 * 
	 * @param content
	 * @param endfix
	 * @return
	 */
	public static String TrimEnd(String content, String endfix) {
		if (content.endsWith(endfix)) {
			int len = content.length();
			return content.substring(0, len - endfix.length());
		}
		return content;
	}

	/***
	 * 
	 * @param list
	 * @param separator
	 * @return
	 */
	public static String listToString(List<String> list, String separator) {
		if (list != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i));
				if (separator != null && i < list.size() - 1) {
					sb.append(separator);
				}
			}
			return sb.toString();
		}
		return null;
	}
}
