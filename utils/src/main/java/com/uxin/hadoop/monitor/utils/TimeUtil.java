package com.uxin.hadoop.monitor.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeUtil {

	/**
	 * 字符串时间转为时间戳
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static long DateToStamp(String s) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		return date.getTime();
	}

	/**
	 * 
	 * @return
	 */
	public static String GetToday() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			return sdf.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String stampToDate(Long lt) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
}
