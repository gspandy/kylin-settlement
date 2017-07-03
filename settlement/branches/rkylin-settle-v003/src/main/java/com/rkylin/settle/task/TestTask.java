package com.rkylin.settle.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("testTask")
public class TestTask {
	private static Logger logger = LoggerFactory.getLogger(TestTask.class);

	public static void main(String[] args) {
	}
	
	public void taskTestNormal() throws Exception {
		logger.info("start >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> taskTest taskTest");
		logger.info(">>> >>>");
		logger.info(">>> >>>");
		logger.info(">>> >>>");
		logger.info(">>> >>>");
		logger.info("end   >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> taskTest taskTest");
	}
	
	public void taskTest2minutes() throws Exception {
		logger.info("start >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> taskTest2minutes");
		Thread.sleep((int) (30 * 1000 * Math.random()));
		logger.info("1/2 ... ... ... taskTest2minutes");
		Thread.sleep((int) (30 * 1000 * Math.random()));
		logger.info("1 ... ... ... taskTest2minutes");
		Thread.sleep((int) (60 * 1000 * Math.random()));
		logger.info("3/2 ... ... ... taskTest2minutes");
		Thread.sleep((int) (60 * 1000 * Math.random()));
		logger.info("2 ... ... ... taskTest2minutes");
		logger.info("end   >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> taskTest2minutes");
	}
	
	public void taskTestException() throws Exception {
		logger.info("start >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> taskTestException");
		if(((Math.random() * 10) / 1) > 6) {int num = 1 / 0;}		
		logger.info("end   >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> >>> taskTestException");
	}
	
}
