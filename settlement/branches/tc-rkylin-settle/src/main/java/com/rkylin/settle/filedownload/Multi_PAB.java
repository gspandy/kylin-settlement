package com.rkylin.settle.filedownload;

import java.io.File;
import java.math.BigDecimal;
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

import com.rkylin.file.txt.TxtReader;
import com.rkylin.file.txt.TxtWriter;
import com.rkylin.gaterouter.dto.bankaccount.pad.AccountRelationQueryDto;
import com.rkylin.gaterouter.dto.bankaccount.pad.AccountRelationQueryRespDto;
import com.rkylin.gaterouter.dto.bankaccount.pad.AccountRelationQueryRespDto.SubAccountRelationRespDto;
import com.rkylin.gaterouter.dto.bankaccount.pad.AccountTransDetailQueryDto;
import com.rkylin.gaterouter.dto.bankaccount.pad.AccountTransDetailQueryRespDto;
import com.rkylin.gaterouter.dto.bankaccount.pad.AccountTransFlowQueryDto;
import com.rkylin.gaterouter.dto.bankaccount.pad.AccountTransFlowQueryRespDto;
import com.rkylin.gaterouter.service.PabAccountService;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.logic.ReadFileLogic;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.pojo.ParameterInfoQuery;
import com.rkylin.settle.util.SettlementUtil;

