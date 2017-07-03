package com.rkylin.settle.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rkylin.settle.settleInterface.SettleWebInterface;

@Controller
@RequestMapping("/update_acc_to_detail")
@Scope("prototype")
public class UpdateAccToDetailController extends BaseController {
	@Autowired
	private SettleWebInterface settleWebInterface;
	
	/***
     * 打开 从上游数据中回写手续费 和 协议,渠道
     * @param query
     * @throws Exception
     */
    @RequestMapping("/open_update_acc_to_detail")
    public String openUpdateAccToDetail() {
        return "/settlement/update_acc_to_detail/update_acc_to_detail";
    }
    
    @RequestMapping("/do_update")
    public void updateAccToDetail(String updateType, String accountDate, Integer month, Integer year, String beginDate, String endDate) throws Exception {
    	Map<String, Object> resultMap = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Calendar cld = Calendar.getInstance();
    	Date beginD = null;
    	Date endD = null;
    	
    	if("day".equals(updateType)) {
    		resultMap = settleWebInterface.updateAccountInfoToDetailInfo(sdf.parse(accountDate));
    	} else if("month".equals(updateType)) {
    		cld.set(cld.get(cld.YEAR), month - 1, 1, 0, 0, 0);
    		beginD = cld.getTime();
    		cld.set(cld.get(cld.YEAR), month, 1, 0, 0, 0);
    		endD = cld.getTime();
    		resultMap = settleWebInterface.updateAccountInfoToDetailInfo(beginD, endD);
    	} else if("year".equals(updateType)) {
    		cld.set(year, 0, 1, 0, 0, 0);
    		beginD = cld.getTime();
    		cld.set(year + 1, 0, 1, 0, 0, 0);
    		endD = cld.getTime();
    		resultMap = settleWebInterface.updateAccountInfoToDetailInfo(beginD, endD);
    	} else if("begin_end".equals(updateType)) {
    		resultMap = settleWebInterface.updateAccountInfoToDetailInfo(sdf.parse(beginDate), sdf.parse(endDate));
    	} else {
    		throw new Exception("系统异常 updateType:" + updateType + "未定义!");
    	}
    	logger.info("msg:" + String.valueOf(resultMap.get("msg")) + ", code:" + String.valueOf(resultMap.get("code")) + ", rows:" + String.valueOf(resultMap.get("rows")));
    	
    	//json形式的返回值
        writeText("执行成功, 修改交易" + resultMap.get("rows") + "条");
    }
    
    @RequestMapping("/do_update_pos")
    public void updateAccToDetailPos(String updateType, String accountDate, Integer month, Integer year, String beginDate, String endDate) throws Exception {
    	Map<String, Object> resultMap = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Calendar cld = Calendar.getInstance();
    	Date beginD = null;
    	Date endD = null;
    	
    	if("day".equals(updateType)) {
    		resultMap = settleWebInterface.updateAccountInfoToDetailInfoPos(sdf.parse(accountDate));
    	} else if("month".equals(updateType)) {
    		cld.set(cld.get(cld.YEAR), month - 1, 1, 0, 0, 0);
    		beginD = cld.getTime();
    		cld.set(cld.get(cld.YEAR), month, 1, 0, 0, 0);
    		endD = cld.getTime();
    		resultMap = settleWebInterface.updateAccountInfoToDetailInfoPos(beginD, endD);
    	} else if("year".equals(updateType)) {
    		cld.set(year, 0, 1, 0, 0, 0);
    		beginD = cld.getTime();
    		cld.set(year + 1, 0, 1, 0, 0, 0);
    		endD = cld.getTime();
    		resultMap = settleWebInterface.updateAccountInfoToDetailInfoPos(beginD, endD);
    	} else if("begin_end".equals(updateType)) {
    		resultMap = settleWebInterface.updateAccountInfoToDetailInfoPos(sdf.parse(beginDate), sdf.parse(endDate));
    	} else {
    		throw new Exception("系统异常 updateType:" + updateType + "未定义!");
    	}
    	logger.info("msg:" + String.valueOf(resultMap.get("msg")) + ", code:" + String.valueOf(resultMap.get("code")) + ", rows:" + String.valueOf(resultMap.get("rows")));
    	
    	//json形式的返回值
        writeText("执行成功, 修改交易" + resultMap.get("rows") + "条");
    }
}
