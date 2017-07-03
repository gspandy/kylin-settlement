package com.rkylin.settle.service;

import java.util.Date;
import java.util.Map;

import com.rkylin.crps.pojo.BaseResponse;
import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.settle.pojo.SettleTransDetailApi;

public interface DsfService {
	/**
	 * 查询代收付交易在清结算的状态dflag
	 *  0未处理
	 *	1处理中
	 *	4代收付失败
	 *	40 代收付失败，结果推送账户失败
	 *	41 代收付失败，结果推送账户成功
	 *	6 代收付成功
	 *	60 代收付成功，结果推送账户失败
	 *	61 代收付成功，结果推送账户成功
	 *	99 异常
	 *	null 非代收付交易单
	 * @param orderNo
	 * @param funcCode
	 * @return
	 */
	public SettleTransDetailApi queryDsfStatus(String orderNo,String funcCode);
	
	/**
	 * 接收代收付的返回结果 (代收付系统调用)
	 * @param baseResponse
	 * @return
	 */
	public String receiveDsfResult(BaseResponse baseResponse);
	
	/**
	 * 退票失败的或异常的手工重发给账户系统
	 */
	public void sendAccountTP(Integer[] ids,Integer[] deliverStatusIds);

	/**
	 * 支付的回盘文件
	 * @param fileType 文件类型
	 * @param batchNo 文件批次号
	 * @param accountDate 账期
	 */
	public Map<String,String> returnRopFile(Integer fileType,String batchNo,Date accountDate);
	
	/**
	 * 代收付结果单笔通知接口
	 * @param orderDetail
	 * @return
	 */
	public String dubboNotify(OrderDetail orderDetail);
	
	/**
	 * 代收付结果批量通知接口
	 * @param orderDetails
	 * @return
	 */
	public String dubboNotify(OrderDetails orderDetails);
}