@Component("multi_PAB")
public class Multi_PAB {
	protected static Logger logger = LoggerFactory.getLogger(Multi_PAB.class);
	@Autowired
	PabAccountService pabAccountService;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	SettleTransAccountManager settleTransAccountManager;
	@Autowired
	ReadFileLogic readFileLogic;
	@Autowired
	SettlementUtil settlementUtil;
	/**
	 * 平安账户当日历史交易明细查询
	 * @param invoicedate	账期
	 * @param rootInstCd	机构协议
	 * @param accountNo		账号
	 * @return
	 * @throws Exception
	 */
	public Map<String,Map<String,String>> pab_4013(String invoicedate, String rootInstCd, String accountNo) throws Exception {
    	logger.info("平安账户当日历史交易明细查询，输入日期：["+ invoicedate +"]["+ rootInstCd +"]["+ accountNo +"] ————————————START————————————");
    	Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");
		//交易信息结构体
		Map<String,Map<String,String>> rtnMapD = new HashMap<String,Map<String,String>>();
		//多渠道系统查询交易接口返回值
		AccountTransDetailQueryRespDto rtnResp = new AccountTransDetailQueryRespDto();
		//多渠道系统查询交易接口入参
		AccountTransDetailQueryDto whereQuery = new AccountTransDetailQueryDto();	
    	/**
    	 * 获取账期
    	 */
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	String accountDate = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
//	    		keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
//	    		invoicedate = parameterInfoManager.queryList(keyList).get(0).getParameterValue();
	    		invoicedate = String.valueOf(settlementUtil.getAccountDate("yyyy-MM-dd", 0, "String"));
	    		accountDate = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", -1, "String"));
			} else {
	    		accountDate = invoicedate.replace("-", "");
	    	}
	    } catch (Exception e2) {
	    	logger.error("计算账期异常！" + e2.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "计算账期异常！");
			rtnMapD.put("rtnCode", rtnMap);
			return rtnMapD;
	    }
    	logger.info(">>> >>> 【平安账户当日历史交易明细查询】  取得的账期为 :"+ accountDate);

    	logger.info(">>> >>> 调用多渠道接口 accountTransDetailQuery");
		//报文共通头
		whereQuery.setSysNo(SettleConstants.MULTI_HAED);
		whereQuery.setTransCode(SettleConstants.MULTI_TRANS_CODE);
		whereQuery.setOrgNo(rootInstCd);
		whereQuery.setBusiCode(SettleConstants.MULTI_BUSI_CODE);
		whereQuery.setChannelNo(SettleConstants.MULTI_CHANNAL_NO_07);
		whereQuery.setSignType(1);
		whereQuery.setSignMsg(whereQuery.sign(SettleConstants.MULTI_KEY));
		//报文体
		whereQuery.setAccountNo(accountNo);
		whereQuery.setCurrency("CNY");
		whereQuery.setStartDate(accountDate);
		whereQuery.setEndDate(accountDate);
		//调用多渠道系统查询交易
		try {
	    	logger.info(">>> >>> ~");
			rtnResp = pabAccountService.accountTransDetailQuery(whereQuery);
			if (!"100000".equals(rtnResp.getReturnCode())) {
				logger.error("第一次调用, 调用多渠道接口失败！" +rtnResp.getReturnMsg());
				Thread.sleep(3000);
    	    	logger.info(">>> >>> 第二次调用~");
    			rtnResp = pabAccountService.accountTransDetailQuery(whereQuery);
    			if (!"100000".equals(rtnResp.getReturnCode())) {
    				logger.error("第二次调用, 调用多渠道接口失败！" +rtnResp.getReturnMsg());
    				Thread.sleep(3000);
        	    	logger.info(">>> >>> 第三次调用~");
        			rtnResp = pabAccountService.accountTransDetailQuery(whereQuery);
    			}
			}
			
			if (!"100000".equals(rtnResp.getReturnCode())) {
				rtnMap.put("errCode", "0000");
				rtnMap.put("errMsg", "调用多渠道接口失败！");
				rtnMapD.put("rtnCode", rtnMap);
		    	logger.error("第三次调用, 调用多渠道接口失败！" +rtnResp.getReturnMsg());
			} else {
				rtnMapD = editDate(rtnResp);
			}
		} catch (Exception e) {
	    	logger.error("调用多渠道接口accountTransDetailQuery异常！" + e.getMessage());
		}

    	return rtnMapD;
	}
	
	/***
	 * 编辑多渠道查询交易接口返回值 AccountTransDetailQueryRespDto => Map<String,Map<String,String>>
	 * @param rtnResp
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Map<String,String>> editDate(AccountTransDetailQueryRespDto rtnResp) {
		Map subMap = new HashMap();
		Map<String,Map<String,String>> editMap = new HashMap<String,Map<String,String>>();
		
		if (rtnResp.getTotalCount() == 0) {
			subMap.put("errCode", "0001");
			subMap.put("errMsg", "取得数据件数为0 ！");
			editMap.put("rtnCode", subMap);
			return editMap;
		}
		
		Map writerMap = null;
		AccountTransDetailQueryRespDto.TransDetailRespDto subBean = null;
		BigDecimal amount = new BigDecimal("0");
		BigDecimal amount_post = new BigDecimal("0");
		BigDecimal con100 = new BigDecimal("100");
		for (int i = 0; i < rtnResp.getTransDetailRespDtoList().size(); i++) {
			subBean = rtnResp.getTransDetailRespDtoList().get(i);
			writerMap = new HashMap();
			if ("FEE".equals(subBean.getSummary())) {
				continue;
			}
			writerMap.put("F_1", subBean.getAccountDate()+subBean.getTransTime());
			writerMap.put("F_2", subBean.getHostTransNo());
			writerMap.put("F_3", "1");
			writerMap.put("F_4", subBean.getPayerAccountNo());
			writerMap.put("F_5", subBean.getPayerAccountName());
			writerMap.put("F_6", " ");
			writerMap.put("F_7", subBean.getDcFlag());
			writerMap.put("F_8", subBean.getCurrency());
			amount = new BigDecimal(subBean.getTransPayment());
			amount = amount.multiply(con100);
			amount = amount.setScale(0,BigDecimal.ROUND_HALF_UP);
			writerMap.put("F_9", amount);
			writerMap.put("F_10", "N");
			writerMap.put("F_11", subBean.getReceiverAccountNo());
			writerMap.put("F_12", subBean.getReceiverAccountName());
			writerMap.put("F_13", subBean.getSummary());
			amount = new BigDecimal(subBean.getTransFee());
			amount_post = new BigDecimal(subBean.getPostFee());
			amount = amount.multiply(con100);
			amount_post = amount_post.multiply(con100);
			amount = amount.add(amount_post);
			amount = amount.setScale(0,BigDecimal.ROUND_HALF_UP);
			writerMap.put("F_14", amount);
			writerMap.put("F_15", "4013");
			writerMap.put("F_16", "16");
			if (editMap.containsKey(subBean.getHostTransNo())) {
				subMap = new HashMap();
				subMap = editMap.get(subBean.getHostTransNo());
				if ("C".equals(subBean.getDcFlag())) {
					subMap.put("F_1", subBean.getAccountDate()+subBean.getTransTime());
					subMap.put("F_2", subBean.getHostTransNo());
					subMap.put("F_3", "1");
					subMap.put("F_4", subBean.getPayerAccountNo());
					subMap.put("F_5", subBean.getPayerAccountName());
					subMap.put("F_6", " ");
					subMap.put("F_7", subBean.getDcFlag());
					subMap.put("F_8", subBean.getCurrency());
					amount = new BigDecimal(subBean.getTransPayment());
					amount = amount.multiply(con100);
					amount = amount.setScale(0,BigDecimal.ROUND_HALF_UP);
					subMap.put("F_9", amount);
					subMap.put("F_10", "N");
					subMap.put("F_11", subBean.getReceiverAccountNo());
					subMap.put("F_12", subBean.getReceiverAccountName());
					subMap.put("F_13", subBean.getSummary());
					amount = new BigDecimal(subBean.getTransFee());
					amount_post = new BigDecimal(subBean.getPostFee());
					amount = amount.multiply(con100);
					amount_post = amount_post.multiply(con100);
					amount = amount.add(amount_post);
					amount = amount.setScale(0,BigDecimal.ROUND_HALF_UP);
					subMap.put("F_14", amount);
					subMap.put("F_15", "4013");
					subMap.put("F_16", "16");
				} else {
				}
			} else {
				editMap.put(subBean.getHostTransNo(), writerMap);
			}
		}
		return editMap;
	}
	
	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	public void editNewCollateFile(List<String> fileNameL,String accountDate2,String accountDate,String merchantCode) {
		String fileName = "";
		File collFile = null;
    	TxtReader txtReader = new TxtReader();
    	TxtWriter txtWriter = new TxtWriter();
    	List<Map> fileList = new ArrayList<Map>();
    	List<Map> headList = new ArrayList<Map>();
    	List<Map> tailList = new ArrayList<Map>();
    	Map fileMap;
    	List<Map> fileListSub;
    	Map fileMapSub;
    	int jj = 0;
    	int zz = 1;
		for (int i =0;i<fileNameL.size();i++) {
			fileName = fileNameL.get(i);
			fileListSub = new ArrayList<Map>();
			collFile = new File(fileName);
			if (!collFile.exists()) {
				continue;
			}
			try {
				txtReader.setEncode("GBK");
				fileListSub = txtReader.txtreader(collFile , SettleConstants.DEDT_SPLIT4);
			} catch(Exception e) {
				logger.error(">>> >>>对账文件读取异常！" + e.getMessage());
				return;
			}
			
			if (fileListSub == null || fileListSub.size() ==0) {
				continue;
			}
			fileMapSub = new HashMap();
			for (int j=1;j<=fileListSub.size();j++) {
				fileMap = new HashMap();
				fileMapSub = fileListSub.get(j-1);
				if (fileMapSub.size() ==4 || fileMapSub.size() ==1) {
					continue;
				}
				jj = 0;
				zz = 1;
				for (int z=0;z<fileMapSub.size();z++) {
					fileMap.put("F_"+zz, fileMapSub.get("L_"+jj));
					jj++;
					zz++;
				}
				//fileMap.put("F_"+zz, fileName.substring(fileName.length()-16, fileName.length()-12));
				fileList.add(fileMap);
			}
		}
		
		Map configMap = new HashMap();
		String localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator + accountDate +"_PAB_BankPayment_05_"+merchantCode+".txt";
		configMap.put("FILE", localfilennew);
		
		fileMap = new HashMap();
		fileMap.put("F_1", "START");
		headList.add(fileMap);
		configMap.put("REPORT-HEAD", headList);
		
		fileMap = new HashMap();
		fileMap.put("F_1", "END");
		tailList.add(fileMap);
		configMap.put("REPORT-TAIL", tailList);
		
		try {
			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT4, "GBK");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage());
			return;
		}
	}
	
	/**
	 * 平安账户主子账户关系
	 */
	public Map<String, Object> pab_AccountFind(String invoicedate,String rootInstCd,String accountNo) throws Exception {
    	logger.info("平安账户主子账户关系，输入日期：["+ invoicedate +"]["+ rootInstCd +"]["+ accountNo +"] ————————————START————————————");
    	Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");
		
		AccountRelationQueryRespDto rtnResp = new AccountRelationQueryRespDto();
		AccountRelationQueryDto whereQuery = new AccountRelationQueryDto();
		
		//报文共通头
		whereQuery.setSysNo(SettleConstants.MULTI_HAED);
		whereQuery.setTransCode(SettleConstants.MULTI_TRANS_CODE);
		whereQuery.setOrgNo(rootInstCd);
		whereQuery.setBusiCode(SettleConstants.MULTI_BUSI_CODE);
		whereQuery.setChannelNo(SettleConstants.MULTI_CHANNAL_NO_06);
		whereQuery.setSignType(1);
		whereQuery.setSignMsg(whereQuery.sign(SettleConstants.MULTI_KEY));
		
		//报文体
		whereQuery.setAccountNo(accountNo);
		whereQuery.setCurrency("CNY");
		
		try {
	    	logger.info(">>> >>> 第一次调用~");
			rtnResp = pabAccountService.accountRelationQuery(whereQuery);
			if (!"100000".equals(rtnResp.getReturnCode())) {
				Thread.sleep(1000);
    	    	logger.info(">>> >>> 第二次调用~");
    			rtnResp = pabAccountService.accountRelationQuery(whereQuery);
    			if (!"100000".equals(rtnResp.getReturnCode())) {
    				Thread.sleep(1000);
        	    	logger.info(">>> >>> 第三次调用~");
        			rtnResp = pabAccountService.accountRelationQuery(whereQuery);
    			}
			}
			if (!"100000".equals(rtnResp.getReturnCode())) {
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "调用取得上游账户余额失败！");
				return rtnMap;
			}
		} catch (Exception e) {
	    	logger.error("调用多渠道接口accountRelationQuery异常！" + e.getMessage());
		}
		
		if (rtnResp.getTotalCount() == 0) {
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "调用取得上游账户余额失败！");
			return rtnMap;
		}
		
		List<SubAccountRelationRespDto> subList = rtnResp.getSubAccountRelationRespDtoList();
		SubAccountRelationRespDto subBean = null;
		Map<String,Object> settleTransAccountMap = new HashMap<String,Object>();
		settleTransAccountMap.put("INTER_MERCHANT_CODE", rtnResp.getAccountNo());
		settleTransAccountMap.put("BATCH_NO", rtnResp.getAccountName());
		settleTransAccountMap.put("CURRENCY", rtnResp.getCurrency());
		settleTransAccountMap.put("OBLIGATE1", rtnResp.getCurrency());
		settleTransAccountMap.put("REMARK", rtnResp.getLastModifyDate()+"|"+rtnResp.getOrgNum()+"|"+rtnResp.getTotalCount());
		
		settleTransAccountMap.put("MERCHANT_CODE", rootInstCd);
		settleTransAccountMap.put("TRANS_TYPE", "YQYE");
		settleTransAccountMap.put("SETTLE_TIME", invoicedate+ " 00:00:00");
		settleTransAccountMap.put("PAY_CHANNEL_ID", SettleConstants.PAY_CHANNEL_ID_PAB);
		settleTransAccountMap.put("READ_TYPE", SettleConstants.ACCOUNT_TYPE_PAB);
		
		for (int i=0;i<subList.size();i++) {
			subBean = subList.get(i);
			settleTransAccountMap.put("TRANS_AMOUNT", subBean.getBalance());
			settleTransAccountMap.put("ORDER_NO", subBean.getSubAccountNo());
			settleTransAccountMap.put("OBLIGATE2", subBean.getSubAccountName());
			if ("N".equals(subBean.getStatus())) {
				settleTransAccountMap.put("STATUS_ID", "1");
				settleTransAccountMap.put("READ_STATUS_ID", "1");
			} else {
				settleTransAccountMap.put("STATUS_ID", "0");
				settleTransAccountMap.put("READ_STATUS_ID", "0");
			}
			readFileLogic.insertAndUpdateSettleTransAccount(settleTransAccountMap);
		}
		return rtnMap;
	}
	 
	/**
	 * 查询账户历史余额 4012
	 */
