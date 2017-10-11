package com.uxin.hadoop.monitor.handles.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.uxin.hadoop.monitor.core.UserCore;
import com.uxin.hadoop.monitor.service.vo.ServiceReturnModel;
import com.uxin.hadoop.monitor.utils.Log4jUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public ServiceReturnModel login(String username, String password,
			HttpServletResponse response, HttpServletRequest request) {
		ServiceReturnModel returnViewModel = ServiceReturnModel
				.getDefaultFailModel();
		try {
			String resultsString = UserCore.domainLoginApi(username, password);
			if (resultsString == null || resultsString.length() == 0) {
				returnViewModel = ServiceReturnModel.getFailModel("网络异常");
			} else {
				JSONObject objJson = JSONObject.parseObject(resultsString);
				if (objJson.getInteger("status").equals(0)) {// 登录成功
					UserCore.setUserAccount(response, username);
					returnViewModel = ServiceReturnModel
							.getSucessModel("Success");
				} else {
					returnViewModel = ServiceReturnModel.getFailModel(objJson
							.getString("msg"));
				}
			}

		} catch (Exception e) {
			Log4jUtil.error(e);
			returnViewModel = ServiceReturnModel.getExceptionModel(e);
		}
		return returnViewModel;
	}

	/**
	 * 获取用户名
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/name")
	@ResponseBody
	public String getUserName(HttpServletRequest request) {
		return UserCore.getUserAccount(request);
	}

	/**
	 * 退出
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/loginOut")
	@ResponseBody
	public String LoginOut(HttpServletResponse response,
			HttpServletRequest request) {
		UserCore.loginOut(response, request);
		return "";
	}
}
