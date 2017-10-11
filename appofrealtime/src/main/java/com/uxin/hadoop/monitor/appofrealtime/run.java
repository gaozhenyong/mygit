package com.uxin.hadoop.monitor.appofrealtime;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.uxin.hadoop.monitor.config.AppConfig;
import com.uxin.hadoop.monitor.config.BaseConfig;
import com.uxin.hadoop.monitor.config.BaseConfig.ProjectClassEnum;
import com.uxin.hadoop.monitor.service.TaskService;
import com.uxin.hadoop.monitor.utils.Log4jUtil;

/**
 * 监控实时任务
 * 
 * @author gaozhenyong
 * 
 */
public class run {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("===========app start");
		init();
		start();
		System.out.println("===========app end");
	}

	/**
	 * init 程序 config
	 */
	private static void init() {
		try {
			BaseConfig.Init(ProjectClassEnum.RealTime_MonitorApp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 
	 */
	private static void start() {
		System.out.println("===========monitor task start");
		while (true) {
			taskService.MonitorRealtimeTask();
			if (AppConfig.AppRunIntervalTime == -1) {// 如果AppRunIntervalTime=-1只执行一次，跳出循环。否则轮训执行。
				break;
			} else {
				try {
					Thread.sleep(AppConfig.AppRunIntervalTime);// 线程休眠
					System.out.println("===========run again");
				} catch (InterruptedException e) {
					Log4jUtil.error(e);
				}
			}
		}
	}

	static ApplicationContext context = new FileSystemXmlApplicationContext(
			"classpath:applicationContext.xml");// 加载springMVC和mabatis注入配置文件

	static TaskService taskService = (TaskService) context
			.getBean("taskService");
}