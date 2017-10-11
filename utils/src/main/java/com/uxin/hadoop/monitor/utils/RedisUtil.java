package com.uxin.hadoop.monitor.utils;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisUtil {

	public static Jedis getJedis(String server, int port, String password) {
		Jedis jedis = new Jedis(server, port);
		jedis.auth(password);// 验证密码
		return jedis;
	}

	// ================== 以下是关于连接连接池的用法=============
	/***
	 * 
	 * @return
	 */
	public static ShardedJedis getJedisInShardedPool() {
		return shardedJedisPool.getResource();
	}

	/***
	 * 
	 * @return
	 */
	public static Jedis getJedisInByDefaultPool(String server, int port) {
		if (jedisPool == null) {
			jedisPool = new JedisPool(new JedisPoolConfig(), server, port);
		}
		return jedisPool.getResource();
	}

	/***
	 * 
	 * @return
	 */
	public static Jedis getJedisInPool() {
		return jedisPool.getResource();
	}

	/**
	 * 
	 * @param config
	 * @param server
	 * @param port
	 * @param timeout
	 */
	public static void initJedisPool(JedisPoolConfig config, String server,
			int port, int timeout) {
		if (config == null)
			config = new JedisPoolConfig();
		if (jedisPool == null)
			jedisPool = new JedisPool(config, server, port, timeout);
	}

	/**
	 * 
	 * @param config
	 * @param server
	 * @param port
	 */
	public static void initialShardedPool(JedisPoolConfig config,
			String server, int port) {
		// slave链接
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo(server, port, "master"));

		if (shardedJedisPool == null)
			shardedJedisPool = new ShardedJedisPool(config, shards);
	}

	/**
	 * 
	 * @param maxTotal
	 * @param maxIdle
	 * @param maxWaitMillis
	 * @param testOnBorrow
	 * @return
	 */
	public static JedisPoolConfig getJedisPoolConfig(int maxTotal, int maxIdle,
			long maxWaitMillis, boolean testOnBorrow) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		config.setTestOnBorrow(testOnBorrow);
		return config;
	}

	private static JedisPool jedisPool;// 非切片池
	private static ShardedJedisPool shardedJedisPool;// 切片池
}
