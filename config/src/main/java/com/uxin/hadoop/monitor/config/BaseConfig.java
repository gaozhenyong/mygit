package com.uxin.hadoop.monitor.config;

public class BaseConfig {

	public static void Init(ProjectClassEnum clas) {
		System.out.println("===========BaseConfig Init");
		if (clas == ProjectClassEnum.Web_SubmitTask) {
			FileRootPath = WebRoot + "/WEB-INF/conf/properties/";
		} else if (clas == ProjectClassEnum.Tez_MonitorApp) {
			FileRootPath = AppRoot + "/conf/";
		} else if (clas == ProjectClassEnum.RealTime_MonitorApp) {
			FileRootPath = AppRoot + "/conf/";
		} else {
			FileRootPath = AppRoot + "/conf/";
		}
	}

	public enum ProjectClassEnum {
		Tez_MonitorApp, RealTime_MonitorApp, Web_SubmitTask
	}

	public static String AppRoot = System.getProperty("user.dir");
	public static String WebRoot = System.getProperty("web.root");
	public static String FileRootPath = AppRoot + "/conf/";
}
