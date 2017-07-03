package com.rkylin.settle.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.Rop.api.domain.FileUrl;
import com.rkylin.settle.common.UploadAndDownLoadUtils;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.manager.ParameterInfoOldManager;
import com.rkylin.settle.manager.SettleBatchManageManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettlementManager;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoOld;
import com.rkylin.settle.pojo.ParameterInfoOldQuery;
import com.rkylin.settle.pojo.ParameterInfoQuery;
import com.rkylin.settle.pojo.SettleBatchManage;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;

@Component("settlementUtil")
public class SettlementUtil {
	private static Logger logger = LoggerFactory
			.getLogger(SettlementUtil.class);

	@Autowired
	HttpServletRequest request;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	ParameterInfoOldManager parameterInfoOldManager;
	@Autowired
	SettlementManager settlementManager;
	@Autowired
	SettleBatchManageManager settleBatchManageManager;
	@Autowired
	@Qualifier("settleParameterInfoManager")
	private SettleParameterInfoManager settleParameterInfoManager;		//'清算'属性表Manager

	/**
	 * 文件下载
	 * 
	 * @param type
	 *            文件类型
	 * @param batch
	 *            批次号
	 * @param accountDate
	 *            账期 yyyyMMdd
	 * @param filepath
	 *            生成文件路径+文件名
	 * @param priOrPubKey
	 *            公钥或者私钥
	 * @param flg
	 *            私钥时为1，公钥为其他
	 * @param appKey
	 *            ropKey
	 * @param appSecret
	 *            ropSecret
	 * @param ropUrl
	 *            ropUrl
	 * 
	 * @return
	 */
	public Map<String, String> getFileFromROP(int type, String batch,
			Date accountDate, String filepath, String priOrPubKey, int flg,
			String appKey, String appSecret, String ropUrl, String fileType) {

		Map<String, String> rtnMap = new HashMap<String,String>();
		rtnMap.put("errMsg", "ok");
		rtnMap.put("errCode", "0000");
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	try {
			String urlKeyStr = "";
			List<FileUrl> urlKey = UploadAndDownLoadUtils.getUrlKeys(type, accountDate, batch, SettleConstants.FILE_XML,appKey,appSecret,ropUrl);
			if (urlKey == null || urlKey.size() == 0) {
				logger.error("urlkey不存在！");
				rtnMap.put("errMsg", "urlkey不存在！");
				rtnMap.put("errCode", "0001");
				return rtnMap;
			}
			String dfilename;
			String dfilepath;
			for (FileUrl fileurl:urlKey) {
				urlKeyStr = fileurl.getUrl_key();
				dfilename = type+fileurl.getInvoice_date()+"_"+fileurl.getBatch()+"."+fileType;
				dfilepath = UploadAndDownLoadUtils.downloadFile(formatter.format(accountDate),SettleConstants.FILE_XML, urlKeyStr,dfilename,request,type,priOrPubKey,flg,appKey,appSecret,ropUrl);
				this.copyFile(dfilepath+File.separator+dfilename, filepath);
				break;
			}
		} catch (Exception e) {
			rtnMap.put("errMsg", "文件下载失败！" + e.getMessage());
			rtnMap.put("errCode", "0001");
			return rtnMap;
		}
		File file = new File(filepath);
		if (!file.exists()) {
			rtnMap.put("errMsg", "文件不存在！");
			rtnMap.put("errCode", "0001");
			return rtnMap;
		}
    	
		rtnMap.put("file", filepath);
		return rtnMap;
	}
	/**
	 * 文件上传
	 * 
	 * @param type
	 *            文件类型
	 * @param batch
	 *            批次号
	 * @param accountDate
	 *            账期 yyyyMMdd
	 * @param filepath
	 *            生成文件路径+文件名
	 * @param priOrPubKey
	 *            公钥或者私钥
	 * @param flg
	 *            私钥时为1，公钥为其他
	 * @param appKey
	 *            ropKey
	 * @param appSecret
	 *            ropSecret
	 * @param ropUrl
	 *            ropUrl
	 * 
	 * @return
	 */
	public Map<String, String> setFileFromROP(int type, String batch,
			Date accountDate, String filepath, String priOrPubKey, int flg,
			String appKey, String appSecret, String ropUrl) {
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("errMsg", "ok");
		rtnMap.put("errCode", "0000");
		File file = new File(filepath);
		if (!file.exists()) {
			rtnMap.put("errMsg", "文件不存在！");
			rtnMap.put("errCode", "0001");
		}

		String rtnurlKey = "";
		try {
			rtnurlKey = UploadAndDownLoadUtils.uploadFile(filepath, type,
					accountDate, batch, SettleConstants.FILE_XML, priOrPubKey,
					flg, appKey, appSecret, ropUrl);
			if (rtnurlKey == null || "".equals(rtnurlKey)) {
				logger.error("文件上传失败！");
				rtnMap.put("errMsg", "文件上传失败！");
				rtnMap.put("errCode", "0001");
				return rtnMap;
			}
			rtnMap.put("urlKey", rtnurlKey);
		} catch (Exception e) {
			logger.error(">>> >>> 异常:" + e.getMessage());
			e.printStackTrace();
			rtnMap.put("errMsg", "文件上传失败！" + e.getMessage());
			rtnMap.put("errCode", "0001");
			return rtnMap;
		}

		return rtnMap;
	}

