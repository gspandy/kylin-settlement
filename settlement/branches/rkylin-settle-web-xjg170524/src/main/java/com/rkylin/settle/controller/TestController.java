package com.rkylin.settle.controller;


import java.io.InputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rkylin.settle.settleInterface.SettleWebInterface;

/**
 * @Description: 交易结果管理
 * @author Yang
 * @Create Time: 2015-6-12下午12:59:48
 * @version V1.00
 */
@Controller
@RequestMapping("/test")
@Scope("prototype")
public class TestController extends BaseController {
	@Autowired
	private SettleWebInterface settleWebInterface;

	/***
	 * 测试
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/test_settle_web_dubbo")
	public String testSettleWebDubbo() throws Exception {
		System.out.println("开始 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		System.out.println("结束 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		return "";
	}
}
