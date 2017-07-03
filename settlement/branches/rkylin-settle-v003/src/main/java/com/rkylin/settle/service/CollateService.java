package com.rkylin.settle.service;

import java.util.Date;
import java.util.Map;

public interface CollateService {
	/**
	 * 从多渠道读取交易信息并存入'清算'DB
	 * @param accountDate
	 * @return 提示信息
	 * 定时任务, 07:00 / 天
	 */
	public Map<String, Object> getTransDetailFromMultiGate() throws Exception;
	/**
     * 指定账期下载通联对账文件
     * @param rootInstId 机构号
     * @param fileType 文件类型：WG：网关支付，ZF：支付系统
     * @param accountDate 上游账期
     * @return
     */
    public Map<String, String> tlFileDown(String rootInstId, String fileType, String accountDate) throws Exception;
    /**
	 * @Description: 读取通联对账文件，并录入数据库
	 * @param marchantCode 机构号
	 * @param readType 交易类型-网关支付:01, 代收付:02
	 * @param accountDate  账期
	 * @param fileType 文件类型-WG:网关支付 ZF：代收付
	 * @param payChannelId 渠道号 01通联、02支付宝
	 * @return
	 * @throws Exception
	 * @autor CaoYang
	 */
	public Map<String, Object> readCollateFile(String marchantCode, String readType, String accountDate,String fileType,String payChannelId) throws Exception;
	/***
	 * 对账
	 * @param payChannelId
	 * @param accountType
	 * @param merchantCode
	 * @throws Exception
	 */
	public void collage(String payChannelId,String accountType,String merchantCode, String bussType) throws Exception;
	
	public void collage(String payChannelId, String[] accountType, String merchantCode, String bussType) throws Exception;
	/***
	 * 作成下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createCollateFile(String marchantCode, String readType) throws Exception;
	
	public Map<String, Object> createCollateFile(String marchantCode,String readType, Date accountDate) throws Exception;
	/***
	 * 上传下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> uploadCollateFile(String marchantCode, String readType) throws Exception;
	
	public Map<String, Object> uploadCollateFile(String marchantCode,String readType, Date accountDate) throws Exception;
	/***
	 * 通联 -下载上游对账文件并入库
	 * @param marchantCode 机构号
	 * @param readType 交易类型-网关支付:01, 代收付:02
	 * @param accountDate  账期
	 * @param fileType 文件类型-WG:网关支付 ZF：代收付
	 * @param payChannelId 渠道号 01通联、02支付宝
	 */
	public void tLFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception;
	/***
	 * 连连支付 -下载上游对账文件并入库
	 * @param marchantCode 机构号
	 * @param readType 交易类型-网关支付:01, 代收付:02, 微信:03, 连连快捷支付:04
	 * @param accountDate  账期
	 * @param fileType 文件类型-WG:网关支付 ZF：代收付 LLKJ: 连连快捷支付
	 * @param payChannelId 渠道号 01通联、02支付宝、03微信、04连连快捷支付
	 */
	public void lLFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception;
	/***
	 * 联动优势支付 -下载上游对账文件并入库
	 * @param marchantCode 机构号
	 * @param readType 交易类型-网关支付:01, 代收付:02, 微信:03, 连连快捷支付:04
	 * @param accountDate  账期
	 * @param fileType 文件类型-WG:网关支付 ZF：代收付 LLKJ: 连连快捷支付
	 * @param payChannelId 渠道号 01通联、02支付宝、03微信、04连连快捷支付
	 */
	public void lDFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception;
	/***
	 * 畅捷支付 -下载上游对账文件并入库
	 * @param marchantCode 机构号
	 * @param readType 交易类型-网关支付:01, 代收付:02, 微信:03, 连连快捷支付:04
	 * @param accountDate  账期
	 * @param fileType 文件类型-WG:网关支付 ZF：代收付 LLKJ: 连连快捷支付
	 * @param payChannelId 渠道号 01通联、02支付宝、03微信、04连连快捷支付 05联动 S01畅捷
	 */
	public void cjFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception;
	/**
	 * 对账(账户跟多渠道)
	 * @param funcCodes
	 * @param payChannelId
	 * @param readType
	 * @throws Exception
	 */
	public void collageAccAndMulti(String[] funcCodes,String payChannelId,String readType,String merchantCode,Date accountDate) throws Exception;
	/***
	 * 多渠道 -下载上游对账文件并入库
	 * @param marchantCode 机构号
	 * @param readType 交易类型-网关支付:01, 代收付:02, 微信:03, 连连快捷支付:04
	 * @param accountDate  账期
	 * @param fileType 文件类型-PAYQ
	 * @param payChannelId 渠道号 01通联、02支付宝、03微信、04连连快捷支付 05联动 S01畅捷Y01平安
	 */
	public void multiFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception;
	/***
	 * 多渠道 -下载上游对账文件并入库
	 * @param marchantCode 机构号
	 * @param readType 交易类型-网关支付:01, 代收付:02, 微信:03, 连连快捷支付:04
	 * @param accountDate  账期
	 * @param fileType 文件类型-CMBC
	 * @param payChannelId 渠道号 01通联、02支付宝、03微信、04连连快捷支付 05联动 S01畅捷Y01平安
	 */
	public void multiFileDownAndReadCollateFile_FTP(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception;

	/***
	 * 多渠道 -下载上游对账文件并入库
	 * @param marchantCode 机构号
	 * @param readType 交易类型-网关支付:01, 代收付:02, 微信:03, 连连快捷支付:04
	 * @param accountDate  账期
	 * @param fileType 文件类型-CMBC
	 * @param payChannelId 渠道号 01通联、02支付宝、03微信、04连连快捷支付 05联动 S01畅捷Y01平安
	 */
	public void multiFileDownAndReadCollateFileP_FTP(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception;

	/***
	 * 下载平安银行B2B对账文件
	 * @param marchantCode
	 * @param readType
	 * @param accountDate
	 * @param fileType
	 * @param payChannelId
	 * @throws Exception
	 */
	public void padB2BDownAndReadCollateFile_SFTP(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception;
	
	/***
	 * 下载快付通支付对账文件
	 * @param marchantCode
	 * @param readType
	 * @param accountDate
	 * @param fileType
	 * @param payChannelId
	 * @throws Exception
	 */
	public void lycheeDownAndReadCollateFile_FTP(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception;
}
