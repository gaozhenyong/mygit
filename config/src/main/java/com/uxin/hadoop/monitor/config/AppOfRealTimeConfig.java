package com.uxin.hadoop.monitor.config;

import com.uxin.hadoop.monitor.utils.IoUtil;

public class AppOfRealTimeConfig {

	public static int AppRunIntervalTime = 10000;
	public static String ApplicationTypes;
	public static String ApplicationStates;
	static {
		AppRunIntervalTime = Integer.valueOf(IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppCorePath, "AppRunIntervalTime"));
		ApplicationTypes = IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppCorePath, "ApplicationTypes");
		ApplicationStates = IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppCorePath, "ApplicationStates");
	}
}
