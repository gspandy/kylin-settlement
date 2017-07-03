package com.rkylin.settle.settleInterface.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.filedownload.CheckfileDownload;
import com.rkylin.settle.filedownload.CjzfFileDown;
import com.rkylin.settle.filedownload.HtglRepayDownload;
import com.rkylin.settle.filedownload.LianDongFileDownload;
import com.rkylin.settle.filedownload.LycheeDownload;
import com.rkylin.settle.filedownload.MultiFileDown;
import com.rkylin.settle.filedownload.Multi_PAB;
import com.rkylin.settle.filedownload.PABB2BDownload;
import com.rkylin.settle.filedownload.ROPFileDown;
import com.rkylin.settle.logic.BalanceLogic;
import com.rkylin.settle.logic.CollateFileLogic;
import com.rkylin.settle.logic.CollateForPosLogic;
import com.rkylin.settle.logic.CollateLogic;
import com.rkylin.settle.logic.FromLoanLogic;
import com.rkylin.settle.logic.GetMTAegisTransOrderInfo;
import com.rkylin.settle.logic.ProfitFroLoanLogic;
import com.rkylin.settle.logic.ProfitLogic;
import com.rkylin.settle.logic.ReadFileLogic;
import com.rkylin.settle.pojo.SettlePosDetail;
import com.rkylin.settle.service.AccountService;
import com.rkylin.settle.service.CollectService;
import com.rkylin.settle.service.DsfService;
import com.rkylin.settle.service.ProfitService;
import com.rkylin.settle.service.SettlePosDetailService;
import com.rkylin.settle.service.SettleTransAccountService;
import com.rkylin.settle.service.SettleTransDetailService;
import com.rkylin.settle.settleInterface.SettleWebInterface;
import com.rkylin.settle.util.LogicConstantUtil;

public class SettleWebInterfaceImpl implements SettleWebInterface {
	//日志对象
	private static Logger logger = LoggerFactory.getLogger(SettleWebInterfaceImpl.class);
	@Autowired
	private ProfitLogic profitLogic; // 分润相关逻辑
	@Autowired
	private ProfitFroLoanLogic profitFroLoanLogic;  // 贷款相关逻辑
	@Autowired
	private FromLoanLogic fromLoanLogic; // 编辑贷款系统数据
	@Autowired
	private CollateLogic collateLogic; // 对账相关逻辑
	@Autowired
	private CollateForPosLogic collateForPosLogic; // 对账相关逻辑
	@Autowired
	private ReadFileLogic readFileLogic; // 读取文件相关逻辑
	@Autowired
	private CollateFileLogic collateFileLogic; // 写入文件相关逻辑
	@Autowired
	private ProfitService profitService; // 分润相关service
	@Autowired
	private AccountService accountService; // 分润相关service
	@Autowired
	private CheckfileDownload checkfileDownload; // 下载上游对账文件
	@Autowired
	private LianDongFileDownload lianDongFileDownload; // 下载上游对账文件
	@Autowired
	private CjzfFileDown cjzfFileDown; // 下载上游对账文件
	@Autowired
	private DsfService dsfService; // 代收付service
	@Autowired
	private GetMTAegisTransOrderInfo getMTAegisTransOrderInfo;	//读取账务系统交易
	@Autowired
	private CollectService collectService;	//汇总交易信息
	@Autowired
	private BalanceLogic balanceLogic;
	@Autowired
	private Multi_PAB multi_PAB;
	@Autowired
	private MultiFileDown multiFileDown;
	@Autowired
	private LogicConstantUtil logicConstantUtil;
	@Autowired
	private ROPFileDown rOPFileDown;
	@Autowired
	private SettleTransDetailService settleTransDetailService;
	@Autowired
	private SettlePosDetailService settlePosDetailService;
	@Autowired
	private PABB2BDownload pABB2BDownload;
	@Autowired
	private LycheeDownload lycheeDownload;
	@Autowired
	private SettleTransAccountService settleTransAccountService;
	@Autowired
	private HtglRepayDownload htglRepayDownload;
	/***
	 * 存储过程 读取账户系统交易并录入清结算库中
	 */
	@Override
	public void readAccDate() throws Exception {
		profitService.readAccDateBySP();
	}
	/***
	 * 存储过程 读取账户系统交易并录入清结算库中
	 */
	@Override
	public void readAccOldDate() throws Exception {
		profitService.readAccOldDateBySP();
	}
	/**
	 * @Description: 读取账户系统交易并录入清结算库中
	 * @param beginDate
	 *            开始时间yyyy-MM-dd HH:mm:ss
	 * @param endDate
	 *            结束时间yyyy-MM-dd HH:mm:ss
	 * @author CLF
	 */
	@Override
	public void getAccountOldTransOrderInfos(Date beginDate, Date endDate)
			throws Exception {
		getMTAegisTransOrderInfo.getAccountOldTransOrderInfos(beginDate, endDate);
	}
	/**
	 * @Description: 读取账户系统交易并录入清结算库中
	 * @param beginDate
	 *            开始时间yyyy-MM-dd HH:mm:ss
	 * @param endDate
	 *            结束时间yyyy-MM-dd HH:mm:ss
	 * @author CLF
	 */
	@Override
	public void getAccountTransOrderInfos(Date beginDate, Date endDate)
			throws Exception {
		getMTAegisTransOrderInfo.getAccountTransOrderInfos(beginDate, endDate);
	}
	/**
	 * @Description: 读取贷款系统交易并录入清结算库中
	 * @param accDate
	 *            记账日期yyyy-MM-dd HH:mm:ss
	 * @author SRB
	 */
	@Override
	public void getLoanTransOrderInfos(Date accDate)
			throws Exception {
		fromLoanLogic.getLoanDetailFromLoan(accDate);
	}
	/**
	 * 从多渠道读取交易信息并存入'清算'DB
	 * 
	 * @param accountDate
	 * @return 提示信息
	 */
	@Override
	public Map<String, Object> getTransDetailFromMultiGate(Date accountDate)
			throws Exception {
		return collateLogic.getTransDetailFromMultiGate(accountDate);
	}

