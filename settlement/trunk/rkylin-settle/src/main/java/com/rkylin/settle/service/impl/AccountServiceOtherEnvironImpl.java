package com.rkylin.settle.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.settle.datasource.DataSourceContextHolder;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.logic.CollateOtherEnvironLogic;
import com.rkylin.settle.service.AccountOtherEnvironService;
import com.rkylin.settle.util.SettlementUtil;

@Service("accountOtherEnvironService")
public class AccountServiceOtherEnvironImpl implements AccountOtherEnvironService{
	private static Logger logger = LoggerFactory.getLogger(AccountServiceOtherEnvironImpl.class);
	@Autowired
	private CollateOtherEnvironLogic collageLogic;
	@Autowired
	private SettlementUtil settlementUtil;
	
	/**
	 * 其他环境对账
	 */
	@Override
	@Transactional(rollbackFor = SettleException.class, propagation = Propagation.NESTED)
	public void otherEnvironCollage(Date accountDate,String otherEnviron,String rule) throws Exception {
		logger.info(">>> >>> >>> >>> Service otherEnvironCollage 其他环境对账（"+otherEnviron+"环境）开始 ... ...");
		/*
		 * uat:uat环境
		 */
		DataSourceContextHolder.setDbType(otherEnviron);
		//*********账期的取得*********//
		if(accountDate == null) {
			accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", -1, "Date");
		}
    	//*********对账开始*********//
    	try {
    		
    		List<Map<String, Object>> tAccountList = collageLogic.querySettleTransAccount(accountDate);
    		List<Map<String, Object>> tDetailList = collageLogic.querySettleTransDetail(accountDate);
    		if(rule == null || "".equals(rule)){
    			//充值数据对账
        		collageLogic.doCollate(tAccountList, tDetailList,otherEnviron,"01");
        		//提现数据对账
        		collageLogic.doCollate(tAccountList, tDetailList,otherEnviron,"02");
    		}else{
    			collageLogic.doCollate(tAccountList, tDetailList,otherEnviron,rule);
    		}
    		
    	} catch (Exception e2) {
			logger.error("其他环境对账异常！" + e2.getMessage());
			e2.printStackTrace();
			throw new SettleException("其他环境对账异常");
		}
    	logger.info(">>> >>> >>> >>> Service otherEnvironCollage 其他环境对账（"+otherEnviron+"环境）结束 ... ...");
	}
	
}
