package com.uxin.hadoop.monitor.config;

import com.uxin.hadoop.monitor.utils.IoUtil;

public class AppConfig {

	public static int TaskMaxTimeOut = 18000000; // 1800000;//设定超时时长 ，毫秒单位
	public static Boolean KillTimeoutTaskFlag;// 标识 超时任务是否kill
	public static int AppRunIntervalTime = 10000;
	public static String ApplicationTypes;// hadoop任务类型
	public static String ApplicationStates;// hadoop任务类型
	static {
		String s = IoUtil.getPropertiesSingleValue(FilePathConfig.AppCorePath,
				"TaskMaxTimeOut");
		if (s != null)
			TaskMaxTimeOut = Integer.valueOf(s);
		s = IoUtil.getPropertiesSingleValue(FilePathConfig.AppCorePath,
				"KillTimeoutTaskFlag");
		if (s != null)
			KillTimeoutTaskFlag = Boolean.valueOf(s);
		s = IoUtil.getPropertiesSingleValue(FilePathConfig.AppCorePath,
				"AppRunIntervalTime");
		if (s != null)
			AppRunIntervalTime = Integer.valueOf(s);

		ApplicationTypes = IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppCorePath, "ApplicationTypes");
		ApplicationStates = IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppCorePath, "ApplicationStates");
	}
}
