package com.rkylin.settle.settleInterface;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettlePosDetail;

public interface SettleWebInterface {
	/**
	 * 存储过程 读取账户系统 二期 交易并录入清结算库中
	 * @throws Exception
	 */
	public void readAccDate() throws Exception;
	/**
	 * 存储过程 读取账户系统 一期 交易并录入清结算库中
	 * @throws Exception
	 */
	public void readAccOldDate() throws Exception;
	/**
	 * @Description: 读取账户系统二期交易并录入清结算库中
	 * @param beginDate    开始时间yyyy-MM-dd HH:mm:ss
	 * @param endDate  结束时间yyyy-MM-dd HH:mm:ss
	 * @author CLF
	 */
	public void getAccountTransOrderInfos(Date beginDate, Date endDate) throws Exception;
	/**
	 * @Description: 读取贷款系统交易并录入清结算库中
	 * @param accDate
	 *            记账日期yyyy-MM-dd HH:mm:ss
	 * @author SRB
	 */
	public void getLoanTransOrderInfos(Date accDate) throws Exception;
	/**
	 * 从多渠道读取交易信息并存入'清算'DB
	 * @param accountDate
	 * @return 提示信息
	 */
	public Map<String, Object> getTransDetailFromMultiGate(Date accountDate) throws Exception;
	/**
	 * @Description: 读取通联对账文件，并录入数据库
	 * @param marchantCode 机构号
	 * @param readType 交易类型-网关支付:01, 代收付:02
	 * @param accountDate  账期
	 * @param fileType 文件类型-WG:网关支付 ZF：代收付
	 * @param payChannelId 渠道号 01通联、02支付宝
	 * @return
	 * @throws Exception
	 * @autor CLF
	 */
	public Map<String, Object> readCollateFile(String marchantCode, String readType, String accountDate,String fileType,String payChannelId) throws Exception;
	/**
	 * 上传下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> uploadCollateFile(String marchantCode, String readType, Date accountDate) throws Exception;
	/**
	 * 上传下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @param batch					批次号
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> uploadCollateFile(String marchantCode, String readType, Date accountDate, String batch) throws Exception;
	/**
	 * 生成下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createCollateFile(String marchantCode, String readType, Date accountDate) throws Exception;
	/**
	 * 对账
	 */
	public void collage(String payChannelId, String accountType, String merchantCode, String bussType) throws Exception;
	/**
	 * 对账
	 */
	public void collage(String payChannelId,String accountType,String merchantCode, Date accountDate, String bussType) throws Exception;
	/**
	 * 开始清分 ... ...
	 */
	public Map<String, Object> doProfit() ;
	/**
     * 指定账期下载通联对账文件
     * @param rootInstId 机构号
     * @param fileType 文件类型：WG：网关支付，ZF：支付系统
     * @param accountDate 上游账期
     * @return
     */
	public Map<String, String> tlFileDown(String rootInstId, String fileType, String accountDate) throws Exception;
	/**
	   * 指定账期下载连连快捷支付对账文件
	   * @return
	   */
	public Map<String, String> lianLianFileDown(String accountDate, String merchantCode, String readType) throws Exception;
	/**
	 * 指定账期下载 联动优势 WG 对账文件
	 * 
	 * @return
	 */
	public Map<String, String> lDWGFileDown(String accountDate,
			String merchantCode, String readType) throws Exception;
	/**
	 * 指定账期下载 畅捷支付 ZF 对账文件
	 * 
	 * @return
	 */
	public Map<String, String> cJZFFileDown(String accountDate,
			String merchantCode, String readType) throws Exception;
	/**
	 * 结算分润结果 [调用账户系统接口]
	 * @return 提示信息
	 */
	public Map<String, Object> doProfigBalance() throws Exception;
	/**
	 * 结算分润结果 [调用账户系统接口]
	 * @return 提示信息
	 */
	public Map<String, Object> doProfigBalance(String[] ids) throws Exception;
	
	/**
	 * 汇总未处理的代付、提现、代收和一分钱代付
	 * @param ids 交易表的主键ID构成的数组
	 * @param flag 标记值, 0:代付和提现,1:代收和一分钱代付,2:课栈T0提现,其他值：代收、代付、提现、一分钱代付、课栈T0提现
	 * @param rootInstCds 机构号
	 */
	public Map<String, Object> manualSendToDsf(Integer[] ids,String flag, String[] rootInstCds,String[] funcCodes) throws Exception;
	
	/**
	 * 代收付结果推送给订单
	 * @param ids 交易表主键ID构成的数组
	 */
	public Map<String, Object> doProfigBalanceForLoan() throws Exception;
	/***
	 * 结算贷款分润结果 [调用账户系统接口]
	 * 
	 * @return 提示信息
	 */
	public Map<String, Object> doProfigBalanceForLoan(String[] ids) throws Exception;
	/***
	 * 刷新 多渠道'ChannelHome' 和 清结算'PayChannelId' 对应关系
	 * 画面使用
	 */
	public void refreshMultiGateChannelHome2PayChannelId();
	
