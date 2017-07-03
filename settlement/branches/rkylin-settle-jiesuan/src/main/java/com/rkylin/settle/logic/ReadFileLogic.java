package com.rkylin.settle.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransAccountQuery;

/***
 * 清分系统通用对账业务文件处理逻辑
 * @author Yang
 */
@Component("readFileLogic")
public class ReadFileLogic extends FileBaseLogic {
    @Autowired
    SettleTransAccountManager settleTransAccountManager;
	/**
	 * @Description: 读取上游对账文件
	 * @param marchantCode 机构号
	 * @param readType     交易类型-网关支付:01, 代收付:02
	 * @param invoicedate  账期
	 * @param payChannelId 渠道号 01通联、02支付宝
	 * @param specialColumnMap 特殊信息列[不属于交易信息的内容]
	 * @return
	 * @throws Exception
	 * @author CLF
	 */
	public Map<String, Object> readCollateFile(String marchantCode, String readType, String invoicedate, String payChannelId, Map<String, Object> specialColumnMap) throws Exception {
		logger.info("读取上游对账文件 ————————————START————————————"+new Date());
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		
    	try {
    	    String accountDate = invoicedate.replace("-", "");
			logger.info("取得的账期为"+ accountDate);
			//文件属性信息
			SettleFile fileInfo = null;
			//文件属性信息Map 包含 body head tail
			Map<String, SettleFile> fileInfoMap = null;
			//head文件属性信息
			SettleFile headFileInfo = null;
			//foot文件属性信息
			SettleFile tailFileInfo = null;
			//文件列信息
			List<SettleFileColumn> settleFileColumns = null;
			//head文件列信息
			List<SettleFileColumn> headFileColumns = null;
			//tailt文件列信息
			List<SettleFileColumn> tailFileColumns = null;
			//文件需要的信息结构体
			List<Map> fileList = null;
			//head文件需要的信息结构体
//			List<Map<String, String>> headFileList = null;
			//tail文件需要的信息结构体
//			List<Map<String, String>> tailFileList = null;
			try {
				//获取 文件属性信息
				fileInfoMap = super.getSettleFile(marchantCode, readType, payChannelId, 2);
				if(fileInfoMap == null) {
					logger.info(">>> 获取 文件属性信息 0条");
					return this.editResultMap(rtnMap, "0", "获取 文件属性信息 0条");
				}
				if(fileInfoMap.get("body") != null) {
					fileInfo = fileInfoMap.get("body");
				} else {
					logger.info(">>> 获取 body文件属性信息 0条");
					return this.editResultMap(rtnMap, "0", "获取 body文件属性信息 0条");
				}
				//如果DB文件信息里没有head文件信息,文件中就存在head文件内容
				if(fileInfoMap.get("head") != null) headFileInfo = fileInfoMap.get("head");
				//如果DB文件信息里没有tail文件信息,文件中就存在tail文件内容
				if(fileInfoMap.get("tail") != null) tailFileInfo = fileInfoMap.get("tail");
			} catch (Exception e) {
				logger.error(">>> 获取 文件属性信息 异常");
				logger.error(">>> 异常信息:" + e.getMessage());
				e.printStackTrace();
				return this.editResultMap(rtnMap, "-1", "获取 文件属性信息 异常");
			}

			try {
				//获取 '文件列' 信息
				settleFileColumns = this.getSettleFileColumn(fileInfo.getFileSubId());
				if(settleFileColumns == null || settleFileColumns.size() <= 0) {
					logger.info(">>> 获取 文件列信息 0条");
					return this.editResultMap(rtnMap, "0", "获取 文件列信息 0条");
				}
				if(headFileInfo != null) {//如果head文件信息不为null,获取head文件关联'列'信息
					headFileColumns = this.getSettleFileColumn(headFileInfo.getFileSubId());
					if(headFileColumns == null || headFileColumns.size() <= 0) {
						logger.info(">>> 获取 head文件列信息 0条");
						return this.editResultMap(rtnMap, "0", "获取 head文件列信息 0条");
					}
				}
				if(tailFileInfo != null) {//如果tail文件信息不为null,获取tail文件关联'列'信息
					tailFileColumns = this.getSettleFileColumn(tailFileInfo.getFileSubId());
					if(tailFileColumns == null || tailFileColumns.size() <= 0) {
						logger.info(">>> 获取 foot文件列信息 0条");
						return this.editResultMap(rtnMap, "0", "获取 foot文件列信息 0条");
					}
				}
			} catch (Exception e) {
				logger.error(">>> 获取 文件列信息 异常");
				logger.error(">>> 异常信息:" + e.getMessage());
				e.printStackTrace();
				return this.editResultMap(rtnMap, "-1", "获取 文件列信息 异常");
			}
			
			try {
				//获取 上游对账交易信息
				if(payChannelId.equals(SettleConstants.PAY_CHANNEL_ID_CMBC) && ",AgentPay,AgentPay_DL,AgentPay_FDL,Refund,Refund_DL,Refund_FDL,".indexOf(","+readType+",")!=-1){
					fileList = super.readCmbcUpFile(fileInfo, accountDate);
	        	}else{
	        		fileList = super.readUpFile(fileInfo, accountDate);
	        	}
				
				if (null == fileList || fileList.isEmpty()) {
					logger.info(">>> 读入上游对账文件内容为空");
					return this.editResultMap(rtnMap, "0", "读入上游对账文件内容为空");
				}
			} catch (Exception e) {
				logger.error(">>> 读取上游对账交易信息  异常");
				logger.error(">>> 异常信息:" + e.getMessage());
				e.printStackTrace();
				return this.editResultMap(rtnMap, "-1", "读取上游对账交易信息  异常");
			}
			
			List<Map<String,Object>> insertList = null;
			try {
				//编辑 文件需要的信息结构体
				insertList = this.createFile4List(fileList, settleFileColumns, headFileColumns, tailFileColumns, fileInfo, headFileInfo, tailFileInfo, specialColumnMap);
				if (null == insertList || insertList.size() == 0) {
					logger.info(">>> 编辑DB需要的信息结构体为空");
					return this.editResultMap(rtnMap, "0", "编辑DB需要的信息结构体为空");
				}
			} catch (Exception e) {
				logger.error(">>> 编辑 DB需要的信息结构体 异常");
				logger.error(">>> 异常信息:" + e.getMessage());
				e.printStackTrace();
				return this.editResultMap(rtnMap, "-1", "编辑 DB需要的信息结构体 异常");
			}
			
			logger.info(">>>>>>上游对账信息录入数据库开始");
//			插入数据库SETTLE_TRANS_ACCOUNT
			if((SettleConstants.PAY_CHANNEL_ID_YB).equals(payChannelId)){
				//易宝的进行编辑
				String strTransAmount = null;
				BigDecimal transAmount = null;
				String strFeeAmount = null;
				BigDecimal feeAmount = null;
				String strObligate1 = null;
				BigDecimal obligate1 = null;
				for (Map<String,Object> map : insertList) {
					 map.put("STATUS_ID", "1");
					 strTransAmount = (String)map.get("TRANS_AMOUNT");
					 transAmount = new BigDecimal(StringUtils.isBlank(strTransAmount)?"0":strTransAmount);
				     map.put("TRANS_AMOUNT",transAmount.multiply(new BigDecimal("100")).setScale(0));
				     strFeeAmount = (String)map.get("FEE_AMOUNT");
				     feeAmount = new BigDecimal(StringUtils.isBlank(strFeeAmount)?"0":strFeeAmount);
				     map.put("FEE_AMOUNT",feeAmount.multiply(new BigDecimal("100")).setScale(0));
				     if((SettleConstants.ACCOUNT_TYPE_YB_AUTH).equals(readType)||(SettleConstants.ACCOUNT_TYPE_YB_CHANGECARD).equals(readType)){
				    	 strObligate1 = (String)map.get("OBLIGATE1");
				    	 obligate1 = new BigDecimal(StringUtils.isBlank(strObligate1)?"0":strObligate1);
					     map.put("OBLIGATE1",obligate1.multiply(new BigDecimal("100")).setScale(0));
				     }
				     if((SettleConstants.ACCOUNT_TYPE_YB_PAYMENT).equals(readType)||(SettleConstants.ACCOUNT_TYPE_YB_REFUND).equals(readType)){
				    	 if(map.get("OBLIGATE2")!=null && (((String)(map.get("OBLIGATE2"))).indexOf("SUCCESS")!=-1 || ((String)(map.get("OBLIGATE2"))).indexOf("成功")!=-1)){
				    		 this.insertAndUpdateSettleTransAccount(map);
				    	 }
				     }else{
				    	 this.insertAndUpdateSettleTransAccount(map);
				     }
					
				}
			}else{
				for (Map<String,Object> map : insertList) {
				    map.put("STATUS_ID", "1");
					this.insertAndUpdateSettleTransAccount(map);
				}
			}
			
			logger.info(">>>>>>上游对账信息录入数据库结束");
    	} catch (Exception z) {
			logger.error("上游数据读入失败，请联系相关负责人"+z.getMessage());
			rtnMap.put("errCode", "0068");
			rtnMap.put("errMsg", "上游数据读入失败，请联系相关负责人");
			throw new SettleException("上游数据读入失败，请联系相关负责人");
    	}
		logger.info("读取上游对账文件 ————————————END————————————"+new Date());
		return rtnMap;
	}
	
