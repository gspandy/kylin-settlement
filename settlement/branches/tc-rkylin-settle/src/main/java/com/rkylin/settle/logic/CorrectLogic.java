package com.rkylin.settle.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.pojo.SettleTransProfit;

/***
 * 冲正逻辑
 * @author Yang
 */
@Component("correctLogic")
public class CorrectLogic extends BasicLogic {
	@Autowired
	private SettleTransProfitManager settleTransProfitManager;		//'清算'分润结果Manager
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;		//'清算'交易信息Manager
	/***
	 * 冲正|抹账|退票
	 * @param settleTransDetailList 冲正交易List
	 * @return 提示信息
	 * @throws Exception
	 */
	public Map<String, Object> doCorrect(List<Map<String, Object>> settleTransDetailList) throws Exception {
		logger.info(">>> >>> >>> >>> 开始 实时清分(冲正|抹账|退票)操作! >>> >>> >>> >>>");
		//提示信息Map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//修改冲正交易query对象
		Map<String, Object> updateDetailMap = new HashMap<String, Object>();
		//需要更新分润信息的List
		List<Integer> totalTransProfitIdList = new ArrayList<Integer>();
		//冲正成功的交易信息ID[用于批量更新]
		List<Integer> successTransDetailIds = new ArrayList<Integer>();
		//遍历冲正交易迭代器
		Iterator<Map<String, Object>> settleTransDetailIter =  settleTransDetailList.iterator();
		//冲正交易IDList
		List<String> corrIdList = new ArrayList<String>();
		//抹账交易IDList
		List<String> wipeOutIdList = new ArrayList<String>();
		//退票交易订单号List
		List<String> sendBackIdList = new ArrayList<String>();
		//退票交易IDList
		List<Integer> sendBackTransDetailIdList = new ArrayList<Integer>();
		
		while(settleTransDetailIter.hasNext()) {
			//当前交易
			Map<String, Object> detail = settleTransDetailIter.next();
			//记录冲正成功的交易信息ID
			Integer transDetailId = Integer.parseInt(String.valueOf(detail.get("TRANS_DETAIL_ID")));
			//原交易系统订单号
			String originalOrderId = null;
			//交易来源
			String dataFrom = String.valueOf(detail.get("DATA_FROM"));
			//原交易订单号存储位置
			String originalOrderNoColumnName = "4".equals(dataFrom) ? "ORIGINAL_ORDER_ID" : "0".equals(dataFrom) ? "ORDER_PACKAGE_NO" : "unknow";
			
			if(SettleConstants.CORR_FUNC_CODE.equals(String.valueOf(detail.get("FUNC_CODE"))) || "10012".equals(String.valueOf(detail.get("FUNC_CODE")))) {
				if(detail.get(originalOrderNoColumnName) == null) {
					logger.error(">>> >>> >>> 错误: 二期交易 冲正 交易的"+ originalOrderNoColumnName +"字段,不应该为 null");
				} else {
					originalOrderId = String.valueOf(detail.get(originalOrderNoColumnName));
					corrIdList.add(originalOrderId);
				}
			} else if(SettleConstants.WIPEOUT_FUNC_CODE.equals(String.valueOf(detail.get("FUNC_CODE")))) {
				if(detail.get(originalOrderNoColumnName) == null) {
					logger.error(">>> >>> >>> 错误: 抹账 交易的"+ originalOrderNoColumnName +"字段,不应该为 null");
				} else {
					originalOrderId = String.valueOf(detail.get(originalOrderNoColumnName));
					wipeOutIdList.add(originalOrderId);
				}
			} else if(SettleConstants.SENDBACK_FUNC_CODE.equals(String.valueOf(detail.get("FUNC_CODE")))) {
				if(detail.get(originalOrderNoColumnName) == null) {
					logger.error(">>> >>> >>> 错误: 退票 交易的"+ originalOrderNoColumnName +"字段,不应该为 null");
				} else {
					originalOrderId = String.valueOf(detail.get(originalOrderNoColumnName));
					sendBackIdList.add(originalOrderId);
					sendBackTransDetailIdList.add(transDetailId);
				}
			}
			
			//获取由当前交易所产生的分润结果
			List<SettleTransProfit> transProfitList = null;
			try {
				transProfitList = super.getProfitListByTheDetail(detail);
				if(transProfitList == null || transProfitList.size() < 1) {
					logger.info(">>> >>> >>> [ID:"+transDetailId+"] 此'冲正'交易信息,不包含'分润'结果信息!");
					successTransDetailIds.add(transDetailId);
					continue;
				}
			} catch (Exception e) {
				logger.error(">>> >>> >>> 异常: [ID:"+transDetailId+"] 此'冲正'交易信息,获取'分润'结果信息   异常!");
				e.printStackTrace();
				continue;
			}
			//迭代分润结果
			Iterator<SettleTransProfit> transProfitIter =  transProfitList.iterator();
			while(transProfitIter.hasNext()) {
				//当前分润结果
				SettleTransProfit prfit = transProfitIter.next();
				//存如idList,用于批量更新
				totalTransProfitIdList.add(prfit.getTransProfitId());
			}
			successTransDetailIds.add(transDetailId);
		}
		/*
		 * 批量更新冲正后的分润结果
		 */
		if(totalTransProfitIdList.size() > 0) {
			logger.info(">>> >>> >>> 批量更新 '冲正'分润结果!");
			this.updateProfitStatAndRemark(totalTransProfitIdList);
		} else {
			logger.info(">>> >>> >>> 此次操作, 无需更新'冲正'分润结果!");
		}
		/*
		 * 更新交易信息
		 */
		updateDetailMap.put("isBilled", 1);
		updateDetailMap.put("reverseFlag", 1);
		updateDetailMap.put("readStatusId", 6);
		if(successTransDetailIds.size() > 0) {
			logger.info(">>> >>> >>> 批量更新 '冲正'交易信息!");
			updateDetailMap.put("idList", successTransDetailIds);
			settleTransDetailManager.updateTransStatusId(updateDetailMap);
		} else {
			logger.info(">>> >>> >>> 此次操作, 无需更新'冲正'交易信息!");
		}
		/*
		 * 更新被'冲正,抹账,退款'原交易信息
		 */
		if(corrIdList.size() > 0) this.updateMTAegisCorrectOrder(this.queryTransDetailIdsByOrderNo(corrIdList));
		if(wipeOutIdList.size() > 0) this.updateMTAegisWipeOutTheAccount(this.queryTransDetailIdsByOrderNo(wipeOutIdList));
		if(sendBackIdList.size() > 0) {
			List<Integer> sendBackTransIdList = this.queryTransDetailIdsByOrderNo(sendBackIdList);		
			this.updateMTAegisSendBack(sendBackTransIdList);								
			this.updatePayChannelIdByoriginalTrans(sendBackTransIdList, sendBackTransDetailIdList);
		}
		logger.info("<<< <<< <<< <<< 结束 实时清分(冲正|抹账|退票)操作! <<< <<< <<< <<<");
		return resultMap;
	}
	/**
	 * 通过订单号查询交易主键ID
	 * @param orderNoList
	 * @return
	 */
	private List<Integer> queryTransDetailIdsByOrderNo(List<String> orderNoList) {
		return settleTransDetailManager.queryTransDetailIdsByOrderNo(orderNoList);
	}
	/***
	 * 批量更新冲正后的分润结果
	 * @param idList
	 * @return
	 */
	private int updateProfitStatAndRemark(List<Integer> idList) {
		logger.info(">>> >>> >>> >>> 进入   批量更新冲正后的分润结果");
		Map<String, Object> query = new HashMap<String, Object>();
		String remark = "冲正:因产生此条分润结果的交易被'冲正',所以不做结算处理";
		query.put("idList", idList);		//主键ID的集合
		query.put("statusId", 2);			//'不处理'状态位
		query.put("remark", remark);		//备注
		return settleTransProfitManager.updateByIdList(query);
	}
	/**
	 * 更新 '被冲正' 交易信息
	 * @param transDetailIds
	 * @return
	 */
	private int updateMTAegisCorrectOrder(List<Integer> transDetailIds) {
		return settleTransDetailManager.updateMTAegisCorrectOrder(transDetailIds);
	}
	/**
	 * 更新 '被抹账' 交易信息
	 * @param transDetailIds
	 * @return
	 */
	private int updateMTAegisWipeOutTheAccount(List<Integer> transDetailIds) {
		return settleTransDetailManager.updateMTAegisWipeOutTheAccount(transDetailIds);
	}
	/**
	 * 更新 '被退票' 交易信息
	 * @param transDetailIds
	 * @return
	 */
	private int updateMTAegisSendBack(List<Integer> transDetailIds) {
		return settleTransDetailManager.updateMTAegisSendBack(transDetailIds);
	}
	/***
	 * 根据查询原订单payChannelId并回写到相应的退票交易上
	 */
	private void updatePayChannelIdByoriginalTrans(List<Integer> originalOrderIdsArr, List<Integer> idsArr) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("originalOrderIdsArr", originalOrderIdsArr);
		map.put("idsArr", idsArr);
		Integer result = settleTransDetailManager.updatePayChannelIdByoriginalTrans(map);
		if(result < 1) {
			throw new Exception("根据查询原订单payChannelId并回写到相应的退票交易异常,修改结果影响数据为" + result);
		}
	}
}
