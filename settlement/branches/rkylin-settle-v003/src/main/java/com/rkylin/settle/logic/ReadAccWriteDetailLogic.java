package com.rkylin.settle.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransDetail;

@Service("readAccWriteDetailLogic")
public class ReadAccWriteDetailLogic extends BasicLogic {
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;
	@Autowired
	private SettleTransAccountManager settleTransAccountManager;
	/**
	 * 将accountList中的readStatusId写入detailList
	 * @param detailList
	 * @param accountList
	 * @return
	 */
	public Map<String, Object> doWriteToSettleTransDetail(List<Map<String, Object>> detailList, List<Map<String, Object>> accountList) throws Exception {
		logger.info(">>> >>> >>> 开始 将accountList中的readStatusId写入detailList");
		//对账keyList
		List<String> keys = null;
		//返回值Map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//settleTransDetail交易集合
		Map<String, Map<String, Object>> detailMapStru = null;
		//settleTransAccount交易集合
		Map<String, Map<String, Object>> accountMapStru = null;
		//settleTransDetail实例
		Map<String, Object> detailMap = null;
		//settleTransAccount实例
		Map<String, Object> accountMap = null;
		//匹配成功settleTransDetail集合List
		List<Map<String, Object>> successDetailMapList = new ArrayList<Map<String, Object>>();
		//匹配失败settleTransDetail集合List
		List<Map<String, Object>> failDetailMapList = new ArrayList<Map<String, Object>>();
		//同步结果结构体List
		List<SettleBalanceEntry> settleBalanceEntryList =  new ArrayList<SettleBalanceEntry>();
		//结果结构体
		SettleBalanceEntry settleBalanceEntry = null; 
		
		//创建detail交易key
		keys = new ArrayList<String>();
		keys.add("ORDER_AMOUNT");
		keys.add("ORDER_NO");
		//生成detail交易结构体
		try {
			detailMapStru = super.editCollateStructure(detailList, keys);
		} catch (Exception e) {
			logger.error("异常:生成detail交易结构体", e);
			throw e;
		}
		//创建account交易key
		keys = new ArrayList<String>();
		keys.add("TRANS_AMOUNT");
		keys.add("ORDER_NO");
		//生产account交易结构体
		try {
			accountMapStru = super.editCollateStructure(accountList, keys);
		} catch (Exception e) {
			logger.error("异常:生成account交易结构体", e);
			throw e;
		}
		//获取匹配key的Set
		Set<String> detailMapKeySet = detailMapStru.keySet();
		//遍历settleTransDetail交易匹配settleTransAccount交易并回写readStatusId
		for(String ruleKey : detailMapKeySet) {
			//清算交易实体
			detailMap = detailMapStru.get(ruleKey);
			//交易结果实体
			settleBalanceEntry = new SettleBalanceEntry();
			//3:状态回写结果
			settleBalanceEntry.setBalanceType(3);
			//匹配key
			settleBalanceEntry.setObligate1(ruleKey);
			//账期
			settleBalanceEntry.setAccountDate((Date) detailMap.get("ACCOUNT_DATE"));
			//匹配成功
			if(accountMapStru.containsKey(ruleKey)) {//[我有, 他也有]
				//上游交易实体
				accountMap = accountMapStru.get(ruleKey);
				//未知状态
				detailMap.put("READ_STATUS_ID", accountMap.get("READ_STATUS_ID"));
				//存入list准备update操作
				successDetailMapList.add(detailMap);
				//封装交易结果信息
				settleBalanceEntry.setOrderNo(String.valueOf(detailMap.get("ORDER_NO")));
				String detailIdAndAccountId = detailMap.get("TRANS_DETAIL_ID") + "#" + accountMap.get("TRANS_ACCOUNT_ID");
				settleBalanceEntry.setObligate2(detailIdAndAccountId);
				settleBalanceEntry.setStatusId(1);
				settleBalanceEntry.setRemark("成功:将accountList中的readStatusId写入detailList");
				logger.info(">>> >>> 匹配成功 , detailId&AccountId:" + detailIdAndAccountId + " ruleKey:" + ruleKey + ", readStatusId:" + detailMap.get("READ_STATUS_ID"));
			} else {//匹配失败
				failDetailMapList.add(detailMap);
				settleBalanceEntry.setOrderNo(String.valueOf(detailMap.get("ORDER_NO")));
				settleBalanceEntry.setObligate2(detailMap.get("TRANS_DETAIL_ID") + "#");
				settleBalanceEntry.setStatusId(0);
				settleBalanceEntry.setRemark("失败:将accountList中的readStatusId写入detailList");
				logger.info(">>> >>> 匹配失败 , detailId&AccountId:" + detailMap.get("TRANS_DETAIL_ID") + "#" + " ruleKey:" + ruleKey + ", readStatusId:" + detailMap.get("READ_STATUS_ID"));
			}
			settleBalanceEntryList.add(settleBalanceEntry);
		}
		//修改settleTransDetail的readStatusId
		try {
			this.updateSettleTransDetail(successDetailMapList);
		} catch (Exception e) {
			logger.error("异常:修改settleTransDetail的readStatusId", e);
			throw e;
		}
		//更新&插入对账结果表
		try {
			super.insertAndUpdateSettleBalanceEntry(settleBalanceEntryList);
		} catch (Exception e) {
			logger.error("异常://更新&插入对账结果表", e);
			throw e;
		}

		logger.info("<<< <<< <<< 结束 将accountList中的readStatusId写入detailList");
		return resultMap;
	}
	/**
	 * 修改settleTransDetail的readStatusId
	 * @param detailList
	 */
	private void updateSettleTransDetail(List<Map<String, Object>> detailList) throws Exception {
		logger.info(">>> >>> >>> 开始 修改settleTransDetail的readStatusId");
		Iterator<Map<String, Object>> detailIterator = detailList.iterator();
		Map<String, Object> detailMap = null;
		SettleTransDetail settleTransDetail = null;
		while(detailIterator.hasNext()) {
			detailMap = detailIterator.next();
			settleTransDetail = new SettleTransDetail();
			settleTransDetail.setTransDetailId(Integer.parseInt(String.valueOf(detailMap.get("TRANS_DETAIL_ID"))));
			settleTransDetail.setReadStatusId(Integer.parseInt(String.valueOf(detailMap.get("READ_STATUS_ID"))));
			settleTransDetailManager.updateSettleTransDetail(settleTransDetail);
		}
		logger.info("<<< <<< <<< 结束 修改settleTransDetail的readStatusId");
	}
	/**
	 * 使用orderNo查询SETTLE_TRANS_ACCOUNT交易
	 * @param orderNoList
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> querySettleTransAccount(String[] orderNoArr) throws Exception {
		logger.info(">>> >>> >>> 开始 使用orderNo查询SETTLE_TRANS_ACCOUNT交易");
		List<Map<String, Object>> accountList = null;
		try {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("orderNoArr", orderNoArr);
			accountList = settleTransAccountManager.selectByOrderNo(queryMap);
		} catch (Exception e) {
			logger.error("使用orderNo查询SETTLE_TRANS_ACCOUNT交易异常:", e);
			throw e;
		}
		logger.info("<<< <<< <<< 结束 使用orderNo查询SETTLE_TRANS_ACCOUNT交易");
		return accountList;
	}
	/**
	 * 获取需要回写状态的SETTLE_TRANS_DETAIL交易
	 * @param beginAccountDate
	 * @param endAccountDate
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> querySettleTransDetail(Date beginDate, Date endDate) throws Exception {
		logger.info(">>> >>> >>> 开始 获取需要回写状态的SETTLE_TRANS_DETAIL交易");
		List<Map<String, Object>> detailList = null;
		try {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("beginDate", beginDate);
			queryMap.put("endDate", endDate);
			queryMap.put("statusIdsArr", this.queryUnKnowStatusIdArr());
			detailList = settleTransDetailManager.selectByUnKnowStatus(queryMap);
		} catch (Exception e) {
			logger.error("查询需要回写状态的SETTLE_TRANS_DETAIL交易异常:", e);
			throw e;
		}
		logger.info("<<< <<< <<< 结束 获取需要回写状态的SETTLE_TRANS_DETAIL交易");
		return detailList;
	}
	/**
	 * 查询未知交易状态
	 * @return
	 * @throws Exception
	 */
	private String[] queryUnKnowStatusIdArr() throws Exception {
		logger.info(">>> >>> >>> 开始 查询未知交易状态");
		String[] readStatusIdArr = null;
		String parameterType = SettleConstants.PARAMETER_TYPE_UNKNOW_STATUS;
		List<SettleParameterInfo> settleParameterInfoList = null;
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(parameterType);
		query.setStatusId(1);
		settleParameterInfoList = settleParameterInfoManager.queryList(query);
		readStatusIdArr = getIdsArrFromParamterValue(settleParameterInfoList, ",");
		logger.info("<<< <<< <<< 结束  查询未知交易状态");
		return readStatusIdArr;
	}
	/**
	 * 将SETTLE_PARAMETER_INFO中PARAMETER_VALUE字段split成数组
	 * @param settleParameterInfoList
	 * @param regex
	 * @return
	 */
	private String[] getIdsArrFromParamterValue(List<SettleParameterInfo> settleParameterInfoList, String regex) throws Exception {
		logger.info(">>> >>> >>> 开始 将SETTLE_PARAMETER_INFO中PARAMETER_VALUE字段split成数组");
		if(settleParameterInfoList == null || settleParameterInfoList.size() < 1) {
			String msg = "将SETTLE_PARAMETER_INFO中PARAMETER_VALUE字段通过'"+regex+"'split成数组异常:入参settleParameterInfoList为null或者size长度小于 1";
			logger.error(msg);
			throw new SettleException(msg);
		}
		String[] statusIdsArr = null;
		SettleParameterInfo settleParameterInfo = null;
		String statusIdsStr = "";
		Iterator<SettleParameterInfo> settleParameterInfoIterator = settleParameterInfoList.iterator();
		while(settleParameterInfoIterator.hasNext()) {
			settleParameterInfo = settleParameterInfoIterator.next();
			statusIdsStr += "," + settleParameterInfo.getParameterValue();
		}
		statusIdsStr = statusIdsStr.substring(1);
		logger.info(">>> >>> statusIdsStr : " + statusIdsStr);
		statusIdsArr = statusIdsStr.split(",");
		logger.info("<<< <<< <<< 结束 将SETTLE_PARAMETER_INFO中PARAMETER_VALUE字段split成数组");
		return statusIdsArr;
	}
}