	/**
	 * @Description: 读取通联对账文件，并录入数据库
	 * @param marchantCode
	 *            机构号
	 * @param readType
	 *            交易类型-网关支付:01, 代收付:02
	 * @param accountDate
	 *            账期
	 * @param fileType
	 *            文件类型-WG:网关支付 ZF：代收付
	 * @param payChannelId
	 *            渠道号 01通联、02支付宝
	 * @return
	 * @throws Exception
	 * @autor CLF
	 */
	@Override
	public Map<String, Object> readCollateFile(String marchantCode,
			String readType, String accountDate, String fileType,
			String payChannelId) throws Exception {
		return collateFileLogic.readCollateFile(marchantCode, readType,
				accountDate, fileType, payChannelId);
	}

	/***
	 * 上传下游对账文件到ROP
	 * 
	 * @param marchantCode
	 *            机构号
	 * @param readType
	 *            交易类型
	 * @param accountDate
	 *            账期
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> uploadCollateFile(String marchantCode,
			String readType, Date accountDate)
			throws Exception {
		return collateFileLogic.uploadCollateFile(marchantCode, readType,
				accountDate);
	}
	/***
	 * 上传下游对账文件到ROP
	 * 
	 * @param marchantCode
	 *            机构号
	 * @param readType
	 *            交易类型
	 * @param accountDate
	 *            账期
	 * @param accountDate
	 *            批次号
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> uploadCollateFile(String marchantCode,
			String readType, Date accountDate, String batch)
			throws Exception {
		return collateFileLogic.uploadCollateFile(marchantCode, readType,
				accountDate, batch);
	}

	/***
	 * 生成下游对账文件到ROP
	 * 
	 * @param marchantCode
	 *            机构号
	 * @param readType
	 *            交易类型
	 * @param accountDate
	 *            账期
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> createCollateFile(String marchantCode,
			String readType, Date accountDate)
			throws Exception {
		return collateFileLogic.createCollateFile(marchantCode, readType,
				accountDate);
	}
	
	/***
	 * 对账
	 */
	@Override
	public void collage(String payChannelId, String accountType,
			String merchantCode, String bussType) throws Exception {
		accountService.collage(payChannelId, accountType, merchantCode,
				bussType);
	}

	/***
	 * 对账
	 */
	@Override
	public void collage(String payChannelId, String accountType,
			String merchantCode, Date accountDate, String bussType)
			throws Exception {
		accountService.collage(payChannelId, accountType, merchantCode,
				accountDate, bussType);
	}

	/***
	 * 开始清分 ... ...
	 */
	@Override
	public Map<String, Object> doProfit() {
		return profitService.doProfit();
	}

	/***
	 * 贷款系统清分 ... ...
	 */
	@Override
	public Map<String, Object> doProfitForLoan() {
		return profitFroLoanLogic.doProfit(null);
	}
	
