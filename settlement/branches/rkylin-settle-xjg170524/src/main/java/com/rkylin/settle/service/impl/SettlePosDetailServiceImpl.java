package com.rkylin.settle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.order.domain.M000003ServiceBean;
import com.rkylin.order.service.OrderInfoBaseService;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettlePosDetailManager;
import com.rkylin.settle.pojo.SettlePosDetail;
import com.rkylin.settle.pojo.SettlePosDetailQuery;
import com.rkylin.settle.service.SettlePosDetailService;
import com.rkylin.settle.util.SettlementUtil;
//import com.rkylin.wheatfield.api.PaymentInternalOutService;
//import com.rkylin.wheatfield.response.ErrorResponse;

/***
 * 下游对账交易信息业务逻辑
 * @author Yang
 *
 */
@Service("settleTransPosService")
public class SettlePosDetailServiceImpl implements SettlePosDetailService {
	private static Logger logger = LoggerFactory.getLogger(SettlePosDetailServiceImpl.class);
	@Autowired
	private SettlePosDetailManager settlePosDetailManager;
	@Autowired
	private SettlementUtil settlementUtil;
	@Autowired
	private OrderInfoBaseService orderInfoBaseService;
	
	@Override
	public Map<String, Object> updateAccountInfoToDetailInfo() throws Exception {
		//默认系统账期
		return this.updateAccountInfoToDetailInfo((Date) settlementUtil.getAccountDate("yyyy-MM-dd", -1, "Date"));
	}
	@Override
	public Map<String, Object> updateAccountInfoToDetailInfo(Date accountDate) throws Exception {
		//批量修改accountDate当天交易信息
		return this.updateAccountInfoToDetailInfo(accountDate, null);
	}
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public Map<String, Object> updateAccountInfoToDetailInfo(Date beginDate, Date endDate) throws Exception {
		logger.info(">>> >>> >>> >>> 开始: 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		String code = "";
		String msg = "";
		Integer rows = -1;
		queryMap.put("beginDate", beginDate);
		queryMap.put("endDate", endDate);
		rows = settlePosDetailManager.updateAccountInfoToDetailInfo(queryMap);
		if(rows > -1) {
			code = "1";
			msg = "success";
		} else {
			code = "0";
			msg = "fail";
			logger.error(">>> >>> >>> 失败: 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表");
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("rows", rows);
		logger.info("<<< <<< <<< <<< 结束: 修改数据行数【"+ rows +"】 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表");
		return resultMap;
	}
	@Override
	public void selectPosTransFeeAndUpdateOrder() {
		this.selectPosTransFeeAndUpdateOrder(null);
	}
	@Override
	public void selectPosTransFeeAndUpdateOrder(Date date) {
		logger.info(">>> >>> >>> >>> 开始: 查询还款交易手续费并更新订单系统");
		/*
		 * 1.获取账期
		 */
		String invoicedate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    	try {
	    	if (date == null) {
	    		invoicedate = String.valueOf(settlementUtil.getAccountDate("yyyy-MM-dd", -1, "String"));
	    	} else {
	    		invoicedate = sdf.format(date);	
	    	}
	    	date = sdf.parse(invoicedate + " 00:00:00");
	    } catch (Exception e) {
	    	logger.error("异常:查询还款交易手续费并更新订单系统", e);
	    }
    	logger.info(">>> >>> >>> 获取账期: " + sdf.format(date));
    	/*
    	 * 2.获取posDetail表还款交易
    	 */
    	List<SettlePosDetail> settlePosDetailList = null;
    	SettlePosDetailQuery query = new SettlePosDetailQuery();
    	query.setAccountDate(date);
    	query.setStatusId(SettleConstants.STATUS_COLLATE_SUCCESS);
    	query.setPayChannelId("%" + SettleConstants.PAY_CHANNEL_ID_REPAY);
    	settlePosDetailList = settlePosDetailManager.selectRePayTransFee(query);
    	if(settlePosDetailList == null || settlePosDetailList.size() < 1) {
    		logger.info(">>> >>> >>> 获取account表还款交易list为null或者长度为0");
        	return;
    	}
    	logger.info(">>> >>> >>> 获取account表还款交易: " + settlePosDetailList.size());
    	/*
    	 * 3.调用订单系统接口修改订单交易
    	 */
    	M000003ServiceBean bean = new M000003ServiceBean();
    	String isSuccess = null;
    	Integer index = 1;
    	StringBuffer sbf = null;
    	for(SettlePosDetail settlePosDetail : settlePosDetailList) {
    		if(settlePosDetail.getFeeAmount() == null) {
    			logger.info(">>> >>> FeeAmount is Null index:"+ index +", transDetailId:"+ settlePosDetail.getTransDetailId() +", relateOrderNo:" + settlePosDetail.getRelateOrderNo() + ", feeAmount:" + settlePosDetail.getFeeAmount());
    			index ++;
    			continue;
    		}
    		bean.setProductId(settlePosDetail.getProductId());
			bean.setConstid(settlePosDetail.getMerchantCode());
			bean.setUserId(settlePosDetail.getUserId());
			bean.setUserOrderId(settlePosDetail.getRelateOrderNo());
			bean.setUnitPrice(String.valueOf(settlePosDetail.getFeeAmount()));
			sbf = new StringBuffer();
			sbf.append("[ProductId:" + settlePosDetail.getProductId());
			sbf.append(", Constid:" + settlePosDetail.getMerchantCode());
			sbf.append(", UserId:" + settlePosDetail.getUserId());
			sbf.append(", UserOrderId:" + settlePosDetail.getRelateOrderNo());
			sbf.append(", UnitPrice:" + settlePosDetail.getFeeAmount() + "]");
    		try {
    			isSuccess = orderInfoBaseService.updateOrder(bean);
    			if("ok".equals(isSuccess)) {
    				logger.info(">>> >>> 成功! index:"+ index +", transDetailId:"+ settlePosDetail.getTransDetailId() + ", 入参" + sbf.toString() + ", isSuccess:" + isSuccess);
    			} else {
    				logger.info(">>> >>> 失败! index:"+ index +", transDetailId:"+ settlePosDetail.getTransDetailId() + ", 入参" + sbf.toString() + ", isSuccess:" + isSuccess);
    			}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(">>> >>> >>> 异常! index:"+ index +", transDetailId:"+ settlePosDetail.getTransDetailId() + ", 入参" + sbf.toString(), e);
				continue;
			} finally {
				index ++;
			}
    		
    	}
		logger.info("<<< <<< <<< <<< 结束: 查询还款交易手续费并更新订单系统");
	}
}
