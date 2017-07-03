package com.rkylin.settle.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleBalanceEntryQuery;
import com.rkylin.settle.service.SettleBalanceEntryService;
import com.rkylin.settle.settleInterface.SettleWebInterface;
import com.rkylin.settle.util.OutputFileUtil;
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
@RequestMapping("/collate")
@Scope("prototype")
public class SettleCollateController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(SettleCollateController.class);
	@Autowired
	private SettleBalanceEntryService settleBalanceEntryService;
	@Autowired
    private SettleWebInterface settleWebInterface;
	private OutputFileUtil outputFileUtil;
	/***
     * 打开ROP下载文件页面
     * @param query
     * @throws Exception
     */
    @RequestMapping("/open_download_from_rop")
    public String openDownloadFromRop() {
        return "/settlement/collate/download_from_rop";
    }
	/***
     * 打开上游对账文件操作页面
     * @param query
     * @throws Exception
     */
    @RequestMapping("/collate_open_view")
    public String openCollateFile() {
        return "/settlement/collate/query";
    }
	/***
     * 打开上游对账文件操作页面
     * @param query
     * @throws Exception
     */
    @RequestMapping("/collateForPos_open_view")
    public String openCollateForPosFile() {
        return "/settlement/collate/queryForPos";
    }
    /***
     * 打开下载下游对账文件操作页面
     * @param query
     * @throws Exception
     */
    @RequestMapping("/open_download_collatefile2merchant")
    public String openDownloadCollateFile() {
        return "/settlement/collate/download";
    }
    /***
     * 查询下载下游对账文件目录
     * @param query
     * @throws Exception
     */
    @RequestMapping("/query_download_collatefile2merchant")
    public void queryDownloadCollateFile(HttpServletRequest request,HttpServletResponse response)  throws Exception {
    	outputFileUtil = new OutputFileUtil();
    	String fileType = request.getParameter("fileType");
    	/**
    	 * 下游文件参数
    	 */
    	String merchantName = request.getParameter("merchantName");		//机构简称
    	String transType = request.getParameter("transType");			//交易类型
    	/**
    	 * 上游文件参数 
    	 */
    	String channelCodeU = request.getParameter("channelCodeD");		//渠道简称
    	String merchantCodeU = request.getParameter("merchantCodeD");	//机构ID
    	String transTypeU = request.getParameter("transTypeD");			//交易类型
    	String accountDate = request.getParameter("accountDateStr").replace(" 00:00:00", "").replace("-", "");	//账期yyyyMMdd
    	String filePath = null;			//目录
    	String[] filenameArr = null;	//目录下的文件名称数组
    	if(fileType.equals("up")) {//下载 上传的下游对账文件
    		filenameArr = outputFileUtil.getFilenameListFromFileUPath(merchantName, "", transType, accountDate);
    		filePath = SettleConstants.FILE_UP_PATH + "beifen" + File.separator;
    	} else if(fileType.equals("down")) {//下载 下载到服务期的上游对账文件   		
    		filenameArr = outputFileUtil.getFilenameListFromFileDPath(merchantCodeU, channelCodeU, transTypeU, accountDate); 		
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    		Date date = sdf.parse(accountDate);
    		date.setTime(date.getTime() + 24 * 60 * 60 * 1000);
    		String accountDateStr = sdf.format(date);
    		filePath = SettleConstants.FILE_PATH + accountDateStr + File.separator;
    	}
    	//json形式的返回值
    	Map<String,Object> json = new HashMap<String,Object>();
        json.put("result", "ok");
        json.put("msg", "查询成功");
        json.put("filePath", filePath);
        json.put("dataList", filenameArr);
        writeJson(json);
    }
    /***
   	 * 下载服务器上的下游对账文件
   	 * @param query
   	 * @throws Exception
   	 */
   	@RequestMapping("/download_collatefile2merchant")
   	public void downloadFile(String filePath, String filename) throws Exception {
   		// 读到流中  
   		InputStream inStream = null;
   		ServletOutputStream outputStream = null;
   		try {	
   			filePath = filePath + File.separator + filename;
   			inStream = new FileInputStream(filePath);// 文件的存放路径  
   			outputStream = response.getOutputStream();
			// 设置输出的格式  
			response.reset();  
			response.setContentType("bin");  
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
			// 循环取出流中的数据  
			byte[] b = new byte[100];  
			int len;  
			while ((len = inStream.read(b)) > 0) {
				outputStream.write(b, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(inStream != null) inStream.close();
			if(outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
   	}
    /***
     * 下载上游对账文件
     * @param query
     * @throws Exception
     */
    @RequestMapping("/collate_ajax")
    public ModelAndView downLoadAjax(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> map=new HashMap<String,Object>();
        //机构号
        String merchantCode = request.getParameter("merchantCode");
        //对账类型&业务类型
        String accountType = request.getParameter("accountType");
        //渠道号
        String payChannelId = request.getParameter("payChannelId");
        //账期
        String accountDateStr = request.getParameter("accountDateStr");
        //对账类型
        String[] typeArr = accountType.split("&");
        String readType = null;
        String bussType = null;
        if(typeArr.length > 0) {
        	readType = accountType.split("&")[0];
        }
        if(typeArr.length > 1) {
        	bussType = accountType.split("&")[1];
        }
        
        Date accountDate = null;
        boolean result = true;
        String msg = "";
        
        if(StringUtils.isEmpty(merchantCode)){
            result = false;
            msg = "机构号不允许为空";
        }
        if(StringUtils.isEmpty(accountType)){
            result = false;
            msg = "对账类型不允许为空";
        }
        if(StringUtils.isEmpty(payChannelId)){
            result = false;
            msg = "支付渠道不允许为空";
        }
        if(StringUtils.isEmpty(accountDateStr)){
            result = false;
            msg = "账期不允许为空";
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            accountDate = sdf.parse(accountDateStr);
        }
        if(result){
        	try {
        		/***
        		 * 目前只有 通联渠道需要使用bussType参数
        		 */
        		bussType = "01".equals(payChannelId) ? bussType : null;
        		settleWebInterface.collage(payChannelId, readType, merchantCode, accountDate, bussType);
        		msg = "对账成功";
			} catch (Exception e) {
				msg = "对账失败";
				e.printStackTrace();
			}
        }
        map.put("result", result);
        map.put("msg", msg);
        return new ModelAndView(new MappingJacksonJsonView(), map);
    }
    
    /***
     * 下载上游对账文件
     * @param query
     * @throws Exception
     */
    @RequestMapping("/collateForPos_ajax")
    public ModelAndView collageForPos(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> map=new HashMap<String,Object>();
        //机构号
        String merchantCode = request.getParameter("merchantCode");
        //对账类型&业务类型
        String accountType = request.getParameter("accountType");
        //渠道号
        String payChannelId = request.getParameter("payChannelId");
        //账期
        String accountDateStr = request.getParameter("accountDateStr");
        //对账类型
        String[] typeArr = accountType.split("&");
        String readType = null;
        String bussType = null;
        if(typeArr.length > 0) {
        	readType = accountType.split("&")[0];
        }
        if(typeArr.length > 1) {
        	bussType = accountType.split("&")[1];
        }
        
        Date accountDate = null;
        boolean result = true;
        String msg = "";
        
        if(StringUtils.isEmpty(merchantCode)){
            result = false;
            msg = "机构号不允许为空";
        }
        if(StringUtils.isEmpty(accountType)){
            result = false;
            msg = "对账类型不允许为空";
        }
        if(StringUtils.isEmpty(payChannelId)){
            result = false;
            msg = "支付渠道不允许为空";
        }
        if(StringUtils.isEmpty(accountDateStr)){
            result = false;
            msg = "账期不允许为空";
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            accountDate = sdf.parse(accountDateStr);
        }
        if(result){
        	try {
        		/***
        		 * 目前只有 通联渠道需要使用bussType参数
        		 */
        		// bussType = "01".equals(payChannelId) ? bussType : null;
        		settleWebInterface.collageForPos(payChannelId, readType, merchantCode, accountDate, bussType);
        		msg = "对账成功";
			} catch (Exception e) {
				msg = "对账失败";
				e.printStackTrace();
			}
        }
        map.put("result", result);
        map.put("msg", msg);
        return new ModelAndView(new MappingJacksonJsonView(), map);
    }
    
    /**
     * @Description: 查询对账结果
     * @param query
     * @throws Exception
     * @author CLF
     */
    @RequestMapping("/query_ajax")
    public void queryAjax(SettleBalanceEntryQuery query) throws Exception {
        try {
            if(StringUtils.isEmpty(query.getAccountDateStr())){
                Map<String,Object> json = new HashMap<String,Object>();
                json.put("result", "false");
                json.put("msg", "账期不允许为空");
                writeJson(json);
            }
            PagerModel<SettleBalanceEntry> pagerModel = settleBalanceEntryService.query(query);
            returnJsonList(pagerModel);
        } catch (Exception e) {
            logger.error("查询对账结果失败",e);
        }
    }
    /***
     * 打开上传对账文件操作页面
     * @param query
     * @throws Exception
     */
    @RequestMapping("/upload_collate_file_view")
    public String uploadCollateFile() {
        return "/settlement/collate/upload";
    }
    /***
     * 上传对账文件
     * @param query
     * @throws Exception
     */
    @RequestMapping("/upload_collate_file_ajax")
    public ModelAndView upLoadAjax(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> map=new HashMap<String,Object>();
        //机构号
        String merchantCode = request.getParameter("merchantCode");
        //交易类型
        String readType = request.getParameter("readType");
        //ROP文件类型编码
        String ropFileTypeStr = request.getParameter("ropFileType");
        //ROP文件类型编码
        String batch = request.getParameter("batch");
        //Integer ropFileType = 0;
        //账期
        String accountDateStr = request.getParameter("accountDateStr");
        Date accountDate = null;
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
//        if(StringUtils.isEmpty(ropFileTypeStr)){
//            result = false;
//            msg = "ROP文件类型编码不允许为空";
//        }else{
//            //ropFileType = Integer.parseInt(ropFileTypeStr);
//        }
        if(StringUtils.isEmpty(accountDateStr)){
            result = false;
            msg = "账期不允许为空";
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            accountDate = sdf.parse(accountDateStr);
        }
        if(StringUtils.isEmpty(batch)){
        	batch = null;
        }
        if(result){
        	logger.info(">>> >>> >>> 开始上传 下游对账文件至ROP服务器 {merchantCode:"+merchantCode+", readType:"+readType+", ropFileTypeStr:"+ropFileTypeStr+", accountDateStr:"+accountDateStr+", batch:"+batch+"}");
        	Map<String, Object> resultMap = settleWebInterface.uploadCollateFile(merchantCode, readType, accountDate, batch);
        	logger.info("<<< <<< <<< 结束上传 下游对账文件至ROP服务器 {merchantCode:"+merchantCode+", readType:"+readType+", ropFileTypeStr:"+ropFileTypeStr+", accountDateStr:"+accountDateStr+", batch:"+batch+"}");
        	if(resultMap != null) {
        		msg = String.valueOf(resultMap.get("msg"));
        	}
        }
        map.put("result", result);
        map.put("msg", msg);
        return new ModelAndView(new MappingJacksonJsonView(),map);
    }
    
    /***
     * 上传对账文件
     * @param query
     * @throws Exception
     */
    @RequestMapping("/create_collate_file_ajax")
    public ModelAndView createAjax(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> map=new HashMap<String,Object>();
        //机构号
        String merchantCode = request.getParameter("merchantCode");
        //交易类型
        String readType = request.getParameter("readType");
        //ROP文件类型编码
        String ropFileTypeStr = request.getParameter("ropFileType");
        //Integer ropFileType = 0;
        //账期
        String accountDateStr = request.getParameter("accountDateStr");
        Date accountDate = null;
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
//        if(StringUtils.isEmpty(ropFileTypeStr)){
//            result = false;
//            msg = "ROP文件类型编码不允许为空";
//        }else{
//            //ropFileType = Integer.parseInt(ropFileTypeStr);
//        }
        if(StringUtils.isEmpty(accountDateStr)){
            result = false;
            msg = "账期不允许为空";
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            accountDate = sdf.parse(accountDateStr);
        }
        if(result){
        	Map<String, Object> resultMap = settleWebInterface.createCollateFile(merchantCode, readType, accountDate);
        	if(resultMap != null) {
        		msg = String.valueOf(resultMap.get("msg"));
        	}
        }
        map.put("result", result);
        map.put("msg", msg);
        return new ModelAndView(new MappingJacksonJsonView(),map);
    }
    /***
     *  pos交易结算
     */
    @RequestMapping("/settleForPos_ajax")
    public void settleForPos_ajax(String accountDateStr) {
    	logger.info(">>> >>> >>> >>> 开始  pos交易结算 入参　accountDateStr:" + accountDateStr);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> resultMap = null;
    	String resultTxt = null;
    	try {
    		resultMap = settleWebInterface.settleForPos(sdf.parse(accountDateStr));
    		resultTxt = "msg:" + resultMap.get("msg") + ", code:" + resultMap.get("code");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("pos交易结算 异常  ", e);
			resultTxt = "pos交易结算 异常 ";
		}
    	try {
			writeText(resultTxt);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("pos交易结算, writeText方法异常  ", e);
		}
    	logger.info(">>> >>> >>> >>> 结束  pos交易结算");
    }
}