	/**
	 * 指定账期下载通联对账文件
	 * 
	 * @param rootInstId
	 *            机构号
	 * @param fileType
	 *            文件类型：WG：网关支付，ZF：支付系统
	 * @param accountDate
	 *            上游账期
	 * @return
	 */
	@Override
	public Map<String, String> tlFileDown(String rootInstId, String fileType,
			String accountDate) throws Exception {
		return checkfileDownload.tlFileDown(rootInstId, fileType, accountDate);
	}

	/***
	 * 指定账期下载连连快捷支付对账文件
	 * 
	 * @return
	 */
	@Override
	public Map<String, String> lianLianFileDown(String accountDate,
			String merchantCode, String readType) throws Exception {
		return checkfileDownload.lianLianFileDown(accountDate, merchantCode,
				readType);
	}

	/***
	 * 指定账期下载 联动优势 网管 对账文件
	 * 
	 * @return
	 */
	@Override
	public Map<String, String> lDWGFileDown(String accountDate,
			String merchantCode, String readType) throws Exception {
		return lianDongFileDownload.lianDongFileDown(accountDate, merchantCode,
				readType);
	}

	/***
	 * 指定账期下载 畅捷支付 非充值 对账文件
	 * 
	 * @return
	 */
	@Override
	public Map<String, String> cJZFFileDown(String accountDate,
			String merchantCode, String readType) throws Exception {
		return cjzfFileDown.cjFileDown(accountDate, merchantCode, readType);
	}

	/***
	 * 结算分润结果 [调用账户系统接口]
	 * 
	 * @return 提示信息
	 */
	@Override
	public Map<String, Object> doProfigBalance() throws Exception {
		return profitLogic.doProfigBalance();
	}

	/***
	 * 结算分润结果 [调用账户系统接口]
	 * 
	 * @return 提示信息
	 */
	@Override
	public Map<String, Object> doProfigBalance(String[] ids) throws Exception {
		return profitLogic.doProfigBalance(ids);
	}
	/***
	 * 刷新分润规则
	 * 画面使用
	 */
	@Override
	public void refreshProfitKeyTask() {
		profitService.refreshProfitKey();
	}
	/***
	 * 刷新'订单号'与存放位置的对应关系
	 * 画面使用
	 */
	@Override
	public void refreshFuncCodeAndOrderNoRelationTask() {
		profitService.refreshFuncCodeAndOrderNoRelation();
	}
	/***
	 * 刷新'金额'与存放位置的对应关系
	 * 画面使用
	 */
	@Override
	public void refreshFuncCodeAndAmountRelationTask() {
		profitService.refreshFuncCodeAndAmountRelation();
	}
	/***
	 * 手工发送代收付系统
	 * 
	 * @return 提示信息
	 */
	//临时屏蔽掉，现只割接丰年
	//临时屏蔽掉，现只割接丰年
	//临时屏蔽掉，现只割接丰年
	//临时屏蔽掉，现只割接丰年
	@Override
	public Map<String, Object> manualSendToDsf(Integer[] ids,String flag, String[] rootInstCds,String[] funcCodes) throws Exception {
		dsfService.dealDsf(ids,flag,rootInstCds,null,funcCodes,null);
		return null;
	}
//	@Override
//	public Map<String, Object> manualSendToDsf(Integer[] ids,String flag, String[] rootInstCds,String[] funcCodes) throws Exception {
//		dsfService.dealDsf(ids,flag,new String[]{"M000001"},null,funcCodes,null);
//		return null;
//	}
	
	/***
	 * 手工发送代收付结果给订单系统(从交易表直接推送)
	 * 
	 * @return 提示信息
	 */
	@Override
	public Map<String, Object> manualSendOrder(Integer[] ids) throws Exception {
//		dsfService.sendOrder(ids,null);
		dsfService.returnAccount(ids,null);
		return null;
	}
	
	/***
	 * 手工发送代收付结果给订单系统(从结算表->汇总表->交易表->订单系统)
	 * 
	 * @return 提示信息
	 */
	@Override
	public Map<String, Object> sendOrderFromInvoice(Integer[] ids) throws Exception {
		dsfService.sendOrderAgain(ids,null);
		return null;
	}
	
