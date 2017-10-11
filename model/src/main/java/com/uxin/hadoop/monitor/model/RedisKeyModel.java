package com.uxin.hadoop.monitor.model;

public class RedisKeyModel {

	/***
	 * 
	 * @param key
	 * @param timeout
	 *            过期时间，以秒为单位
	 * @param dataType
	 */
	public RedisKeyModel(String key, int expire, RedisDataType dataType) {
		this.Key = key;
		this.Expire = expire;
		this.DataType = dataType;
	}

	public String getKey() {
		return this.Key;
	}

	public int getExpire() {
		return this.Expire;
	}

	public RedisDataType getDataType() {
		return this.DataType;
	}

	private String Key;
	private int Expire;// 以秒为单位
	private RedisDataType DataType;

	public enum RedisDataType {
		string, list, map, set, hash
	}
}