	/**
	 * 取得一个机构号
	 * 
	 * @param parameterCode
	 *            P2P机构号
	 * @param parameterType
	 *            类型
	 * 
	 * @return
	 */
	public String getParameterInfoByCode(String parameterCode,
			String parameterType) {
		// TODO Auto-generated method stub
		ParameterInfoQuery query = new ParameterInfoQuery();
		query.setParameterType(parameterType);
		query.setParameterCode(parameterCode);
		query.setStatusId(1);
		List<ParameterInfo> parameterInfos = parameterInfoManager
				.queryList(query);
		if (parameterInfos != null && parameterInfos.size() > 0) {
			return parameterInfos.get(0).getParameterValue();
		}
		return null;
	}

	/**
	 * 取得全部机构号
	 * 
	 * @param parameterType
	 *            类型
	 * @param flg
	 *            key设置1：VALUE为MapKey，其他：CODE为MapKey
	 * 
	 * @return
	 */
	public Map<String, String> getParameterCodeByType(String parameterType,
			int flg) {
		// TODO Auto-generated method stub
		ParameterInfoQuery query = new ParameterInfoQuery();
		query.setParameterType(parameterType);
		query.setStatusId(1);
		HashMap<String, String> parameterCode = new HashMap<String, String>();
		List<ParameterInfo> parameterInfos = parameterInfoManager
				.queryList(query);
		if (parameterInfos != null && parameterInfos.size() > 0) {
			for (int i = 0; i < parameterInfos.size(); i++) {
				if (flg == 1) {
					parameterCode.put(
							parameterInfos.get(i).getParameterValue(),
							parameterInfos.get(i).getParameterCode());
				} else {
					parameterCode.put(parameterInfos.get(i).getParameterCode(),
							parameterInfos.get(i).getParameterValue());
				}
			}
			return parameterCode;
		}
		return null;
	}

