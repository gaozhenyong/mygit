package com.uxin.hadoop.monitor.config;

import com.uxin.hadoop.monitor.utils.IoUtil;

public class MailTempletConfig {
	public static String TaskTimeoutSubject;
	public static String TaskTimeoutBody;
	public static String taskTimeoutMailTo;

	public static String TaskHangsSubject;
	public static String TaskHangsBody;
	public static String TaskHangsMailTo;

	static {
		// 超时任务发送mail配置
		TaskTimeoutSubject = IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppMailTempletPath, "taskTimeoutSubject");
		TaskTimeoutBody = IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppMailTempletPath, "taskTimeoutBody");
		taskTimeoutMailTo = IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppMailTempletPath, "taskTimeoutMailTo");
		// 实时任务挂掉 发送mail配置
		TaskHangsSubject = IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppMailTempletPath, "TaskHangsSubject");
		TaskHangsBody = IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppMailTempletPath, "TaskHangsBody");
		TaskHangsMailTo = IoUtil.getPropertiesSingleValue(
				FilePathConfig.AppMailTempletPath, "TaskHangsMailTo");
	}
}