	/**
	 * 发送代收付交易给代收付系统
	 */
	//临时屏蔽掉，现只割接丰年
	//临时屏蔽掉，现只割接丰年
	//临时屏蔽掉，现只割接丰年
	@Override
	public void sendDsf(String dataSource,String[] inRootInstCds,Integer[] orderTypes,Integer[] ids){
		dsfService.sendDsf(dataSource,inRootInstCds, orderTypes,ids);
	}
	/*
	public void sendDsf(String dataSource,String[] inRootInstCds,Integer[] orderTypes,Integer[] ids){
		dsfService.sendDsf(dataSource,new String[]{"M000001"}, orderTypes,ids);
	}*/
	
	//临时屏蔽掉，现只割接丰年
	//临时屏蔽掉，现只割接丰年
	//临时屏蔽掉，现只割接丰年
	@Override
	public Map<String, Object> summaryByIds(Integer[] ids) throws Exception{
		dsfService.dealDsf(ids,null,null,null,null,null);
		return null;
	}
	/*@Override
	public Map<String, Object> summaryByIds(Integer[] ids) throws Exception{
		dsfService.dealDsf(ids,null,new String[]{"M000001"},null,null,null);
		return null;
	}*/
	
	/***
	 * 结算贷款分润结果 [调用账户系统接口]
	 * 
	 * @return 提示信息
	 */
	@Override
	public Map<String, Object> doProfigBalanceForLoan() throws Exception {
		return profitFroLoanLogic.doProfigBalance();
	}

	/***
	 * 结算贷款分润结果 [调用账户系统接口]
	 * 
	 * @return 提示信息
	 */
	@Override
	public Map<String, Object> doProfigBalanceForLoan(String[] ids) throws Exception {
		return profitFroLoanLogic.doProfigBalance(ids);
	}
	
	
	
	@Override
	public void refreshDealProductCodeTofuncCode() {
		profitService.refreshDealProductCodeTofuncCode();
	}
	
	@Override
	public Map<String, Object> manualSendAccountTP(Integer[] ids,Integer[] deliverStatusIds) throws Exception {
		//TODO
		dsfService.sendAccountTP(ids,deliverStatusIds);
		return null;
	}
	
	@Override 
	public Map<String, Object> doCollect(Integer collectType) throws Exception {
		return doCollect(collectType, null);
	}
	
