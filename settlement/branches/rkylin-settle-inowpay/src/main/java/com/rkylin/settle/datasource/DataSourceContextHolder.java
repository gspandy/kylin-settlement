package com.rkylin.settle.datasource;

/**
 * @ClassName: DataSourceContextHolder
 * @Description: 数据库切换工具类
 * @author: 王森
 * @date: 2017/6/5
 */
public class DataSourceContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDbType(String dbType) {
		contextHolder.set(dbType);
	}

	public static String getDbType() {
		return ((String) contextHolder.get());
	}

	public static void clearDbType() {
		contextHolder.remove();
	}
}
