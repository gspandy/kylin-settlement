package com.rkylin.settle.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.FinanaceAccountManager;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.manager.SettleFinanaceAccountManager;
import com.rkylin.settle.pojo.FinanaceAccountQuery;
import com.rkylin.settle.pojo.FinanaceAccount;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoQuery;
import com.rkylin.settle.pojo.SettleFinanaceAccount;
import com.rkylin.settle.pojo.SettleFinanaceAccountQuery;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.utils.SendSMS;

@Component("hTHLogic")
public class HTHLogic {
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(HTHLogic.class);
	
	@Autowired
	FinanaceAccountManager finanaceAccountManager;
	@Autowired
	SettleFinanaceAccountManager settlefinanaceAccountManager;
	@Autowired
	SettlementUtil settlementUtil;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	
	/***
	 * 读取每日帐户期末余额
	 * @param rootInstCd	机构号
	 * @param accountDate	账期
	 * @param hthType	银企直联类型  PAYQ：平安银行
	 * @return 提示信息 {code: 结果编号, msg: 运行结果信息}
	 */
	public Map<String,String> readFinAccountData(String rootInstCd, String hthType) {
		Map<String,String> rtnMap = new HashMap<String,String>();
		rtnMap.put("errMsg", "ok");
		rtnMap.put("errCode", "0000");
	
    	/**
    	 * 判断日中是否正常结束
    	 */
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
            logger.error("清洁算开始异常","日终没有正常结束，不能开始清洁算操作！");
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date accountDate = null;
        try {
            //DB中账期为当前日期T，计算出账期应为前一日T-1
            accountDate = (Date)settlementUtil.getAccountDate("yyyy-MM-dd",-1,"Date");
        } catch (Exception e2) {
            logger.error("银企直联类型为未知值！" + e2.getMessage());
            rtnMap.put("errCode", "0001");
            rtnMap.put("errMsg", "银企直联类型为未知值");
            return rtnMap;
        }
        logger.info("取得账期："+accountDate);
		
		FinanaceAccountQuery wherequery = new FinanaceAccountQuery();
		wherequery.setRootInstCd(rootInstCd);
		wherequery.setStatusId("1");
		if ("PAYQ".equals(hthType)) {
			wherequery.setGroupManage("'P000085','P000086'");
		} else {
            logger.error("计算账期异常！");
            rtnMap.put("errCode", "0001");
            rtnMap.put("errMsg", "计算账期异常");
            return rtnMap;
		}
		
		List<FinanaceAccount> resultList = new ArrayList<FinanaceAccount>();
		
		try {
			resultList = finanaceAccountManager.queryList(wherequery);
		} catch(Exception e) {
            logger.error("取得账户侧数据异常！"+e.getMessage());
		}
		
		SettleFinanaceAccount settleFinanaceAccount = new SettleFinanaceAccount();
		SettleFinanaceAccountQuery settleFinanaceAccountQuery = new SettleFinanaceAccountQuery();
		FinanaceAccount finanaceAccount = new FinanaceAccount();
		List<SettleFinanaceAccount> sfaList = new ArrayList<SettleFinanaceAccount>();
		
		for (int i=0;i<=resultList.size();i++ ) {
			finanaceAccount = resultList.get(i);

			settleFinanaceAccountQuery = new SettleFinanaceAccountQuery();
			settleFinanaceAccount = new SettleFinanaceAccount();
			
			settleFinanaceAccountQuery.setFinAccountId(finanaceAccount.getFinAccountId());
			settleFinanaceAccountQuery.setAccountDate(accountDate);

			settleFinanaceAccount.setRootInstCd(finanaceAccount.getRootInstCd());
			settleFinanaceAccount.setFinAccountId(finanaceAccount.getFinAccountId());
			settleFinanaceAccount.setFinAccountTypeId(finanaceAccount.getFinAccountTypeId());
			settleFinanaceAccount.setFinAccountName(finanaceAccount.getFinAccountName());
			settleFinanaceAccount.setAccountCode(finanaceAccount.getAccountCode());
			settleFinanaceAccount.setAccountRelateId(finanaceAccount.getAccountRelateId());
			settleFinanaceAccount.setGroupManage(finanaceAccount.getGroupManage());
			settleFinanaceAccount.setGroupSettle(finanaceAccount.getGroupSettle());
			settleFinanaceAccount.setReferUserId(finanaceAccount.getReferUserId());
			settleFinanaceAccount.setCurrency(finanaceAccount.getCurrency());
			settleFinanaceAccount.setAmount(finanaceAccount.getAmount());
			settleFinanaceAccount.setBalanceUsable(finanaceAccount.getBalanceUsable());
			settleFinanaceAccount.setBalanceSettle(finanaceAccount.getBalanceSettle());
			settleFinanaceAccount.setBalanceFrozon(finanaceAccount.getBalanceFrozon());
			settleFinanaceAccount.setBalanceOverLimit(finanaceAccount.getBalanceOverLimit());
			settleFinanaceAccount.setBalanceCredit(finanaceAccount.getBalanceCredit());
			settleFinanaceAccount.setStartTime(finanaceAccount.getStartTime());
			settleFinanaceAccount.setEndTime(finanaceAccount.getEndTime());
			settleFinanaceAccount.setBussControl(finanaceAccount.getBussControl());
			settleFinanaceAccount.setRemark(finanaceAccount.getRemark());
			settleFinanaceAccount.setStatusId(finanaceAccount.getStatusId());
			settleFinanaceAccount.setRecordMap(finanaceAccount.getRecordMap());
			settleFinanaceAccount.setAccountDate(accountDate);
			
			try {
				sfaList = settlefinanaceAccountManager.queryList(settleFinanaceAccountQuery);
				if (sfaList.size() > 0) {
					settlefinanaceAccountManager.updateFinanaceAccount(settleFinanaceAccount);
				} else {
					settlefinanaceAccountManager.saveFinanaceAccount(settleFinanaceAccount);
				}
			} catch(Exception e) {
				continue;
			}
		}
		return rtnMap;
	}
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
