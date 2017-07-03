package com.rkylin.settle.service;


import java.util.Date;
import java.util.Map;

import com.rkylin.crps.pojo.BaseResponse;
import com.rkylin.settle.pojo.SettleTransDetail;


public interface DsfService {
	/**
	 * 汇总未处理的代付、提现、代收和一分钱代付
	 * @param ids 交易表的主键ID构成的数组
	 * @param flag 标记值, 0:代付和提现,1:代收和一分钱代付,2:课栈T0提现,其他值：代收、代付、提现、一分钱代付、课栈T0提现
	 * @param rootInstCds 机构号
	 * @param inStep 步长,用于计算账期
	 * @param funcCodes 功能编码 ,4013:代收,4014：代付，4014_1一分钱代付，4016：提现
	 * @param businessTypes 业务类型：值为1是课栈T0提现，其他值(含null)则是其它业务
	 */
	public void dealDsf(Integer[] ids,String flag,String[] rootInstCds,Integer inStep,String[] funcCodes,String[] businessTypes);
	
	/**
	 * 发送代收付交易给代收付系统
	 * @param dataSource 数据来源
	 * @param inRootInstCds 机构号
	 * @param orderTypes 订单类型
	 * @param ids 结算表的主键ID构成的数组
	 */
	public void sendDsf(String dataSource,String[] inRootInstCds,Integer[] orderTypes,Integer[] ids);
	
	/**
	 * 接收代收付的返回结果 (代收付系统调用)
	 * @param baseResponse 代收付推结果给请结算的请求参数
	 * @return
	 */
	public String receiveDsfResult(BaseResponse baseResponse);
	
	
	/**
	 * 代收付结果推送给订单
	 * @param ids 交易表主键ID构成的数组
	 * @param dflags 交易表的代收付交易的代收付状态
	 */
	public void sendOrder(Integer[] ids,Integer[] dflags);
	
	/**
	 * 查询代收付交易在交易表的代收付状态
	 * @param orderNo 订单号
	 * @param funcCode 功能编码  4013:代收,4014：代付，4014_1一分钱代付，4016：提现
	 * @return
	 * @throws Exception
	 */
	public SettleTransDetail queryDsfStatus(String orderNo,String funcCode) throws Exception;
	
	/**
	 * 对于代收付返回的由于银行系统忙等导致的失败单重发代收付
	 */
	public void sendDsfAgain(Integer[] ids);
	
	/**
	 * 从结算表开始->汇总表->交易表->发送订单系统
	 * @param ids 结算表的主键id
	 * @param statusIds 结算表的状态
	 */
	public void sendOrderAgain(Integer[] ids,Integer[] statusIds);
	
	/**
	 * 退票的或代收付推送账户失败的手工重发
	 * @param ids 交易表主键ID
	 * @param deliverStatusIds 1：退票推送账户失败
	 */
	public void sendAccountTP(Integer[] ids,Integer[] deliverStatusIds);
	
	/**
	 * 将结算中的代收付结果回写到汇总表和交易表
	 * @param ids 结算表的主键ID
	 * @param statusIds 结算表的状态
	 */
	public void WriteResult(Integer[] ids,Integer[] statusIds);
	
	/**
	 *从代收付系统读取结果
	 */
	public String getResultFromDSF(String rootInstCd, String businessCode,String requestNo, String orderNos, String accountDate);
	
	/**
	 * 代收付结果推送给账户
	 * @param ids 交易表主键ID构成的数组
	 * @param dflags 交易表的代收付交易的代收付状态
	 */
	public void returnAccount(Integer[] ids,Integer[] dflags);
	

	/**
	 * 支付的回盘文件
	 * @param fileType 文件类型
	 * @param batchNo 文件批次号
	 * @param accountDate 账期
	 */
	public Map<String,String> returnRopFile(Integer fileType,String batchNo,Date accountDate);
}
