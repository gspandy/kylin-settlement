package com.rkylin.settle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rkylin.settle.settleInterface.SettleWebInterface;

/**
 * 头寸信息管理
 * @author CaoYang
 *
 */
@Controller
@RequestMapping("/cav")
@Scope("prototype")
public class CancelAfterVerificationController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(CancelAfterVerificationController.class);
	@Autowired
	private SettleWebInterface settleWebInterface;
	/***
	 * 打开查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/open_create")
	public String openQuery() {
		return "/settlement/cav/create";
	}
	/**
	 * 生成头寸信息
	 * @param rootInstCd	机构号
	 * @param funcCode		交易码
	 * @param userId		用户ID
	 * @param productId		管理分组
	 * @param isAll			全表生成头寸
	 */
    @RequestMapping("/create")
    public void openView() {
    	//返回值
    	String msg = "fail";
    	try {
    		settleWebInterface.doCancelAfterVerification();
    		msg = "执行成功!";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:", e);
			msg = "失败!";
		}
    	
    	try {
			writeText(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:", e);
		}
    }
}