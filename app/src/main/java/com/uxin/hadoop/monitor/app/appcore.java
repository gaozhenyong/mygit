package com.uxin.hadoop.monitor.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.uxin.hadoop.monitor.config.AppConfig;
import com.uxin.hadoop.monitor.config.MailTempletConfig;
import com.uxin.hadoop.monitor.config.RedisDataKeyConfig;
import com.uxin.hadoop.monitor.core.MailCore;
import com.uxin.hadoop.monitor.core.RedisCore;
import com.uxin.hadoop.monitor.hadoopservice.model.applicationModel;
import com.uxin.hadoop.monitor.hadoopservice.model.applicationUrlParamsModel;
import com.uxin.hadoop.monitor.model.RedisMailRecordModel;
import com.uxin.hadoop.monitor.utils.Log4jUtil;
import com.uxin.hadoop.monitor.utils.OsUtils;
import com.uxin.hadoop.monitor.utils.TimeUtil;

public class appcore {

	public static void CheckTimeoutTask() {
		try {
			long todayStamp = TimeUtil.DateToStamp(TimeUtil.GetToday());// 获取今日时间戳
			applicationUrlParamsModel paramsModel = new applicationUrlParamsModel();
			paramsModel.applicationTypes = AppConfig.ApplicationTypes;
			paramsModel.startedTimeBegin = todayStamp;
			List<applicationModel> list = new ArrayList<applicationModel>();// 获取今天的hadoop任务列表
			if (list != null) {
				Long now = new Date().getTime();
				for (applicationModel model : list) {
					String state = model.state.toUpperCase();// 存储任务状态
					if ((AppConfig.ApplicationStates).contains(state)) {// 判断任务状态
						long timeDiff = now - model.startedTime;
						if (timeDiff > AppConfig.TaskMaxTimeOut)// 执行超过规定最大时间
						{
							System.out.println("任务超时：" + model.id);
							Jedis jedis = RedisCore.getJedis();
							if (jedis != null) {
								List<String> timeOutList = jedis.lrange(
										RedisDataKeyConfig.TaskListTimeOutModel
												.getKey(), 0, -1);// 获取redis内容
								boolean isSend = false;
								int index = 0;
								if (timeOutList == null) {
									isSend = true;
								} else {
									for (int i = 0; i < timeOutList.size(); i++) {
										String str = timeOutList.get(i);
										JSONObject object = JSONObject
												.parseObject(str);
										if (object.getString("id")
												.equalsIgnoreCase(model.id)) {
											index = i;
											if (object.getLong("createtime") < model.startedTime)// 比对上次发邮件时间和hadoop任务开始时间，用来辨别邮件是否已经发过，
																									// 防止邮件重发
												isSend = true;
											break;
										}
									}
								}
								if (index == 0) {
									isSend = true;
								}
								if (isSend) {
									RedisMailRecordModel mailModel = new RedisMailRecordModel();
									mailModel.tomail = String
											.format(MailTempletConfig.taskTimeoutMailTo,
													model.user + "@xin.com");// 收件人
									mailModel.id = model.id;
									mailModel.createtime = new Date().getTime();
									if (index > 0) {
										jedis.lset(
												RedisDataKeyConfig.TaskListTimeOutModel
														.getKey(), index,
												JSON.toJSONString(mailModel));// 更新redis内容
									} else {
										jedis.lpush(
												RedisDataKeyConfig.TaskListTimeOutModel
														.getKey(),
												JSON.toJSONString(mailModel));// 添加redis内容
									}
									String mailBody = JSON.toJSONString(model);// 邮件内容
									MailCore.SendMail(
											mailModel.tomail,
											MailTempletConfig.TaskTimeoutSubject,
											mailBody, null);// send mail
									if (AppConfig.KillTimeoutTaskFlag) {// 超时任务是否需要kill
										OsUtils.processLinux(
												"yarn application -kill "
														+ model.id.trim(),
												false, false);
									}
								}

							} else {
								Log4jUtil.error("获取redis实例 出现错误。");
							}

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
