package com.rkylin.settle.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rkylin.settle.settleInterface.SettleWebInterface;
import com.rop.constants.Constants;
import com.rop.constants.SpringBeanConstants;
import com.rop.response.Response;
import com.rop.service.IAPIService;
import com.rop.service.ISecurityService;
import com.rop.utils.LogUtils;
import com.rop.utils.ModelAndViewUtils;
import com.rop.utils.SpringBeanUtils;

@Controller
@RequestMapping("/rop")
@Scope("prototype")
public class RopController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(RopController.class);
	@Autowired
	private HttpServletRequest request;
	@Autowired
	@Resource(name=SpringBeanConstants.SECURITY_SERVICE)
	private ISecurityService securityService;
	@Autowired
	private SettleWebInterface settleWebInterface;
	/***
	 * ROP对账文件下载
	 * @return 提示信息
	 */
	@RequestMapping("/rop_download")
	public void rOPFileDown(String type, String batch, String invoicedate, String priOrPubKey, String fileType) {
		logger.info(">>> >>> >>> >>> 开始  从ROP下载文件 入参:{type:"+type+", batch:"+batch+", invoicedate:"+invoicedate+", priOrPubKey:"+priOrPubKey+", fileType:"+fileType+"}");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> resultMap = null;
		String errCode = null;
		String errMsg = null;
		String resultTxt = null;
		try {
			resultMap = settleWebInterface.rOPFileDown(type, batch, sdf.parse(invoicedate), priOrPubKey, fileType);
			errCode = resultMap.get("errCode");
			errMsg = resultMap.get("errMsg");
			resultTxt = "errMsg:" + errMsg + ", errCode:" + errCode;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("从ROP下载文件 异常:", e);
			resultTxt = "从ROP下载文件 异常";
		}
		try {
			writeText(resultTxt);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("从ROP下载文件, writeText方法 异常:", e);
		}
		logger.info("<<< <<< <<< <<< 结束 从ROP下载文件");
		return;
	}
}