	/**
	 * 退票的或一分钱代付推送账户失败的手工重发
	 * @param ids 交易表主键ID
	 * @param deliverStatusIds 1：退票推送账户失败
	 */
	public Map<String, Object> manualSendAccountTP(Integer[] ids,Integer[] deliverStatusIds) throws Exception;
	/**
	 * 汇总
	 * @param collectType	汇总类型
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doSettleInvoice() throws Exception;
	/***
	 * 根据分润结算表调用账户生成代付/提现交易
	 * 
	 * @return 提示信息
	 */
	public Map<String, Object> doInvoiceSettle() throws Exception;
	/***
	 * 根据分润结算表调用账户生成头寸代付/提现交易
	 * 
	 * @return 提示信息
	 */
	public Map<String, Object> doInvoiceSettleForCash() throws Exception;
	/***
	 * 40142交易汇总
	 * @return 提示信息
	 */
	public Map<String, Object> hth_pay() throws Exception;
	/***
	 * 平安银行下载
	 * @return 提示信息
	 */
	public Map<String, String> pABFileDown(String accountDate,
			String merchantCode, String readType) throws Exception;
	/***
	 * 通联微信下载
	 * @return 提示信息
	 */
	public Map<String, String> tlwxFileDown(String accountDate,
			String merchantCode, String readType) throws Exception;
	/***
	 * 订单推送交易
	 * @return 提示信息
	 */
	public Map<String, Object> insertPosData(List<SettlePosDetail> settlePosDetail) throws Exception;
	/***
	 * 从代收付系统读取结果
	 * @return 提示信息
	 */
	public String getResultFromDsf(String rootInstCd,String businessCode,String requestNo,String orderNos,String accountDate) throws Exception;
	/**
	 * @Description: 读取账户系统一期交易并录入清结算库中
	 * @param beginDate    开始时间yyyy-MM-dd HH:mm:ss
	 * @param endDate  结束时间yyyy-MM-dd HH:mm:ss
	 * @author CLF
	 */
	void getAccountOldTransOrderInfos(Date beginDate, Date endDate) throws Exception;
	/**
	 * 代收付结果推送给订单
	 * @param ids 交易表主键ID构成的数组
	 */
	Map<String, Object> manualSendOrder(Integer[] ids) throws Exception;
	/**
	 * 从结算表开始->汇总表->交易表->发送订单系统
	 * @param ids 结算表的主键id
	 */
	Map<String, Object> sendOrderFromInvoice(Integer[] ids) throws Exception;
	/**
	 * 根据选中的交易表的ID汇总代收付数据到汇总表和结算表
	 * @param ids 交易表的主键ID
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> summaryByIds(Integer[] ids) throws Exception;
	/**
	 * 刷新'func_code'与deal_product_code的对应关系
	 * 画面使用
	 */
	void refreshDealProductCodeTofuncCode();
	/**
	 * 汇总
	 * @param collectType	汇总类型
	 * @param accountDate	账期
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> doCollect(Integer collectType) throws Exception;
	/**
	 * 汇总
	 * @param collectType	汇总类型
	 * @param accountDate	账期
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> doCollect(Integer collectType, Date accountDate) throws Exception;
	/**
	 * 汇总信息发送MQ
	 * @param ske
	 * @throws Exception
	 */
	Map<String, Object> doCollectBySettleKernelEntry(Long id) throws Exception;
	/**
	 * 将结算中的代收付结果回写到汇总表和交易表
	 * @param ids 结算表的主键ID
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> manualWriteResult(Integer[] ids) throws Exception;
	/**
	 * 手工触账户跟多渠道对账
	 * @param inRootInstCd 机构号
	 * @param accounteDate 账期
	 * @param readType 对账类型
	 * @throws Exception
	 */
	void collecteAccAndMulti(String inRootInstCd, Date accounteDate,String readType) throws Exception;
	/**
	 * 从交易画面发起汇总
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	void doCollectByPage(Integer collectType, Integer[] idsArr) throws Exception;
	/**
	 * 从汇总画面发起汇总
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	void doCollectByPage(Integer collectType, Date accountDate, String merchantCode, String payChannelId, String funcCode) throws Exception;
	/***
	 * 贷款系统清分 ... ...
	 */
	public Map<String, Object> doProfitForLoan();
	/**
	 * 从订单系统读取交易信息并存入'清算'DB
	 * 
	 * @param accountDate
	 * @return 提示信息
	 */
	public Map<String, Object> getPosDetailFromOrder(Date accountDate)
			throws Exception;
	/***
	 * ROP对账文件下载
	 * @return 提示信息
	 */
	public Map<String, String> rOPFileDown(String type,
			String batch, Date invoicedate,String priOrPubKey,String fileType) throws Exception;
	/***
	 * 对账
	 */
	public void collageForPos(String payChannelId, String accountType,
			String merchantCode, String bussType) throws Exception;
	/***
	 * 对账
	 */
	public void collageForPos(String payChannelId, String accountType,
			String merchantCode, Date accountDate, String bussType)
			throws Exception;

