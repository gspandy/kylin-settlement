package com.rkylin.settle.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.util.OutputFileUtil;

@Controller
@RequestMapping("/my_download")
@Scope("prototype")
public class MyDownloadController extends BaseController {
	private OutputFileUtil outputFileUtil;
	/***
     * 打开 我的下载
     * @param query
     * @throws Exception
     */
    @RequestMapping("/download")
    public String download() {
        return "/settlement/my_download/download";
    }
    /***
     * 查询下载下游对账文件目录
     * @param query
     * @throws Exception
     */
    @RequestMapping("/query_download_file")
    public void queryDownloadCollateFile(HttpServletRequest request,HttpServletResponse response)  throws Exception {
    	outputFileUtil = new OutputFileUtil();
    	String transType = "-1".equals(request.getParameter("transType")) ? null : request.getParameter("transType");
    	String accountDate = request.getParameter("downloadDate").replace(" 00:00:00", "").replace("-", "");	//账期yyyyMMdd
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = sdf.parse(accountDate);
    	String accountDateStr = sdf.format(date);
    	String filePath = SettleConstants.MY_DOWNLOAD_PATH + accountDateStr + File.separator;	//目录
    	File folder = new File(filePath);
    	if(!folder.exists()) folder.mkdirs();
    	Map<String, String>[] mapArr = null;	//目录下的文件对象数组
    	mapArr = outputFileUtil.getFilenameListFromMyDLPath(folder, transType, null);
		
    	//json形式的返回值
    	Map<String,Object> json = new HashMap<String,Object>();
        json.put("result", "ok");
        json.put("msg", "查询成功");
        json.put("filePath", filePath);
        json.put("dataList", mapArr);
        writeJson(json);
    }
}