//	public Map<String, Object> pab_AccountHisB(String invoicedate,String rootInstCd,String accountNo) throws Exception {
//    	logger.info("平安账户历史余额查询，输入日期：["+ invoicedate +"]["+ rootInstCd +"]["+ accountNo +"] ————————————START————————————");
//    	Map<String, Object> rtnMap = new HashMap<String, Object>();
//		rtnMap.put("errCode", "0000");
//		rtnMap.put("errMsg", "成功");
//		
//		HistoryBalanceQueryRespDto rtnResp = new HistoryBalanceQueryRespDto();
//		HistoryBalanceQueryDto whereQuery = new HistoryBalanceQueryDto();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat formatter1 = new SimpleDateFormat("hhmmssSSS");
//        
//        /**
//    	 * 获取账期
//    	 */
//    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
//    	SettlementUtil settlementUtil = new SettlementUtil();
//    	String accountDate = "";
//    	String accountDate2 = "";
//    	try {
//	    	if (invoicedate == null || "".equals(invoicedate)) {
//	    		keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
//	    		invoicedate = parameterInfoManager.queryList(keyList).get(0).getParameterValue();
//				/*
//				 * settlementUtil.getAccountDate(String fromat,int step,String returnType)
//				 * 注入 parameterInfoManager对账失败
//				 */
//	    		accountDate = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", -1, "String"));
//				accountDate2 = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", 0, "String"));
//	    	} else {
//	    		accountDate = invoicedate.replace("-", "");
//	    		accountDate2 = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", 1, "String"));
//	    	}
//	    } catch (Exception e2) {
//	    	logger.error("计算账期异常！" + e2.getMessage());
//			rtnMap.put("errCode", "0001");
//			rtnMap.put("errMsg", "计算账期异常！");
//			return rtnMap;
//	    }
//    	logger.info(">>> >>> 【平安账户历史余额查询】  取得的账期为 :"+ accountDate);
//        
//        
//		logger.info(">>> >>> 取得账户内平安银行对应账户信息");
//    	List<SettleFinanaceAccount> resList = new ArrayList<SettleFinanaceAccount>();
//    	SettleFinanaceAccountQuery where2Query = new SettleFinanaceAccountQuery();
//	
//    	where2Query.setAccountDate(formatter.parse(accountDate));
//    	where2Query.setStatusId("1");
//    	where2Query.setRootInstCd(rootInstCd);
//    	if (accountNo != null && "".equals(accountNo)) {
//    		where2Query.setReferUserId(accountNo);
//    	}
//    	
//    	resList = settleFinanaceAccountManager.queryList(where2Query);
//    	
//    	if (resList == null || resList.size() == 0) {
//        	logger.info(">>> >>> 取得账户件数为零！");
//			rtnMap.put("errCode", "0001");
//			rtnMap.put("errMsg", "取得账户件数为零！");
//			return rtnMap;
//    	}
//
//    	SettleFinanaceAccount subBean1 = new SettleFinanaceAccount();
//    	SettleFinanaceAccount updateBean = new SettleFinanaceAccount();
//    	List<SettleFinanaceAccount> updateList = new ArrayList<SettleFinanaceAccount>();
//		
//    	for (int i=0;i<resList.size();i++) {
//    		subBean1 = new SettleFinanaceAccount();
//    		subBean1 = resList.get(i);
//    		whereQuery = new HistoryBalanceQueryDto();
//    		//报文共通头
//    		whereQuery.setSysNo(SettleConstants.MULTI_HAED);
//    		whereQuery.setTransCode(SettleConstants.MULTI_TRANS_CODE);
//    		whereQuery.setOrgNo(rootInstCd);
//    		whereQuery.setBusiCode(SettleConstants.MULTI_BUSI_CODE);
//    		whereQuery.setChannelNo(SettleConstants.MULTI_CHANNAL_NO_12);
//    		whereQuery.setSignType(1);
//    		whereQuery.setSignMsg(whereQuery.sign(SettleConstants.MULTI_KEY));
//    		
//			//报文体
//			whereQuery.setAccountNo(subBean1.getReferUserId());
//			whereQuery.setReserve("对账用查询！"); 
//			whereQuery.setHisDate(accountDate);
//			
//			try {
//		    	logger.info(">>> >>> 第一次调用~");
//				updateBean = subBean1;
//				updateBean.setRemark("5:查询历史余额成功");//状态为5，代表从上游查询交易成功
//				rtnResp = pabAccountService.historyBalanceQuery(whereQuery);
//				if (!"100000".equals(rtnResp.getReturnCode())) {
//					Thread.sleep(1000);
//	    	    	logger.info(">>> >>> 第二次调用~");
//	    			rtnResp = pabAccountService.historyBalanceQuery(whereQuery);
//	    			if (!"100000".equals(rtnResp.getReturnCode())) {
//	    				Thread.sleep(1000);
//	        	    	logger.info(">>> >>> 第三次调用~");
//	        			rtnResp = pabAccountService.historyBalanceQuery(whereQuery);
//	    			}
//				}
//    			if (!"100000".equals(rtnResp.getReturnCode())) {
//    				updateBean = subBean1;
//    				updateBean.setRemark("4:查询历史余额失败");//状态为4，代表从上游查询交易失败
//    			} else {
//	    			Map<String,Object> settleTransAccountMap = new HashMap<String,Object>();
//	    			settleTransAccountMap.put("INTER_MERCHANT_CODE", rtnResp.getAccountNo());
//	    			settleTransAccountMap.put("CURRENCY", rtnResp.getCurrency());
//	    			settleTransAccountMap.put("OBLIGATE1", rtnResp.getCcyType());
//	    			
//	    			settleTransAccountMap.put("MERCHANT_CODE", rootInstCd);
//	    			settleTransAccountMap.put("TRANS_TYPE", "YQYE");
//	    			settleTransAccountMap.put("SETTLE_TIME", invoicedate+ " 00:00:00");
//	    			settleTransAccountMap.put("PAY_CHANNEL_ID", SettleConstants.PAY_CHANNEL_ID_PAB);
//	    			settleTransAccountMap.put("READ_TYPE", SettleConstants.ACCOUNT_TYPE_PAB);
//	    			
//    				settleTransAccountMap.put("TRANS_AMOUNT", rtnResp.getHisBalance());
//    				settleTransAccountMap.put("SETTLE_AMOUNT", rtnResp.getHisBookBalance());
//    				settleTransAccountMap.put("ORDER_NO", rtnResp.getAccountNo());
//					settleTransAccountMap.put("STATUS_ID", "1");
//					settleTransAccountMap.put("READ_STATUS_ID", "1");
//    				readFileLogic.insertAndUpdateSettleTransAccount(settleTransAccountMap);
//    			}
//				updateList.add(updateBean);
//			} catch (Exception e) {
//		    	logger.error("调用多渠道接口historyBalanceQuery异常！" + e.getMessage());
//				rtnMap.put("errCode", "0001");
//				rtnMap.put("errMsg", "调用多渠道接口historyBalanceQuery异常！");
//			}
//    	}
//
//    	if (updateList.size() > 0) {
//	    	logger.info("调用接口失败件数：" + updateList.size());
//	    	updateBean = new SettleFinanaceAccount();
//	    	for (int i=0;i<updateList.size();i++) {
//	    		updateBean = updateList.get(i);
//	    		updateBean.setUpdatedTime(null);
//	    		updateBean.setCreatedTime(null);
//	        	settleFinanaceAccountManager.updateFinanaceAccount(updateBean);
//	    	}
//    	}
		
