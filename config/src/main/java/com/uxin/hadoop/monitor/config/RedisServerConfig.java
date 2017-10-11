package com.uxin.hadoop.monitor.config;

import com.uxin.hadoop.monitor.utils.IoUtil;

public final class RedisServerConfig {
	public static String server;
	public static int port;
	public static String password;
	public static int maxTotal;
	public static int maxWaitMillis;
	public static int maxIdle;
	public static boolean testOnBorrow;
	public static boolean testOnReturn;
	public static boolean testWhileIdle;
	public static boolean setBlockWhenExhausted;

	static {
		server = IoUtil.getPropertiesSingleValue(FilePathConfig.RedisPath,
				"redis.server");
		port = Integer.valueOf(IoUtil.getPropertiesSingleValue(
				FilePathConfig.RedisPath, "redis.port"));
		password = IoUtil.getPropertiesSingleValue(FilePathConfig.RedisPath,
				"redis.password");
		maxTotal = Integer.valueOf(IoUtil.getPropertiesSingleValue(
				FilePathConfig.RedisPath, "redis.pool.maxTotal"));
		maxWaitMillis = Integer.valueOf(IoUtil.getPropertiesSingleValue(
				FilePathConfig.RedisPath, "redis.pool.maxWaitMillis"));
		maxIdle = Integer.valueOf(IoUtil.getPropertiesSingleValue(
				FilePathConfig.RedisPath, "redis.pool.maxIdle"));
		testOnBorrow = Boolean.valueOf(IoUtil.getPropertiesSingleValue(
				FilePathConfig.RedisPath, "redis.pool.testOnBorrow"));
		testOnReturn = Boolean.valueOf(IoUtil.getPropertiesSingleValue(
				FilePathConfig.RedisPath, "redis.pool.testOnReturn"));
		testWhileIdle = Boolean.valueOf(IoUtil.getPropertiesSingleValue(
				FilePathConfig.RedisPath, "redis.pool.testWhileIdle"));
		testOnBorrow = Boolean.valueOf(IoUtil.getPropertiesSingleValue(
				FilePathConfig.RedisPath, "redis.pool.setBlockWhenExhausted"));
	}
}
