package com.rkylin.settle.service;

import java.util.Map;

public interface SettleParameterInfoService {
	Map<String, String> selectPayChannelIdInfo();

	Map<String, String> selectPayChannelIdInfo(String payChannelId);
	
	Map<String, String> selectMerchantCdIdInfo();

	Map<String, String> selectMerchantCdIdInfo(String merchantCd);
	
	Map<String, String> selectFuncCodeInfo();
	
	Map<String, String> selectFuncCodeInfo(String funcCode);
}
