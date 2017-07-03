package com.rkylin.settle.service.impl;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rkylin.settle.filedownload.CheckfileDownload;
import com.rkylin.settle.response.Response;
import com.rkylin.settle.response.TLBalanceGetResponse;
import com.rkylin.settle.service.IErrorResponseService;
import com.rkylin.settle.service.ROPSettleService;

@Service("rOPSettleService")
public class ROPSettleServiceImpl implements ROPSettleService, IAPIService {
	private static Logger logger = LoggerFactory
			.getLogger(ROPSettleServiceImpl.class);
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	CheckfileDownload checkfileDownload;

	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		TLBalanceGetResponse response = new TLBalanceGetResponse();
		String rootInstId = "";
		String accountNo = "";
		try {

			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if (keyObj.equals("rootinstid")) {
						rootInstId = value;
					} else if (keyObj.equals("accountno")) {
						accountNo = value;
					}
				}
			}
		} catch (Exception ex) {
			logger.error("参数输入转换错误:" + ex.getMessage());
			return errorResponseService.getErrorResponse("P1", "参数输入转换错误!");
		}
		if ("ruixue.wheatfield.ratetemplate.operate".equals(methodName)) {
			// 取得输入参数
			if (rootInstId == null || "".equals(rootInstId)) {
				return errorResponseService.getErrorResponse("P2", "机构号不能为空");
			}
			if (accountNo == null || "".equals(accountNo)) {
				return errorResponseService.getErrorResponse("P3", "账户不能为空");
			}
			try {
				String balance = checkfileDownload.merAcctBalance(rootInstId,
						accountNo);
				if ("失败".equals(balance)) {
					response.setIs_success(true);
					response.setBalance(balance);
					response.setRtn_msg("成功");
				} else {
					response.setIs_success(false);
					response.setRtn_msg("取得余额失败");
					return errorResponseService.getErrorResponse("S1",
							"取得余额失败！");
				}
			} catch (Exception ex) {
				logger.error("取得余额异常:" + ex.getMessage());
				return errorResponseService.getErrorResponse("P4", "取得余额异常！");
			}
		}
		return response;
	}
}