	@Override 
	public Map<String, Object> doCollect(Integer collectType, Date accountDate) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			collectService.doCollect(collectType, accountDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("汇总信息异常:", e);
			resultMap.put("code", -1);
			resultMap.put("msg", "汇总异常!");
			return resultMap;
		}
		resultMap.put("code", 1);
		resultMap.put("msg", "汇总成功!");
		return resultMap;
	}
	
	@Override 
	public Map<String, Object> doCollectBySettleKernelEntry(Long id) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			collectService.doCollectBySettleKernelEntry(id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("汇总信息异常:", e);
			resultMap.put("code", -1);
			resultMap.put("msg", "汇总异常!");
			return resultMap;
		}
		resultMap.put("code", 1);
		resultMap.put("msg", "汇总成功!");
		return resultMap;
	}
	@Override
	public Map<String, Object> manualWriteResult(Integer[] ids)
			throws Exception {
		dsfService.WriteResult(ids, null);
		return null;
	}
	
	@Override
	public void collecteAccAndMulti(String inRootInstCd,
			Date accounteDate) throws Exception {
		String payChannelId = "AM01";
		String readType = "01";
		String[] funcCodes = new String[]{"4015","4017"};
		try {
			accountService.collageAccAndMulti(funcCodes, payChannelId, readType,inRootInstCd,accounteDate);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 手工触发对账 【账户与 多渠道】");
			logger.error("",e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 从交易画面发起汇总
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	@Override
	public void doCollectByPage(Integer collectType, Integer[] idsArr) throws Exception {
		collectService.doCollect(collectType, idsArr);
	}
	/**
	 * 从汇总画面发起汇总
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	@Override
	public void doCollectByPage(Integer collectType, Date accountDate, String merchantCode, String payChannelId, String funcCode) throws Exception {
		collectService.doCollect(collectType, accountDate, merchantCode, payChannelId, funcCode);
	}
	/***
	 * 平安银行下载
	 * @return 提示信息
	 */
	@Override
	public Map<String, String> pABFileDown(String accountDate,
			String merchantCode, String readType) throws Exception {
		return multiFileDown.multi_4013C006(accountDate, merchantCode, readType);
	}
	/***
	 * 通联微信下载
	 * @return 提示信息
	 */
	@Override
	public Map<String, String> tlwxFileDown(String accountDate,
			String merchantCode, String readType) throws Exception {
		return multiFileDown.multiFileDown(accountDate, merchantCode, readType);
	}
	@Override
	public void refreshMultiGateChannelHome2PayChannelId() {
		logicConstantUtil.refreshMultiGateChannelHome2PayChannelId();
	}
	/***
	 * 接受订单系统
	 * @return 提示信息
	 */
	@Override
	public Map<String, Object> insertPosData(List<SettlePosDetail> settlePosDetail) throws Exception {
		return collateForPosLogic.insertPosData(settlePosDetail);
	}
	//临时屏蔽掉，现只割接丰年
	//临时屏蔽掉，现只割接丰年
	//临时屏蔽掉，现只割接丰年
	@Override
	public String getResultFromDsf(String rootInstCd, String businessCode,String requestNo, String orderNos, String accountDate) throws Exception {
		return dsfService.getResultFromDSF(rootInstCd, businessCode, requestNo, orderNos, accountDate);
	}
//	@Override
//	public String getResultFromDsf(String rootInstCd, String businessCode,String requestNo, String orderNos, String accountDate) throws Exception {
//		return dsfService.getResultFromDSF("M000001", businessCode, requestNo, orderNos, accountDate);
//	}
	@Override
	public Map<String, Object> doInvoiceSettle() throws Exception {
		return profitFroLoanLogic.doInvoiceSettle(null);
	}
	@Override
	public Map<String, Object> doInvoiceSettleForCash() throws Exception {
		return profitFroLoanLogic.doInvoiceSettleForCash(null);
	}
	@Override
	public Map<String, Object> hth_pay() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/***
	 * 根据分润结果表生成代付/提现交易数据
	 * 
	 * @return 提示信息
	 */
	@Override
	public Map<String, Object> doSettleInvoice() throws Exception {
		return profitFroLoanLogic.doSettleInvoice(null);
	}
	/**
	 * 从订单系统读取交易信息并存入'清算'DB
	 * 
	 * @param accountDate
	 * @return 提示信息
	 */
	@Override
	public Map<String, Object> getPosDetailFromOrder(Date accountDate)
			throws Exception {
		return collateForPosLogic.getTransDetailFromOrder(accountDate);
	}
	/***
	 * ROP对账文件下载
	 * @return 提示信息
	 */
	@Override
	public Map<String, String> rOPFileDown(String type,
			String batch, Date invoicedate,String priOrPubKey,String fileType) throws Exception {
		int flg = 2;
		if (priOrPubKey.indexOf("PUBLIC") != -1) {
			flg = 0;
		}
		if (priOrPubKey.indexOf("PRIVATE") != -1) {
			flg = 1;
		}
		return rOPFileDown.ROPfileDown(Integer.parseInt(type), batch, invoicedate, flg, priOrPubKey, fileType);
	}
	/***
	 * 对账
	 */
	@Override
	public void collageForPos(String payChannelId, String accountType,
			String merchantCode, String bussType) throws Exception {
		accountService.collageForPos(payChannelId, accountType, merchantCode,
				bussType);
	}

	/***
	 * 对账
	 */
	@Override
	public void collageForPos(String payChannelId, String accountType,
			String merchantCode, Date accountDate, String bussType)
			throws Exception {
		accountService.collageForPos(payChannelId, accountType, merchantCode,
				accountDate, bussType);
	}

	/***
	 * pos交易结算
	 */
	@Override
	public Map<String,Object> settleForPos( Date accountDate) throws Exception {
		return collateForPosLogic.getPosToSettle(accountDate);
	}
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表
	 */
	@Override
	public Map<String, Object> updateAccountInfoToDetailInfo() throws Exception {
		Map<String, Object> resultMap = null;
		try {
			resultMap = settleTransDetailService.updateAccountInfoToDetailInfo();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表", e);
		}
		return resultMap;
	}
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表
	 */
	@Override
	public Map<String, Object> updateAccountInfoToDetailInfo(Date account)
			throws Exception {
		Map<String, Object> resultMap = null;
		try {
			resultMap = settleTransDetailService.updateAccountInfoToDetailInfo(account);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表", e);
		}
		return resultMap;
	}
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表
	 */
	@Override
	public Map<String, Object> updateAccountInfoToDetailInfo(Date beginDate,
			Date endDate) throws Exception {
		Map<String, Object> resultMap = null;
		try {
			resultMap = settleTransDetailService.updateAccountInfoToDetailInfo(beginDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表", e);
		}
		return resultMap;
	}
	@Override
	public Map<String, Object> doWriteToSettleTransDetail() throws Exception {
		Map<String, Object> resultMap = null;
		try {
			resultMap = settleTransDetailService.doWriteToSettleTransDetail();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 将accountList中的readStatusId写入detailList", e);
		}
		return resultMap;
	}
	@Override
	public Map<String, Object> doWriteToSettleTransDetail(Date accountDate)
			throws Exception {
		Map<String, Object> resultMap = null;
		try {
			resultMap = settleTransDetailService.doWriteToSettleTransDetail(accountDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 将accountList中的readStatusId写入detailList", e);
		}
		return resultMap;
	}
	@Override
	public Map<String, Object> doWriteToSettleTransDetail(Date beginDate, Date endDate) throws Exception {
		Map<String, Object> resultMap = null;
		try {
			resultMap = settleTransDetailService.doWriteToSettleTransDetail(beginDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 将accountList中的readStatusId写入detailList", e);
		}
		return resultMap;
	}
	
	/**
	 * 调用计息更新状态接口
	 * @param rootInstCd
	 * @param userId
	 * @param shouldRepaymentDate
	 * @param totalAmount
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> updateStatusByRecord(String rootInstCd,String userId,Date shouldRepaymentDate,Long totalAmount) throws Exception{
		return collateForPosLogic.updateStatusByRecord(rootInstCd, userId, shouldRepaymentDate, totalAmount);
	}
	
	/***
	 * 融宝对账文件下载
	 * @return 提示信息
	 */
	@Override
	public Map<String, String> rbWGFileDown(String merchantCode, String fileType,
			String invoicedate,String payChannelId) throws Exception {
		return multiFileDown.multiFileDown(invoicedate, merchantCode, fileType, payChannelId);
	}
	
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表
	 */
	@Override
	public Map<String, Object> updateAccountInfoToDetailInfoPos() throws Exception {
		Map<String, Object> resultMap = null;
		try {
			resultMap = settlePosDetailService.updateAccountInfoToDetailInfo();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表", e);
		}
		return resultMap;
	}
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表
	 */
	@Override
	public Map<String, Object> updateAccountInfoToDetailInfoPos(Date account)
			throws Exception {
		Map<String, Object> resultMap = null;
		try {
			resultMap = settlePosDetailService.updateAccountInfoToDetailInfo(account);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表", e);
		}
		return resultMap;
	}
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表
	 */
	@Override
	public Map<String, Object> updateAccountInfoToDetailInfoPos(Date beginDate,
			Date endDate) throws Exception {
		Map<String, Object> resultMap = null;
		try {
			resultMap = settlePosDetailService.updateAccountInfoToDetailInfo(beginDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表", e);
		}
		return resultMap;
	}
	/***
	 * 下载平安银行对账文件pABB2B
	 */
	@Override
	public Map<String, String> pABB2BFileDown(String accountDate, String marchantCode)  throws Exception {
		Map<String, String> resultMap = null;
		try {
			resultMap = pABB2BDownload.pABB2BFileDown(accountDate, marchantCode);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 下载平安银行对账文件pABB2B", e);
		}
		return resultMap;
	}
	/***
	 * 下载 快付通支付对账文件
	 */
	@Override
	public Map<String, String> lycheeFileDown(String accountDate, String marchantCode)  throws Exception {
		Map<String, String> resultMap = null;
		try {
			resultMap = lycheeDownload.lycheeFileDown(accountDate, marchantCode);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常: 下载 快付通支付对账文件", e);
			throw e;
		}
		return resultMap;
	}
	
	@Override
	public Map<String, String> ybFileDown(String merchantCode, String fileType,
			String invoicedate, String payChannelId) throws Exception {
		return multiFileDown.multiFileDown(invoicedate, merchantCode, fileType, payChannelId);
	}
	
	@Override
	public void selectTestTransAndSumAmountYQZL(Date date) {
		settleTransAccountService.selectTestTransAndSumAmountYQZL(date);
	}
	
	@Override
	public Map<String, String> msAgentPayorRefundFileDown(String merchantCode, String fileType,
			String invoicedate, String payChannelId) throws Exception {
		return multiFileDown.multiFileDown(invoicedate, merchantCode, fileType, payChannelId);
	}
	
	@Override
	public void selectPosTransFeeAndUpdateOrder(Date date) {
		settlePosDetailService.selectPosTransFeeAndUpdateOrder(date);
	}
	@Override
	public Map<String, String> htglRepayFileDown(String accountDate,
			String marchantCode) throws Exception {
		return htglRepayDownload.fileDown(accountDate, marchantCode);
	}
}