	/***
	 * pos交易结算
	 */
	public Map<String,Object> settleForPos( Date accountDate) throws Exception;
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfo() throws Exception;
	
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表
	 * @param accountDate 账期日期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfo(Date account) throws Exception;
	
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表
	 * @param beginDate 账期开始日期
	 * @param endDate 	账期结束日期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfo(Date beginDate, Date endDate) throws Exception;
	
	/***
	 * 将accountList中的readStatusId写入detailList
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doWriteToSettleTransDetail() throws Exception;
	/***
	 * 将accountList中的readStatusId写入detailList
	 * @param accountDate
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doWriteToSettleTransDetail(Date accountDate) throws Exception;
	/***
	 * 将accountList中的readStatusId写入detailList
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doWriteToSettleTransDetail(Date beginDate, Date endDate) throws Exception;
	/**
	 * 调用计息更新状态接口
	 * @param rootInstCd
	 * @param userId
	 * @param shouldRepaymentDate
	 * @param totalAmount
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateStatusByRecord(String rootInstCd,String userId,Date shouldRepaymentDate,Long totalAmount) throws Exception;
	
	/**
	 * 下载融宝网关对账文件
	 * @param invoicedate
	 * @param merchantCode
	 * @param fileType
	 * @param payChannelId
	 * @return
	 */
	public Map<String, String> rbWGFileDown(String merchantCode, String fileType,String invoicedate,String payChannelId) throws Exception;

	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfoPos() throws Exception;
	
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表
	 * @param accountDate 账期日期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfoPos(Date account) throws Exception;
	
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表
	 * @param beginDate 账期开始日期
	 * @param endDate 	账期结束日期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfoPos(Date beginDate, Date endDate) throws Exception;
	
	/***
	 * 下载平安银行对账文件pABB2B
	 * @param accountDate
	 * @param marchantCode
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> pABB2BFileDown(String accountDate, String marchantCode)  throws Exception;
	
	//实现类 比接口 多着3个方法 可能是 代码版本问题
	/**
	 * 发送代收付交易给代收付系统
	 */
	public void sendDsf(String dataSource,String[] inRootInstCds,Integer[] orderTypes,Integer[] ids);
	/***
	 * 刷新'金额'与存放位置的对应关系
	 * 画面使用
	 */
	public void refreshFuncCodeAndAmountRelationTask();
	/***
	 * 刷新'订单号'与存放位置的对应关系
	 * 画面使用
	 */
	public void refreshFuncCodeAndOrderNoRelationTask();
	/***
	 * 刷新分润规则
	 * 画面使用
	 */
	public void refreshProfitKeyTask();
	/***
	 * 下载 快付通支付对账文件
	 */
	public Map<String, String> lycheeFileDown(String accountDate, String marchantCode)  throws Exception;

	/**
	 * 下载易宝对账文件
	 * @param invoicedate
	 * @param merchantCode
	 * @param fileType
	 * @param payChannelId
	 * @return
	 */
	public Map<String, String> ybFileDown(String merchantCode, String fileType,String invoicedate,String payChannelId) throws Exception;
	/**
	 * 查询银企直联测试交易并汇总金额
	 * @param date
	 */
	void selectTestTransAndSumAmountYQZL(Date date);
	/**
	 * 查询还款交易手续费并更新订单系统
	 * @param date
	 */
	void selectPosTransFeeAndUpdateOrder(Date date);
	
	/**
	 * 下载民生单笔代付或退票对账文件
	 * @param invoicedate
	 * @param merchantCode
	 * @param fileType
	 * @param payChannelId
	 * @return
	 */
	public Map<String, String> msAgentPayorRefundFileDown(String merchantCode, String fileType,String invoicedate,String payChannelId) throws Exception;
	/***
	 * 下载 会唐国旅 还款文件接口
	 */
	public Map<String, String> htglRepayFileDown(String accountDate, String marchantCode)  throws Exception;
	/***
	 * 生成头寸信息
	 * @return
	 * @throws Exception
	 */
	public String createPositionInfo() throws Exception;
	/***
	 * 生成头寸信息
	 * @param pstRootInstCd
	 * @param pstFuncCode
	 * @param pstUserId
	 * @param pstProductId
	 * @return
	 * @throws Exception
	 */
	public String createPositionInfo(String pstRootInstCd, String pstFuncCode, String pstUserId, String pstProductId) throws Exception;
}