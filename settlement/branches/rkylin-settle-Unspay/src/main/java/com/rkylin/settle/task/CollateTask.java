package com.rkylin.settle.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.service.AccountService;
import com.rkylin.settle.service.CollateService;

/***
 * 通联对账相关定时任务
 * @author Yang
 */
@SuppressWarnings("deprecation")
public class CollateTask {
	private static Logger logger = LoggerFactory.getLogger(CollateTask.class);
	@Autowired
	private CollateService collateService;
	@Autowired 
	private AccountService accountService;
	/***
	 * 从多渠道读取交易信息并存入'清算'DB
	 * 定时任务, 07:00 / 天
	 */
	public void getTransDetailFromMultiGate() {
		logger.info(">>> >>> >>> 开始: 从多渠道读取交易信息并存入'清算'DB");
		Map<String, Object> resultMap = null;
		try {
			resultMap = collateService.getTransDetailFromMultiGate();
		} catch (Exception e) {
			logger.info(">>> >>> >>> 异常: 从多渠道读取交易信息并存入'清算'DB");
			logger.error("",e);
			e.printStackTrace();
		}
		this.printResultMessage(resultMap);
		logger.info("<<< <<< <<< 结束: 从多渠道读取交易信息并存入'清算'DB");
	}
	/***
	 * 下载上游对账文件并入库
	 * 代收付对账 (会唐,丰年,君融贷,棉庄) 07:00
	 */
	public void tLFileDownAndReadCollateFileZF() {
		logger.info(">>> >>> >>> 开始: 下载上游对账文件并入库, 代收付对账 (通联) 07:00");
		String marchantCode;			//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_GENERATE; 		//交易类型-网关支付:01, 代收付:02
		String accountDate = null; 		//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_GENERATE; 		//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_TL;		//渠道号 01通联、02支付宝
		try {
			/*
			 *会唐
			 */
			marchantCode = Constants.HT_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (会唐) 07:00");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *丰年/展酷
			 */
			marchantCode = Constants.FN_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (丰年) 07:00");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *君融贷
			 */
			marchantCode = Constants.JRD_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (君融贷) 07:00");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *融数 = 食全食美 + 棉庄
			 */
			marchantCode = Constants.RS_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (棉庄) 07:00");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *指尖代言
			 */
			marchantCode = Constants.ZJ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (指尖代言) 07:00");
			e.printStackTrace();
		}
		try {
			/*
			 *展酷
			 */
			marchantCode = Constants.ZK_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (展酷) 07:00");
			e.printStackTrace();
		}
		try {
			/*
			 *领客科技
			 */
			marchantCode = Constants.LKKJ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (领客科技) 07:00");
			e.printStackTrace();
		}
		try {
			/*
			 *悦视觉
			 */
			marchantCode = Constants.YSJ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (悦视觉) 07:00");
			e.printStackTrace();
		}
		try {
			/*
			 *天下房仓
			 */
			marchantCode = Constants.TXFC_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (天下房仓) 07:00");
			e.printStackTrace();
		}
		try {
			/*
			 *课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (课栈) 07:00");
			e.printStackTrace();
		}
		try {
			/*
			 *沃雷特
			 */
			marchantCode = Constants.WLT_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 代收付对账 (沃雷特) 07:00");
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载上游对账文件并入库, 代收付对账 (会唐,丰年,君融贷,棉庄) 07:00");
	}
	/***
	 * 对账
	 * 代收付对账 (会唐,丰年/展酷,君融贷,棉庄) 07:30
	 */
	public void collateZF() {
		logger.info(">>> >>> >>> 开始: 对账, 代收付对账 (通联) 07:30");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_TL;
		String accountType = SettleConstants.ACCOUNT_TYPE_GENERATE;
		try {
			String merchantCode = Constants.HT_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (会唐) 07:30");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.FN_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (丰年/展酷) 07:30");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.JRD_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (君融贷) 07:30");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (棉庄) 07:30");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.ZJ_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (指尖代言) 07:30");
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.ZK_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (展酷) 07:30");
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.LKKJ_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (领客科技) 07:30");
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.YSJ_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (悦视觉) 07:30");
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.TXFC_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (领客科技) 07:30");
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.KZ_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (课栈) 07:30");
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.WLT_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 代收付对账 (沃雷特) 07:30");
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账, 代收付对账 (通联) 07:30");
	}
	/***
	 * 上传下游对账文件
	 * (全机构) 10:50
	 */
	public void uploadCollateFile() {
		logger.info(">>> >>> >>> 开始: 上传下游对账文件");
		Map<String, Object> resultMap;
		//String readType = SettleConstants.ACCOUNT_TYPE_GENERATE;
		String marchantCode = null;
		String readType = null;
		try {
			logger.info(">>> >>> >>> 上传下游对账文件");
			resultMap = collateService.uploadCollateFile(marchantCode, readType);
			this.printResultMessage(resultMap);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 上传下游对账文件 10:50");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 上传下游对账文件");
	}
	/***
	 * 下载上游对账文件并入库
	 * 网关支付对账(会唐,君融贷,展酷)
	 */
	public void tLFileDownAndReadCollateFileWG() {
		logger.info(">>> >>> >>> 开始: 下载上游对账文件并入库, 网关支付对账(会唐,君融贷)");
		String marchantCode;			//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_CHANNEL; 		//交易类型-网关支付:01, 代收付:02
		String accountDate = null; 		//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_CHANNEL; 		//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_TL;		//渠道号 01通联、02支付宝
		try {
			/*
			 *会唐
			 */
			marchantCode = Constants.HT_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 网关支付对账(会唐)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*	
			 *君融贷
			 */
			marchantCode = Constants.JRD_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 网关支付对账(君融贷)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *丰年协议
			 */
			marchantCode = Constants.FN_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 网关支付对账(丰年)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *展酷 协议
			 */
			marchantCode = Constants.ZK_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 网关支付对账(展酷)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *融数 协议
			 */
			marchantCode = Constants.RS_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 网关支付对账(融数)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *领客科技 协议
			 */
			marchantCode = Constants.LKKJ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 网关支付对账(领客科技)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *天下房仓
			 */
			marchantCode = Constants.TXFC_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 网关支付对账(天下房仓)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 网关支付对账(课栈)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *沃雷特
			 */
			marchantCode = Constants.WLT_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 网关支付对账(沃雷特)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *指尖 协议
			 */
			marchantCode = Constants.ZJ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 网关支付对账(指尖)");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载上游对账文件并入库, 网关支付对账(会唐,君融贷,展酷)");
	}
	/***
	 * 下载上游对账文件并入库
	 * 移动支付对账(君融贷)
	 */
	public void tLFileDownAndReadCollateFileYD() {
		logger.info(">>> >>> >>> 开始: 下载上游对账文件并入库, 移动支付对账(君融贷)");
		String marchantCode;			//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_MOBILE; 		//交易类型-网关支付:01, 代收付:02
		String accountDate = null; 		//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_MOBILE; 		//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_TL;		//渠道号 01通联、02支付宝
		try {
			/*
			 *君融贷
			 */
			marchantCode = Constants.JRD_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 移动支付对账(君融贷)", e);
			e.printStackTrace();
		}
		try {
			/*
			 *天下房仓
			 */
			marchantCode = Constants.TXFC_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 移动支付对账(天下房仓)", e);
			e.printStackTrace();
		}
	/*	try {
			
			 *课栈
			 
			marchantCode = Constants.KZ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 移动支付对账(课栈)", e);
			e.printStackTrace();
		}*/
		logger.info("<<< <<< <<< 结束: 下载上游对账文件并入库, 移动支付对账(君融贷)");
	}
	/***
	 * 下载上游对账文件并入库
	 * 微信支付对账(指尖代言)
	 */
	public void tLFileDownAndReadCollateFileWX() {
		logger.info(">>> >>> >>> 开始: 下载上游对账文件并入库, 微信支付对账(通联)");
		String marchantCode;			//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_WXMOBILE; 		//交易类型-网关支付:01, 代收付:02
		String accountDate = null; 		//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_WXMOBILE; 		//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_TL;
		try {
			/*
			 *指尖代言
			 */
			marchantCode = Constants.ZJ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 微信支付对账(指尖代言)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 微信支付对账(指尖代言)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *领客科技
			 */
			marchantCode = Constants.LKKJ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 微信支付对账(指尖代言)");
			e.printStackTrace();
		}
		try {
			/*
			 *天下房仓
			 */
			marchantCode = Constants.TXFC_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 微信支付对账(天下房仓)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 微信支付对账(课栈)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *沃雷特
			 */
			marchantCode = Constants.WLT_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 微信支付对账(沃雷特)");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载上游对账文件并入库, 微信支付对账(通联)");
	}
	/***
	 * 下载上游对账文件并入库
	 * SDK对账(指尖代言)
	 */
	public void tLFileDownAndReadCollateFileTLSDK() {
		logger.info(">>> >>> >>> 开始: 下载上游对账文件并入库, 通联SDK付对账(通联)");
		String marchantCode;										//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_TLSDK; 		//交易类型-网关支付:01, 代收付:02
		String accountDate = null; 									//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_TLSDK; 		//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_TL;
//		try {
//			/*
//			 * 融数钱包
//			 */
//			marchantCode = Constants.RSQB_ID;
//			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
//		} catch (Exception e) {
//			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 通联SDK支付对账(融数钱包)");
//			logger.error("",e);
//			e.printStackTrace();
//		}
		try {
			/*
			 * 天下房仓
			 */
			marchantCode = Constants.TXFC_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 通联SDK支付对账(天下房仓)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 * 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 通联SDK支付对账(课栈)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *沃雷特
			 */
			marchantCode = Constants.WLT_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 移动支付对账(沃雷特)", e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载上游对账文件并入库, 通联SDK支付对账(通联)");
	}
	/***
	 * 下载上游对账文件并入库
	 * 通联WXAPP
	 */
	public void tLFileDownAndReadCollateFileWXAPP() {
		logger.info(">>> >>> >>> 开始: 下载上游对账文件并入库, 通联微信APP付对账(通联)");
		String marchantCode;										//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_WXAPP; 		//交易类型-网关支付:01, 代收付:02
		String accountDate = null; 									//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_WXAPP; 		//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_TL;
		try {
			/*
			 * 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.tLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载上游对账文件并入库, 通联微信APP支付对账(课栈)");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载上游对账文件并入库, 通联微信APP支付对账(通联)");
	}
	/***
	 * 对账
	 * 网关&移动支付对账,移动支付对账,微信(通联)
	 */
	public void collateWGAndYD() {
		logger.info(">>> >>> >>> 开始: 对账, 网关&移动支付对账&微信&SDK 对账(通联)");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_TL;
		String accountTypeWG = SettleConstants.ACCOUNT_TYPE_CHANNEL;
		String accountTypeYD = SettleConstants.ACCOUNT_TYPE_MOBILE;
		String accountTypeWX = SettleConstants.ACCOUNT_TYPE_WXMOBILE;
		String accountTypeTLSDK = SettleConstants.ACCOUNT_TYPE_TLSDK;
		String accountTypeWXAPP = SettleConstants.ACCOUNT_TYPE_WXAPP;
		
		try {
			String merchantCode = Constants.HT_ID;
			collateService.collage(payChannelId, accountTypeWG, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账,移动支付对账(会唐)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.JRD_ID;
			/*
			 * 君融贷网关支付对账
			 */
//			collateService.collage(payChannelId, accountTypeWG, merchantCode, bussTypeWG);
			/*
			 * 君融贷移动支付对账
			 */
			collateService.collage(payChannelId, new String[]{accountTypeYD, accountTypeWG}, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账,移动支付对账(君融贷 移动, 网关)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.FN_ID;
			collateService.collage(payChannelId, accountTypeWG, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账,移动支付对账(丰年/展酷/卖家云)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.ZJ_ID;
			collateService.collage(payChannelId, new String[]{accountTypeWG, accountTypeWX}, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账,微信支付对账(指尖代言)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, new String[]{accountTypeWG, accountTypeWX}, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账,微信支付对账(融数网关, 微信)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.ZK_ID;
			collateService.collage(payChannelId, accountTypeWG, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账,微信支付对账(展酷)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.LKKJ_ID;
			collateService.collage(payChannelId, new String[]{accountTypeWG, accountTypeWX}, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账,微信支付对账(领客科技", e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.TXFC_ID;
			collateService.collage(payChannelId, new String[]{accountTypeWG, accountTypeYD, accountTypeWX, accountTypeTLSDK}, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账,微信支付对账(天下房仓)", e);
			e.printStackTrace();
		}
//		try {
//			String merchantCode = Constants.RSQB_ID;
//			collateService.collage(payChannelId, accountTypeTLSDK, merchantCode, null);
//		} catch (Exception e) {
//			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账, SDK付对账(融数钱包)", e);
//			e.printStackTrace();
//		}
		try {
			String merchantCode = Constants.KZ_ID;
			collateService.collage(payChannelId, new String[]{accountTypeWG,accountTypeWX,accountTypeWXAPP,accountTypeTLSDK}, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账, SDK付对账(课栈)", e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.WLT_ID;
			collateService.collage(payChannelId, new String[]{accountTypeWG,accountTypeWX,accountTypeTLSDK}, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关&移动支付对账, SDK付对账(沃雷特)", e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账, 网关&移动支付对账&微信&SDK 对账(通联)");
	}
	/***
	 *生成下游对账文件
	 * （全机构）10:30
	 */
	public void crateCollateFile() {
		logger.info(">>> >>> >>> 开始: 生成下游对账文件");
		Map<String, Object> resultMap;
		String readType = null;
		String marchantCode = null;
		try {
			logger.info(">>> >>> >>> 生成下游对账文件");
			marchantCode = "";
			resultMap = collateService.createCollateFile(marchantCode, readType);
			this.printResultMessage(resultMap);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 生成下游对账文件");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info(">>> >>> >>> 结束: 生成下游对账文件");
	}
	/***
	 * 下载连连支付移动快捷支付对账文件
	 */
	public void lLKJFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载【融数 与 连连快捷支付】上游对账文件并入库, 移动快捷支付对账(融数) ");
		String marchantCode;												//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_LLKJ; 				//交易类型-网关支付:01, 代收付:02, 移动支付:03, 连连快捷支付:04
		String accountDate = null; 											//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_LLKJ; 				//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_LIANLIAN;		//渠道号 01通联、02支付宝、03微信、04支付宝
		try {
			/*
			 *连连支付 - 君融贷
			 */
			marchantCode = Constants.JRD_ID;
			collateService.lLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【君融贷 与 连连快捷支付】上游对账文件并入库, 移动快捷支付对账(君融贷) ");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *连连支付 - 融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.lLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【融数 与 连连快捷支付】上游对账文件并入库, 移动快捷支付对账(融数) ");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *连连支付 - 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.lLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【融数 与 连连快捷支付】上游对账文件并入库, 移动快捷支付对账(课栈) ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载【融数 与 连连快捷支付】上游对账文件并入库, 移动快捷支付对账(融数) ");
	}
	/***
	 * 对账
	 * 连连快捷支付
	 */
	public void collateLLKJ() {
		logger.info(">>> >>> >>> 开始: 对账, 【融数 与 连连快捷支付】上游对账文件并入库, 移动快捷支付对账(融数) ");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_LIANLIAN;
		String bussTypeLLKJ = null;
		String accountTypeLLKJ = SettleConstants.ACCOUNT_TYPE_LLKJ;
		try {
			String merchantCode = Constants.JRD_ID;
			collateService.collage(payChannelId, accountTypeLLKJ, merchantCode, bussTypeLLKJ);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 【融数 与 连连快捷支付】上游对账文件并入库, 移动快捷支付对账(融数) ");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountTypeLLKJ, merchantCode, bussTypeLLKJ);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 【融数 与 连连快捷支付】上游对账文件并入库, 移动快捷支付对账(融数) ");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.KZ_ID;
			collateService.collage(payChannelId, accountTypeLLKJ, merchantCode, bussTypeLLKJ);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 【课栈 与 连连快捷支付】上游对账文件并入库, 移动快捷支付对账(课栈) ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账, 【融数 与 连连快捷支付】上游对账文件并入库, 移动快捷支付对账(融数) ");
	}
	/***
	 * 下载 连连支付ZF 对账文件
	 */
	public void lLZFFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载【连连支付ZF】上游对账文件并入库");
		String marchantCode;												//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_GENERATE; 				//交易类型-网关支付:01, 代收付:02, 移动支付:03, 连连快捷支付:04
		String accountDate = null; 											//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_GENERATE; 				//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_LIANLIAN;		//渠道号 01通联、02支付宝、03微信、04支付宝
		try {
			/*
			 *连连支付 - 君融贷
			 */
			marchantCode = Constants.JRD_ID;
			collateService.lLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【连连支付ZF】上游对账文件并入库 ", e);
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *连连支付 - 融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.lLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【连连支付ZF】上游对账文件并入库 ", e);
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *连连支付 - 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.lLFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载课栈【连连支付ZF】上游对账文件并入库 ", e);
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载【连连支付ZF】上游对账文件并入库 ");
	}
	/***
	 * 对账
	 * 连连支付
	 */
	public void collateLLZF() {
		logger.info(">>> >>> >>> 开始: 对账, 【 连连支付ZF】上游对账文件并入库");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_LIANLIAN;
		String bussTypeLLKJ = null;
		String accountTypeLLKJ = SettleConstants.ACCOUNT_TYPE_GENERATE;
		try {
			String merchantCode = Constants.JRD_ID;
			collateService.collage(payChannelId, accountTypeLLKJ, merchantCode, bussTypeLLKJ);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 【连连支付ZF】", e);
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountTypeLLKJ, merchantCode, bussTypeLLKJ);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 【连连支付ZF】", e);
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.KZ_ID;
			collateService.collage(payChannelId, accountTypeLLKJ, merchantCode, bussTypeLLKJ);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 【课栈连连支付ZF】", e);
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账, 【连连支付ZF】");
	}
	/***
	 * 下载联动优势 网关 对账文件
	 */
	public void lDWGFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载【融数 与 联动优势】上游对账文件并入库, 网管支付对账(融数) ");
		String marchantCode;												//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_CHANNEL; 				//交易类型-网关支付:01, 代收付:02, 移动支付:03, 连连快捷支付:04
		String accountDate = null; 											//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_CHANNEL; 				//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_UMPAY;		//渠道号 01通联、02支付宝、03微信、04支付宝
		try {
			/*
			 *联动优势支付 - 融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.lDFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【融数 与 联动优势】上游对账文件并入库, 网管支付对账(融数) ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载【融数 与 联动优势】上游对账文件并入库, 网管支付对账(融数) ");
	}
	/***
	 * 下载联动优势 代收付 对账文件
	 */
	public void lDZFFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载【融数 与 联动优势】上游对账文件并入库, ZF支付对账(融数) ");
		String marchantCode;												//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_GENERATE; 				//交易类型-网关支付:01, 代收付:02, 移动支付:03, 连连快捷支付:04
		String accountDate = null; 											//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_GENERATE; 				//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_UMPAY;		//渠道号 01通联、02支付宝、03微信、04支付宝
		try {
			/*
			 *联动优势支付 - 融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.lDFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【融数 与 联动优势】上游对账文件并入库, ZF支付对账(融数) ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载【融数 与 联动优势】上游对账文件并入库, ZF支付对账(融数) ");
	}
	/***
	 * 对账
	 * 联动网管支付
	 */
	public void collateLDWG() {
		logger.info(">>> >>> >>> "+ (new Date()).toLocaleString() +" 开始:对账 【融数 与 联动优势】上游对账文件并入库, 网管支付对账(融数) ");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_UMPAY;
		String bussType = null;
		String accountType = SettleConstants.ACCOUNT_TYPE_CHANNEL;
		try {
			String merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountType, merchantCode, bussType);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账 【融数 与 联动优势】上游对账文件并入库, 网管支付对账(融数) ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账 【融数 与 联动优势】上游对账文件并入库, 网管支付对账(融数) ");
	}
	/***
	 * 对账
	 * 联动网管支付
	 */
	public void collateLDZF() {
		logger.info(">>> >>> >>> "+ (new Date()).toLocaleString() +" 开始:对账 【融数 与 联动优势】上游对账文件并入库, 代收付 对账(融数) ");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_UMPAY;
		String bussType = null;
		String accountType = SettleConstants.ACCOUNT_TYPE_GENERATE;
		try {
			String merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountType, merchantCode, bussType);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账 【融数 与 联动优势】上游对账文件并入库, 代收付 对账(融数) ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账 【融数 与 联动优势】上游对账文件并入库, 代收付 对账(融数) ");
	}
	/***
	 * 下载畅捷支付代收付对账文件
	 */
	public void cJZFFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> "+ (new Date()).toLocaleString() +" 开始: 下载【融数 与 畅捷支付】上游对账文件并入库, 代收付对账(融数) ");
		String marchantCode;												//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_GENERATE; 				//交易类型-网关支付:01, 代收付:02, 移动支付:03, 连连快捷支付:04
		String accountDate = null; 											//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_GENERATE; 				//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_CHANPAY;		//渠道号 01通联、02支付宝、03微信、04支付宝
		try {
			/*
			 *联动优势支付 - 融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.cjFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【融数 与 畅捷支付】上游对账文件并入库, 代收付对账(融数) ");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *畅捷支付 - 融德
			 */
			marchantCode = Constants.RD_ID;
			collateService.cjFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【融德 与 畅捷支付】上游对账文件并入库, 代收付对账(融德) ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载【融数 与 畅捷支付】上游对账文件并入库, 代收付对账(融数) ");
	}
	/***
	 * 对账
	 * 联动网管支付
	 */
	public void collateCJZF() {
		logger.info(">>> >>> >>> "+ (new Date()).toLocaleString() +" 开始:对账 【融数 与 畅捷支付】上游对账文件并入库, 代收付对账(融数, 融德)");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_CHANPAY;
		String bussType = null;
		String accountType = SettleConstants.ACCOUNT_TYPE_GENERATE;
		try {
			String merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountType, merchantCode, bussType);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账 【融数 与 畅捷支付】上游对账文件并入库, 代收付对账(融数, 融德)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.RD_ID;
			collateService.collage(payChannelId, accountType, merchantCode, bussType);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账 【融德 与 畅捷支付】上游对账文件并入库, 代收付对账(融德, 融德)");
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账 【融数 与 畅捷支付】上游对账文件并入库, 代收付对账(融数, 融德)");
	}
	/***
	 * 打印提示信息
	 */
	private void printResultMessage(Map<String, Object> resultMap) {
		String code = String.valueOf(resultMap.get("code"));
		String msg = String.valueOf(resultMap.get("msg"));
		logger.info(">>> >>> >>> code:" + code + ", msg:" + msg);
	}
	
	/***
	 * 内部对账
	 * 账户跟多渠道
	 */
	public void collateAccAndMulti() {
		logger.info(">>> >>> >>> "+ (new Date()).toLocaleString() +" 开始:对账 【账户与 多渠道】");
		String payChannelId = "AM01";
		String readType = "01";
		String[] funcCodes = new String[]{"4015","4017"};
		try {
			collateService.collageAccAndMulti(funcCodes,payChannelId,readType,null,null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 开始:对账 【账户与 多渠道】");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 开始:对账 【账户与 多渠道】");
	}
	
	/***
	 * 下载平安银企 代收付 对账文件
	 */
	public void PABZFFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载&入库【融数 与 平安银企】上游对账文件并入库, 支付对账(融数) ");
		String marchantCode;												//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_PAB; 				//交易类型-网关支付:01, 代收付:02, 移动支付:03, 连连快捷支付:04
		String accountDate = null; 											//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_PAB; 				//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_PAB;		//渠道号 01通联、02支付宝、03微信、04支付宝
		try {
			/*
			 *平安银企 - 融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.multiFileDownAndReadCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【融数 与 平安银企】上游对账文件并入库, 支付对账(融数) ");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *民生银企 - 君融贷
			 */
			marchantCode = Constants.JRD_ID;
			payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;
			fileType = SettleConstants.ACCOUNT_COED_CMBC;
			readType = SettleConstants.ACCOUNT_TYPE_CMBC;
			collateService.multiFileDownAndReadCollateFile_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【融数 与 民生银企】上游对账文件并入库, 支付对账(融数) ");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *民生银企 - 沣盈
			 */
			marchantCode = "N000001";
			payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;
			fileType = SettleConstants.ACCOUNT_COED_CMBC;
			readType = SettleConstants.ACCOUNT_TYPE_CMBC;
			collateService.multiFileDownAndReadCollateFile_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【沣盈 与 民生银企】上游对账文件并入库, 支付对账(沣盈 ) ");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			/*
			 *民生银企 - 盛繁
			 */
			marchantCode = "N000002";
			payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;
			fileType = SettleConstants.ACCOUNT_COED_CMBC;
			readType = SettleConstants.ACCOUNT_TYPE_CMBC;
			collateService.multiFileDownAndReadCollateFile_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【盛繁 与 民生银企】上游对账文件并入库, 支付对账(盛繁) ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载【融数 与 平安银企】上游对账文件并入库, 支付对账(融数) ");
	}
	/***
	 * 对账
	 * 平安银企 代收付
	 */
	public void collatePAZF() {
		logger.info(">>> >>> >>> "+ (new Date()).toLocaleString() +" 开始:对账 【融数 与平安银企】上游对账文件并入库, 代收付对账(融数)");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_PAB;
		String bussType = null;
		String accountType = SettleConstants.ACCOUNT_TYPE_PAB;
		try {
			String merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountType, merchantCode, bussType);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账 【融数 与 平安银企】上游对账文件并入库, 代收付对账(融数)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = Constants.JRD_ID;
			payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;
			accountType = SettleConstants.ACCOUNT_TYPE_CMBC;
			collateService.collage(payChannelId, accountType, merchantCode, bussType);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账 【融数 与 平安银企】上游对账文件并入库, 代收付对账(融数)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode = "N000001";
			payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;
			accountType = SettleConstants.ACCOUNT_TYPE_CMBC;
			collateService.collage(payChannelId, accountType, merchantCode, bussType);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账 【沣盈 与 平安银企】上游对账文件并入库, 代收付对账(沣盈)");
			logger.error("",e);
			e.printStackTrace();
		}
		try {
			String merchantCode =  "N000002";
			payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;
			accountType = SettleConstants.ACCOUNT_TYPE_CMBC;
			collateService.collage(payChannelId, accountType, merchantCode, bussType);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账 【盛繁 与 平安银企】上游对账文件并入库, 代收付对账(盛繁)");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账 【融数 与 平安银企】上游对账文件并入库, 代收付对账(融数)");
	}

	/***
	 * 下载平安跨行 代收付 对账文件
	 */
	public void pABKHKFFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载【平安跨行】");
		String marchantCode = null;													//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_PABKHKF; 				//交易类型-网关支付:01, 代收付:02, 移动支付:03, 连连快捷支付:04
		String accountDate = null; 												//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_PABKHKF; 				//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_PAB;				//渠道号 01通联、02支付宝、03微信、04支付宝
//		try {
//			/*
//			 *平安银企 - 融数钱包
//			 */
//			marchantCode = Constants.RSQB_ID;
//			collateService.multiFileDownAndReadCollateFile_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
//		} catch (Exception e) {
//			logger.error(">>> >>> >>> 异常: 下载【 平安跨行】("+ marchantCode +") ");
//			logger.error("",e);
//			e.printStackTrace();
//		}
		try {
			/*
			 *平安银企 - 融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.multiFileDownAndReadCollateFile_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【 平安跨行】("+ marchantCode +") ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info(">>> >>> >>> 结束: 下载【平安跨行】");
	}
	/***
	 * 对账
	 * 平安跨行 代收付
	 */
	public void collatePAKHKF() {
		logger.info(">>> >>> >>> 开始:对账 【平安跨行】");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_PAB;
		String merchantCode = null;
		String bussType = null;
		String accountType = SettleConstants.ACCOUNT_TYPE_PABKHKF;
//		try {
//			merchantCode = Constants.RSQB_ID;
//			collateService.collage(payChannelId, accountType, merchantCode, bussType);
//		} catch (Exception e) {
//			logger.error(">>> >>> >>> 异常: 对账 【平安跨行】("+ merchantCode +")");
//			logger.error("",e);
//			e.printStackTrace();
//		}
		try {
			merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountType, merchantCode, bussType);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账 【 平安跨行】("+ merchantCode +")");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账 【平安跨行】");
	}
	
	/***
	 * 下载平安B2B 对账文件
	 */
	public void pABB2BFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载&入库【平安B2B】");
		String marchantCode = null;											//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_PABB2B; 				//交易类型-网关支付:01, 代收付:02, 移动支付:03, 连连快捷支付:04
		String accountDate = null; 											//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_PABB2B; 				//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_PAB;			//渠道号 01通联、02支付宝、03微信、04支付宝
		try {
			/*
			 *平安银企 - 融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.padB2BDownAndReadCollateFile_SFTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载&入库【平安B2B】("+marchantCode+")");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info(">>> >>> >>> 结束: 下载&入库【平安B2B】");
	}
	/***
	 * 对账
	 * 平安B2B
	 */
	public void collatePABB2B() {
		logger.info(">>> >>> >>> 开始:对账 【平安B2B】");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_PAB;
		String merchantCode = null;
		String bussType = null;
		String accountType = SettleConstants.ACCOUNT_TYPE_PABB2B;
		try {
			merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountType, merchantCode, bussType);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账 【融数 与 平安跨行】("+ merchantCode +")");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束:对账 【平安B2B】");
	}
	
	/***
	 * 下载融宝网关支付 对账文件
	 */
	public void RBWGFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载【融宝-融数】上游对账文件并入库, 网关支付对账(融数)");
		String marchantCode;												//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_CHANNEL; 				//交易类型-网关支付:01, 代收付:02, 移动支付:03, 连连快捷支付:04
		String accountDate = null; 											//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_CHANNEL; 				//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_RB;		//渠道号 01通联、02支付宝、03微信、04支付宝
		try {
			/*
			 *融宝 - 融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常:下载【融宝-融数】上游对账文件并入库, 网关支付对账(融数)");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束:下载【融宝-融数】上游对账文件并入库, 网关支付对账(融数) ");
	}
	
	/***
	 * 对账
	 * 网关对账(融宝)
	 */
	public void collateRBWG() {
		logger.info(">>> >>> >>> 开始: 对账, 网关 对账(融宝)");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_RB;
		String accountTypeWG = SettleConstants.ACCOUNT_TYPE_CHANNEL;
		try {
			String merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountTypeWG, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 网关 对账(融宝)");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账, 网关 对账(融宝)");
	}
	
	/***
	 * 下载快付通 对账文件
	 */
	public void lycheeFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载【快付通-代收付】上游对账文件并入库");
		String marchantCode;
		String readType = SettleConstants.ACCOUNT_TYPE_GENERATE;
		String accountDate = null;
		String fileType = SettleConstants.ACCOUNT_COED_GENERATE;
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_LYCHEE;
		try {
			/*
			 *测试-丰盈
			 */
			marchantCode = Constants.FYBL_ID;
			collateService.lycheeDownAndReadCollateFile_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载【快付通-代收付】上游对账文件并入库", e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载【快付通-代收付】上游对账文件并入库");
	}
	
	/***
	 * 对账【快付通-代收付】
	 */
	public void collateLychee() {
		logger.info(">>> >>> >>> 开始: 对账【快付通-代收付】");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_LYCHEE;
		String accountTypeWG = SettleConstants.ACCOUNT_TYPE_GENERATE;
		try {
			String merchantCode = Constants.FYBL_ID;
			collateService.collage(payChannelId, accountTypeWG, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账【快付通-代收付】", e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账【快付通-代收付】");
	}
	
	/***
	 * 易宝总的入口
	 */
	public void ybFileDownAndCollateAll() {
		this.ybAuthFileDownAndReadCollateFile();
		this.ybPaymentFileDownAndReadCollateFile();
		this.collateYBAuth();
		this.ybWithDrawFileDownAndReadCollateFile();
		this.collateYBWithDraw();
		this.ybChangeCardFileDownAndReadCollateFile();
		this.ybRefundFileDownAndReadCollateFile();
		this.collateYBRefund();
		this.ybPayDivideFileDownAndReadCollateFile();
	}
	
	
	/***
	 * 下载易宝鉴权绑卡对账文件
	 */
	public void ybAuthFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载易宝鉴权绑卡对账文件并入库 ");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_YB_AUTH;//交易类型:"AUTH" 易宝-鉴权绑卡
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_TYPE_YB_AUTH;//交易类型-"AUTH" 易宝-鉴权绑卡
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;//渠道号 S03
		try {
			/*
			 *易宝鉴权绑卡 - 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载易宝鉴权绑卡对账文件并入库,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载易宝鉴权绑卡对账文件并入库,课栈");
	}
	
	/***
	 * 下载易宝支付对账文件
	 */
	public void ybPaymentFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载易宝支付对账文件并入库 ");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_YB_PAYMENT;//交易类型:"PAYMENT" 易宝-支付
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_TYPE_YB_PAYMENT;//交易类型-"PAYMENT" 易宝-支付
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;//渠道号 S03
		try {
			/*
			 *易宝支付- 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载易宝支付对账文件并入库,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载易宝支付对账文件并入库,课栈");
	}
	
	/***
	 * 对账
	 * 易宝支付
	 */
	public void collateYBAuth(){
		logger.info(">>> >>> >>> 开始: 对账,易宝支付");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;
		String accountType = SettleConstants.ACCOUNT_TYPE_YB_PAYMENT;
		try {
			String merchantCode = Constants.KZ_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账,易宝支付");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账,易宝支付");
	}
	
	
	/***
	 * 下载易宝提现对账文件
	 */
	public void ybWithDrawFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载易宝提现对账文件并入库 ");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_YB_WITHDRAW;//交易类型:"WITHDRAW" 易宝-提现
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_TYPE_YB_WITHDRAW;//交易类型-"WITHDRAW" 易宝-提现
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;//渠道号 S03
		try {
			/*
			 *易宝提现- 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载易宝提现对账文件并入库,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载易宝提现对账文件并入库,课栈");
	}
	
	/***
	 * 对账
	 * 易宝提现WITHDRAW
	 */
	public void collateYBWithDraw(){
		logger.info(">>> >>> >>> 开始: 对账,易宝提现");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;
		String accountType = SettleConstants.ACCOUNT_TYPE_YB_WITHDRAW;
		try {
			String merchantCode = Constants.KZ_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账,易宝提现");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账,易宝提现");
	}
	
	/***
	 * 下载易宝换卡对账文件
	 */
	public void ybChangeCardFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载易宝换卡对账文件并入库 ");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_YB_CHANGECARD;//交易类型:"WITHDRAW" 易宝-提现
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_TYPE_YB_CHANGECARD;//交易类型-"WITHDRAW" 易宝-提现
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;//渠道号 S03
		try {
			/*
			 *易宝换卡- 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载易宝换卡对账文件并入库,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载易宝换卡对账文件并入库,课栈");
	}
	
	/***
	 * 下载易宝退款对账文件
	 */
	public void ybRefundFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载易宝退款对账文件并入库 ");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_YB_REFUND;//交易类型:"WITHDRAW" 易宝-提现
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_TYPE_YB_REFUND;//交易类型-"WITHDRAW" 易宝-提现
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;//渠道号 S03
		try {
			/*
			 *易宝退款- 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载易宝退款对账文件并入库,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载易宝退款对账文件并入库,课栈");
	}
	/***
	 * 对账
	 * 易宝退款WITHDRAW
	 */
	public void collateYBRefund(){
		logger.info(">>> >>> >>> 开始: 对账,易宝退款");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;
		String accountType = SettleConstants.ACCOUNT_TYPE_YB_REFUND;
		try {
			String merchantCode = Constants.KZ_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账,易宝退款");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账,易宝退款");
	}
	
	
	/***
	 * 下载易宝分账对账文件
	 */
	public void ybPayDivideFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载易宝分账对账文件并入库 ");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_YB_PAYDIVIDE;//交易类型:"PAYDIVIDE" 易宝-分账
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_TYPE_YB_PAYDIVIDE;//交易类型-"PAYDIVIDE" 易宝-分账
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;//渠道号 S03
		try {
			/*
			 *易宝分账- 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载易宝分账对账文件并入库,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载易宝分账对账文件并入库,课栈");
	}
	
	/***
	 * 民生单笔代付总的入口
	 */
	public void cMBCFileDownAndCollateAll() {
		this.cMBCAgentPayDLFileDownAndReadCollateFile();
		this.cMBCAgentPayFDLFileDownAndReadCollateFile();
		this.collatecMBCAgent();
		this.cMBCRefundDLFileDownAndReadCollateFile();
		this.cMBCRefundFDLFileDownAndReadCollateFile();
		this.collatecMBCAgentRefund();
	}
	
	/***
	 * 下载民生银行-单笔联机代付文件（代理支付）
	 */
	public void cMBCAgentPayDLFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始:下载 民生银行-单笔联机代付文件");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_DL;
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_CMBC_AgentPay_DL;
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;//渠道号 Y02
		try {
			/*
			 *民生银行-单笔联机代付- 课栈
			 */
			marchantCode = Constants.JRD_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载 民生银行-单笔联机代付文件并入库,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载 民生银行-单笔联机代付文件并入库,课栈");
	}
	/***
	 * 下载民生银行-单笔联机代付文件(非代理支付）
	 */
	public void cMBCAgentPayFDLFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始:下载 民生银行-单笔联机代付文件");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_FDL;
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_CMBC_AgentPay_FDL;
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;//渠道号 Y02
		try {
			/*
			 *民生银行-单笔联机代付- 课栈
			 */
			marchantCode = Constants.JRD_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载 民生银行-单笔联机代付文件并入库,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载 民生银行-单笔联机代付文件并入库,课栈");
	}
	/***
	 * 对账
	 * 民生银行-单笔联机代付
	 */
	public void collatecMBCAgent(){
		logger.info(">>> >>> >>> 开始: 对账,民生银行-单笔联机代付");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;
		String accountType = "AgentPay";
		try {
			String merchantCode = Constants.JRD_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账,民生银行-单笔联机代付");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账,民生银行-单笔联机代付");
	}
	
	/***
	 * 下载民生银行-单笔联机代付退票(代理)
	 */
	public void cMBCRefundDLFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始:下载 民生银行-单笔联机代付退票文件");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_CMBC_Refund_DL;
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_CMBC_Refund_DL;
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;//渠道号 Y02
		try {
			/*
			 *民生银行-单笔联机代付退票
			 */
			marchantCode = Constants.JRD_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载 民生银行-单笔联机代付退票文件,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载 民生银行-单笔联机代付退票文件,课栈");
	}
	
	/***
	 * 下载民生银行-单笔联机代付退票文件(非代理)
	 */
	public void cMBCRefundFDLFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始:下载 民生银行-单笔联机代付退票文件");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_CMBC_Refund_FDL;
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_CMBC_Refund_FDL;
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;//渠道号 Y02
		try {
			/*
			 *民生银行-单笔联机代付退票- 课栈
			 */
			marchantCode = Constants.JRD_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载 民生银行-单笔联机代付退票文件,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载 民生银行-单笔联机代付退票文件,课栈");
	}
	
	/***
	 * 对账
	 * 民生银行-单笔联机代付退票
	 */
	public void collatecMBCAgentRefund(){
		logger.info(">>> >>> >>> 开始: 对账,民生银行-单笔联机代付退票");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_CMBC;
		String accountType = SettleConstants.ACCOUNT_TYPE_CMBC_Refund;
		try {
			String merchantCode = Constants.JRD_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账,民生银行-单笔联机代付退票");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账,民生银行-单笔联机代付退票");
	}
	
	/***
	 * 易宝代付下载对账文件并入库、对账
	 */
	public void ybDf(){
		this.collateYbDfFileDownAndReadCollateFile();
		this.collateYbDf();
	}
	
	
	/***
	 * 易宝代付
	 * 下载易宝代付对账文件并入库
	 */
	public void collateYbDfFileDownAndReadCollateFile(){
		logger.info(">>> >>> >>> 开始:下载 易宝代付文件");
		String marchantCode;//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_YB_DF;
		String accountDate = null;//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_YB_DF;
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;//渠道号 S03
		try {
			/*
			 *易宝代付- 课栈
			 */
			marchantCode = Constants.KZ_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 下载 易宝代付文件,课栈 ");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 下载 易宝代付文件,课栈");
	}
	
	/***
	 * 对账
	 * 易宝代付
	 */
	public void collateYbDf(){
		logger.info(">>> >>> >>> 开始: 对账,易宝代付");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_YB;
		String accountType = SettleConstants.ACCOUNT_TYPE_YB_DF;
		try {
			String merchantCode = Constants.KZ_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 易宝代付");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账, 易宝代付");
	}
	
	public void rbDf(){
		this.rBDfFileDownAndReadCollateFile();
		this.collateRbDf();
	}
	
	/***
	 * 下载融宝代付对账文件
	 */
	public void rBDfFileDownAndReadCollateFile() {
		logger.info(">>> >>> >>> 开始: 下载【融宝代付-融数】上游对账文件并入库");
		String marchantCode;												//机构号
		String readType = SettleConstants.ACCOUNT_TYPE_RB_DF; 				//交易类型-网关支付:01, 代收付:02, 移动支付:03, 连连快捷支付:04
		String accountDate = null; 											//账期 null == 程序默认账期 T-1
		String fileType = SettleConstants.ACCOUNT_COED_RB_DF; 				//交易类型-网关支付:01, 代收付:02
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_RB;		//渠道号 01通联、02支付宝、03微信、04支付宝
		try {
			/*
			 *融宝代付 - 融数
			 */
			marchantCode = Constants.RS_ID;
			collateService.multiFileDownAndReadCollateFileP_FTP(marchantCode, readType, accountDate, fileType, payChannelId);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常:下载【融宝代付】上游对账文件并入库");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束:下载【融宝代付】上游对账文件并入库 ");
	}
	
	/***
	 * 对账
	 * 融宝代付
	 */
	public void collateRbDf() {
		logger.info(">>> >>> >>> 开始: 对账, 融宝代付");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_RB;
		String accountType = SettleConstants.ACCOUNT_TYPE_RB_DF;
		try {
			String merchantCode = Constants.RS_ID;
			collateService.collage(payChannelId, accountType, merchantCode, null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 对账, 融宝代付");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 对账,融宝代付");
	}
	
	/***
	 * 对账
	 * 银生宝代收
	 */
	public void collateUnsPay() {
		logger.info(">>> >>> >>> 开始: 定时对账, ");
		String payChannelId = SettleConstants.PAY_CHANNEL_ID_UNSPAY;//渠道号S06
		String accountType = SettleConstants.ACCOUNT_TYPE_GENERATE;//对账类型02
		try {
			String merchantCode = Constants.RS_ID;//机构号M00000X
			//下载文件并入库
			collateService.unsPayFileDownAndReadCollateFile( merchantCode, accountType, null, null, payChannelId);
			//开始对账
			collateService.collage(payChannelId, accountType, merchantCode, null);
			//accountService.collage("S06", "02", "M00000X",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-06-26 00:00:00") ,null);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 定时对账, 银生宝代收");
			logger.error("",e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 定时对账,银生宝代收");
	}
}
