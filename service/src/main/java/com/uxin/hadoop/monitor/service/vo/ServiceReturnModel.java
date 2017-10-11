package com.uxin.hadoop.monitor.service.vo;

public class ServiceReturnModel {

	public ServiceReturnModel(boolean sucess, int _code, String msg,
			Object _result) {
		State = sucess;
		code = _code;
		message = msg;
		result = _result;
	}

	public static ServiceReturnModel getSucessModel(String msg) {
		return new ServiceReturnModel(true, 1, msg, null);
	}

	public static ServiceReturnModel getSucessModel(String msg, Object obj) {
		return new ServiceReturnModel(true, 1, msg, obj);
	}

	public static ServiceReturnModel getFailModel(String msg) {
		return new ServiceReturnModel(false, 0, msg, null);
	}

	public static ServiceReturnModel getExceptionModel(Exception e) {
		return new ServiceReturnModel(false, 2, e.toString(), null);
	}

	public static ServiceReturnModel getDefaultFailModel() {
		return getFailModel("fail");
	}

	public static ServiceReturnModel getDefaultSuccessModel() {
		return getFailModel("success");
	}

	public boolean State = false;
	public int code;// 0失败，1成功，2异常
	public String message;
	public Object result;
}
