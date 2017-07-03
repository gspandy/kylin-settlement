package com.rkylin.settle.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.rkylin.settle.util.PagerModel;

public class BaseController {
	protected HttpServletRequest request;

	protected HttpServletResponse response;

	protected HttpSession session;

	protected static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request,
			HttpServletResponse response) {

		this.request = request;

		this.response = response;

		this.session = request.getSession();
	}

	/**
	 * 返回单条记录
	 * 
	 * @param json
	 *            字符串
	 */
	public void writeText(String text) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(text);
	}

	/**
	 * 返回多条记录
	 * 
	 * @param PagerModel
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void returnJsonList(PagerModel pagerModel) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		Map json = new HashMap();
		json.put("result", pagerModel.getResult());
		json.put("msg", pagerModel.getMsg());
		json.put("totalNum", pagerModel.getTotal());
		json.put("dataList",pagerModel.getList());
		json.put("dataMapList",pagerModel.getMapList());
		json.put("dataMap",pagerModel.getDataMap());
		json.put("obj", pagerModel.getObj());
		writeJson(json);
	}
	
	
	public void writeJson(Object obj) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		response.setContentType("text/html;charset=UTF-8");
		mapper.writeValue(response.getWriter(), obj);
	}
	/**
	 * 将请求的参数存到session中，用于返回重现条件
	 * @param sessionKey
	 */
//	@RequestMapping("/saveParamsToSession")
//	public void saveParamsToSession(String sessionKey){
//		if(sessionKey==null || "".equals(sessionKey)){
//			sessionKey="savedParamsForSession";
//		}
//		Map map = new HashMap();
//		map = request.getParameterMap();
//		session.setAttribute(sessionKey, map);
//	}
	
	/**
	 * 下载文件，中文文件名编码格式utf-8
	 * @param response HTTP响应流
	 * @param aliasName 显示下载文件名
	 * @param filePath 下载文件实际路径，包含文件名
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected String downTheFile(String aliasName,String filePath) throws IOException {
		
		String agent = request.getHeader("User-Agent");
		
		if(agent != null && agent.toLowerCase().indexOf("firefox") > 0)
        {
			aliasName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(aliasName.getBytes("UTF-8")))) + "?=";    
        }
        else
        {
        	aliasName =  java.net.URLEncoder.encode(aliasName, "UTF-8");
//        	if (!isMSIE) {
//    			aliasName=  new String(aliasName.replaceAll(" ", "").getBytes("utf-8"),"iso8859-1");
//    		} else{
//    			aliasName= URLEncoder.encode(aliasName, "UTF-8");
//    		}
       }
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/octet-stream");
		response.addHeader(
				"Content-Disposition",
				"attachment; filename=\""
						+ aliasName
						+ "\"");
		ServletOutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(filePath);
		try {
			int len;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} finally{
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