//		return rtnMap;
//	}
	
	/**
	 * 查询账户实时余额 4001
	 */
//	public Map<String, Object> pab_Account_4001(String rootInstCd, String accountNo) throws Exception {
//    	logger.info("平安账户实时余额查询，["+ accountNo +"] ————————————START————————————");
//    	Map<String, Object> rtnMap = new HashMap<String, Object>();
//		rtnMap.put("errCode", "0000");
//		rtnMap.put("errMsg", "成功");
//		
//		AccountBalanceQueryRespDto rtnResp = new AccountBalanceQueryRespDto();
//		AccountBalanceQueryDto whereQuery = new AccountBalanceQueryDto();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat formatter1 = new SimpleDateFormat("hhmmssSSS");
//
//		whereQuery = new AccountBalanceQueryDto();
//		//报文共通头
//		whereQuery.setSysNo(SettleConstants.MULTI_HAED);
//		whereQuery.setTransCode(SettleConstants.MULTI_TRANS_CODE);
//		whereQuery.setOrgNo(rootInstCd);
//		whereQuery.setBusiCode(SettleConstants.MULTI_BUSI_CODE);
//		whereQuery.setChannelNo(SettleConstants.MULTI_CHANNAL_NO_04);
//		whereQuery.setSignType(1);
//		whereQuery.setSignMsg(whereQuery.sign(SettleConstants.MULTI_KEY));
//		
//		//报文体
//		whereQuery.setAccountNo(accountNo);
//		whereQuery.setCurrency("CNY");
//		
//		try {
//	    	logger.info(">>> >>> 第一次调用~");
//			rtnResp = pabAccountService.accountBalanceQuery(whereQuery);
//			if (!"100000".equals(rtnResp.getReturnCode())) {
//				Thread.sleep(1000);
//    	    	logger.info(">>> >>> 第二次调用~");
//    			rtnResp = pabAccountService.accountBalanceQuery(whereQuery);
//    			if (!"100000".equals(rtnResp.getReturnCode())) {
//    				Thread.sleep(1000);
//        	    	logger.info(">>> >>> 第三次调用~");
//        			rtnResp = pabAccountService.accountBalanceQuery(whereQuery);
//    			}
//			}
//			if (!"100000".equals(rtnResp.getReturnCode())) {
//				rtnMap.put("errCode", "0000");
//				rtnMap.put("errMsg", "余额取得失败！");
//				rtnMap.put("balance", "");
//			} else {
//				rtnMap.put("accountNo", rtnResp.getAccountNo());
//				rtnMap.put("accountName", rtnResp.getAccountName());
//				rtnMap.put("currency", rtnResp.getCurrency());
//				rtnMap.put("ccyType", rtnResp.getCcyType());
//				rtnMap.put("balance", rtnResp.getBalance());
//				rtnMap.put("totalAmount", rtnResp.getTotalAmount());
//			}
//		} catch (Exception e) {
//	    	logger.error("调用多渠道接口accountBalanceQuery异常！" + e.getMessage());
//			rtnMap.put("errCode", "0001");
//			rtnMap.put("errMsg", "调用多渠道接口accountBalanceQuery异常！");
//		}
//    	logger.info("平安账户实时余额查询，["+ accountNo +"] ————————————END————————————");
//		return rtnMap;
//	}
	/**
	 * 查询账户实时余额 C008
	 */
