package com.rkylin.settle.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransProfit;

/***
 * 挂账逻辑
 * @author Yang
 */
@Component("billLogic")
public class BillLogic extends BasicLogic {
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;	//'清算'属性表Manager
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;		//'清算'交易信息Manager
	@Autowired
	private SettleTransProfitManager settleTransProfitManager;		//'清算'分润结果交易信息Manager
	@Autowired
	private SettleTransBillManager settleTransBillManager;			//'清算'挂账交易信息Manager
	/***
	 * 挂账 [消费后退款]
	 * @return
	 */
	public Map<String, Object> doBill(List<Map<String, Object>> settleTransDetailList) throws Exception {
		logger.info(">>> >>> >>> >>> 进入  '消费后退款' 操作");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> updateDetailMap = new HashMap<String, Object>();
		List<Integer> successTransDetailIds = new ArrayList<Integer>();
		if(settleTransDetailList == null || settleTransDetailList.size() < 1) {
			String msg = "查询退款交易信息 [消费后退款明细] 0条!";
			logger.info(">>> >>> >>> " + msg);
			super.editResultMap(resultMap, "0", msg);
			return resultMap;
		}
		/*
		 * 遍历 查询挂账退款交易List
		 */
		Iterator<Map<String,Object>> settleTransDetailIter = settleTransDetailList.iterator();
		while(settleTransDetailIter.hasNext()) {
			//当前退款交易信息
			Map<String,Object> detail = settleTransDetailIter.next();
			//声明分润结果List
			List<SettleTransProfit> settleTransProfitList = null;
			//声明挂账信息List
			List<SettleTransBill> settleTransBillList = null;
			//获取交易基本信息[日志]
			Integer id = Integer.parseInt(String.valueOf(detail.get("TRANS_DETAIL_ID")));					//主键ID
			String orderNo = String.valueOf(detail.get("ORDER_NO"));					//订单号
			String merchantCode = String.valueOf(detail.get("MERCHANT_CODE"));			//机构号
			String detailInfo = "[ID:"+id+"; ORDER_NO:"+orderNo+"; MERCHANT_CODE:"+merchantCode+"]";	//提示信息
			/*
			 * 查询退款分润结果
			 */
			try {
				settleTransProfitList = super.getProfitListByTheDetail(detail);
				if(settleTransProfitList == null || settleTransProfitList.size() < 1) {
					String msg = "查询退款分润结果  0条!" + detailInfo  + "; ";
					msg = resultMap.get("msg") == null ? msg : resultMap.get("msg") + msg; 
					logger.info(">>> >>> >>> " + msg);
					super.editResultMap(resultMap, "0", msg);
					continue;
				}
			} catch (Exception e) {
				String msg = "异常: 查询退款分润结果  异常!" + detailInfo  + "; ";
				msg = resultMap.get("msg") == null ? msg : resultMap.get("msg") + msg; 
				super.editResultMap(resultMap, "-1", msg);
				e.printStackTrace();
				continue;
			}
			/*
			 * 按照退款分润结果创建退款信息List
			 */
			try {
				settleTransBillList = this.createTransBill(settleTransProfitList, detail);
				if(settleTransBillList == null || settleTransBillList.size() < 1) {
					String msg = "查询退款分润结果[按照记录创建挂账信息List]  0条!" + detailInfo + "; ";
					msg = resultMap.get("msg") == null ? msg : resultMap.get("msg") + msg; 
					logger.info(">>> >>> >>> " + msg);
					super.editResultMap(resultMap, "0", msg);
					successTransDetailIds.add(id);
					continue;
				}
			} catch (Exception e) {
				String msg = "异常:　查询退款分润结果[按照记录创建挂账信息List]  异常!" + detailInfo  + "; ";
				msg = resultMap.get("msg") == null ? msg : resultMap.get("msg") + msg; 
				logger.info(">>> >>> >>> " + msg);
				super.editResultMap(resultMap, "-1", msg);
				e.printStackTrace();
				continue;
			}
			super.insertAndUpdateTransBill(settleTransBillList);
			successTransDetailIds.add(id);
			logger.debug(detailInfo + "处理成功!" + detailInfo);
		}
		/*
		 * 更新交易信息
		 */
		if(successTransDetailIds.size() > 0) {
			updateDetailMap.put("isBilled", 1);
			updateDetailMap.put("idList", successTransDetailIds);
			settleTransDetailManager.updateTransStatusId(updateDetailMap);
		} else {
			logger.info(">>> >>> >>> 此次操作, 无需更新'挂账'交易信息!");
		}
		/*
		 * 封装提示信息
		 */
		if(resultMap.size() > 0) {
			String msg = "保存'挂账信息'完成, 但未全都成功!";
			logger.info(">>> >>> >>> " + msg);
			msg = resultMap.get("msg") == null ? msg : resultMap.get("msg") + msg;
			super.editResultMap(resultMap, "1", msg);
		} else {
			String msg = "全都成功!";
			logger.info(">>> >>> >>> " + msg);
			super.editResultMap(resultMap, "1", msg);
		}
		return resultMap;
	}
	/***
	 * 查询退款分润结果[按照记录创建挂账信息List]
	 * @param settleTransProfitList	退款分润结果List
	 * @param settleTransDetail		退款交易信息
	 * @return
	 */
	private List<SettleTransBill> createTransBill(List<SettleTransProfit> settleTransProfitList, Map<String, Object> settleTransDetail) throws SettleException {
		logger.info(">>> >>> >>> >>> 进入 按照记录创建挂账信息List");
		//创建挂账信息List
		List<SettleTransBill> settleTransBillList = new ArrayList<SettleTransBill>();
		//遍历待退款分润结果
		Iterator<SettleTransProfit> iter = settleTransProfitList.iterator();
		while(iter.hasNext()) {
			//当前待退款分润结果信息
			SettleTransProfit profit = iter.next();
			//创建挂账信息对象
			SettleTransBill bill = new SettleTransBill();
			//从分润结果获取挂账信息
			String orderNo = profit.getOrderNo();
			String billNo = String.valueOf(profit.getTransProfitId());	//分润结果ID
			String rootInstCd = profit.getRootInstCd();					//机构号
			String productId = profit.getProductId();					//产品好
			String userId  = profit.getUserId();						//用户ID
			Long billAmount = profit.getProfitAmount();					//分润金额/挂账金额
			Integer billType = 1;										//挂账退款
			Integer statusId = 0;										//未进行退款操作
			String remark = "未进行退款操作";								//备注
			Date accountDate = (Date) settleTransDetail.get("ACCOUNT_DATE");	//退款账期
			//封装挂账信息对象
			bill.setOrderNo(orderNo);
			bill.setBillNo(billNo);
			bill.setRootInstCd(rootInstCd);
			bill.setProductId(productId);
			bill.setUserId(userId);
			bill.setBillAmount(billAmount);
			bill.setBillType(billType);
			bill.setStatusId(statusId);
			bill.setRemark(remark);
			bill.setAccountDate(accountDate);
			settleTransBillList.add(bill);
		}
		return settleTransBillList;
	}
	/***
	 * 查询挂账退款交易 [funcCode为4020的退款是消费后退款需走挂账流程]
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> getTheBillListFromDetail() throws SettleException {
		logger.info(">>> >>> >>> >>> 进入   查询挂账退款交易 [funcCode为4020的退款是消费后退款需走挂账流程]");
		//查询条件Map
		Map<String, Object> queryMap = new HashMap<String, Object>();
		//获取消费后退款对应业务编码
		List<String> funcCodes = super.getFuncCodeFromParamInfo("BILL_FUNC_CODES");
		queryMap.put("funcCodes", funcCodes);
		queryMap.put("orderType", 0);
		queryMap.put("isBilled", 1);
		return settleTransDetailManager.selectBillTransInfo(queryMap);
	}
}
