package com.rkylin.settle.service;

import java.util.Date;

public interface AccountService {
	/***
	 * 对账
	 * @param payChannelId	渠道号
	 * @param accountType	01(网关与上游渠道对账) 02（代收付与上游渠道对账）03（与账户对账）
	 * @param merchantCode	机构号
	 * @param bussType	支付标志
	 * @throws Exception
	 */
	void collage(String payChannelId,String accountType,String merchantCode, String bussType) throws Exception;
	/***
	 * 对账
	 * @param payChannelId	渠道号
	 * @param accountType	01(网关与上游渠道对账) 02（代收付与上游渠道对账）03（与账户对账）
	 * @param merchantCode	机构号
	 * @param accountDate	账期 null == T-1账期
	 * @param bussType	支付标志
	 * @throws Exception
	 */
	void collage(String payChannelId,String accountType,String merchantCode, Date accountDate, String bussType) throws Exception;
	
	/**
	 * 对账(账户跟多渠道)
	 * @param funcCodes
	 * @param payChannelId
	 * @param readType
	 * @throws Exception
	 */
	void collageAccAndMulti(String[] funcCodes,String payChannelId,String readType,String inRootInstCd,Date accounteDate) throws Exception;
	/**
	 * 对账(账户跟多渠道)
	 * @param payChannelId
	 * @param accountType
	 * @param merchantCode
	 * @param accountDate
	 * @param bussType
	 * @throws Exception
	 */
	void collage(String payChannelId, String[] accountType, String merchantCode, Date accountDate, String bussType) throws Exception;
	/**
	 * 对账(账户跟多渠道)
	 * @param payChannelId
	 * @param accountTypeArr
	 * @param merchantCode
	 * @param bussType
	 * @throws Exception
	 */
	void collage(String payChannelId, String[] accountTypeArr, String merchantCode, String bussType) throws Exception;
	/**
	 * 对账(账户跟多渠道)
	 * @param payChannelId
	 * @param accountType
	 * @param merchantCode
	 * @param accountDate
	 * @param bussType
	 * @throws Exception
	 */
	void collageForPos(String payChannelId,String accountType,String merchantCode, String bussType) throws Exception;
	/***
	 * 对账(收单)
	 * @param payChannelId	渠道号
	 * @param accountType	01(网关与上游渠道对账) 02（代收付与上游渠道对账）03（与账户对账）
	 * @param merchantCode	机构号
	 * @param accountDate	账期 null == T-1账期
	 * @param bussType	支付标志
	 * @throws Exception
	 */
	void collageForPos(String payChannelId,String accountType,String merchantCode, Date accountDate, String bussType) throws Exception;
	/***
	 * 对账(收单)
	 * @param payChannelId	渠道号
	 * @param accountType数组	01(网关与上游渠道对账) 02（代收付与上游渠道对账）03（与账户对账）
	 * @param merchantCode	机构号
	 * @param bussType	支付标志
	 * @throws Exception
	 */
	void collageForPos(String payChannelId, String[] accountTypeArr, String merchantCode, String bussType) throws Exception;
	/***
	 * 对账(收单)
	 * @param payChannelId	渠道号
	 * @param accountType数组	01(网关与上游渠道对账) 02（代收付与上游渠道对账）03（与账户对账）
	 * @param merchantCode	机构号
	 * @param accountDate	账期 null == T-1账期
	 * @param bussType	支付标志
	 * @throws Exception
	 */
	void collageForPos(String payChannelId,String[] accountTypeArr,String merchantCode, Date accountDate, String bussType) throws Exception;
}
