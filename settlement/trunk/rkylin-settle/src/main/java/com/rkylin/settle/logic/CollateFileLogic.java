package com.rkylin.settle.logic;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.util.SettlementUtil;

/***
 * 清分系统通用对账业务文件处理逻辑
 * @author Yang
 */
@Component("collateFileLogic")
public class CollateFileLogic {
  //日志对象
    protected static Logger logger = LoggerFactory.getLogger(CollateFileLogic.class);
	@Autowired
	private SettlementUtil settlementUtil;//清算工具类
	//清算属性表
	@Autowired
    private SettleParameterInfoManager settleParameterInfoManager;
	//读取对账文件并入库
	@Autowired
	private ReadFileLogic readFileLogic;
	//上传下游对账文件到ROP
    @Autowired
    private WriteFileLogic writeFileLogic;
	
    /***
	 * 作成下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createCollateFile(String marchantCode, String readType) throws Exception {
		//从DB中过去账期信息, 账期在DB中每日更新
		return this.createCollateFile(marchantCode, readType, null);
	}
	/***
	 * 作成下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createCollateFile(String marchantCode, String readType, Date accountDate) throws Exception {
	    Map<String,Object> rtnMap = new HashMap<String,Object>();
		List<SettleParameterInfo> rtnMarchantCodeList =  this.getDownMerchantCode(marchantCode);
		Map<String,Object> createMap = new HashMap<String,Object>();
		if (rtnMarchantCodeList == null) {
            logger.error("没有可操作的机构！");
            rtnMap.put("code", "-1");
            rtnMap.put("msg", "没有可操作的机构！");
            return rtnMap;
		}

		/***
		 * 业务约定:
		 * key=accountDate, value=当前账期
		 */
		Map<String, Object> specialColumnMap = new HashMap<String, Object>();
		SettleParameterInfo rtnMarchantCode = new SettleParameterInfo();
		String code = "";
		String msg = "";
		String[] readTypeStr = null;
		for (int i=0;i<rtnMarchantCodeList.size();i++) {
			rtnMarchantCode = rtnMarchantCodeList.get(i);
			
			if (readType == null) {
				readTypeStr = rtnMarchantCode.getObligate1().split(",");
				for (int j =0;j<readTypeStr.length;j++) {
					readType = readTypeStr[j];
					specialColumnMap.put("accountDate", true);
					specialColumnMap.put("totalAmount", true);
					specialColumnMap.put("totalFCAmount", true);
					specialColumnMap.put("totalCount", true);
					createMap = writeFileLogic.createCollateFile(rtnMarchantCode.getParameterCode(), readType, accountDate, specialColumnMap,rtnMarchantCode.getParameterValue());
					code = code + marchantCode + "|" + readType + ":" + createMap.get("code") + ";";
					msg = msg + createMap.get("msg") + ";";
				}
				readType = null;
			} else {
				specialColumnMap.put("accountDate", true);
				specialColumnMap.put("totalAmount", true);
				specialColumnMap.put("totalFCAmount", true);
				specialColumnMap.put("totalCount", true);
				createMap = writeFileLogic.createCollateFile(rtnMarchantCode.getParameterCode(), readType, accountDate, specialColumnMap,rtnMarchantCode.getParameterValue());
				code = code + marchantCode + "|" + readType + ":" + createMap.get("code") + ";";
				msg = msg + createMap.get("msg") + ";";
			}
		}
        rtnMap.put("code", code);
        rtnMap.put("msg", msg);
        return rtnMap;
	}
    
	/***
	 * 上传下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> uploadCollateFile(String marchantCode, String readType) throws Exception {
		//从DB中过去账期信息, 账期在DB中每日更新
		return this.uploadCollateFile(marchantCode, readType, null, null);
	}
	/***
	 * 上传下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> uploadCollateFile(String marchantCode, String readType, Date accountDate) throws Exception {
		//从DB中过去账期信息, 账期在DB中每日更新
		return this.uploadCollateFile(marchantCode, readType, accountDate, null);
	}
	/***
	 * 上传下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @param batch					批次号
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> uploadCollateFile(String marchantCode, String readType, Date accountDate, String batch) throws Exception {
		Map<String,Object> rtnMap = new HashMap<String,Object>();
			List<SettleParameterInfo> rtnMarchantCodeList =  this.getDownMerchantCode(marchantCode);
			Map<String,Object> createMap = new HashMap<String,Object>();
			if (rtnMarchantCodeList == null) {
	            logger.error("没有可操作的机构！");
	            rtnMap.put("code", "-1");
	            rtnMap.put("msg", "没有可操作的机构！");
	            return rtnMap;
			}

			/***
			 * 业务约定:
			 * key=accountDate, value=当前账期
			 */
			Map<String, Object> specialColumnMap = new HashMap<String, Object>();
			SettleParameterInfo rtnMarchantCode = new SettleParameterInfo();
			String code = "";
			String msg = "";
			String[] readTypeStr = null;
			for (int i=0;i<rtnMarchantCodeList.size();i++) {
				rtnMarchantCode = rtnMarchantCodeList.get(i);
				
				specialColumnMap.put("accountDate", true);
				specialColumnMap.put("totalAmount", true);
				specialColumnMap.put("totalFCAmount", true);
				specialColumnMap.put("totalCount", true);
				
				if (readType == null) {
					readTypeStr = rtnMarchantCode.getObligate1().split(",");
					for (int j =0;j<readTypeStr.length;j++) {
						readType = readTypeStr[j];
						createMap = writeFileLogic.uploadCollateFile(rtnMarchantCode.getParameterCode(), readType, accountDate, specialColumnMap, batch);
						code = code + marchantCode + "|" + readType + ":" + createMap.get("code") + ";";
						msg = msg + createMap.get("msg") + ";";
						Thread.sleep(2000);
					}
					readType = null;
				} else {
					createMap = writeFileLogic.uploadCollateFile(rtnMarchantCode.getParameterCode(), readType, accountDate, specialColumnMap, batch);
					code = code + marchantCode + "|" + readType + ":" + createMap.get("code") + ";";
					msg = msg + createMap.get("msg") + ";";
					Thread.sleep(2000);
				}
			}
	        rtnMap.put("code", code);
	        rtnMap.put("msg", msg);
	        return rtnMap;
	}
	/**
	 * @Description: 读取通联对账文件，并录入数据库
	 * @param marchantCode 机构号
	 * @param readType 交易类型-网关支付:01, 代收付:02
	 * @param accountDate  账期
	 * @param fileType 文件类型-WG:网关支付; ZF：代收付; YD:通联支付; LLKJ：连连移动快捷支付
	 * @param payChannelId 渠道号 01通联、02支付宝
	 * @return
	 * @throws Exception
	 * @autor CLF
	 */
	public Map<String, Object> readCollateFile(String marchantCode, String readType, String accountDate,String fileType,String payChannelId) throws Exception {
	    logger.info("==start======== 读取 对账文件，并录入数据库=============");
	    /*
	     * 业务约定:
	     * key=accountDate, value=当前账期
	     * key=MERCHANT_CODE、PAY_CHANNEL_ID、READ_TYPE, value=入参
	     * key=IS_INVOICE, value=0
	     */
	    Map<String, Object> specialColumnMap = new HashMap<String, Object>();
	    
	    Map<String,Object> rtnMap = new HashMap<String,Object>();
	    if (accountDate == null || "".equals(accountDate)) {
            try {
                //DB中账期为当前日期T，计算出账期应为前一日T-1
                accountDate = (String)settlementUtil.getAccountDate("yyyy-MM-dd",-1,"");
            } catch (Exception e2) {
                logger.error("计算账期异常！" + e2.getMessage());
                rtnMap.put("errCode", "0001");
                rtnMap.put("errMsg", "计算账期异常");
                return rtnMap;
            }
        }
	    //对账文件中有数据，但是需要程序处理
	    specialColumnMap.put("SETTLE_TIME", accountDate);
	    //对账文件中有数据，但是需要程序处理
	    specialColumnMap.put("MERCHANT_CODE", marchantCode);
	    specialColumnMap.put("PAY_CHANNEL_ID", payChannelId);
	    specialColumnMap.put("READ_TYPE", readType);
	    specialColumnMap.put("IS_INVOICE", "0");
	    //通联网管交易状态
	    if("01".equals(payChannelId) && SettleConstants.ACCOUNT_TYPE_CHANNEL.equals(readType)) specialColumnMap.put("READ_STATUS_ID", 16);
	    //通联移动交易状态
	    if("01".equals(payChannelId) && SettleConstants.ACCOUNT_TYPE_MOBILE.equals(readType)) specialColumnMap.put("READ_STATUS_ID", 16);
	    //通联移动交易状态
	    if("01".equals(payChannelId) && SettleConstants.ACCOUNT_TYPE_TLSDK.equals(readType)) specialColumnMap.put("READ_STATUS_ID", 16);
	    
	    //融宝网关交易状态
	    if("S02".equals(payChannelId) && SettleConstants.ACCOUNT_TYPE_CHANNEL.equals(readType)) {
	    	specialColumnMap.put("READ_STATUS_ID", 16);
	    	specialColumnMap.put("TRANS_TYPE", "4015");
	    }
	    
	    /*
	     * 读取通联代收付对账文件时，将文件中的交易状态转换
	     * 成功 0000|4000--16
	     * 处理中 2000|2001|2003|2005|2007|2008|0003|0014--12
	     * 其他--14
	     */
        specialColumnMap.put("OBLIGATE1", this.convertTransType(payChannelId));
        
        //易宝网关交易状态
	    if("S03".equals(payChannelId)) {
	    	if(readType.equals(SettleConstants.ACCOUNT_TYPE_YB_DF)){
	    		specialColumnMap.put("OBLIGATE1", "易宝代付");
	    	}else{
	    		specialColumnMap.put("OBLIGATE1", "易宝");
		    	specialColumnMap.put("READ_STATUS_ID", 16);
		    	specialColumnMap.put("FILE_TYPE", 0);
		    	if(readType.equals(SettleConstants.ACCOUNT_TYPE_YB_AUTH)){
		    		specialColumnMap.put("TRANS_TYPE", "311001");
		    		specialColumnMap.put("TRANS_AMOUNT", new BigDecimal("0"));
		    	}else if(readType.equals(SettleConstants.ACCOUNT_TYPE_YB_PAYMENT)){
		    		specialColumnMap.put("TRANS_TYPE", "4015");
		    	}else if(readType.equals(SettleConstants.ACCOUNT_TYPE_YB_WITHDRAW)){
		    		specialColumnMap.put("TRANS_TYPE", "4016");
		    	}else if(readType.equals(SettleConstants.ACCOUNT_TYPE_YB_CHANGECARD)){
		    		specialColumnMap.put("TRANS_TYPE", "311008");
		    		specialColumnMap.put("TRANS_AMOUNT", new BigDecimal("0"));
		    	}else if(readType.equals(SettleConstants.ACCOUNT_TYPE_YB_REFUND)){
		    		specialColumnMap.put("TRANS_TYPE", "4017");
		    	}else if(readType.equals(SettleConstants.ACCOUNT_TYPE_YB_PAYDIVIDE)){
		    		specialColumnMap.put("TRANS_TYPE", "311005");
		    	}else{
		    		logger.info("易宝没有匹配的readType,readType="+readType);
		    	}
	    	}
	    	
	    }
	    if("Y02".equals(payChannelId)) {
	    	if(readType.equals(SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_DL)||readType.equals(SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_FDL)){
	    		specialColumnMap.put("READ_TYPE", "AgentPay");
	    	}else if(readType.equals(SettleConstants.ACCOUNT_TYPE_CMBC_Refund_DL)||readType.equals(SettleConstants.ACCOUNT_TYPE_CMBC_Refund_FDL)){
	    		specialColumnMap.put("READ_TYPE", "Refund");
	    	}else{
	    		logger.info("不要对readType进行转换");
	    	}
	    }
	    
	    
	    return readFileLogic.readCollateFile(marchantCode, readType, accountDate, payChannelId, specialColumnMap);
	}
	/**
	 * @Description: 通联代收付对账文件，交易状态转换
	 * @return key=parameter_value ;value=parameter_code
	 * @autor CLF
	 */
	public Map<String,String> convertTransType(String payChannelId){
	    logger.info(">>> 读取parameterInfo中交易状态，parameterType:1000000006");
	    SettleParameterInfoQuery query = new SettleParameterInfoQuery ();
	    //交易状态常量 - 读取通联代收付对账文件的功能编码
        query.setParameterType(SettleConstants.PARAMETER_TYPE_TRANS_STATUS);
        query.setProductId(payChannelId);
        List<SettleParameterInfo> paramInfoList = settleParameterInfoManager.queryList(query);
        Map<String,String> transTypeMap = new HashMap<String,String>();
        for(SettleParameterInfo info:paramInfoList){
            transTypeMap.put(info.getParameterValue(), info.getParameterCode());
        }
	    return transTypeMap;
	}
	
	/***
	 * 获取全部机构号[merchantCode]和机构号在下游中包含的其他子机构号
	 * @param merchantCode
	 * @return
	 * @throws ParseException
	 */
	private List<SettleParameterInfo> getDownMerchantCode(String merchantCode) throws ParseException{
    	SettleParameterInfoQuery keyList1 =  new SettleParameterInfoQuery();
    	keyList1.setParameterType(SettleConstants.PARAMETER_DOWN_MERCHANT);
    	if (merchantCode != null && !"".equals(merchantCode)) {
    		keyList1.setParameterCode(merchantCode);
    	}
    	List<SettleParameterInfo> settleParameterInfo = settleParameterInfoManager.queryList(keyList1);
    	if(settleParameterInfo.size() > 0){
    		return settleParameterInfo;
    	}else{
    		logger.info("parameter_info中 未配置全部机构号或机构号对应关系对应关系");
    		return null;
    	}
	}
	
}
