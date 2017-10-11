package com.uxin.hadoop.monitor.core;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.uxin.hadoop.monitor.config.RedisServerConfig;

public class RedisCore {

	private static JedisPool jedisPool = getJedisPool();

	public static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis jedis = jedisPool.getResource();
				jedis.auth(RedisServerConfig.password);
				return jedis;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 
	 */
	public static void returnResource(Jedis jedis) {
		jedisPool.returnResource(jedis);
	}

	/***
	 * 
	 * @return
	 */
	private static JedisPool getJedisPool() {
		if (jedisPool == null) {
			try {
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(RedisServerConfig.maxTotal);
				config.setMaxIdle(RedisServerConfig.maxIdle);
				config.setMaxWaitMillis(RedisServerConfig.maxWaitMillis);
				return new JedisPool(config, RedisServerConfig.server,
						RedisServerConfig.port);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			return jedisPool;
		}
		return null;
	}
}