	/**
	 * 取得对应批次号
	 * 
	 * @param accountDate
	 *            账期
	 * @param batchType
	 *            对应不同业务的不同类型ID
	 * 
	 * @return
	 */
	public String getBatchNo(Date accountDate, String batchType,
			String rootInstCd) {
		List<SettleBatchManage> rtnList = null;
		SettleBatchManage batchManageQuery = new SettleBatchManage();
		SettleBatchManage batchManage = new SettleBatchManage();
		String rtnSrc = "";
		ArithUtil arithUtil = new ArithUtil();

		batchManageQuery.setRootInstCd(rootInstCd);
		batchManageQuery.setAccountDate(accountDate);
		batchManageQuery.setBatchType(batchType);

		rtnList = settlementManager.selectbatchno(batchManageQuery);

		if (rtnList.size() == 0) {
			rtnSrc = "";
		} else if (rtnList.size() == 1) {
			batchManage = rtnList.get(0);
			try {
				rtnSrc = batchManage.getBatchCons()
						+ this.pad(batchManage.getBatchNum(),
								batchManage.getBatchNumLen(), 1);
			} catch (Exception e) {
				rtnSrc = "";
			}
			batchManage.setBatchId(null);
			batchManage.setCreatedTime(null);
			batchManage.setUpdatedTime(null);
			batchManage.setBatchSubNo(arithUtil.doubleAdd(
					batchManage.getBatchSubNo(), "1"));
			batchManage.setAccountDate(accountDate);
			batchManage.setBatchNum(arithUtil.doubleAdd(
					batchManage.getBatchNum(), "1"));
			settleBatchManageManager.saveSettleBatchManage(batchManage);
		} else {
			batchManage = rtnList.get(1);
			try {
				rtnSrc = batchManage.getBatchCons()
						+ this.pad(batchManage.getBatchNum(),
								batchManage.getBatchNumLen(), 1);
			} catch (Exception e) {
				rtnSrc = "";
			}
			batchManage.setBatchNum(arithUtil.doubleAdd(
					batchManage.getBatchNum(), "1"));
			settleBatchManageManager.saveSettleBatchManage(batchManage);
		}

		if (!"".equals(rtnSrc)) {

		}

		return rtnSrc;
	}

	/**
	 * @Description: 获取账期，格式化入参，返回格式化参数
	 * @param accountDate
	 *            账期
	 * @param fromat
	 *            账期格式化
	 * @param step
	 *            步长
	 * @param returnType
	 *            返回类型
	 * @param returnFromat
	 *            根据returnFromat格式化返回值
	 * @return
	 * @throws Exception
	 * @autor CLF
	 */
	public Object getAccountDate(String accountDate, String fromat, int step,
			String returnType, String returnFromat) throws Exception {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		if (!StringUtils.isEmpty(fromat)) {
			formatter1 = new SimpleDateFormat(fromat);
		}
		SimpleDateFormat formatter = new SimpleDateFormat(returnFromat);

		c.setTime(formatter1.parse(accountDate));
		c.add(Calendar.DAY_OF_MONTH, step);

		if ("Date".equals(returnType)) {
			return c.getTime();
		} else {
			return formatter.format(c.getTime());
		}
	}

	/**
	 * 取得日期
	 * 
	 * @param
	 * @return
	 */
	public Object getAccountDate(String accountDate, String fromat, int step,
			String returnType) throws Exception {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter = new SimpleDateFormat(fromat);

		c.setTime(formatter1.parse(accountDate));
		c.add(Calendar.DAY_OF_MONTH, step);

		if ("Date".equals(returnType)) {
			return c.getTime();
		} else {
			return formatter.format(c.getTime());
		}
	}

	/**
	 * 取得会计系统账期
	 * 
	 * @param
	 * @return
	 */
//	public Object getAccountDate(String fromat, int step, String returnType)
//			throws Exception {
//		ParameterInfoQuery keyList = new ParameterInfoQuery();
//		keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
//		keyList.setParameterType(SettleConstants.ACCOUNTDATE_TYPE);
//		keyList.setStatusId(1);
//
//		List<ParameterInfo> parameterInfo = parameterInfoManager
//				.queryList(keyList);
//		if (parameterInfo.size() > 0 && parameterInfo != null) {
//			try {
//				Calendar c = Calendar.getInstance();
//				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
//				SimpleDateFormat formatter = new SimpleDateFormat(fromat);
//				c.setTime(formatter1.parse(parameterInfo.get(0)
//						.getParameterValue()));
//				c.add(Calendar.DAY_OF_MONTH, step);
//				if ("Date".equals(returnType)) {
//					return c.getTime();
//				} else {
//					return formatter.format(c.getTime());
//				}
//			} catch (Exception e2) {
//				logger.error("计算账期异常！" + e2.getMessage());
//				throw new SettleException("计算账期异常！");
//			}
//		} else {
//			logger.error("系统账期取得异常！");
//			throw new SettleException("系统账期取得异常！");
//		}
//	}