//	public Map<String, Object> pab_Account_C008(String rootInstCd, String accountNo, String subAccountNo) throws Exception {
//    	logger.info("平安账户实时余额查询，["+ accountNo +"] ————————————START————————————");
//    	Map<String, Object> rtnMap = new HashMap<String, Object>();
//		rtnMap.put("errCode", "0000");
//		rtnMap.put("errMsg", "成功");
//		
//		SubAccountBalanceQueryRespDto rtnResp = new SubAccountBalanceQueryRespDto();
//		SubAccountBalanceQueryDto whereQuery = new SubAccountBalanceQueryDto();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat formatter1 = new SimpleDateFormat("hhmmssSSS");
//
//		whereQuery = new SubAccountBalanceQueryDto();
//		//报文共通头
//		whereQuery.setSysNo(SettleConstants.MULTI_HAED);
//		whereQuery.setTransCode(SettleConstants.MULTI_TRANS_CODE);
//		whereQuery.setOrgNo(rootInstCd);
//		whereQuery.setBusiCode(SettleConstants.MULTI_BUSI_CODE);
//		whereQuery.setChannelNo(SettleConstants.MULTI_CHANNAL_NO_05);
//		whereQuery.setSignType(1);
//		whereQuery.setSignMsg(whereQuery.sign(SettleConstants.MULTI_KEY));
//		
//		//报文体
//		whereQuery.setAccountNo(accountNo);
//		whereQuery.setSubAccountNo(subAccountNo);
//		whereQuery.setCurrency("CNY");
//		
//		try {
//	    	logger.info(">>> >>> 第一次调用~");
//			rtnResp = pabAccountService.subAccountBalanceQuery(whereQuery);
//			if (!"100000".equals(rtnResp.getReturnCode())) {
//				Thread.sleep(1000);
//    	    	logger.info(">>> >>> 第二次调用~");
//    			rtnResp = pabAccountService.subAccountBalanceQuery(whereQuery);
//    			if (!"100000".equals(rtnResp.getReturnCode())) {
//    				Thread.sleep(1000);
//        	    	logger.info(">>> >>> 第三次调用~");
//        			rtnResp = pabAccountService.subAccountBalanceQuery(whereQuery);
//    			}
//			}
//			if (!"100000".equals(rtnResp.getReturnCode())) {
//				rtnMap.put("errCode", "0000");
//				rtnMap.put("errMsg", "余额取得失败！");
//				rtnMap.put("balance", "");
//			} else {
//				rtnMap.put("accountNo", rtnResp.getAccountNo());
//				rtnMap.put("subAccountNo", rtnResp.getSubAccountNo());
//				rtnMap.put("subaccountName", rtnResp.getSubAccountName());
//				rtnMap.put("currency", rtnResp.getCurrency());
//				rtnMap.put("balance", rtnResp.getBalance());
//			}
//		} catch (Exception e) {
//	    	logger.error("调用多渠道接口accountBalanceQuery异常！" + e.getMessage());
//			rtnMap.put("errCode", "0001");
//			rtnMap.put("errMsg", "调用多渠道接口accountBalanceQuery异常！");
//		}
//    	logger.info("平安账户实时余额查询，["+ accountNo +"] ————————————END————————————");
//		return rtnMap;
//	}
	/**
	 * 平安账户交易明细查询_C006
	 */
	public Map<String,Map<String,String>> pab_C006(String invoicedate,String rootInstCd,String accountNo) throws Exception {
    	logger.info("平安账户当日历史交易明细查询，输入日期：["+ invoicedate +"]["+ rootInstCd +"]["+ accountNo +"] ————————————START————————————");
    	Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");
		Map<String,Map<String,String>> rtnMapD = new HashMap<String,Map<String,String>>();
		
		AccountTransFlowQueryRespDto  rtnResp = new AccountTransFlowQueryRespDto();
		AccountTransFlowQueryDto  whereQuery = new AccountTransFlowQueryDto ();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("hhmmssSSS");
		
		
    	/**
    	 * 获取账期
    	 */
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	String accountDate = "";
    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
//	    		keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
//	    		invoicedate = parameterInfoManager.queryList(keyList).get(0).getParameterValue();
	    		invoicedate = String.valueOf(settlementUtil.getAccountDate("yyyy-MM-dd", 0, "String"));
				/*
				 * settlementUtil.getAccountDate(String fromat,int step,String returnType)
				 * 注入 parameterInfoManager对账失败
				 */
	    		accountDate = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", -1, "String"));
				accountDate2 = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", 0, "String"));
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    		accountDate2 = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", 1, "String"));
	    	}
	    } catch (Exception e2) {
	    	logger.error("计算账期异常！" + e2.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "计算账期异常！");
			rtnMapD.put("rtnCode", rtnMap);
			return rtnMapD;
	    }
    	logger.info(">>> >>> 【平安账户当日历史交易明细查询】  取得的账期为 :"+ accountDate);

    	logger.info(">>> >>> 调用多渠道接口accountTransDetailQuery");
