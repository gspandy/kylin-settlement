package com.rkylin.settle.logic;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransInvoiceManager;
import com.rkylin.settle.manager.SettleTransSummaryManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;
import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.util.RkylinMailUtil;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.TxtReader;
import com.rkylin.settle.util.toExcel;

/***
 * 代收付数据库层逻辑
 * @author youyu
 *
 */
@Component("dsfLogic")
public class DsfLogic extends BasicLogic {
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;			//交易信息Manager
	
	@Autowired
	private SettleTransSummaryManager settleTransSummaryManager;			//汇总表Manager
	
	@Autowired
	private SettleTransInvoiceManager settleTransInvoiceManager;			//结算表Manager
	
	@Autowired
	private SettlementLogic settlementLogic;						//清算逻辑, 待优化
	
	@Autowired
	private SettlementUtil settlementUtil;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ActiveMQQueue queueDest;
	
	@Autowired
	Properties userProperties;
	
	@Autowired
	SettleParameterInfoManager settleParameterInfoManager;
	
	/***
	 * 获取代收付交易
	 * @param dFlags 代收付标记位的Integer数组
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String,Object>> getDsfTransDetail(Map<String, Object> map) throws Exception {
		List<Map<String,Object>> dsfDetailList = null;
		try{
			dsfDetailList = settleTransDetailManager.selectDsfTransInfo(map);
		}catch(Exception e){
			logger.error(">>>>>getDsfTransDetail方法异常！！！！！！！！！！");
			throw new Exception(e.getMessage());
		}
		return dsfDetailList;
	}
	
	/***
	 * 获取代收付交易count
	 * @param dFlags 代收付标记位的Integer数组
	 * @return
	 * @throws Exception 
	 */
	public int getDsfTransDetailCount(Map<String, Object> map) throws Exception {
		int result = 0;
		try{
			result = settleTransDetailManager.selectDsfTransCount(map);
		}catch(Exception e){
			logger.error(">>>>>getDsfTransDetailCount方法异常！！！！！！！！！！");
		}
		return result;
	}
	
	/**
	 * 插入或更新汇总表中的数据
	 * @throws Exception 
	 */
	public void insertTransSummery(List<SettleTransSummary> transSummeryList) throws Exception {
		logger.info(">>> >>> >>> 向汇总表中插入数据 ... ...");
		try{
			if (transSummeryList !=null && transSummeryList.size() > 0) {
				for (SettleTransSummary subbean : transSummeryList) {
					settleTransSummaryManager.saveSettleTransSummary(subbean);
				}
			}
		}catch(Exception e){
			logger.error(">>>>>insertTransSummery方法发生异常!!!!!!!!"+e.getMessage());
			throw new Exception(e.getMessage());
		}
	
	}
	
