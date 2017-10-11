package com.uxin.hadoop.monitor.config;

import com.uxin.hadoop.monitor.model.RedisKeyModel;
import com.uxin.hadoop.monitor.model.RedisKeyModel.RedisDataType;

public class RedisDataKeyConfig {
	public static RedisKeyModel TaskListTimeOutModel = new RedisKeyModel(
			"REDIS_TASKLISTOFTIMEOUT", 86400, RedisDataType.list);
	public static RedisKeyModel ExceptionList = new RedisKeyModel(
			"REDIS_EXCEPTIONLIST", 86400, RedisDataType.list);
	public static RedisKeyModel TaskProcessInfoList = new RedisKeyModel(
			"REDIS_TASKPROCESSINFOLIST", 86400, RedisDataType.list);
}
