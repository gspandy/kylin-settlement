package com.rkylin.settle.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * spring提供的 redis工具类
 */
@Service("redisTaskUtil")
public class RedisTaskUtil {
	private static Logger logger = LoggerFactory.getLogger(RedisTaskUtil.class);
	@Autowired
	private RedisTemplate<String, byte[]> redisTemplate;
	@Autowired
	private RedisConnectionFactory factory;
	/**
	 * 封锁定时任务
	 * @param id	redis对应定时任务的key
	 */
	public boolean doTaskClose(String id) {
		//5分钟
		Long seconds = 5L * 60L;
		return doTaskClose(id, seconds);
	}
	/**
	 * 封锁定时任务
	 * @param id	redis对应定时任务的key
	 */
	public boolean doTaskClose(String id, Long seconds) {
		//redis链接
		RedisConnection connection = null;
		//redis的key
		byte[] key = id.getBytes();
		//redis.setNX方法返回值
		boolean setIsSuccess = false;
		//时间戳
		Long timestamp = null;
		
		try {
			//获取redis链接
			connection = getRedisConnection();
			//向redis中存储
			setIsSuccess = connection.setNX(key, String.valueOf((new Date()).getTime()).getBytes());
			//逻辑判断死锁
//			if(!setIsSuccess) {
//				timestamp = Long.valueOf(new String(connection.get(key)));//获取value中的时间戳
//				if(isExpired(timestamp, seconds * 1000L)) {//判断是否失效
//					this.doTaskOpen(new String(key));//删除key
//					setIsSuccess = connection.setNX(key, String.valueOf((new Date()).getTime()).getBytes());//重新调用setNX
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("doTaskClose 方法发生异常", e);
		} finally {
			this.closeRedisConnection(connection);
		}
		
		boolean expireIsSuccess = false;
		
		if(setIsSuccess) expireIsSuccess = this.redisExpire(key, seconds);
		
		return setIsSuccess && expireIsSuccess;
	}
	/**
	 * 解锁定时任务
	 * @param id	redis对应定时任务的key
	 */
	public void doTaskOpen(String id) {
		byte[] key = id.getBytes();
		RedisConnection connection = null;
		
		try {
			connection = getRedisConnection();
			connection.del(key);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("doTaskOpen 方法发生异常", e);
		} finally {
			this.closeRedisConnection(connection);
		}
	}
	/**
	 * 设置redis key生效时间
	 * @param key
	 * @param seconds
	 * @return
	 */
	private boolean  redisExpire(byte[] key, Long seconds) {
		RedisConnection connection = null;
		boolean isSuccess = false;
		
		try {
			connection = getRedisConnection();
			isSuccess = connection.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("redisExpire 方法发生异常", e);
		} finally {
			closeRedisConnection(connection);
		}
		
		return isSuccess;
	}
	/**
	 * 获取redis链接
	 * @return
	 */
	private RedisConnection getRedisConnection() {
		if(factory == null) factory = redisTemplate.getConnectionFactory();
		return factory.getConnection();
	}
	/**
	 * 关闭redis链接
	 */
	private void closeRedisConnection(RedisConnection connection) {
		if(connection != null) connection.close();
	}
	/**
	 * 用value存储的时间戳判断key是否过期
	 * @param timestamp
	 * @param expire
	 * @return
	 */
	private boolean isExpired(Long timestamp, Long expire) {
		Date now = new Date();
		if(now.getTime() < (timestamp + expire)) {
			return true;
		} else{
			return false;
		}
	}

	/**
	 * 通过字符串的key获取value是字符串类型的值
	 * @param key
	 * @return
	 */
	public String getStrValByKey(String key) {
		byte[] resultArr = null;
		String val = null;
		byte[] keyArr = key.getBytes();
		RedisConnection connection = null;
		try {
			connection = getRedisConnection();
			resultArr = connection.get(keyArr);
			val =  new String(resultArr);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getStrValByKey 方法发生异常", e);
		} finally {
			this.closeRedisConnection(connection);
		}
		return val;
	}
	
}
