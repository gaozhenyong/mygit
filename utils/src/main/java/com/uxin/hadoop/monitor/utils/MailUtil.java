package com.uxin.hadoop.monitor.utils;

import java.util.Calendar;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class MailUtil {

	/**
	 * 
	 * @param smtpHost
	 * @param smtpPort
	 * @param from
	 * @param fromUserPassword
	 * @param to
	 *            多个人以逗号隔开
	 * @param subject
	 * @param content
	 * @param contentType
	 *            ， "text/html;charset=UTF-8"，可为null,默认为文本格式
	 * @throws MessagingException
	 */
	public static void sendMessage(String smtpHost, String smtpPort,
			String from, String fromUserPassword, String to, String subject,
			String content, String contentType) throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true"); // 使用验证
		props.put("mail.smtp.host", smtpHost);// SMTP主机地址
		props.put("mail.smtp.port", smtpPort);// 端口号
		Authenticator authenticator = new MyAuthenticator(from,
				fromUserPassword); // 验证
		// 创建session
		Session session = Session.getInstance(props, authenticator);
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		message.setFrom(new InternetAddress(from));

		// 设置收件人
		String[] to_addrs = to.split(",");
		InternetAddress[] toAddress = new InternetAddress[to_addrs.length];
		for (int i = 0; i < to_addrs.length; i++) {
			toAddress[i] = new InternetAddress(to_addrs[i]);
		}
		message.addRecipients(RecipientType.TO, toAddress);
		// 设置主题
		message.setSubject(subject, "UTF-8");
		// 设置邮件内容
		if (contentType != null && contentType.length() > 0) {
			message.setContent(content, contentType);// contentType="text/html;charset=UTF-8"
		} else {
			message.setText(content, "UTF-8");// 也可以不用显式的制定消息的内容类型：
		}
		message.setSentDate(Calendar.getInstance().getTime());// 设置发件时间

		// 发送
		Transport.send(message);
	}
}

class MyAuthenticator extends Authenticator {
	String userName = "";
	String password = "";

	public MyAuthenticator() {

	}

	public MyAuthenticator(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}
