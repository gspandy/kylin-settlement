package com.rkylin.settle.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rkylin.settle.constant.RopConstants;
import com.rkylin.settle.filedownload.CheckfileDownload;
import com.rkylin.settle.filedownload.LianDongFileDownload;
import com.rop.response.Response;
import com.rop.service.IAPIService;
import com.rop.utils.LogUtils;
import com.rop.utils.ModelAndViewUtils;
import com.rop.utils.SpringBeanUtils;

@Controller
@Scope("prototype")
public class RopController {
//	private static Logger logger = LoggerFactory
//			.getLogger(RopController.class);
	@Autowired
	private HttpServletRequest request;
	/*@Autowired
	@Resource(name=SpringBeanConstants.SECURITY_SERVICE)
	private ISecurityService securityService;

	@Autowired
	private CheckfileDownload checkfileDownload;
	
	@Autowired
	private CheckfileDownload checkfileDownload;
	*/
    
	@Autowired
	private CheckfileDownload checkfileDownload;
	@Autowired
	private LianDongFileDownload lianDongFileDownload;
	
	
	
	@RequestMapping("/ropapi")
    public ModelAndView execute() {
    	Date startTime = new Date();

		@SuppressWarnings("unchecked")
		Map<String, String[]> requestParams = request.getParameterMap();

		//logger.info("-----------appkey--------------"+requestParams.get(Constants.SYS_PARAM_APP_KEY)[0]);
    	// 1. 验证request参数
    	Response response = null;//securityService.verifyRequest(requestParams);

        String method = request.getParameter(RopConstants.SYS_PARAM_METHOD);
        
        String format = request.getParameter(RopConstants.SYS_PARAM_FORMAT);
    	
        // 如果验证通过
    	if(response == null){
        	// 2. 通过适配器调用相应API
    		IAPIService apiService = (IAPIService)SpringBeanUtils.getBean(method);

    		response = apiService.doJob(requestParams,method);
    	}

    	if(!response.getCallResult()) {
    		method = "error";
    	}

        // 3. 将API结果映射为相应数据格式并返回
        ModelAndView mav = ModelAndViewUtils.getModelAndView(method, format, response);

        String ip = request.getRemoteAddr();
        
        Date endTime = new Date();

        LogUtils.info(requestParams, startTime, endTime, response, ip);

        return mav;
    }
    @RequestMapping("/test00")
    public ModelAndView test00() {
//    	String oldPath = "/mydata/duizhang/download/test/bak/";
//    	String newPath = "/mydata/duizhang/download/test/";
//    	String filename = "LL_20151111_M000005_";
    	//checkfileDownload.editNewLianLianCollateFile(oldPath, newPath, filename);
    	return null;
    }
    @RequestMapping("/test01")
    public ModelAndView test01() {
//    	String oldPath = "/mydata/duizhang/download/test/bak/";
//    	String newPath = "/mydata/duizhang/download/test/";
//    	String filename = "LD_20151111_M000005";
    	//lianDongFileDownload.editNewLianDongCollateFile(oldPath, newPath, filename);
    	return null;
    }
}
