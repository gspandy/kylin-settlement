package com.rkylin.settle.service.impl;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.service.SettleService;
import com.rongcapital.mtkernel.pojo.ModifyFinAccountVo;
import com.rongcapital.mtkernel.response.CommonResponse;
import com.rongcapital.mtkernel.service.FinanceAccountServiceApi;
import com.rop.response.Response;
import com.rop.service.IAPIService;

@Service("settleService")
public class SettleServiceImpl implements SettleService,IAPIService{
	protected static Logger logger = LoggerFactory.getLogger(SettleServiceImpl.class);
	@Autowired
	FinanceAccountServiceApi financeAccountService;
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFinAccountId(String subjectCode, String rootInstCd,
			String currency, String channelCode, String agreementCode) throws Exception {
		String code  = null;
		String msg  = null;
		try{
			ModifyFinAccountVo params = new ModifyFinAccountVo();
			params.setSubjectCode(subjectCode);
			params.setRootInstCd(rootInstCd);
			params.setChannelCode(channelCode);
			params.setCurrency(currency);
			params.setAgreementCode(agreementCode);
			logger.info("开始调用会计接口financeAccountService查询FinAccountId,封装前的参数subjectCode="+subjectCode+",rootInstCd="+rootInstCd+",channelCode="+channelCode+",currency="+currency+",agreementCode="+agreementCode);
			CommonResponse commonRes =  financeAccountService.getFinAccountId(params);
			if(commonRes !=null){
				code  = commonRes.getCode();
				msg  = commonRes.getMsg();
				logger.info("结束调用会计接口financeAccountService查询FinAccountId,会计返回code="+code+",msg="+msg);
			}else{
				logger.info("结束调用会计接口financeAccountService查询FinAccountId,会计返回null");
			}
		}catch(Exception e){
			logger.error("getFinAccountId方法发生异常！e="+e);
			throw new Exception("发生异常,e"+e);
		}
	
		if("WF0000".equals(code)){//成功
			return msg;
		}else{
			return null;
		}
	}
}
