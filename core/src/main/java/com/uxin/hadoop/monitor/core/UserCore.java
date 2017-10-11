package com.uxin.hadoop.monitor.core;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uxin.hadoop.monitor.config.WebConfig;
import com.uxin.hadoop.monitor.utils.UrlUtil;

public class UserCore {

	public final static String AccountKey = "AccountName";
	private final static int LoginCookieExpire = 7 * 24 * 60 * 60;// 7天

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserAccount(HttpServletRequest request) {
		Cookie cookie = getCookie(request, AccountKey);
		if (cookie != null) {
			return cookie.getValue();
		}
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param accountName
	 */
	public static void setUserAccount(HttpServletResponse response,
			String accountName) {
		Cookie nameCookie = new Cookie(AccountKey, accountName);
		nameCookie.setPath("/");
		nameCookie.setMaxAge(LoginCookieExpire);
		response.addCookie(nameCookie);
	}

	/**
	 * 
	 * @param request
	 */
	public static void loginOut(HttpServletResponse response,
			HttpServletRequest request) {
		Cookie cookie = getCookie(request, AccountKey);
		if (cookie != null) {
			cookie.setValue(null);
			cookie.setMaxAge(0);// 立即销毁cookie
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static String domainLoginApi(String username, String password) {
		String postData = String.format(
				"data={\"username\":\"%s\",\"password\":\"%s\"}", username,
				password);
		String jsonString = UrlUtil.HttpRequest(WebConfig.DomainLoginApi, null,
				postData);
		return jsonString;
	}

	/**
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	private static Cookie getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					return cookie;
				}
			}
		}
		return null;
	}
}
