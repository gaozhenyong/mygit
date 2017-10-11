package com.uxin.hadoop.monitor.handles.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.uxin.hadoop.monitor.core.UserCore;

/**
 * 登录拦截器
 * 
 * @author gaozhenyong
 * 
 */
public class LoginHandlerIntercepter implements HandlerInterceptor {

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception e)
			throws Exception {
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj, ModelAndView model)
			throws Exception {

	}

	/***
	 * 拦截除登录页之外的所有页面
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj) throws Exception {
		String requestURI = request.getRequestURI();
		if (requestURI.indexOf("/user/login") > 0) {// 登录页面，不验证，直接返回true
			return true;
		} else {
			if (UserCore.getUserAccount(request) == null) {
				return false;
			} else {
				return true;
			}
		}

	}
}