//    	SettleFinanaceAccount subBean = new SettleFinanaceAccount();
    	List<String> fileList = new ArrayList<String>();
    	String fileTag = formatter1.format(new Date());
		
		//报文共通头
		whereQuery.setSysNo(SettleConstants.MULTI_HAED);
		whereQuery.setTransCode(SettleConstants.MULTI_TRANS_CODE);
		whereQuery.setOrgNo(rootInstCd);
		whereQuery.setBusiCode(SettleConstants.MULTI_BUSI_CODE);
		whereQuery.setChannelNo(SettleConstants.MULTI_CHANNAL_NO_08);
		whereQuery.setSignType(1);
		whereQuery.setSignMsg(whereQuery.sign(SettleConstants.MULTI_KEY));
		
		//报文体
		whereQuery.setAccountNo(accountNo);
		whereQuery.setStartDate(accountDate);
		whereQuery.setEndDate(accountDate);
		// whereQuery.setReserve(""); //非必入项目

		try {
	    	logger.info(">>> >>> 第一次调用~");
			rtnResp = pabAccountService.accountTransFlowQuery(whereQuery);
			if (!"100000".equals(rtnResp.getReturnCode())) {
				Thread.sleep(1000);
    	    	logger.info(">>> >>> 第二次调用~");
    			rtnResp = pabAccountService.accountTransFlowQuery(whereQuery);
    			if (!"100000".equals(rtnResp.getReturnCode())) {
    				Thread.sleep(1000);
        	    	logger.info(">>> >>> 第三次调用~");
        			rtnResp = pabAccountService.accountTransFlowQuery(whereQuery);
    			}
			}

			if (!"100000".equals(rtnResp.getReturnCode())) {
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "调用多渠道C006接口失败！"+rtnResp.getReturnMsg());
		    	logger.error("调用多渠道C006接口失败！"+rtnResp.getReturnMsg());
				rtnMapD.put("rtnCode", rtnMap);
		    	return rtnMapD;
			} else {
				rtnMapD = editDate_C006(rtnResp);
			}
		} catch (Exception e) {
	    	logger.error("调用多渠道接口accountTransDetailQuery异常！" + e.getMessage());
		}
    	
    	return rtnMapD;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Map<String,String>> editDate_C006(AccountTransFlowQueryRespDto rtnResp) {
		boolean rtnflg = true;
		Map<String,Map<String,String>> editMap = new HashMap<String,Map<String,String>>();
		Map subMap = new HashMap<String,String>();
    	TxtWriter txtWriter = new TxtWriter();
//		String localPath = SettleConstants.FILE_PATH +accountDate2 + File.separator;
		List<Map> writerList = new ArrayList<Map>();
		List<Map> headList = new ArrayList<Map>();
		List<Map> tailList = new ArrayList<Map>();
		
		if (rtnResp == null || rtnResp.getTotalAcount() == 0) {
			subMap.put("errCode", "0001");
			subMap.put("errMsg", "取得数据件数为0 ！");
			editMap.put("rtnCode", subMap);
			return editMap;
		}
		logger.error(">>> >>>C006查询流水件数："+rtnResp.getTotalAcount());
		
		Map writerMap = null;
		AccountTransFlowQueryRespDto.TransFlowDetailRespDto subBean = null;
		BigDecimal amount = new BigDecimal("0");
		BigDecimal con100 = new BigDecimal("100");
		for (int i=0;i<rtnResp.getTransFlowDetailRespDtoList().size();i++) {
			subBean = rtnResp.getTransFlowDetailRespDtoList().get(i);
			writerMap = new HashMap();	
			writerMap.put("F_1", subBean.getAccountingDate()+subBean.getTransTime());
			writerMap.put("F_2", subBean.getBankLogNo());
			writerMap.put("F_3", subBean.getBankOrderNo());
			writerMap.put("F_4", subBean.getAccountNo());
			writerMap.put("F_5", subBean.getAccountName());
			writerMap.put("F_6", subBean.getSubAccountName()==null?" ":subBean.getSubAccountName());
			writerMap.put("F_7", subBean.getDcFlag());
			writerMap.put("F_8", subBean.getCurrency());
			amount = new BigDecimal(subBean.getTransAmount());
			amount = amount.multiply(con100);
			amount = amount.setScale(0,BigDecimal.ROUND_HALF_UP);
			writerMap.put("F_9", amount.toString());
			writerMap.put("F_10", subBean.getTransFlag());
			writerMap.put("F_11", subBean.getOppAccountNo());
			writerMap.put("F_12", subBean.getOppAccountName());
			writerMap.put("F_13", subBean.getRemark());
			writerMap.put("F_14", "0"); //手续费
			writerMap.put("F_15", "C006"); //数据来源
			writerMap.put("F_16", "16"); //交易状态
			if (editMap.containsKey(subBean.getBankLogNo())) {
				subMap = new HashMap();
				subMap = editMap.get(subBean.getBankLogNo());
				if ("D".equals(subBean.getDcFlag()) && !subBean.getRemark().contains("手续费")) { //内部调账
					subMap.put("F_1", subBean.getAccountingDate()+subBean.getTransTime());
					subMap.put("F_2", subBean.getBankLogNo());
					subMap.put("F_3", subBean.getBankOrderNo());
					subMap.put("F_4", subBean.getAccountNo());
					subMap.put("F_5", subBean.getAccountName());
					subMap.put("F_6", subBean.getSubAccountName()==null?" ":subBean.getSubAccountName());
					subMap.put("F_7", subBean.getDcFlag());
					subMap.put("F_8", subBean.getCurrency());

					amount = new BigDecimal(subBean.getTransAmount());
					amount = amount.multiply(con100);
					amount = amount.setScale(0,BigDecimal.ROUND_HALF_UP);
					subMap.put("F_9", amount);
					subMap.put("F_10", subBean.getTransFlag());
					subMap.put("F_11", subBean.getOppAccountNo());
					subMap.put("F_12", subBean.getOppAccountName());
					subMap.put("F_13", subBean.getRemark());
					subMap.put("F_14", subMap.get("F_14"));
					subMap.put("F_15", subMap.get("F_15"));
					subMap.put("F_16", subMap.get("F_16"));
				}
				if (subBean.getRemark().contains("手续费")) {
					amount = new BigDecimal(subBean.getTransAmount());
					amount = amount.multiply(con100);
					amount = amount.setScale(0,BigDecimal.ROUND_HALF_UP); 
					subMap.put("F_14", amount); //手续费
				}
			} else {
				editMap.put(subBean.getBankLogNo(), writerMap);
			}
		}
		return editMap;
	}
	
}