	/**
	 * 取得账户系统一期账期
	 * 
	 * @param
	 * @return
	 */
	public Object getAccountDate(String fromat, int step, String returnType)
			throws Exception {
		ParameterInfoOldQuery keyList = new ParameterInfoOldQuery();
//		keyList.setParameterCode(SettleConstants.ACCOUNTDATE_OLD);
		keyList.setParameterType(SettleConstants.ACCOUNTDATE_TYPE_OLD);

		List<ParameterInfoOld> parameterInfo = parameterInfoOldManager
				.queryList(keyList);
		if (parameterInfo.size() > 0 && parameterInfo != null) {
			try {
				Calendar c = Calendar.getInstance();
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat formatter = new SimpleDateFormat(fromat);
				c.setTime(formatter1.parse(parameterInfo.get(0)
						.getParameterValue()));
				c.add(Calendar.DAY_OF_MONTH, step);
				if ("Date".equals(returnType)) {
					return c.getTime();
				} else {
					return formatter.format(c.getTime());
				}
			} catch (Exception e2) {
				logger.error("计算账期异常！" + e2.getMessage());
				throw new SettleException("计算账期异常！");
			}
		} else {
			logger.error("系统账期取得异常！");
			throw new SettleException("系统账期取得异常！");
		}
	}
	
	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) throws Exception {
		int bytesum = 0;
		int byteread = 0;
		File oldfile = new File(oldPath);
		if (oldfile.exists()) { // 文件存在时
			InputStream inStream = new FileInputStream(oldPath); // 读入原文件
			OutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[1444];
			int length;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread; // 字节数 文件大小
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
		}
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}
	}

	/**
	 * 替换字符串
	 * 
	 * @param str
	 *            String 字符串
	 * @param restr
	 *            String 替换字符串
	 * @return String
	 */
	public String nvl(Object str, String restr) throws Exception {
		if (str == null || "".equals(str)) {
			return restr;
		}
		return str.toString();
	}

	/**
	 * 补充字符串
	 * 
	 * @param str
	 *            String 字符串
	 * @param len
	 *            String 长度
	 * @param len
	 *            int step
	 * @return String
	 */
	public String pad(String str, String len, int i) throws Exception {
		int srtint;
		int lenint;
		int strlenint;
		if (isValidInt(str)) {
			srtint = Integer.parseInt(str);
			srtint = srtint + i;
			str = String.valueOf(srtint);
			lenint = Integer.parseInt(len);
			strlenint = str.length();
			if (strlenint > lenint) {
				return str.substring(0, lenint);
			} else {
				for (int ii = 0; ii < lenint - strlenint; ii++) {
					str = "0" + str;
				}
				return str;
			}
		} else {
			return str;
		}
	}

	/***
	 * 判断 String 是否是 int
	 * 
	 * @param input
	 * @return
	 */
	public boolean isValidInt(String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 生成批次号 
	 * 
	 * @param head
	 *            批次号头部
	 * @return 如：返回32位长度的唯一ID
	 * @throws Exception
	 */
	public synchronized String createBatchNo(String head) {
		String batchNo = "";
		try {
//			batchNo=UUID.randomUUID().toString();
//			batchNo=batchNo.replace("-", "");
			batchNo = UUID22.getUUID22();
		} catch (Exception e) {
			logger.error(">>> >>> 异常:生成批次号", e);
		}
		logger.error(">>> >>> 生成批次号:" + batchNo);
		return batchNo;
	}
	public static void main(String[] args) {
		for(int i = 0; i < 100000; i ++) {
			System.out.println(UUID22.getUUID22());
		}
	}
	/**
	 * 判断会计系统, 系统日切是否成功
	 * @return
	 */
	public boolean cutDayIsSuccess4mtkernel() {
		logger.info(">>> >>> >>> 开始 判断会计系统, 系统日切是否成功");
		boolean isSuccess = false;
		List<ParameterInfo> parameterInfo = null;
		/**
		 * 判断日中是否正常结束
		 */
		ParameterInfoQuery keyList = new ParameterInfoQuery();
		keyList.setParameterType(SettleConstants.DAYEND_TYPE);
		keyList.setParameterCode(SettleConstants.DAYEND);
		keyList.setStatusId(1);
		
		try {
			parameterInfo = parameterInfoManager.queryList(keyList);
			isSuccess = "0".equals(parameterInfo.get(0).getParameterValue());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("判断会计系统, 系统日切异常:", e);
		}
		logger.info("<<< <<< <<< 结束 判断会计系统, 系统日切是否成功 :" + isSuccess);
		return isSuccess;
	}
	/**
	 * 判断  账户一期, 系统日切是否成功
	 * @return
	 */
	public boolean cutDayIsSuccess4Account() {
		logger.info(">>> >>> >>> 开始 判断 账户一期, 系统日切是否成功");
		boolean isSuccess = false;
		List<ParameterInfoOld> parameterInfo = null;
		/**
		 * 判断日中是否正常结束
		 */
		ParameterInfoOldQuery keyList = new ParameterInfoOldQuery();
//		keyList.setParameterType(SettleConstants.DAYEND_TYPE_OLD);
		keyList.setParameterCode(SettleConstants.DAYEND_OLD);
//		keyList.setStatus(1);
		
		try {
			parameterInfo = parameterInfoOldManager.queryList(keyList);
			isSuccess = "0".equals(parameterInfo.get(0).getParameterValue());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("判断  账户一期, 系统日切异常:", e);
		}
		logger.info("<<< <<< <<< 结束 判断 账户一期, 系统日切是否成功 :" + isSuccess);
		return isSuccess;
	}
	/***
	 * 获取紧急联系人手机号
	 * @return
	 */
	public String getEmergencyContactMobile() {
		logger.info(">>> >>> >>> 开始 获取紧急联系人手机号");
		//手机号字符串, 多个手机号用','隔开
		String mobiles = "";
		List<SettleParameterInfo> emergencyContactMobile;
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		//查询紧急联系人手机号
		query.setParameterType(SettleConstants.PARAMETER_TYPE_EMERGENCY_CONTACT_MOBILE);
		//状态为1是有效
		query.setStatusId(1);
		emergencyContactMobile = settleParameterInfoManager.queryList(query);
		//迭代查询结果
		Iterator<SettleParameterInfo> mobileIter = emergencyContactMobile.iterator();
		//如有多组紧急联系人则将手机号进行拼接
		while(mobileIter.hasNext()) {
			String theMobile = mobileIter.next().getParameterValue();
			mobiles += "," + theMobile;
		}
		mobiles = mobiles.substring(1);
		logger.info("<<< <<< <<< 结束 获取紧急联系人手机号");
		return mobiles;
	}
	/**
	 * 获取当前系统的时间,返回值Date类型
	 * @return
	 */
	public Date getCurrentTime(){
		Date currentTime = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		try {
			currentTime =  df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentTime;
	}
	
	/**
	 * 将GBK编码的字符串转成UTF-8编码的字符串
	 * 如果输入的就是UTF-8编码的字符串则原样输出
	 * 作用:解决有些系统返回清算的字符串中有用GBK编码导致清算这边乱码的问题
	 * @param inString
	 * @author yy
	 * @return
	 */
	public String TransGBKString2UTF8(String str){
		try {
			if(StringUtils.isNotBlank(str)){
				String gbkStr = new String(str.getBytes("GBK"));
				if(gbkStr.length()<str.length()){
			   		str = new String(str.getBytes("GBK"),"UTF-8");
				}
			 }
		}catch (Exception e) {
			e.printStackTrace();
		}
        return str;
	}
	
}
