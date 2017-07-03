package com.rkylin.settle.service;

import java.util.Date;

public interface AccountOtherEnvironService {
	/**
	 * 对账(账户跟多渠道)
	 * @param payChannelId
	 * @param accountTypeArr
	 * @param merchantCode
	 * @param bussType
	 * @throws Exception
	 */
	void otherEnvironCollage(Date accountDate,String otherEnviron,String rule) throws Exception;

	
}
