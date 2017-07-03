package com.rkylin.settle.service;

import java.util.Map;

import com.rkylin.settle.util.PagerModel;


/**
 * 提供给商户服务平台的接口
 * @author yy
 *
 */
public interface BusinessService {
	
	/**
	 * 根据查询条件进行手续费汇总并返回汇总后的数据
	 * @param accountDate 账期  必填参数
	 * @param merchantCode 协议，没有请传null
	 * @param payChannelId 渠道 ，"全部"传null
	 * @param accountType 类型 "0"充值/退款交易   ，"1"代收付交易
	 * @param pageIndex 当前页的页码 ，从1开始计
	 * @param pageSize 每页显示的条数
	 * @return
	 * @throws Exception
	 */
	public PagerModel<Map<String,Object>> feeAmountSummary(String accountDate,String merchantCode,String payChannelId,String dealType,Integer pageIndex,Integer pageSize)throws Exception;

	/**
	 * 下载全部
	 * @param accountDate 账期  必填参数
	 * @param merchantCode 协议，没有请传null
	 * @param payChannelId 渠道 ，"全部"传null
	 * @param dealType 类型 "0"充值/退款交易   ，"1"代收付交易，"全部"传null
	 * @param fileName 文件名约定用xlsx后缀,xxx.xlsx
	 * @return
	 * @throws Exception
	 */
	public String downloadAllfeeAmountSummary(String accountDate,String merchantCode,String payChannelId,String dealType,String fileName)throws Exception;

}
