package com.uxin.hadoop.monitor.config;

import com.uxin.hadoop.monitor.utils.IoUtil;

public final class MailServerConfig {
	public static String smtpHost;
	public static String smtpPort;
	public static String mail_user;
	public static String mail_pass;

	static {
		smtpHost = IoUtil.getPropertiesSingleValue(FilePathConfig.MailPath,
				"smtpHost");
		smtpPort = IoUtil.getPropertiesSingleValue(FilePathConfig.MailPath,
				"smtpPort");
		mail_user = IoUtil.getPropertiesSingleValue(FilePathConfig.MailPath,
				"mail_user");
		mail_pass = IoUtil.getPropertiesSingleValue(FilePathConfig.MailPath,
				"mail_pass");
	}
}
