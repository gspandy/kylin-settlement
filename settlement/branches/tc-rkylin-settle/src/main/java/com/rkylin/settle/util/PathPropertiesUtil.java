package com.rkylin.settle.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PathPropertiesUtil {
	private static Log log = LogFactory.getLog(PathPropertiesUtil.class);
	private static Properties props;
	
	static {
		log.info(">>> >>> 开始初始化 PathPropertiesUtil");
		props = new Properties();//属性集合对象
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String filePath = rootPath + "path.properties";
		
		log.info(">>> >>> 加载 " + filePath + "配置文件");
		try {
			props.load(new FileInputStream(new File(filePath)));
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException", e);
			e.printStackTrace();
		}
		log.info("<<< <<< 完成初始化 PathPropertiesUtil");
	}
	
	public static Properties getPathProperties() {
		return props;
	}
}