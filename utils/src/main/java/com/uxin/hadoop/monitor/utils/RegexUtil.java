package com.uxin.hadoop.monitor.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	/**
	 * 获取数字字符串
	 */
	public static String GetIntStr(String content) {
		return GetString(content, "\\d+");
	}

	/**
	 * 获取数字
	 */
	public static int GetInt(String content) {
		String s = GetIntStr(content);
		if (s != null && s.length() > 0) {
			return Integer.parseInt(s);
		}
		return 0;
	}

	/**
	 * 获取数字
	 */
	public static int GetInt(String content, int len) {
		String s = GetString(content, String.format("\\d{%s}", len));
		if (s != null && s.length() > 0) {
			return Integer.parseInt(s);
		}
		return 0;
	}

	/**
	 * 根据车型名称获取年款
	 */
	public static int GetYear(String content) {
		String year1 = GetString(content, "\\d{4}[年款|年|款]");
		if (year1 != null && year1.length() > 0) {
			return GetInt(year1, 4);
		} else {
			String year2 = GetString(content, "\\d{2}[年款|年|款]");
			if (year2 != null && year2.length() > 0) {
				int year = GetInt(year2, 2);
				if (year > 50) {
					return Integer.parseInt(String.format("19%s", year));
				} else if (year < 10) {
					return Integer.parseInt(String.format("200%s", year));
				} else {
					return Integer.parseInt(String.format("20%s", year));
				}
			}
		}
		return 0;
	}

	/**
	 * 获取时间格式的年份
	 */
	public static int GetYearV2(String date) {
		return GetInt(GetString(date, "\\d{4}[年|\\.|\\-]"));
	}

	/**
	 * 获取时间格式的月份
	 */
	public static int GetMonth(String date) {
		return GetInt(GetString(date, "[年|\\.|\\-]\\d{1,2}[月|\\.|\\-]?"));
	}

	/**
	 * 获取年月日
	 */
	public static String GetFullDateTimeString(String content) {
		return GetString(
				content,
				"\\d{2,4}[\\.|\\-|\\/|年]\\d{1,2}[\\.|\\-|月|\\/]\\d{1,2}[日|号]?( \\d{1,2}[:|时3]\\{1,2}[:|分]\\{1,2}秒?)?");
	}

	/**
	 * 获取小数
	 */
	public static BigDecimal GetDecimal(String innerText) {
		String s = GetString(innerText, "\\d+[\\.|,]?\\d{0,}");
		if (s != null && s.length() > 0) {
			s = s.replace(",", ".");
			try {
				return new BigDecimal(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new BigDecimal("0");
	}

	/**
	 * 获取小数
	 */
	public static double GetDouble(String innerText) {
		String s = GetString(innerText, "\\d+[\\.|,]?\\d{0,}");
		if (s != null && s.length() > 0) {
			s = s.replace(",", ".");
			try {
				return Double.parseDouble(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 
	 * @param content
	 * @param format
	 * @return
	 */
	public static boolean IsMatch(String content, String format) {
		Pattern pattern = Pattern.compile(format, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 获取
	 */
	public static String Match(String content, String format,
			Boolean isReturnOrgStr) {
		String result = GetString(content, format);
		if (result.length() == 0) {
			result = content;
		}
		return result;
	}

	/**
	 * 获取城市
	 */
	public static String GetCityName(String content) {
		return GetString(content, "(?<=[省|区|市|-])[\u4E00-\u9FA5]+?(?=市)");
	}

	/**
	 * 车牌
	 */
	public static String GetPlateNo(String content) {
		return GetString(
				content,
				"[藏,川,鄂,甘,赣,贵,桂,黑,沪,吉,冀,津,晋,京,辽,鲁,蒙,闽,宁,青,琼,陕,苏,皖,湘,新,渝,豫,粤,云,浙,港,澳,台]{1}[a-zA-Z0-9\\*]{6}");
	}

	/**
	 * 电话
	 */
	public static String GetTel(String content) {
		return GetString(content, "[0-9\\-转]{10,}");
	}

	/**
	 * 根据正则提取content里匹配的内容， regex正则表达式
	 */
	public static String GetString(String content, String regex) {
		try {
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				return matcher.group();
			}
		} catch (Exception e) {

		}

		return null;
	}

	/***
	 * 
	 * @param content
	 * @param regex
	 * @return
	 */
	public static List<String> GetList(String content, String regex) {
		List<String> list = null;
		try {
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(content);
			if (matcher.groupCount() > 0) {
				list = new ArrayList<String>();
				while (matcher.find()) {
					list.add(matcher.group());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static String GetPageName(String url) {
		String pattern = "[a-zA-Z0-9]+?(?=((\\.htm)|(\\.html)|(\\.shtml)|(\\.shtm)|(\\.do)|(\\.jsp)|(\\.PHP)|(\\.aspx)|(\\.cgi)))";
		return GetString(url, pattern);
	}

	/***
	 * 
	 * @param content
	 * @param regex
	 * @return
	 */
	public static String GetString(String content, String regex,
			boolean isReturnOrgString) {
		String result = GetString(content, regex);
		if (result != null && result.length() > 0)
			return result;
		if (isReturnOrgString)
			return content;
		else
			return null;
	}

}
