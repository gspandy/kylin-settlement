package com.rkylin.settle.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Rop.api.DefaultRopClient;
import com.Rop.api.request.WheatfieldUserWithholdRequest;
import com.Rop.api.response.WheatfieldUserWithholdResponse;
import com.rkylin.settle.common.SessionUtils;
import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.TransOrderInfoManager;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoAccount;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.utils.SendSMS;

@Component("balanceLogic")
public class BalanceLogic extends BasicLogic {

	@Autowired
	TransOrderInfoManager transOrderInfoManager;
	@Autowired
	SettleTransDetailManager settleTransDetailManager;
	@Autowired
	SettlementUtil settlementUtil;
	@Autowired
	Properties userProperties;
	
	//String mobile = "13581558215,18500153480,18701022977,18601062528,15001304318"
	String mobile = "13581558215";//暂时用自己手机号测试
	//短信模板: xx系统xx环境，编号××存在问题，请及时解决!
	String content = "清算系统平安环境，编号"+Constants.SMS_NUMBER_2+"存在问题，请及时解决！";//编号0指备付金余额不足
//	String appKey_AC = "";
//	String appSecret_AC ="";
//	String url_AC="";
	
	public Map<String,Object> hth_pay(String accountDateS,String payType) throws Exception {
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("errMsg", "ok");
		rtnMap.put("errCode", "0000");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format_O = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date accountDate = null;
		
		if (accountDateS == null || "".equals(accountDateS)) {
	        try {
	            //DB中账期为当前日期T，计算出账期应为前一日T-1
	            accountDate = (Date)settlementUtil.getAccountDate("yyyy-MM-dd",-1,"Date");
	        } catch (Exception e2) {
	            logger.error("计算账期异常！" + e2.getMessage());
	            rtnMap.put("errCode", "0001");
	            rtnMap.put("errMsg", "计算账期异常");
	    		SendSMS.sendSMS(mobile, content);
	            return rtnMap;
	        }
		} else {
			try {
				accountDate = format.parse(accountDateS);
			} catch (Exception e2) {
	            logger.error("转换账期异常！" + e2.getMessage());
	            rtnMap.put("errCode", "0001");
	            rtnMap.put("errMsg", "转换账期异常");
	    		SendSMS.sendSMS(mobile, content);
	            return rtnMap;
			}
		}
        logger.info("操作账期为："+accountDate);
		
//		List<TransOrderInfo> resultList = new ArrayList<TransOrderInfo>();
//		TransOrderInfoQuery whereQuery = new TransOrderInfoQuery();
//		whereQuery.setFuncCode("40142");
//		whereQuery.setAccountDate(accountDate);
//		
//		resultList = transOrderInfoManager.queryList(whereQuery);
//		
//		if (resultList.size() == 0) {
//            logger.error("取得40142交易为零");
//            rtnMap.put("errCode", "0001");
//            rtnMap.put("errMsg", "取得40142交易为零");
//            return rtnMap;
//		}
//
//        logger.info("取得40142交易笔数："+resultList.size());
//        BigDecimal totalAmount = new BigDecimal("0");
//        BigDecimal amount = new BigDecimal("0");
//        TransOrderInfo transOrderInfo = new TransOrderInfo();
//        for (int i =0;i<resultList.size();i++) {
//        	transOrderInfo = resultList.get(i);
//        	amount = new BigDecimal(transOrderInfo.getAmount().toString());
//        	
//        	totalAmount = totalAmount.add(amount);
//        }


		List<SettleTransDetail> resultList = new ArrayList<SettleTransDetail>();
		SettleTransDetailQuery whereQuery = new SettleTransDetailQuery();
		whereQuery.setFuncCode("40142");
		whereQuery.setAccountDate(accountDate);
		whereQuery.setReadStatusId(1);
		whereQuery.setOrderType(0);
		
		resultList = settleTransDetailManager.queryList(whereQuery);
        
		if (resultList.size() == 0) {
	      logger.error("取得40142交易为零");
	      rtnMap.put("errCode", "0001");
	      rtnMap.put("errMsg", "取得40142交易为零");
	      return rtnMap;
		}
        
		logger.info("取得40142交易笔数："+resultList.size());
		BigDecimal totalAmount = new BigDecimal("0");
		BigDecimal amount = new BigDecimal("0");
		Map<String ,BigDecimal> totAmountMap = new HashMap<String ,BigDecimal>();
		SettleTransDetail settleTransDetail = new SettleTransDetail();
		for (int i =0;i<resultList.size();i++) {
			settleTransDetail = resultList.get(i);
			totalAmount = new BigDecimal("0");
			amount = new BigDecimal("0");
			
			if (totAmountMap.containsKey(settleTransDetail.getMerchantCode())) {
				amount = totAmountMap.get(settleTransDetail.getMerchantCode());
				totalAmount = new BigDecimal(settleTransDetail.getAmount().toString());
				totalAmount = totalAmount.add(amount);
			} else {
				totalAmount = new BigDecimal(settleTransDetail.getAmount().toString());
			}
		  	
			totAmountMap.put(settleTransDetail.getMerchantCode(), totalAmount);
		}
        logger.info("取得40142交易金额："+totAmountMap);
        
        logger.info("准备发起代付交易");
        
        Iterator<Map.Entry<String, BigDecimal>> it = totAmountMap.entrySet().iterator();
        while (it.hasNext()) {
         Map.Entry<String, BigDecimal> entry = it.next();
         
        Date nowDate = new Date();
        TransOrderInfoAccount transOrderInfo = new TransOrderInfoAccount();
        transOrderInfo.setRequestTime(nowDate);
        transOrderInfo.setOrderNo("qjs_"+format_O.format(nowDate));
        transOrderInfo.setOrderDate(nowDate);
        transOrderInfo.setOrderAmount(entry.getValue().longValue());
        transOrderInfo.setOrderCount(1);
        transOrderInfo.setFuncCode("4014");
        transOrderInfo.setInterMerchantCode("141223100000059");  // 平安银行银企直联账号
        transOrderInfo.setMerchantCode(entry.getKey());
        transOrderInfo.setUserId("141223100000059");  // 平安银行银企直联账号
        transOrderInfo.setUserIpAddress("127.0.0.1");//绕过非空校验
        transOrderInfo.setAmount(totalAmount.longValue());
        transOrderInfo.setUserFee(0l);
        transOrderInfo.setErrorCode("P000002");
        transOrderInfo.setBusiTypeId("10");
        transOrderInfo.setRemark("平安银行银企直联每日头寸"+accountDate);
		
        boolean accflg = false;

        logger.info("调用帐户----第一次");
        accflg = toAccWithhold(transOrderInfo);
        if (!accflg) {
            logger.info("调用帐户----第二次");
            accflg = toAccWithhold(transOrderInfo);
            if (!accflg) {
                logger.info("调用帐户----第三次");
                accflg = toAccWithhold(transOrderInfo);
                if (!accflg) {
            		SendSMS.sendSMS(mobile, content);
                }
            }
        }
        }
		return rtnMap;
	}
	
