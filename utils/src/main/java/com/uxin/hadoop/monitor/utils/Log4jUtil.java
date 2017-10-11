package com.uxin.hadoop.monitor.utils;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class Log4jUtil {
	public static void debug(Object message) {
		Logger logger = Logger.getLogger(Log4jUtil.class);
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}

	public static void info(Object message) {
		Logger logger = Logger.getLogger(Log4jUtil.class);
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}

	public static void warn(Object message) {
		Logger logger = Logger.getLogger(Log4jUtil.class);
		logger.warn(message);
	}

	public static void error(Object message) {
		Logger logger = Logger.getLogger(Log4jUtil.class);
		logger.error(message);
	}

	public static void error(Exception e) {
		Logger logger = Logger.getLogger(Log4jUtil.class);
		e.printStackTrace();
		logger.error(e.toString());
	}

	/**
	 * 处理log日志输出相对路径
	 * 
	 * @param rootPath
	 */
	public static void InitLogPath(String rootPath) {
		System.out.println("===========log4j log path init");
		Properties log4jProperty = IoUtil
				.getPropertiesInClassPath("log4j.properties");
		String[] keys = { "log4j.appender.info.File",
				"log4j.appender.debug.File", "log4j.appender.warn.File",
				"log4j.appender.error.File" };
		for (String key : keys) {
			try {
				String org = log4jProperty.getProperty(key);
				if (org != null)
					log4jProperty.setProperty(key, rootPath + org);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		PropertyConfigurator.configure(log4jProperty);
	}
}
