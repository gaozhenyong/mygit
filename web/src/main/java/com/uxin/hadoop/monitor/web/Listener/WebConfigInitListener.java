package com.uxin.hadoop.monitor.web.Listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.uxin.hadoop.monitor.config.BaseConfig;
import com.uxin.hadoop.monitor.config.BaseConfig.ProjectClassEnum;

public class WebConfigInitListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent arg0) {
		BaseConfig.Init(ProjectClassEnum.Web_SubmitTask);
	}

	public void contextDestroyed(ServletContextEvent arg0) {

	}
}