	/**
	 * 往结算表中插入数据
	 * @throws Exception 
	 */
	public void insertTransInvoice(List<SettleTransInvoice> transInvoiceList) throws Exception {
		try{
			if (transInvoiceList !=null && transInvoiceList.size() > 0) {
				logger.info(">>> >>> >>> 向结算表中插入信息 ... ...");
				for (SettleTransInvoice subBean : transInvoiceList) {
					settleTransInvoiceManager.saveSettleTransInvoice(subBean);
				}
			}else{
				logger.info(">>> >>> >>> transInvoiceList为空，不用向结算表中插入信息.....");
			}
		}catch(Exception e){
			logger.error(">>>>>>insertTransInvoice方法发生异常！！！！！"+e.getMessage());
			throw new Exception(e.getMessage());
		}
	
	}
	/**
	 * 更新交易表的DFLAG的值
	 * @param transDetailList
	 * @throws Exception 
	 */
	public void updateSettleTransDetail(List<SettleTransDetail> transDetailList) throws Exception  {
		logger.info(">>> >>> >>> 更新 更新交易表的DFLAG的值 ... ...");
		Map<String, Object> mapStatus1 = new HashMap<String, Object>();
		Map<String, Object> mapStatus99 =  new HashMap<String, Object>();
		List<Integer> idList1 = new ArrayList<Integer>();
		List<Integer> idList99 = new ArrayList<Integer>();
		/*
		 * 
		 */
		if (transDetailList == null || transDetailList.size() < 1) {
			logger.info(">>>>>>> 更新交易表的DFLAG的值, transDetailList == null || transDetailList.size() < 1");
			return;
		}
		/*
		 * 
		 */
		for (SettleTransDetail subbean : transDetailList) {
			if(subbean.getDflag() == SettleConstants.STATUSID_1) {
				idList1.add(subbean.getTransDetailId());
			} else if(subbean.getDflag() == 99) {
				idList99.add(subbean.getTransDetailId());
			} else {
				logger.error(">>>>>>> 更新交易表的DFLAG的值,id:"+ subbean.getTransDetailId() +" Unknow subbean.getDflag()");
			}
		}
		/*
		 * 
		 */
		if(idList1.size() > 0) {
			mapStatus1.put("dflag", SettleConstants.STATUSID_1);
			mapStatus1.put("obligate1", "");
			mapStatus1.put("idList", idList1);
			try {
				settleTransDetailManager.batchUpdateSettleTransDetailDflag(mapStatus1);
			} catch (Exception e) {
				logger.error(">>>>>>>updateSettleTransDetail方法异常！！！！！"+e.getMessage(), e);
			}
		}
		/*
		 * 
		 */
		if(idList99.size() > 0) {
			mapStatus99.put("dflag", 99);
			mapStatus99.put("obligate1", "绑卡信息为空");
			mapStatus99.put("idList", idList99);
			try {
				settleTransDetailManager.batchUpdateSettleTransDetailDflag(mapStatus99);
			} catch (Exception e) {
				logger.error(">>>>>>>updateSettleTransDetail方法异常！！！！！"+e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 查询结算表List
	 * @throws Exception 
	 */
	public List<SettleTransInvoice> queryTransInvoiceList(Map<String,Object> paramMap) throws Exception {
		logger.info(">>> >>> >>> 查询结算表中的信息 ... ...");
		SettleTransInvoiceQuery query = null;
		List<SettleTransInvoice> invoiceList = null;
		try{
			query = new SettleTransInvoiceQuery();
			if(paramMap.get("dataSource") != null){
				query.setDataSource((Integer)paramMap.get("dataSource"));
			}
			if(paramMap.get("statusId") !=null){
				query.setStatusId((Integer)paramMap.get("statusId"));
			}
	    	if(paramMap.get("requestNo") !=null){
	    		query.setRequestNo((String)paramMap.get("requestNo"));
	    	}
	    	if(paramMap.get("dataSources") != null){
				query.setDataSources((Integer[])paramMap.get("dataSources"));
			}
	    	if(paramMap.get("orderTypes") != null){
				query.setOrderTypes((Integer[])paramMap.get("orderTypes"));
			}
	    	if(paramMap.get("rootInstCd") != null){
				query.setRootInstCd((String)paramMap.get("rootInstCd"));
			}
	    	if(paramMap.get("rootInstCds") != null){
				query.setRootInstCds((String [])paramMap.get("rootInstCds"));
			}
	    	if(paramMap.get("ids") != null){
				query.setIds((Integer [])paramMap.get("ids"));
			}
	    	invoiceList = settleTransInvoiceManager.queryList(query);
		}catch(Exception e){
			logger.error(">>>>>queryTransInvoiceList方法异常！！！！");
			throw new Exception(e.getMessage());
		}
		return invoiceList;
	}
	
	/**
	 * 查询结算表
	 * @throws Exception 
	 */
	public List<SettleTransInvoice> queryTransInvoices(Map<String,Object> paramMap) throws Exception {
		logger.info(">>> >>> >>> 查询结算表中的信息 ... ...");
		SettleTransInvoiceQuery query = new SettleTransInvoiceQuery();
		try{
			if(paramMap !=null && paramMap.size()>0){
				if(paramMap.get("requestNo") !=null){
					query.setRequestNo((String)paramMap.get("requestNo"));
				}
		    	if(paramMap.get("limit") != null){
		    		query.setLimit((Integer)paramMap.get("limit"));
		    	}
		    	if(paramMap.get("funcCodes") !=null){
		    		query.setFuncCodes((String[]) paramMap.get("funcCodes"));
				}
		     	if(paramMap.get("dataSource") !=null){
		     		query.setDataSource((Integer)paramMap.get("dataSource"));
		    	}
		     	if(paramMap.get("StatusId") !=null ){
		     		query.setStatusId((Integer) paramMap.get("StatusId"));
		     	}
		    	if(paramMap.get("rootInstCd") !=null){
		    		query.setRootInstCd((String)paramMap.get("rootInstCd"));
		    	}
			}
		}catch(Exception e){
			logger.error(">>>>>>>>queryTransInvoices异常！！！！！！！！");
			throw new Exception(e.getMessage());
		}
		return settleTransInvoiceManager.queryList(query);
	}
	
	/**
	 * 根据条件查询结算表记录数
	 * @throws Exception 
	 */
	public int queryTotalInvoiceByExample(SettleTransInvoiceQuery query) throws Exception {
		logger.info(">>> >>> >>> 查询结算表中的信息 ... ...");
		int total = 0;
		try{
			total =  settleTransInvoiceManager.queryTotalByExample(query);
		}catch(Exception e){
			logger.error(">>>>>>queryTotalInvoiceByExample方法异常！！！！！");
			throw new Exception(e.getMessage());
		}
		return total;
	}
	/**
	 * 【杏仁】现金贷 批量转账文件 通过待结算信息生成结算文件
	 * @param settleTransInvoiceList
	 * @throws Exception
	 */
	public void createXrXjdExcel(List<SettleTransInvoice> settleTransInvoiceList) throws Exception {
		String fileName = this.createXrXjdFileName();
		this.createXrXjdExcel(settleTransInvoiceList, fileName);
	}
	/**
	 * 【杏仁】现金贷 批量转账文件 通过待结算信息生成结算文件
	 * @param settleTransInvoiceList
	 * @param fileName
	 * @throws Exception
	 */
	public void createXrXjdExcel(List<SettleTransInvoice> settleTransInvoiceList, String fileName) throws Exception {
		logger.info(">>> >>> >>> 开始: 【杏仁】现金贷 批量转账文件 通过待结算信息生成结算文件");
		File filePath = new File(SettleConstants.XR_XJD_PATH);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		filePath = new File(SettleConstants.MODELS_PATH);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		//提示信息
		Map<String, String> rtnMap = new HashMap<String, String>();
		//文件目录
		String drectory = SettleConstants.XR_XJD_PATH;
		//金额格式
		DecimalFormat amountFormat = new DecimalFormat();
		//日期格式yyyyMMdd
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//日期格式yyyy-MM-dd HH:mm:s
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//excel文件对象
		File file = null;
		//excl工具类入参
		Map configMap = new HashMap();
		//文件头信息
		List<Map<String, Object>> headList = new ArrayList<Map<String, Object>>();
		//文件内容信息
		List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
		//数据实例(行)
		Map<String, String> detailMap;
		//编辑excl工具类入参
		//模板文件位置
		configMap.put("MODEL_FILE", SettleConstants.MODELS_PATH + SettleConstants.XR_XJD_MODEL);
		//生成文件位置
		configMap.put("FILE", drectory + fileName);
		//excl的sheet页名称 非空串 & 唯一
		configMap.put("SHEET", "转账模板");
		//内容开始行
		configMap.put("firstDetailRow", "1");
		//内容样式行
		configMap.put("firstStyleRow", "1");
		//sheet页数
//		configMap.put("multiSheet", "0");
		//sheet开始索引
//		configMap.put("multiSheet_index", "0");
		//定义文件头信息
		configMap.put("REPORT-HEAD", headList);
		//结算信息pojo
		SettleTransInvoice sti;
		//编辑文件内容
		/*  收款账户列
			收款户名列
			转账金额列
			备注列
			收款银行列
			收款银行支行列
			收款省/直辖市列
			收款市县列
			转出账号/卡
			转账模式
		*/
		int cnt = 0;
		String bank = "";
		String province = "";
		String city = "";
		if(settleTransInvoiceList != null && settleTransInvoiceList.size() > 0) {
			for(int i = 0; i < settleTransInvoiceList.size(); i ++) {
				//数据对象
				sti = settleTransInvoiceList.get(i);
				//行
				detailMap = new HashMap<String, String>();
				detailMap.put("F_1", sti.getAccountNo());//收款账户列
				detailMap.put("F_2", sti.getAccountName());//收款户名列
				detailMap.put("F_3", amountFormat.format((new BigDecimal(sti.getAmount())).divide(new BigDecimal(100)).doubleValue()));//转账金额列
				detailMap.put("F_4", "出借款项" + sti.getOrderNo());//备注列
				
				bank = returnBPC(SettleConstants.PARAMETER_TYPE_CMB_B,sti.getBankCode());
				if ("深圳平安银行".equals(bank)) {
					detailMap.put("F_5", "平安银行");//收款银行列
				} else if ("中国光大银行".equals(bank)) {
					detailMap.put("F_5", "光大银行");//收款银行列
				} else {
					detailMap.put("F_5", bank);//收款银行列
				}
				detailMap.put("F_6", sti.getOpenBankName());//收款银行支行列
				province = sti.getProvince();
				province = province.trim().replace("省", "").replace("市", "").replace("自治区", "");
				province = returnBPC(SettleConstants.PARAMETER_TYPE_CMB_P,province);
				if ("全国".equals(province)) {
					province = "";
				}
				detailMap.put("F_7", province);//收款省/直辖市列
//				city = sti.getCity();
//				city = city.trim().replace("县", "").replace("地区", "").replace("市", "");
//				if (city.length() > 1) {
//					if ("州".equals(city.substring(city.length()-1,city.length()))) {
//						city = city.trim().replace("州", "自治州");
//					}
//				}
//				city = returnBPC(SettleConstants.PARAMETER_TYPE_CMB_C,city);
				detailMap.put("F_8", city);//收款市县列
				detailMap.put("F_9", "6214861007878899");//转出账号/卡
				detailMap.put("F_10", "实时");//转账模式
				fileList.add(detailMap);
			}
			cnt = settleTransInvoiceList.size();
		} else {
			detailMap = new HashMap<String, String>();
			detailMap.put("F_1", "交易笔数为0");//收款账户列
			fileList.add(detailMap);
			configMap.put("FILE", drectory + "笔数为0_"+fileName);
			logger.info(">>> >>> >>> 待结算信息0条 SettleTransInvoiceList is Null or SettleTransInvoiceList.size() < 1");
		}
		//excl工具类创建文件并编辑
		toExcel.WriteDetailSheet(fileList ,configMap ,null);
		//获取excl工具类生成的文件对象
		file = new File(configMap.get("FILE").toString());
		if (!file.exists()) {
			return;
		}
		logger.info("<<< <<< <<< 结束: 【杏仁】现金贷 批量转账文件 通过待结算信息生成结算文件");
		String batchNo = fileName.substring(13, 16);
//		String[] to = new String[]{"liqi@rkylin.com.cn","lijinna@rongcapital.cn"};
//		String[] cc = new String[]{"caoyang@rkylin.com.cn","sunruibin@rongcapital.cn","liqiuying@rongcapital.cn"};
		
		Date accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", 0, "Date");
		String nwDate = sdf.format(accountDate);

		this.sendMail("[杏仁]现金贷支付文件-"+nwDate+"-"+batchNo, "[杏仁]现金贷支付文件-"+nwDate + ",共有交易笔数：" +cnt, "XRXJD", file);
		
		logger.info(">>> >>> 文件上传至ROP服务器 ... ...");
		rtnMap = settlementUtil.setFileFromROP(
								86,
								batchNo,
								accountDate,
								configMap.get("FILE").toString(), 
								userProperties.getProperty("XR_PUBLIC_KEY"), 
								0, // 1为私钥，其他为公钥
								userProperties.getProperty("FSAPP_KEY"), 
								userProperties.getProperty("FSDAPP_SECRET"), 
								userProperties.getProperty("FSROP_URL"));
		Set<Entry<String, String>> entrySet = rtnMap.entrySet();
		for(Entry<String, String> entry : entrySet) {
			logger.error(">>> 调用文件上传至ROP服务器方法返回值 -　" + entry.getKey() + ": " + entry.getValue());
		}
	}
	private String returnBPC(String type,String param) {
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		//参与分润的功能编码
		String parameterType = type;
		//查询parameter表的query对像
		query.setParameterType(parameterType);
		query.setParameterCode(param);
		query.setStatusId(1);
		//查询对象
		List<SettleParameterInfo> parameterInfoList = settleParameterInfoManager.queryList(query);
		
		if(parameterInfoList == null || parameterInfoList.size()==0){
			return param;
		}
		return parameterInfoList.get(0).getParameterValue();
	}
	
	/**
	 * 
	 * @return
	 */
	private String createXrXjdFileName() throws Exception {
		String fileName = SettleConstants.XR_XJD_MODEL;
		String followNum = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", 0, "Date");
		String accountDateStr = sdf.format(accountDate);
		/**
		 * 如果不传入batch及batch为null, 才采用工具类获取批次号
		 */
		followNum = settlementLogic.getBatchNo(accountDate, "xr_xjd", "M000029");
		fileName = fileName.replace("{YMD}", accountDateStr).replace("{NUM}", followNum);
		return fileName;
	}
	
	/**
	 * 将代收付结果写入MQ
	 * @throws Exception 
	 */
	public void writeToMQ(List<OrderDetail> orderDetailList) throws Exception {
		if (orderDetailList==null || orderDetailList.size()==0) {
			logger.info("-------代收付调用清结算代收付结果接收接口,结束<<<<<<<<<<<<<<参数为空<<<<<<<<<<<<<<<<");
			return ;
		}else{
			logger.info(">>>>>>进入writeToMQ方法");
		}
		try{
	 		Destination destination = (Destination)queueDest;
	        for(final OrderDetail orderDetail : orderDetailList){
	        	logger.info("代收付推送回批量接口信息,每条分别是orderNo="+orderDetail.getOrderNo()+",statusId="+orderDetail.getStatusId()+",errMsg="+orderDetail.getErrMsg());
	        	jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
	        	jmsTemplate.send(destination, new MessageCreator() {
	               public Message createMessage(Session session) throws JMSException {  
	                   MapMessage message = session.createMapMessage();
	                   logger.info(">>>创建MQ的message对象成功");
	                   try{
	                	   if(orderDetail !=null){
		                	   message.setString("requestNo", orderDetail.getRequestNo());
			                   message.setString("orderNo", orderDetail.getOrderNo());
			                   message.setString("orgCode", orderDetail.getOrgCode());//上游的入网商户号
			                   if(orderDetail.getStatusId() !=null){
			                	   message.setInt("statusId", orderDetail.getStatusId().intValue());
			                   }
			                   String errMsg = orderDetail.getErrMsg();
			                   if(StringUtils.isNotBlank(errMsg)){//如果代收付返回的errMsg是用GBK编码，则对它进行转码成UTF-8
			                	   String gbkStr = new String (errMsg.getBytes("GBK"));
			                	   if(gbkStr.length()<errMsg.length()){
			                		   errMsg = new String (errMsg.getBytes("GBK"),"UTF-8");
			           			   }
			                   }
			                   message.setString("errorMsg", errMsg);
		                   }
	                   }catch(Exception e){
	                	   logger.error("给MQ的对象message组织信息的时候异常！！！！requestNo="+orderDetail.getRequestNo()+",orderNo="+orderDetail.getOrderNo()+",statusId="+orderDetail.getStatusId()+",errorMsg="+orderDetail.getErrMsg()+e.getMessage());
	                   }
	                   logger.info(">>>组织message信息完成即将return,requestNo="+orderDetail.getRequestNo()+",orderNo="+orderDetail.getOrderNo()+",statusId="+orderDetail.getStatusId()+",errorMsg="+orderDetail.getErrMsg());
	                   return message;
	               }  
	           }); 
			}
		}catch(Exception e){
			logger.error(">>>writeToMQ发生异常！！！！！！！！"+e.getMessage());
			throw new Exception(e.getMessage());
		}
    }
	
	/**
	 * 【杏仁】现金贷 回盘文件 解析
	 * @param settleTransInvoiceList
	 * @param fileName
	 * @throws Exception
	 */
	public int readResFile(File resFile) throws Exception {
		List<OrderDetail> mqList = new ArrayList<OrderDetail>();
		OrderDetail mqBean = null;
		if (!resFile.exists()) {
			logger.info(">>>现金贷 回盘文件 解析 -- 没有结果文件");
            return 1;
		}
		
		TxtReader txtReader = new TxtReader();
		List<Map> fileListSub;
		
//		txtReader.setEncode("UTF-8");
		fileListSub = txtReader.txtreader(resFile , SettleConstants.DEDT_SPLIT2);
		
		if (fileListSub == null || fileListSub.size() ==0) {
			logger.info(">>>现金贷 回盘文件 解析 -- 文件内容为空");
			return 1;
		}
		logger.info(">>>现金贷 回盘文件 解析 -- 文件："+resFile.getName() + ",交易笔数："+fileListSub.size());
		Map fileMapSub = new HashMap();
		String statusId = null;
		String remark = null;
		String orderno = null;
		
		for (int j=1;j<fileListSub.size();j++) {
			// 去除标题行
			// 批次号码
			// 币种
			// 总笔数
			// 处理中笔数
			// 成功笔数
			// 失败笔数
			// 总金额
			// 成功金额
			// 失败金额
			// 交易日期
			// 付款人账号
			// 付款人名称
			// 收款人账号
			// 收款人名称
			// 汇路
			// 开户行
			// 状态
			// 金额
			// 备注
			mqBean = new OrderDetail();
			fileMapSub = fileListSub.get(j);
			statusId = fileMapSub.get("L_16").toString().trim();
			statusId = statusId.replace("\"", "").trim();
			orderno = fileMapSub.get("L_18").toString().replace("\"", "").trim();//.toString().substring(6).trim();
			orderno = orderno.replace("出借款项", "").trim();
			if (fileMapSub.get("L_19") == null) {
				remark = "";
			} else {
				remark = fileMapSub.get("L_19").toString().trim().replace("\"", "");
			}

			if ("失败".equals(statusId)) {
				mqBean.setStatusId(13);
				continue;
			} else if ("已汇出".equals(statusId)) {
				mqBean.setStatusId(15);
			} else {
				return 2;
			}
			
			mqBean.setErrMsg(remark);
			mqBean.setOrderNo(orderno);
			mqList.add(mqBean);
		}
		
		if (mqList.size()>0) {
			this.writeToMQ(mqList);
		}
		return 0;
	}
}