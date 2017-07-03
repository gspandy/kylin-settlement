package com.rkylin.settle.service;

import java.util.Map;

import com.rkylin.settle.pojo.SettleTransDetail;

public interface TradeService {
	// 持久化账户系统交易信息 一期
	Map<String, String> saveAccountTrans(SettleTransDetail settleTransDetail);

	// 上游对账文件读入
	Map<String, String> saveUpstreamTransByrop(String rootInstCd,
				String payChannelId, String readType, String fileType,
				String invoicedate, String batch);
}