	/**
	 * 编辑文件数据
	 * @param fileList 上游对账文件内容
	 * @param settleFileColumns 明细内容
	 * @param headFileColumns 头内容
	 * @param tailFileColumns 尾内容
	 * @param headFileInfo 头信息
	 * @param tailFileInfo 尾信息
	 * @param specialColumnMap 特殊列信息
	 * @return 符合插入DB的数据
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> createFile4List(List<Map> fileList,List<SettleFileColumn> settleFileColumns
			,List<SettleFileColumn> headFileColumns,List<SettleFileColumn> tailFileColumns, SettleFile fileInfo, SettleFile headFileInfo
			,SettleFile tailFileInfo, Map<String, Object> specialColumnMap) throws Exception {
	    logger.info(">>>>>>>>> 编辑文件数据TO结构体 [交易信息&特殊信息]--开始--");
		//编辑文件数据TO结构体
		List<Map<String,Object>> rtnList = new ArrayList<Map<String,Object>>();
		Map<String,String> tempMap = null;
		Map<String,Object> settleTransAccount = new HashMap<String,Object>();
		boolean detailFlg = false;
		
		for (int i = 1; i <= fileList.size(); i ++) {
			tempMap = fileList.get(i-1);
			detailFlg = false;
			// 表头读入
			if (null != headFileInfo) {
				if (headFileColumns !=null && headFileColumns.size()!=0) {
					if (i <= Integer.parseInt(headFileInfo.getTransStatusIds())) {
						detailFlg = true;
						// 判断读入FLG
						if (headFileInfo.getUploadKeyFlg() != 0) {
							settleTransAccount = this.file4ListDetail(tempMap, headFileColumns, "head", null);
						} else {
							continue;
						}
					} 
				}
			}
			// 表尾读入
			if (null != tailFileInfo) {
				if (tailFileColumns != null && tailFileColumns.size()!=0) {
					if (i > fileList.size()-Integer.parseInt(tailFileInfo.getTransStatusIds())) {
						detailFlg = true;
						// 判断读入FLG
						if (tailFileInfo.getUploadKeyFlg() != 0) {
							settleTransAccount = this.file4ListDetail(tempMap, tailFileColumns, "bottom", null);
						} else {
							continue;
						}
					}
				}
			}
			// 明细读入
			if (!detailFlg) {
				settleTransAccount = this.file4ListDetail(tempMap,settleFileColumns,"content", specialColumnMap, fileInfo);
			}
			if (settleTransAccount !=null && settleTransAccount.size() > 0) {
				rtnList.add(settleTransAccount);
			}
		}
		logger.info(">>>>>>>>>编辑文件数据TO结构体--结束-- ，结构体数量：" + rtnList.size());
		return rtnList;
	}
	/**
	 * @Description: 共通：DB数据格式编辑
	 * @param fileMap 上游对账文件内容
     * @param fileLIst 文件列属性
     * @param fileLayout 文件布局：头部、尾部、主题明细
	 * @param specialColumnMap  特殊列key和value
	 * @return 编辑一行入DB数据
	 * @throws Exception
	 * @autor CLF
	 */
	private Map<String,Object> file4ListDetail(Map<String,String> fileMap,List<SettleFileColumn> fileLIst,String fileLayout, Map<String, Object> specialColumnMap) throws Exception  {
		return this.file4ListDetail(fileMap, fileLIst, fileLayout, specialColumnMap, null);
	}
	/**
	 * @Description: 共通：DB数据格式编辑
	 * @param fileMap 上游对账文件内容
     * @param fileLIst 文件列属性
     * @param fileLayout 文件布局：头部、尾部、主题明细
	 * @param specialColumnMap  特殊列key和value
	 * @return 编辑一行入DB数据
	 * @throws Exception
	 * @autor CLF
	 */
	private Map<String,Object> file4ListDetail(Map<String,String> fileMap,List<SettleFileColumn> fileLIst,String fileLayout, Map<String, Object> specialColumnMap, SettleFile fileInfo) throws Exception  {
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		String columnTitle = null;
		String columnTitle_db = null;
		SimpleDateFormat format_d = null;
		SimpleDateFormat format_db = null;
		for (SettleFileColumn fileBean : fileLIst) {
			// DB列名
			columnTitle_db = fileBean.getFileColumnKeyCode();
			// 表里没有文件和DB字段的对应关系，跳过
			if (StringUtils.isBlank(columnTitle_db)) {
				continue;
			}
			//列值
			String valueStr = "";
			// 文件列名
			columnTitle = fileBean.getFileColumnTitle();
		
            if(0 == fileBean.getIsSpecialColumn()){
                valueStr = fileMap.get(columnTitle).toString();
            }else if(1 == fileBean.getIsSpecialColumn()){
                /**
                 * 如果是特殊列，则从specialColumnMap获取列值
                 */
                if(specialColumnMap != null && !specialColumnMap.isEmpty()){
                    valueStr = specialColumnMap.get(columnTitle_db).toString();
                }else{
                    throw new SettleException("specialColumnMap  指定的特殊列信息 为null!");
                }
			} else if(2 == fileBean.getIsSpecialColumn()){
			    valueStr = fileMap.get(columnTitle).toString();
				/*
                 * 场景:
                 * 		通联批量代收付交易处理中.
                 * 		某批次只要有1条交易状态是处理中,多渠道系统对应批次的全部交易状态都是12.
                 * 		但是,通联对账文件中除了处理中的交易状态是'空字符串'以外,其他的都是终态结果例如'0000'.
                 * 影响:
                 * 		对账结果出现伪错账
                 * 原因:
                 * 		多渠道此批次交易状态全部是处理中'12'
                 * 		对账文件中此批次交易有处理中'12'和终态例如'16'等等
                 * 以下是解决此问题的逻辑
                 * */
                boolean isProcessing4TL = 
                		fileInfo != null 
                		&& SettleConstants.PAY_CHANNEL_ID_TL.equals(fileInfo.getPayChannelId()) 
                		&& SettleConstants.ACCOUNT_TYPE_GENERATE.equals(fileInfo.getReadType())
                		&& fileMap.get("L_6").isEmpty() 
                		&& fileMap.get("L_7").isEmpty();
			    //文件中交易状态为空串 认识此次交易为处理中
			    if(valueStr.isEmpty() || isProcessing4TL) {
			    	valueStr = "12";
			    } else {//如果不为空
			    	String mapVal = "";
				    /*	特殊列值为2，表示为通联交易状态--清结算交易状态转换,Map值如下：
				     * 	成功 key=0000|4000,value=16
			         * 	处理中 key=2000|2001|2003|2005|2007|2008|0003|0014,value=12
			         * 	其他 key=other, value=14
				     */
	                Map<String, String> map = (Map<String, String>) specialColumnMap.get(columnTitle_db);
	                Iterator<String> keyIt = map.keySet().iterator();
	                while(keyIt.hasNext()){
	                	mapVal = "";
	                    String key = keyIt.next();
	                    //判断key中是否包含file中的交易状态，如果包含则转换赋值
	                    if(key.contains(valueStr)){
	                        valueStr = mapVal = map.get(key);
	                        break;
	                    }
	                }  
	                if(StringUtils.isBlank(mapVal)){
	                    valueStr = map.get("other");
	                }
			    }
			}
			// 编辑属性
			int dataType = fileBean.getDataType();
			
			switch(dataType) {
			//string和number不做格式处理
			case 1 :
				//格式化字符串
				break; 
			case 2 : 
				//存入'行'value-2代表金额类型为元，需要乘以100
			    BigDecimal big = new BigDecimal(valueStr);
				valueStr = String.valueOf(big.multiply(new BigDecimal(100)).longValue());
				break;
			//date按对应'格式'转化为string
			case 3 :
				//验证是否为空默认账期
				if(StringUtils.isEmpty(valueStr)) {
					valueStr = specialColumnMap.get("SETTLE_TIME") + " 00:00:00";
					break;
				}
				//格式
				String format = fileBean.getDataFormat();
				//转化
				format_d = new SimpleDateFormat(format); 
				//转化
				format_db = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				//存入'行'value
				valueStr = format_db.format(format_d.parse(valueStr));
				break;
			default ://未查询到对应dataType抛出异常 []
				throw new SettleException("异常:此列信息的数据类型未定义, '"+ dataType +"'未定义, 数据类型必须是[ 1:字符串, 2:数字, 3:日期]其中之一");
			}
			// 处理结果MAP
            rtnMap.put(columnTitle_db, valueStr);
		}
		if (rtnMap.size() >0) {
			return rtnMap;
		}
		return null;
	}
	/***
	 * 更新&插入对上游交易信息
	 * @param settleBalanceEntryList
	 * 对账结果List, 暂存对账结果信息
	 */
	public void insertAndUpdateSettleTransAccount(Map<String,Object> settleTransAccountMap) {
		SettleTransAccountQuery query = new SettleTransAccountQuery();
		query.setOrderNo(String.valueOf(settleTransAccountMap.get("ORDER_NO")));
		if(settleTransAccountMap.get("TRANS_FLOW_NO") != null && !"".equals(settleTransAccountMap.get("TRANS_FLOW_NO"))){
			query.setTransFlowNo(String.valueOf(settleTransAccountMap.get("TRANS_FLOW_NO")));
		}
		query.setMerchantCode(String.valueOf(settleTransAccountMap.get("MERCHANT_CODE")));
		query.setTransType(String.valueOf(settleTransAccountMap.get("TRANS_TYPE")));
		
		List<SettleTransAccount> selectList = new ArrayList<SettleTransAccount>();
		
		selectList = settleTransAccountManager.queryList(query);
		Integer count = selectList.size();
		
		if(count < 1) {
			settleTransAccountManager.insertSelectivebyMap(settleTransAccountMap);
		} else {
			if (selectList.get(0).getStatusId() != SettleConstants.STATUS_COLLATE_SUCCESS) {
				settleTransAccountManager.updateByMap(settleTransAccountMap);
			}
		}
	}
}	
