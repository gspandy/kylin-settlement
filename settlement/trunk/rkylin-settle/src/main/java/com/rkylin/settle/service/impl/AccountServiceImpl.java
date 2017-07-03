package com.rkylin.settle.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.logic.CollateForPosLogic;
import com.rkylin.settle.logic.CollateLogic;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.service.AccountService;
import com.rkylin.settle.util.SettlementUtil;
import com.rop.response.Response;
import com.rop.service.IAPIService;

@Service("accountService")
public class AccountServiceImpl implements AccountService,
IAPIService {
	private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	@Autowired
	private CollateLogic collageLogic;
	@Autowired
	private CollateForPosLogic collageForPosLogic;
	@Autowired
	private SettlementUtil settlementUtil;
	@Autowired
	private ParameterInfoManager parameterInfoManager;
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;
	@Autowired
	private Properties userProperties;
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		// TODO Auto-generated method stub
		return null;
	}
	/***
	 * 对账
	 */
	@Override
	public void collage(String payChannelId, String[] accountTypeArr, String merchantCode, String bussType) throws Exception {
		this.collage(payChannelId, accountTypeArr, merchantCode, null, bussType);
	}
	/***
	 * 对账
	 */
	@Override
	public void collage(String payChannelId, String accountType, String merchantCode, String bussType) throws Exception {
		this.collage(payChannelId, accountType, merchantCode, null, bussType);
	}
	@Override
	//参数 accountTpye : 01(网关与上游渠道对账) 02（代收付与上游渠道对账）03（与账户对账）
	public void collage(String payChannelId,String accountType,String merchantCode, Date accountDate, String bussType) throws Exception {
		this.collage(payChannelId, new String[]{accountType}, merchantCode, accountDate, bussType);
	}
	@Override
	@Transactional(rollbackFor = SettleException.class, propagation = Propagation.NESTED)
	//参数 accountTpye : 01(网关与上游渠道对账) 02（代收付与上游渠道对账）03（与账户对账）
	public void collage(String payChannelId, String[] accountTypeArr, String merchantCode, Date accountDate, String bussType) throws Exception {
		//*********账期的取得*********//
		if(accountDate == null) {
			accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", -1, "Date");
		}	
		//与上游对账时，交易数据的时间的取得
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String accountDateBefore = sdf.format(accountDate);
    	Date accountDateBeforeD = getSettlleTime(accountDateBefore, payChannelId);
    	String merchantCodeChange = getMerchantCode(merchantCode, payChannelId);
    	//*********对账开始*********//
    	try {
    		List<Map<String, Object>> tAccountList = null;
    		if(accountTypeArr.length == 1) {
    			tAccountList = collageLogic.querySettleTransAccount(accountDate, payChannelId, merchantCode, accountTypeArr[0]);
    		} else {
    			tAccountList = collageLogic.querySettleTransAccount(accountDate, payChannelId, merchantCode, accountTypeArr);
    		}
    		List<Map<String, Object>> tDetailList = collageLogic.querySettleTransDetail(accountDateBeforeD, payChannelId, accountTypeArr[0], merchantCodeChange,bussType);
    		collageLogic.doCollate(tAccountList, tDetailList, payChannelId, accountTypeArr[0]);
    	} catch (Exception e2) {
			logger.error("对账异常！" + e2.getMessage());
			e2.printStackTrace();
			throw new SettleException("对账异常");
		}
	}
	/***
	 * 与上游对账时，交易数据的时间的取得
	 * @param accountDate
	 * @param payChannelId
	 * @return
	 * @throws ParseException
	 */
	private Date getSettlleTime(String accountDate,String payChannelId) throws ParseException{
    	SettleParameterInfoQuery keyList1 =  new SettleParameterInfoQuery();
    	keyList1.setParameterType(SettleConstants.PARAMETER_TYPE_SETTLETIME);//取上游对账时间
    	keyList1.setParameterCode(payChannelId);
    	List<SettleParameterInfo> settleParameterInfo = settleParameterInfoManager.queryList(keyList1);
    	String settleTime = "";
    	if(settleParameterInfo.size()>0){
    		settleTime = settleParameterInfo.get(0).getParameterValue();
    	}else{
    		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    		settleTime = dateFormat.format(new Date());
    		logger.error("取交易数据的时间异常！默认当前时间时分秒:" + settleTime);
//    		throw new SettleException("取交易数据的时间异常！"); 
    	}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	 
		return sdf.parse(accountDate + " " +settleTime);
	}
	/***
	 * 与上游对账时，交易数据的机构号的取得
	 * @param merchantCode
	 * @param payChannelId
	 * @return
	 * @throws ParseException
	 */
	private String getMerchantCode(String merchantCode, String payChannelId) throws ParseException{
		/*
		 * 机构号转换
		 * 融数为签约机构提供金融服务, 需要借助与第三方支付渠道(通联;连连支付)签约, 解决资金流转问题.
		 * 当融数使用'渠道'为'机构'处理交易时, 多家'机构'有可能使用同一个'渠道'账户.例如:丰年(M000001)和课栈(M000004)都使用丰年(M000001)的机构号
		 */
		//机构号
    	String merchantCodeStr;
    	//字典表查询query对象
    	SettleParameterInfoQuery keyList1 =  new SettleParameterInfoQuery();
    	//查询机构号对应关系
    	keyList1.setParameterType(SettleConstants.PARAMETER_TYPE_MERCHANT);
    	keyList1.setParameterCode(merchantCode);
    	keyList1.setObligate1(payChannelId);
    	List<SettleParameterInfo> settleParameterInfo = settleParameterInfoManager.queryList(keyList1);
    	if(settleParameterInfo.size()>0) {
    		merchantCodeStr = settleParameterInfo.get(0).getParameterValue();
    		//如果得到的结果是'all'返回全部机构号
    		merchantCodeStr = merchantCodeStr.equals("all") ? this.getAllMerchantCodeToSQL() : merchantCodeStr;
    		return merchantCodeStr;
    	} else {
    		logger.error("取机构号对应关系异常！");
    		throw new SettleException("取机构号对应关系异常！"); 
    	}
	}
	/***
	 * 查询全部机构号, 并安装SQL注入的格式输出
	 * @return
	 */
	private String getAllMerchantCodeToSQL() {
//		StringBuffer strBuff = new StringBuffer();
//		strBuff.append("\'"+ Constants.RS_ID +"\'");
//		strBuff.append(",\'"+ Constants.FN_ID +"\'");
//		strBuff.append(",\'"+ Constants.P2P_ID +"\'");
//		strBuff.append(",\'"+ Constants.HT_ID +"\'");
//		strBuff.append(",\'"+ Constants.KZ_ID +"\'");
//		strBuff.append(",\'"+ Constants.HT_CLOUD_ID +"\'");
//		strBuff.append(",\'"+ Constants.JRD_ID +"\'");
//		strBuff.append(",\'"+ Constants.SQSM_ID +"\'");
//		strBuff.append(",\'"+ Constants.MZ_ID +"\'");
//		return strBuff.toString();
		/**
		 * return null 的逻辑详见 SqlMapper
		 */
		return null;
	}
	
	/***
	 * 对账(账户跟多渠道)
	 */
	@Override
	public void collageAccAndMulti(String[] funcCodes,String payChannelId,String readType,String inRootInstCd,Date tempAccounteDate) throws Exception {
		Date accountDate = null;
		Date accountDateBefore2 = null;
		Date accountDateBefore3 = null;
		if(tempAccounteDate !=null){
			accountDate = tempAccounteDate;
			SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
			Calendar date = Calendar.getInstance();
			date.setTime(accountDate);
			date.set(Calendar.DATE, date.get(Calendar.DATE) - 3);
			accountDateBefore3 = dft.parse(dft.format(date.getTime()));
			
			date.set(Calendar.DATE, date.get(Calendar.DATE) + 1);
			accountDateBefore2 = dft.parse(dft.format(date.getTime()));
		}else{
			accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", -1, "Date");//T-1日账期
			accountDateBefore2 = (Date) settlementUtil.getAccountDate("yyyyMMdd", -2, "Date");//T-2日账期
			accountDateBefore3 = (Date) settlementUtil.getAccountDate("yyyyMMdd", -3, "Date");//T-3日账期
		}
    	//*********对账开始*********//
    	try {
    		List<Map<String, Object>> tAccountList = collageLogic.queryDetailAccount(accountDateBefore3, funcCodes,inRootInstCd);//账户数据
    		List<Map<String, Object>> tMultiList = collageLogic.queryDetailMulti(accountDateBefore2, funcCodes,inRootInstCd);//多渠道数据
    		collageLogic.doCollateAccountAndMuti(tAccountList, tMultiList,payChannelId,readType,accountDateBefore3);
    	} catch (Exception e2) {
			logger.error("对账异常！" + e2.getMessage());
			e2.printStackTrace();
			throw new SettleException("对账异常");
		}
	}
	/***
	 * 对账
	 */
	@Override
	public void collageForPos(String payChannelId, String accountType, String merchantCode, String bussType) throws Exception {
		this.collageForPos(payChannelId, accountType, merchantCode, null, bussType);
	}
	/***
	 * 对账
	 */
	@Override
	public void collageForPos(String payChannelId, String accountType, String merchantCode, Date accountDate, String bussType) throws Exception {
		this.collageForPos(payChannelId, new String[]{accountType}, merchantCode, accountDate, bussType);
	}
	/***
	 * 对账
	 */
	@Override
	public void collageForPos(String payChannelId, String[] accountTypeArr, String merchantCode, String bussType) throws Exception {
		this.collageForPos(payChannelId, accountTypeArr, merchantCode, null, bussType);
	}
	@Override
	@Transactional(rollbackFor = SettleException.class, propagation = Propagation.NESTED)
	//参数 accountTpye : 01(网关与上游渠道对账) 02（代收付与上游渠道对账）03（与账户对账）
	public void collageForPos(String payChannelId,String[] accountTypeArr,String merchantCode, Date accountDate, String bussType) throws Exception {
		//*********账期的取得*********//
		if(accountDate == null) {
			accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", -1, "Date");
		}	
		//与上游对账时，交易数据的时间的取得
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String accountDateBefore = sdf.format(accountDate);
    	Date accountDateBeforeD = getSettlleTime(accountDateBefore, payChannelId);
    	String merchantCodeChange = getMerchantCode(merchantCode, payChannelId);
    	List<Map<String, Object>> tDetailList = null;
    	//*********对账开始*********//
    	try {
    		//通联移动支付逻辑判断
    		List<Map<String, Object>> tAccountList = null;
    		if(accountTypeArr.length == 1) {
    			tAccountList = collageLogic.querySettleTransAccount(accountDate, payChannelId, merchantCode, accountTypeArr[0]);
    		} else {
    			tAccountList = collageLogic.querySettleTransAccount(accountDate, payChannelId, merchantCode, accountTypeArr);
    		}
    		tDetailList = collageForPosLogic.querySettlePosDetail(accountDateBeforeD, payChannelId, accountTypeArr[0], merchantCodeChange, bussType);
    		collageForPosLogic.doCollate(tAccountList, tDetailList, payChannelId, accountTypeArr[0]);
    	} catch (Exception e2) {
			logger.error("对账异常！" + e2.getMessage());
			e2.printStackTrace();
			throw new SettleException("对账异常");
		}
	}
}
