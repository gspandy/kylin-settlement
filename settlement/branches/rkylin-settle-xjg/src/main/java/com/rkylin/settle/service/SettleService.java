package com.rkylin.settle.service;

public interface SettleService {
	
	/**
	 * author youyu
	 * @param subjectCode 科目号
	 * @param rootInstCd 机构号
	 * @param currency 币种
	 * @param channelCode 渠道
	 * @param agreementCode 协议 
	 * @return
	 */
	public String getFinAccountId(String subjectCode,String rootInstCd,String currency,String channelCode,String agreementCode) throws Exception;

}
