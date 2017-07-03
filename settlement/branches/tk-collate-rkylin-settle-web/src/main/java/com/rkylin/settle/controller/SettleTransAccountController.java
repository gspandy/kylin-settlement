package com.rkylin.settle.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransAccountQuery;
import com.rkylin.settle.service.SettleTransAccountService;
import com.rkylin.settle.settleInterface.SettleWebInterface;
import com.rkylin.settle.util.PagerModel;
import com.rop.utils.StringUtils;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleTransAccountController
 * 类描述：上游交易信息查询
 * 创建人：CLF
 * 创建时间：2015年8月24日 下午3:07:50
 * 修改人：
 * 修改时间：2015年8月24日 下午3:07:50
 * 修改备注：
 * @version
 */
@Controller
@RequestMapping("/transaccount")
@Scope("prototype")
public class SettleTransAccountController extends BaseController {
	@Autowired
	private SettleTransAccountService settleTransAccountService;
	@Autowired
    private SettleWebInterface settleWebInterface;
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleTransAccountQuery query) throws Exception {
		try {
			PagerModel<SettleTransAccount> pagerModel = settleTransAccountService.query(query);
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 打开查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/account_manager")
	public String openQuery() {
		return "/settlement/trans/account/query";
	}
	/***
     * 打开详情页面
     * @param id
     * @throws Exception
     */
    @RequestMapping("/account_open_view")
    public ModelAndView openView(Long id) {
        ModelAndView modelAndView = new ModelAndView("/settlement/trans/account/view");
        modelAndView.addObject("settleTransAccount", settleTransAccountService.findById(id));
        return modelAndView;
    }
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/account_open_edit")
	public ModelAndView openEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/trans/account/edit");
		modelAndView.addObject("settleTransAccount", settleTransAccountService.findById(id));
		return modelAndView;
	}
	/***
	 * 修改
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/edit_ajax")
	public void editAjax(SettleTransAccount settleTransAccount) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer resultNum = settleTransAccountService.edit(settleTransAccount);
		Boolean isSuccess = false;
		String msg = "";
		
		if(resultNum > 0) {
			isSuccess = true;
			msg = "修改成功!";
		} else {
			isSuccess = false;
			msg = "修改失败!";
		}
		
		resultMap.put("isSuccess", isSuccess);
		resultMap.put("msg", msg);
		
		try {
			writeJson(resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/***
     * 打开上游对账文件操作页面
     * @param query
     * @throws Exception
     */
    @RequestMapping("/collate_file_open_view")
    public String openCollateFile() {
        return "/settlement/trans/account/collatefile";
    }
    /***
     * 下载上游对账文件
     * @param query
     * @throws Exception
     */
    @RequestMapping("/download_ajax")
    public ModelAndView downLoadAjax(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String,Object> map=new HashMap<String,Object>();
    	//返回值提示信息Map
    	Map<String,String> resultMap = null;
    	//机构号
    	String payChanelId = request.getParameter("payChannelId");
    	//机构号
        String merchantCode = request.getParameter("merchantCode");
        //交易类型
        String readType = request.getParameter("readType");
        //账期
        String accountDate = request.getParameter("accountDate");
        boolean result = true;
        String msg = "";
        
        if(StringUtils.isEmpty(merchantCode)){
            result = false;
            msg = "机构号不允许为空";
        }

        if(StringUtils.isEmpty(accountDate)){
            result = false;
            msg = "账期不允许为空";
        }
              
        if(readType.equals("01")) {
        	readType = "WG";
        } else if(readType.equals("02")) {
        	readType = "ZF";
        } else if(readType.equals("03")) {
        	readType = "YD";
        } else if(readType.equals("04")) {
        	readType = "LLKJ";
        } else if(readType.equals("05")) {
        	readType = "PAYQ";
        } else if(readType.equals("06")) {
        	readType = "WX";
        } else if(readType.equals("07")) {
        	readType = "MSYQ";
        } else if(readType.equals("08")) {
        	readType = "TLSDK";
        } else if(readType.equals("09")) {
        	readType = "WXAPP";
        } else if(readType.equals("11")) {
        	readType = "PABKHKF";
        } else if(readType.equals("12")) {
        	readType = "PABB2B";
        } else {
        	 logger.info("readType不用转化");
        }
        if(result) {
        	if ("01".equals(payChanelId)) {
        		if (readType.equals("WX") 
        			|| readType.equals("TLSDK")
        			|| readType.equals("WXAPP")) {
        			resultMap = this.settleWebInterface.tlwxFileDown(accountDate, merchantCode, readType);
        		} else {
        			resultMap = this.settleWebInterface.tlFileDown(merchantCode, readType, accountDate);
        		}
        	} else if ("04".equals(payChanelId)) {
        		resultMap = this.settleWebInterface.lianLianFileDown(accountDate, merchantCode, readType);
        	} else if("05".equals(payChanelId)) {  
        		resultMap = this.settleWebInterface.lDWGFileDown(accountDate, merchantCode, readType);
        	} else if("S01".equals(payChanelId)) {  
        		resultMap = this.settleWebInterface.cJZFFileDown(accountDate, merchantCode, readType);
        	} else if("Y01".equals(payChanelId)) {  
        		if(readType.equals("PABB2B")) {
        			resultMap = this.settleWebInterface.pABB2BFileDown(accountDate, merchantCode);
        		} else if(readType.equals("PABKHKF")) {
        			resultMap = this.settleWebInterface.tlwxFileDown(accountDate, merchantCode, readType);
                } else {
        			resultMap = this.settleWebInterface.pABFileDown(accountDate, merchantCode, readType);
        		}
        	} else if("Y02".equals(payChanelId)) {  
        		if(",AgentPay_DL,AgentPay_FDL,Refund_DL,Refund_FDL,".indexOf(","+readType+",")!=-1){
        			resultMap = this.settleWebInterface.msAgentPayorRefundFileDown(merchantCode, readType, accountDate,payChanelId);
        		}else{
        			resultMap = this.settleWebInterface.tlwxFileDown(accountDate, merchantCode, readType);
        		}
        	} else if("S02".equals(payChanelId)) {  
        		resultMap = this.settleWebInterface.rbWGFileDown(merchantCode, readType, accountDate,payChanelId);
        	} else if("S04".equals(payChanelId)) {
        		resultMap = this.settleWebInterface.lycheeFileDown(accountDate, merchantCode);
        	} else if("S03".equals(payChanelId)) {
        		resultMap = this.settleWebInterface.ybFileDown(merchantCode, readType, accountDate,payChanelId);
        	} else if("P01".equals(payChanelId)) {
        		if("13".equals(readType)) {
        			resultMap = this.settleWebInterface.htglRepayFileDown(accountDate, merchantCode);
        		} else if("10".equals(readType)) {
        			map.put("result", "error request!");
        	        map.put("msg", "使用 【pos-从ROP下载对账文件】 下载对账文件!");
        		}
        	} else {
        		msg = "渠道号不存在!, 请联系技术人员!";
        	}
        }
        if(resultMap != null) {
    		msg = (String) resultMap.get("errMsg");
    	}
        map.put("result", result);
        map.put("msg", msg);
        return new ModelAndView(new MappingJacksonJsonView(), map);
    }
    /**
     * @Description: 读取上游对账文件
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @author CLF
     */
    @RequestMapping("/read_ajax")
    public ModelAndView readLoadAjax(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> map=new HashMap<String,Object>();
        //机构号
        String merchantCode = request.getParameter("merchantCode");
        //交易类型
        String readType = request.getParameter("readType");
        //渠道号
        String payChannelId = request.getParameter("payChannelId");
        //账期
        String accountDate = request.getParameter("accountDate");
        
        boolean result = true;
        String msg = "";
        
        if(StringUtils.isEmpty(merchantCode)){
            result = false;
            msg = "机构号不允许为空";
        }
        if(StringUtils.isEmpty(readType)){
            result = false;
            msg = "交易类型不允许为空";
        }
        if(StringUtils.isEmpty(payChannelId)){
            result = false;
            msg = "支付渠道不允许为空";
        }
        if(StringUtils.isEmpty(accountDate)){
            result = false;
            msg = "账期不允许为空";
        }
        if(result){
            Map<String, Object> resultMap = settleWebInterface.readCollateFile(merchantCode, readType, accountDate, null, payChannelId);
            if(resultMap != null) {
            	msg = String.valueOf(resultMap.get("msg"));
            }
        }
        if(msg.isEmpty()) msg = "读取上游对账文件 成功!";
        
        map.put("result", result);
        map.put("msg", msg);
        return new ModelAndView(new MappingJacksonJsonView(),map);
    }
    
	/***
	 * 打开银企直联测试交易并汇总金额查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/openTestYFQQueryPage")
	public String openTestYFQQueryPage() {
		return "/settlement/trans/account/testYFQQuery";
	}
	
	/**
	 * 查询银企直联测试交易并汇总金额
	 * @param accountDate 账期
	 * @throws Exception
	 */
    @RequestMapping("/testYFQSummary")
    public void testYFQSummary(String accountDate) throws Exception {
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	try{
	    	if(StringUtils.isEmpty(accountDate)){
	        	throw new Exception();
	        }	
	    	SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		settleWebInterface.selectTestTransAndSumAmountYQZL(format.parse(accountDate));
    		resultMap.put("msg", "success");
    	}catch(Exception e){
    		logger.error("testYFQSummary发生异常，e="+e);
    	}
		try {
			writeJson(resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}
