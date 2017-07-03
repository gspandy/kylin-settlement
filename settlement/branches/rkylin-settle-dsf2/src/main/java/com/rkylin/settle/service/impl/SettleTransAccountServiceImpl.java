package com.rkylin.settle.service.impl;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.service.SettleTransAccountService;
import com.rkylin.settle.util.RkylinMailUtil;

@Service("settleTransAccountService")
public class SettleTransAccountServiceImpl implements SettleTransAccountService {
	private static Logger logger = LoggerFactory.getLogger(SettleTransAccountServiceImpl.class);
	
	@Autowired
	SettleTransAccountManager settleTransAccountManager;
	
	@Override
	public void selectTestTransAndSumAmountYQZL() {
		this.selectTestTransAndSumAmountYQZL(null);
	}
	
	@Override
	public void selectTestTransAndSumAmountYQZL(Date date) {
		logger.info(">>> >>> >>> >>> 开始: 查询银企直联测试交易并汇总金额 selectTestTransAndSumAmountYQZL");
		Calendar calendar = Calendar.getInstance();
		
		Integer month = null;
		
		Date beginDate = null;
		Date endDate = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> testTransList = null;
		List<Map<String, Object>> testAmountSumList = null;
		
		if(date != null) {//查本月
			calendar.setTime(date);calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND, 0);
			month = calendar.get(Calendar.MONTH);
			beginDate = calendar.getTime();
			calendar.set(Calendar.MONTH, month + 1);
			endDate = calendar.getTime();
		} else {//查上个月
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND, 0);
			month = calendar.get(Calendar.MONTH);
			endDate = calendar.getTime();
			calendar.set(Calendar.MONTH, -- month);
			beginDate = calendar.getTime();
			
		}
		
		paramMap.put("beginDate", beginDate);
		paramMap.put("endDate", endDate);
		testTransList = settleTransAccountManager.selectTestTrans(paramMap);
		testAmountSumList = settleTransAccountManager.selectTestAmountSum(paramMap);
		
		try {
			this.editTestTransEmail(month, testTransList, testAmountSumList);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常:编辑测试交易汇总和明细信息邮件并发送", e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< <<< 结束: 查询银企直联测试交易并汇总金额 selectTestTransAndSumAmountYQZL");
	}
	
	/**
	 * 编辑测试交易汇总和明细信息邮件并发送
	 * @param month
	 * @param testTransList
	 * @param testAmountSumList
	 * @throws Exception
	 */
	private void editTestTransEmail(Integer month, List<Map<String, Object>> testTransList, List<Map<String, Object>> testAmountSumList) throws Exception {
		logger.info(">>> >>> >>> 开始: 编辑测试交易汇总和明细信息邮件并发送");		
		String[] mailCc = new String[]{"youyu@rongcapital.cn","sunruibin@rongcapital.cn"};
		String[] mailTo = new String[]{"caoyang@rkylin.com.cn"};
		
		String titl = "【" + (month + 1) + "】月 银企直联 测试交易汇总&明细信息";
		StringBuffer mailContext = new StringBuffer();
	
		mailContext.append("银企直联 测试交易汇总&明细信息");
		mailContext.append("<BR><BR>汇总:<BR>");
		for(Map<String, Object> map : testAmountSumList) {
			mailContext.append("【总条数:" + map.get("COUNT_NUM"));
			mailContext.append(", 总金额:" + map.get("SUM_AMOUNT"));
			mailContext.append(", 卡号:" + map.get("CARD_NO"));
			mailContext.append(", 姓名:" + map.get("TESTER"));
			mailContext.append(", 渠道编号:" + map.get("PAY_CHANNEL_ID"));
			mailContext.append(", 渠道名称:" + map.get("CHANNEL_NAME"));
			mailContext.append(", 协议号:" + map.get("MERCHANT_CODE") + "】");
			mailContext.append("<BR>");
		}
		mailContext.append("<BR><BR>明细:<BR>");
		for(Map<String, Object> map : testTransList) {
			mailContext.append("【订单号:" + map.get("TRANS_FLOW_NO"));
			mailContext.append(", 金额:" + map.get("TRANS_AMOUNT"));
			mailContext.append(", 卡号:" + map.get("CARD_NO"));
			mailContext.append(", 姓名:" + map.get("TESTER"));
			mailContext.append(", 渠道编号:" + map.get("PAY_CHANNEL_ID"));
			mailContext.append(", 渠道名称:" + map.get("CHANNEL_NAME"));
			mailContext.append(", 协议号:" + map.get("MERCHANT_CODE"));
			mailContext.append(", 交易时间:" + map.get("REQUEST_TIME"));
			mailContext.append(", 账期:" + map.get("SETTLE_TIME") + "】");
			mailContext.append("<BR>");
		}
		
		RkylinMailUtil.sendMail(titl, mailContext.toString(), mailTo, mailCc);
		logger.info("<<<　<<<　<<<　结束: 编辑测试交易汇总和明细信息邮件并发送");
	}
}
