package com.uxin.hadoop.monitor.core;

import com.uxin.hadoop.monitor.config.MailServerConfig;
import com.uxin.hadoop.monitor.utils.MailUtil;

public class MailCore {

	public static void SendMail(String to, String subject, String body,
			String mailType) {
		try {
			MailUtil.sendMessage(MailServerConfig.smtpHost,
					MailServerConfig.smtpPort, MailServerConfig.mail_user,
					MailServerConfig.mail_pass, to, subject, body, mailType);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
