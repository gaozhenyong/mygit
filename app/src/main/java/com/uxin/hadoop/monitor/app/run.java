package com.uxin.hadoop.monitor.app;

import javax.mail.MessagingException;

import com.uxin.hadoop.monitor.config.AppConfig;
import com.uxin.hadoop.monitor.config.BaseConfig;
import com.uxin.hadoop.monitor.config.BaseConfig.ProjectClassEnum;
import com.uxin.hadoop.monitor.utils.Log4jUtil;

/**
 * 监控超时任务，发邮件给相关人。
 * 
 * @author gaozhenyong
 * 
 */
public class run {
	/**
	 * @param args
	 * @throws MessagingException
	 */
	public static void main(String[] args) {
		System.out.println("===========app start");
		init();
		start();
		System.out.println("===========app end");
	}

	/**
	 * 
	 */
	private static void init() {
		System.out.println("===========init start");
		try {
			BaseConfig.Init(ProjectClassEnum.Tez_MonitorApp);
			Log4jUtil.InitLogPath(BaseConfig.AppRoot); // 初始化log4j配置文件
		} catch (Exception e) {
			e.printStackTrace();
			Log4jUtil.error(e.toString());
		}
	}

	/***
	 * 
	 */
	private static void start() {
		while (true) {
			appcore.CheckTimeoutTask();
			if (AppConfig.AppRunIntervalTime == -1) {// 如果AppRunIntervalTime=-1只执行一次，跳出循环。否则轮训执行。
				break;
			} else {
				try {
					Thread.sleep(AppConfig.AppRunIntervalTime);// 线程休眠
					System.out.println("===========run again");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}