	private boolean toAccWithhold(TransOrderInfoAccount transorderinfo) {

		String appKey_AC=userProperties.getProperty("ACAPP_KEY");
		String appSecret_AC =userProperties.getProperty("ACAPP_SECRET");
		String url_AC=userProperties.getProperty("ACAPP_URL");
		DefaultRopClient ropClient = new DefaultRopClient(url_AC, appKey_AC,
				appSecret_AC, SettleConstants.FILE_XML);
		WheatfieldUserWithholdRequest acRequest=new WheatfieldUserWithholdRequest();
		acRequest.setRequestno(transorderinfo.getRequestNo());
		acRequest.setRequesttime(transorderinfo.getRequestTime());
		acRequest.setTradeflowno(transorderinfo.getTradeFlowNo());
		acRequest.setOrderpackageno("");
		acRequest.setOrderno(transorderinfo.getOrderNo());
		acRequest.setOrderdate(transorderinfo.getOrderDate());
		acRequest.setOrderamount(transorderinfo.getOrderAmount());
		acRequest.setOrdercount(transorderinfo.getOrderCount());
		acRequest.setFunccode(transorderinfo.getFuncCode());
		acRequest.setIntermerchantcode(transorderinfo.getInterMerchantCode());
		acRequest.setMerchantcode(transorderinfo.getMerchantCode());
		acRequest.setUserid(transorderinfo.getUserId());
		acRequest.setAmount(transorderinfo.getAmount());
		acRequest.setFeeamount(transorderinfo.getFeeAmount());
		acRequest.setUserfee(transorderinfo.getUserFee());
		acRequest.setProfit(transorderinfo.getProfit());
		acRequest.setBusitypeid(transorderinfo.getBusiTypeId());
		acRequest.setPaychannelid(transorderinfo.getPayChannelId());
		acRequest.setBankcode(transorderinfo.getBankCode());
		acRequest.setUseripaddress(transorderinfo.getUserIpAddress());
		acRequest.setStatus(1);
		acRequest.setErrorcode(transorderinfo.getErrorCode());
		acRequest.setErrormsg(transorderinfo.getErrorMsg());
		acRequest.setRemark(transorderinfo.getRemark());
		
		acRequest.setProductid(transorderinfo.getErrorCode());

		try {
			WheatfieldUserWithholdResponse acResponse=ropClient.execute(acRequest, SessionUtils.sessionGet(url_AC, appKey_AC,appSecret_AC));
			if("true".equals(acResponse.getIs_success())) {
				return true;
			} else {
				logger.error("帐户侧返回消息：" + acResponse.getMsg());
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("调用帐户异常帐户：" + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
