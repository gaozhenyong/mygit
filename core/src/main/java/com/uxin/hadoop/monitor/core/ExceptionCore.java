package com.uxin.hadoop.monitor.core;

import java.util.List;

import redis.clients.jedis.Jedis;

import com.uxin.hadoop.monitor.config.RedisDataKeyConfig;
import com.uxin.hadoop.monitor.model.ExceptionStoreModel;
import com.uxin.hadoop.monitor.utils.JsonUtil;

public class ExceptionCore {

	public static Jedis jedis = RedisCore.getJedis();

	/**
	 * 
	 * @param model
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void add(ExceptionStoreModel model) {
		if (jedis != null) {
			String json = null;
			try {
				json = JsonUtil.toJson(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (json != null && json.length() > 0) {
				try {
					RedisCore.getJedis().lpush(
							RedisDataKeyConfig.ExceptionList.getKey(), json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/***
	 * 
	 * @param key
	 * @return
	 */
	public static List<ExceptionStoreModel> getList(String key) {
		List<ExceptionStoreModel> list = null;
		if (key == null) {
			key = RedisDataKeyConfig.ExceptionList.getKey();
		}
		List<String> strlist = jedis.lrange(key, 0, -1);
		if (strlist != null) {
			list = JsonUtil.toList(strlist, ExceptionStoreModel.class);
		}
		return list;
	}
}
