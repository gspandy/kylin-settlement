package com.rkylin.settle.task;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.service.impl.AccountServiceOtherEnvironImpl;

/***
 * 其他环境对账定时任务
 * @author Yang
 */
public class CollateOETask {
	private static Logger logger = LoggerFactory.getLogger(CollateOETask.class);
	@Autowired
	private AccountServiceOtherEnvironImpl accountOtherEnvironService;
	/***
	 * UAT环境对账定时任务
	 * 定时任务,  / 天
	 */
	public void uatCollate() {
		logger.info(">>> >>> >>> 开始: UAT环境对账定时任务");
		try {
			Date date = null;//参数传null，默认为当前日-1天
			//date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-06-08 00:00:00");
			accountOtherEnvironService.otherEnvironCollage(date, "uat",null);
		} catch (Exception e) {
			logger.info(">>> >>> >>> 异常: UAT环境对账定时任务");
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: UAT环境对账定时任务");
	}
}
