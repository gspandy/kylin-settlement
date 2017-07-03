package com.rkylin.settle.logic;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.SettleFileColumnManager;
import com.rkylin.settle.manager.SettleFileManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.pojo.SettleFileColumnQuery;
import com.rkylin.settle.pojo.SettleFileQuery;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.TxtReader;

/***
 * 清分系统通用对账业务文件处理逻辑
 * @author Yang
 */
@Component("fileBaseLogic")
public class FileBaseLogic extends BasicLogic {
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;		//'清算'交易信息Manager
	@Autowired
	private SettlementUtil settlementUtil;							//清算工具类
	@Autowired
	private SettleFileManager settleFileManager;					//文件属性信息Manager
	@Autowired
	private SettleFileColumnManager settleFileColumnManager;		//文件列信息Manager
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;	//清算属性表
	
	/**
	 * 读取上游对账文件内容
	 * @param fileInfo 文件属性
	 * @param accountDate 账期
	 * @return 文件内容
	 * @throws Exception
	 */
	protected List<Map> readUpFile(SettleFile fileInfo,String accountDate) throws Exception {
		// 上游对账文件存放路径
	    String parentFileName = (String)settlementUtil.getAccountDate(accountDate,"yyyyMMdd",1,"","yyyyMMdd");
		String filePath = SettleConstants.FILE_PATH + parentFileName + File.separator;
		String filename = fileInfo.getFilePrefix().replace("{YMD}", accountDate)+"."+fileInfo.getFilePostfix();
		
		File file = new File(filePath+filename);
		if (!file.exists()) {
		    logger.error(">>>>>>>上游对账文件不存在，读取文件路径为："+file.getAbsolutePath());
			return null;
		}
	    logger.info(">>>>>>>读取文件路径为："+file.getAbsolutePath());
		
		//读取上游对账文件
		TxtReader txtReader = new TxtReader();

		txtReader.setEncode(fileInfo.getFileEncode());
		List<Map> fileList = txtReader.txtreader(file , fileInfo.getSplitStr());
		
		return fileList;
	}
	/***
	 * 获取文件上传目录
	 * @param accountDate	账期
	 * @return
	 */
	protected String getUploadPath(Date accountDate) throws Exception {
		logger.info(">>> >>> >>> 获取文件上传目录");
		//日期格式对账
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//获取string形式的账期
		logger.info(">>> >>> >>> 获取文件上传目录:"+accountDate);
		String dateStr = sdf.format(accountDate);
		logger.info(">>> >>> >>> 获取文件上传目录:"+dateStr);
		//拼接过去上传目录 SettleConstants.FILE_UP_PATH:约定目录 + dateStr:账期string +　 File.separator：动态获取系统分隔符
		String path = SettleConstants.FILE_UP_PATH + dateStr + File.separator;
		logger.info(">>> >>> >>> 获取文件上传目录:"+path);
		//声明实例化目录
		File filePath = new File(path);
		if (!filePath.exists()) {//此目录不存在
			//创建目录
			filePath.mkdirs();
		}
		return path;
	}
	/***
	 * 生成上传文件名称
	 * @param fileInfo		文件属性Bean
	 * @param accountDate	账期
	 * @return
	 */
	protected String getUploadFileName(SettleFile fileInfo, Date accountDate) {
		logger.info(">>> >>> >>> 生成上传文件名称");
		//账期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//文件名称buff
		StringBuffer fileNameBuff = new StringBuffer();
		//前缀
		String prefix = fileInfo.getFilePrefix(); 
		//账期string
		String dateStr = sdf.format(accountDate);
		//后缀, 定义文件类型
		String postfix = fileInfo.getFilePostfix();
		//文件名称字符串拼接
		fileNameBuff.append(prefix);
		fileNameBuff.append("_");
		fileNameBuff.append(dateStr);
		fileNameBuff.append(".");
		fileNameBuff.append(postfix);
		return fileNameBuff.toString();
	}
	/***
	 * 获取文本输出类writeTxt方法的入参configMap[设计信息映射]
	 * @param headFileList 头文件信息
	 * @param tailFileList 尾文件信息
	 * @param path
	 * @return
	 */
	protected Map<String, Object> getConfigMap(List<Map<String, String>> headFileList,List<Map<String, String>> tailFileList, String path) {
		logger.info(">>> >>> >>> 设计信息映射");
		//设计信息映射
		Map<String, Object> configMap = new HashMap<String, Object>();
		//head文件信息不为null, 设置head文件信息
		if(headFileList != null) configMap.put("REPORT-HEAD", headFileList);
		//tail文件信息不为null, 设置tail文件信息
		if(tailFileList != null) configMap.put("REPORT-TAIL", tailFileList);
		//设置文件上传路径
		if(path != null && !path.trim().equals("")) {
			configMap.put("FILE", path);
		} else {
			throw new SettleException("传入参数 path[文件上传路径]不能为空字符串或者null!");
		}
		return configMap;
	}
	/***
	 * 获取下游对账交易信息
	 * @param fileInfo
	 * @param accountDate
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, Object>> getCollateTransDetail(SettleFile fileInfo, Date accountDate, String marchantCodes) throws Exception {
		logger.info(">>> >>> >>> 获取下游对账交易信息");
		//账期Date对象编辑成yyyyMMdd 00:00:00 格式的字符串
		String accountDateStr = this.getAccountDateStr(accountDate);
		//创建查询query的map
		Map<String, Object> map = new HashMap<String, Object>();
		//机构号 
		map.put("marchantCodes", marchantCodes);
		//功能编码
		String funcCodesStr = fileInfo.getFuncCodes();
		String[] funcCodesArr = funcCodesStr.split(",");
		map.put("funcCodes", funcCodesArr);
		//账期的字符串形式
		map.put("accountDate", accountDateStr);
		//文件与交易信息funcCodes的关系[1:include包含, 2:difference差集]
		map.put("funcRelation", fileInfo.getFuncRelation());
		//交易状态
		String statusIdsStr = fileInfo.getTransStatusIds();
		String[] statusIdsArr = statusIdsStr.split(",");
		map.put("statusIds", statusIdsArr);
		//订单类型0交易,1结算单, 对账就对结算单
		map.put("orderType", 0);
		//调用manager进行查询
		List<Map<String, Object>> settleTransDetailList = settleTransDetailManager.selectCollateTransInfo(map);
		return settleTransDetailList;
	}
	/***
	 * 编辑 账期Date对象编辑 成 yyyyMMdd 00:00:00 格式的字符串
	 * @param accountDate 账期Date对象
	 * @return yyyyMMdd 00:00:00格式的账期
	 */
	protected String getAccountDateStr(Date accountDate) throws Exception {
		logger.info(">>> >>> >>> 获取下游对账交易信息 编辑 账期Date对象编辑 成 yyyyMMdd 00:00:00 格式的字符串");
		//日期格式对账
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//将账期的Date对象形式 转换成 String对象形式
		String accountDateStr = sdf.format(accountDate) + " 00:00:00";
		return accountDateStr;
	}
	/**
	 * @Description: 获取文件属性信息
	 * @param marchantCode 机构号
	 * @param readType     交易类型-网关支付:01, 代收付:02
	 * @param payChannelId 渠道号 01通联、02支付宝
	 * @param fileActive   文件作用1：写入，2：读取
	 * @return
	 * @throws Exception
	 * @author CLF
	 */
	protected Map<String, SettleFile> getSettleFile(String marchantCode, String readType, String payChannelId, int fileActive) throws Exception {
		logger.info(">>> >>> >>> 获取文件属性信息");
		//文件信息Map
		Map<String, SettleFile> fileInfoMap  = new HashMap<String, SettleFile>();
		//创建查询文件属性信息query
		SettleFileQuery query = new SettleFileQuery();
		query.setRootInstCd(marchantCode);
		query.setReadType(readType);
		if (null != payChannelId) {
			query.setPayChannelId(payChannelId);	
		}
		//文件作用1:'写入'信息文件模板,2:'读取'信息文件模板
		query.setFileActive(fileActive);
		query.setStatusId(1);
		//查询对账文件属性信息
		List<SettleFile> settleFiles = settleFileManager.queryList(query);
		if(settleFiles.size() < 1) {//未获取到文件信息, 抛出异常!
			throw new SettleException("查询结果条数为0, 请核对传入参数!");
		} else {
			//遍历
			Iterator<SettleFile> iter = settleFiles.iterator();
			while(iter.hasNext()) {
				//声明对账文件对象
				SettleFile fileInfo = iter.next();
				//判断文件信息类型
				if("body".equals(fileInfo.getFileType())) {//body:文件主要内容
					fileInfoMap.put("body", fileInfo);
				} else if("head".equals(fileInfo.getFileType())) {//head:文件头
					fileInfoMap.put("head", fileInfo);
				} else if("tail".equals(fileInfo.getFileType())) {//tail:文件尾
					fileInfoMap.put("tail", fileInfo);
				} else {//异常
					throw new SettleException("文件信息 查询 异常: '"+ fileInfo.getFileType() +"'是未定义的文件类型!, 文件类型必须是[body, head, tail]之中的一个");
				}
			}
		}
		return fileInfoMap;
	}
	/***
	 * 获取下游文件列信息
	 * @param fileSubId 文件属性信息关联列信息的字段
	 * @return 文件列信息
	 * @throws Exception
	 */
	protected List<SettleFileColumn> getSettleFileColumn(Integer fileSubId) throws Exception {
		logger.info(">>> >>> >>>　查询文件'列'信息");
		//初始化查询条件
		SettleFileColumnQuery query = new SettleFileColumnQuery();
		query.setFileSubId(fileSubId);
		query.setStatusId(1);	//可用
		List<SettleFileColumn> settleFileColumns = settleFileColumnManager.queryList(query);
		return settleFileColumns;
	}
	/***
	 * 获取传入机构号[merchantCode]在下游中包含的其他子机构号,如果没有返回自身
	 * @param merchantCode
	 * @return
	 * @throws ParseException
	 */
	protected String getDownMerchantCode(String merchantCode) throws ParseException{
    	SettleParameterInfoQuery keyList1 =  new SettleParameterInfoQuery();
    	keyList1.setParameterType(SettleConstants.PARAMETER_DOWN_MERCHANT);
    	keyList1.setParameterCode(merchantCode);
    	List<SettleParameterInfo> settleParameterInfo = settleParameterInfoManager.queryList(keyList1);
    	if(settleParameterInfo.size() > 0){
    		return settleParameterInfo.get(0).getParameterValue();
    	}else{
    		logger.info("parameter_info中 未配置下游机构号对应关系, 默认返回 传入机构号");
    		return "'"+ merchantCode +"'";
    	}
	}
	/***
	 * 交易金额汇总
	 * @param settleTransDetailList
	 * @return
	 */
	protected Long getTotalAmount(List<Map<String, Object>> settleTransDetailList) {
		return getTotalCustom("AMOUNT", settleTransDetailList);
	}
	/***
	 * 最终对账金额汇总
	 * @param settleTransDetailList
	 * @return
	 */
	protected Long getTotalFCAmount(List<Map<String, Object>> settleTransDetailList) {
		return getTotalCustom("FC_AMOUNT", settleTransDetailList);
	}
	/***
	 * 自定义金额汇总
	 * @param settleTransDetailList
	 * @return
	 */
	protected Long getTotalCustom(String key, List<Map<String, Object>> settleTransDetailList) {
		BigDecimal amount = null;
		BigDecimal amountS = new BigDecimal("0");
		Iterator<Map<String, Object>> iter = settleTransDetailList.iterator();
		
		while(iter.hasNext()) {
			Map<String, Object> item = iter.next();
			amount = new BigDecimal(String.valueOf(item.get(key)));
			amountS = amountS.add(amount);
		}	
		return amountS.longValue();
	}
	

	/**
	 * 读取上游对账文件内容
	 * @param fileInfo 文件属性
	 * @param accountDate 账期
	 * @return 文件内容
	 * @throws Exception
	 */
	protected List<Map> readCmbcUpFile(SettleFile fileInfo,String accountDate) throws Exception {
		// 上游对账文件存放路径
	    String parentFileName = (String)settlementUtil.getAccountDate(accountDate,"yyyyMMdd",0,"","yyyyMMdd");
		String filePath = SettleConstants.FILE_PATH + parentFileName + File.separator;
		String filename = fileInfo.getFilePrefix().replace("{YMD}", accountDate)+"."+fileInfo.getFilePostfix();
		
		File file = new File(filePath+filename);
		if (!file.exists()) {
		    logger.error(">>>>>>>上游对账文件不存在，读取文件路径为："+file.getAbsolutePath());
			return null;
		}
	    logger.info(">>>>>>>读取文件路径为："+file.getAbsolutePath());
		
		//读取上游对账文件
		TxtReader txtReader = new TxtReader();

		txtReader.setEncode(fileInfo.getFileEncode());
		List<Map> fileList = txtReader.txtreader(file , fileInfo.getSplitStr());
		
		return fileList;
	}
}
