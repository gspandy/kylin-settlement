package com.rkylin.settle.service;

import java.util.Map;

public interface ProfitService {
	/***
	 * 开始清分 ... ...
	 * 定时任务, 执行1次/2分钟
	 */
	public Map<String, Object> doProfit();
	/***
	 * 清分后结算
	 * 定时任务, 执行1次/1天
	 */
	public Map<String, Object> doProfitBalance();
	/***
	 * 刷新分润规则
	 * 定时任务, 执行1次/1天
	 * 画面使用
	 */
	public void refreshProfitKey();
	/***
	 * 刷新'订单号'与存放位置的对应关系
	 * 定时任务, 待定
	 * 画面使用
	 */
	public boolean refreshFuncCodeAndOrderNoRelation();
	/***
	 * 刷新'金额'与存放位置的对应关系
	 * 定时任务, 待定
	 * 画面使用
	 */
	public boolean refreshFuncCodeAndAmountRelation();
	/***
	 * 读取账户系统交易信息 二期
	 * 定时任务, 执行1次/2分钟
	 */
	public void getAccountTransOrderInfos() throws Exception;
	/***
	 * 读取账户系统交易信息 一期
	 * 定时任务, 执行1次/2分钟
	 */
	public void getAccountOldTransOrderInfos() throws Exception;
	/***
	 * 读取账户系统T日全部交易信息
	 * 存储过程
	 */
	public void readAccDateBySP() throws Exception;
	/***
	 * 读取账户系统T日全部交易信息
	 * 存储过程
	 */
	public void readAccOldDateBySP() throws Exception;
	/***
	 * 刷新'func_code'与deal_product_code的对应关系
	 * 画面使用
	 */
	public boolean refreshDealProductCodeTofuncCode();
}
