package com.rkylin.settle.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.file.txt.TxtWriter;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.util.SettlementUtil;

/***
 * 清分系统通用对账业务文件处理逻辑
 * @author Yang
 */
@Component("writeFileLogic")
public class WriteFileLogic extends FileBaseLogic {
	@Autowired
	private SettlementUtil settlementUtil;							//清算工具类
	@Autowired
	private Properties userProperties;								//user.properties文件
	@Autowired
	private SettlementLogic settlementLogic;						//清算逻辑, 待优化
	
	/***
	 * 作成下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @param specialColumnMap		特殊信息列[不属于交易信息的内容]
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createCollateFile(String marchantCode, String readType, Map<String, Object> specialColumnMap, String marchantCodes) throws Exception {
		return this.createCollateFile(marchantCode, readType, null, specialColumnMap, marchantCodes);
	}
	/***
	 * 作成下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @param specialColumnMap		特殊信息列[不属于交易信息的内容]
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createCollateFile(String marchantCode, String readType, Date accountDate, Map<String, Object> specialColumnMap, String marchantCodes) throws Exception {
		//提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//'清算'下游对账交易信息
		List<Map<String, Object>> settleTransDetailList = null;
		//文件属性信息
		SettleFile fileInfo = null;
		//head文件属性信息
		SettleFile headFileInfo = null;
		//foot文件属性信息
		SettleFile tailFileInfo = null;
		//文件属性信息Map 包含 body head tail
		Map<String, SettleFile> fileInfoMap = null;
		//文件列信息
		List<SettleFileColumn> settleFileColumns = null;
		//head文件列信息
		List<SettleFileColumn> headFileColumns = null;
		//tailt文件列信息
		List<SettleFileColumn> tailFileColumns = null;
		//文件需要的信息结构体
		List<Map<String, String>> fileList = null;
		//head文件需要的信息结构体
		List<Map<String, String>> headFileList = null;
		//tail文件需要的信息结构体
		List<Map<String, String>> tailFileList = null;
		
		try {
			//获取 文件属性信息
			fileInfoMap = this.getSettleFile(marchantCode, readType, null, 1);
			if(fileInfoMap == null) {
				logger.info(">>> 获取 文件属性信息 0条");
				return this.editResultMap(resultMap, "0", "获取 文件属性信息 0条");
			}
			if(fileInfoMap.get("body") != null) {
				fileInfo = fileInfoMap.get("body");
			} else {
				logger.info(">>> 获取 body文件属性信息 0条");
				return this.editResultMap(resultMap, "0", "获取 body文件属性信息 0条");
			}
			//如果DB文件信息里没有head文件信息,文件中就存在head文件内容
			if(fileInfoMap.get("head") != null) headFileInfo = fileInfoMap.get("head");
			//如果DB文件信息里没有tail文件信息,文件中就存在tail文件内容
			if(fileInfoMap.get("tail") != null) tailFileInfo = fileInfoMap.get("tail");
		} catch (Exception e) {
			logger.error(">>> 获取 文件属性信息 异常");
			logger.error(">>> 异常信息:" + e.getMessage());
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取 文件属性信息 异常");
		}
		
		//验证账期
		Date fileDate = null;
		if(accountDate == null) {//如果账期为null, 即是未传入账期
			//从DB中过去账期信息, 账期在DB中每日更新 
			fileDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", -1, "Date");	
			accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", fileInfo.getDateStep(), "Date");	
		} else {
			fileDate = accountDate;
		}
		
		try {
			//获取 '文件列' 信息
			settleFileColumns = this.getSettleFileColumn(fileInfo.getFileSubId());
			if(settleFileColumns == null || settleFileColumns.size() <= 0) {
				logger.info(">>> 获取 文件列信息 0条");
				return this.editResultMap(resultMap, "0", "获取 文件列信息 0条");
			}
			if(headFileInfo != null) {//如果head文件信息不为null,获取head文件关联'列'信息
				headFileColumns = this.getSettleFileColumn(headFileInfo.getFileSubId());
				if(headFileColumns == null || headFileColumns.size() <= 0) {
					logger.info(">>> 获取 head文件列信息 0条");
					return this.editResultMap(resultMap, "0", "获取 head文件列信息 0条");
				}
			}
			if(tailFileInfo != null) {//如果tail文件信息不为null,获取tail文件关联'列'信息
				tailFileColumns = this.getSettleFileColumn(tailFileInfo.getFileSubId());
				if(tailFileColumns == null || tailFileColumns.size() <= 0) {
					logger.info(">>> 获取 foot文件列信息 0条");
					return this.editResultMap(resultMap, "0", "获取 foot文件列信息 0条");
				}
			}
		} catch (Exception e) {
			logger.error(">>> 获取 文件列信息 异常");
			logger.error(">>> 异常信息:" + e.getMessage());
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取 文件列信息 异常");
		}
		try {
			//获取 '清算'下游对账交易信息
			settleTransDetailList = super.getCollateTransDetail(fileInfo, accountDate, marchantCodes);
			if(settleTransDetailList == null || settleTransDetailList.size() <= 0) {
				logger.info(">>> 获取 '清算'下游对账交易信息  0条 生成文件的内容为空!");
				//return this.editResultMap(resultMap, "0", "获取 '清算'下游对账交易信息  0条");
			}
		} catch (Exception e) {
			logger.error(">>> 获取 '清算'下游对账交易信息  异常");
			logger.error(">>> 异常信息:" + e.getMessage());
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取 '清算'下游对账交易信息  异常");
		}
		/*
		 * start 编辑特殊列映射[可能随业务增加发生变化]
		 */
		specialColumnMap = this.configspecialColumnMap(specialColumnMap, accountDate, settleTransDetailList);
		try {
			//编辑 文件需要的信息结构体
			fileList = this.createList4File(settleFileColumns, settleTransDetailList, specialColumnMap);
			//head文件'列'信息不为null, 编辑head文件需要的信息结构体
			if(headFileColumns != null) headFileList = this.createList4File(headFileColumns, specialColumnMap);
			//tail文件'列'信息不为null, 编辑tail文件需要的信息结构体
			if(tailFileColumns != null) tailFileList = this.createList4File(tailFileColumns, specialColumnMap);
		} catch (Exception e) {
			logger.error(">>> 编辑 文件需要的信息结构体 异常");
			logger.error(">>> 异常信息:" + e.getMessage());
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "编辑 文件需要的信息结构体 异常");
		}
		String path = this.getUploadPath(fileDate);								//文件目录
		logger.info(">>> 获取 文件目录为："+path);
		String uploadFileName = this.getUploadFileName(fileInfo, fileDate);		//文件名称
		String targetFile = path + uploadFileName;									//文件目录+文件名称, 目标文件
		Map<String, Object> configMap = this.getConfigMap(headFileList, tailFileList, targetFile);	//编辑设置映射
		//生成文件到目标位置[文件备份]
		logger.info(">>> >>> 创建本地文件... ...");
		TxtWriter.WriteTxt(fileList, configMap, fileInfo.getSplitStr(), fileInfo.getFileEncode());
		logger.info(">>> >>> 创建本地文件 已完成 !");
		this.editResultMap(resultMap, "1", "文件上传成功!");
		return resultMap;
	}
	
	/***
	 * 上传下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param specialColumnMap		特殊信息列[不属于交易信息的内容]
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> uploadCollateFile(String marchantCode, String readType, Map<String, Object> specialColumnMap, Integer ropFileType) throws Exception {
		return this.uploadCollateFile(marchantCode, readType, null, specialColumnMap, null);
	}
	/***
	 * 上传下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @param specialColumnMap		特殊信息列[不属于交易信息的内容]
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> uploadCollateFile(String marchantCode, String readType, Date accountDate, Map<String, Object> specialColumnMap) throws Exception {
		return this.uploadCollateFile(marchantCode, readType, accountDate, specialColumnMap, null);
	}
	/***
	 * 上传下游对账文件到ROP
	 * @param marchantCode 			机构号
	 * @param readType				交易类型
	 * @param accountDate			账期
	 * @param specialColumnMap		特殊信息列[不属于交易信息的内容]
	 * @param 批次号
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> uploadCollateFile(String marchantCode, String readType, Date accountDate, Map<String, Object> specialColumnMap, String batch) throws Exception {
		//提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//提示信息
		Map<String, String> rtnMap = new HashMap<String, String>();
		//文件属性信息
		SettleFile fileInfo = null;
		//文件属性信息Map 包含 body head tail
		Map<String, SettleFile> fileInfoMap = null;

		//验证账期
		if(accountDate == null) {//如果账期为null, 即是未传入账期
			//从DB中过去账期信息, 账期在DB中每日更新  	
			accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", -1, "Date");	
		}
		
		try {
			//获取 文件属性信息
			fileInfoMap = this.getSettleFile(marchantCode, readType, null, 1);
			if(fileInfoMap == null) {
				logger.info(">>> 获取 文件属性信息 0条");
				return this.editResultMap(resultMap, "0", "获取 文件属性信息 0条");
			}
			if(fileInfoMap.get("body") != null) {
				fileInfo = fileInfoMap.get("body");
			} else {
				logger.info(">>> 获取 body文件属性信息 0条");
				return this.editResultMap(resultMap, "0", "获取 body文件属性信息 0条");
			}
		} catch (Exception e) {
			logger.error(">>> 获取 文件属性信息 异常");
			logger.error(">>> 异常信息:" + e.getMessage());
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取 文件属性信息 异常");
		}

		if (fileInfo.getRopFileType() == null) {
			logger.info(">>> >>> ROP文件类型为空 ... ...");
			this.editResultMap(resultMap, "1", "ROP文件类型为空!");
			return resultMap;
		} else {
			String path = this.getUploadPath(accountDate);								//文件目录
			String uploadFileName = this.getUploadFileName(fileInfo, accountDate);		//文件名称
			String targetFile = path + uploadFileName;									//文件目录+文件名称, 目标文件

			//ROP公钥OR私钥
			String publicOrpirvateKey = userProperties.getProperty(fileInfo.getUploadKeyName());
			//密钥类型 1:私钥, 0:公钥
			Integer flg = fileInfo.getUploadKeyFlg();
			//获取供下游查询的批次号[accountDate:账期, ropBatchNo:ROP批次号 例:10000002, marchantCode:机构号 例:M000001]
			/**
			 * 如果不传入batch及batch为null, 才采用工具类获取批次号
			 */
			if(batch == null) batch = settlementLogic.getBatchNo(accountDate, fileInfo.getRopBatchNo(), marchantCode);
			logger.info(">>> 生成批次号:["+ batch +"]");		
			//文件信息加密并上传到ROP
			logger.info(">>> >>> 文件上传至ROP服务器 ... ...");
			rtnMap = settlementUtil.setFileFromROP(
									fileInfo.getRopFileType(),
									batch,
									accountDate,
									targetFile, 
									publicOrpirvateKey, 
									flg, 
									userProperties.getProperty("FSAPP_KEY"), 
									userProperties.getProperty("FSDAPP_SECRET"), 
									userProperties.getProperty("FSROP_URL"));
			Set<Entry<String, String>> entrySet = rtnMap.entrySet();
			for(Entry<String, String> entry : entrySet) {
				logger.error(">>> 调用文件上传至ROP服务器方法返回值 -　" + entry.getKey() + ": " + entry.getValue());
			}
			this.editResultMap(resultMap, "1", "文件上传成功!");
			return resultMap;
		}
	}
	
	/***
	 * 特殊列的常用值设置
	 * @param specialColumnMap		特殊列Map
	 * @param accountDate			账期
	 * @param settleTransDetailList	交易信息List
	 * @return
	 */
	private Map<String, Object> configspecialColumnMap(Map<String, Object> specialColumnMap, Date accountDate, List<Map<String, Object>> settleTransDetailList) {
		if(Boolean.parseBoolean(String.valueOf(specialColumnMap.get(SettleConstants.SPECIAL_COLUMN_ACCOUNT)))) {//账期
			String key = SettleConstants.SPECIAL_COLUMN_ACCOUNT;
			specialColumnMap.put(key, accountDate);
		}
		if(Boolean.parseBoolean(String.valueOf(specialColumnMap.get(SettleConstants.SPECIAL_COLUMN_TOTAL_AMOUNT)))) {//总金额
			String key = SettleConstants.SPECIAL_COLUMN_TOTAL_AMOUNT;
			specialColumnMap.put(key, this.getTotalAmount(settleTransDetailList));
		}
		if(Boolean.parseBoolean(String.valueOf(specialColumnMap.get(SettleConstants.SPECIAL_COLUMN_TOTAL_FCAMOUNT)))) {//总金额
			String key = SettleConstants.SPECIAL_COLUMN_TOTAL_FCAMOUNT;
			specialColumnMap.put(key, this.getTotalFCAmount(settleTransDetailList));
		}
		if(Boolean.parseBoolean(String.valueOf(specialColumnMap.get(SettleConstants.SPECIAL_COLUMN_TOTAL_COUNT)))) {//总条数
			String key = SettleConstants.SPECIAL_COLUMN_TOTAL_COUNT;
			specialColumnMap.put(key, settleTransDetailList.size());
		}
		return specialColumnMap;
	}
	/***
	 * 创建生成文件内容需要的信息结构体
	 * @param settleFileColumns			文件'列'信息
	 * @param settleTransDetailList		写入对账文件的交易信息
	 * @param specialColumnMap			文件的特殊列信息Map, 此类信息不是从'对账文件的交易信息'获取的, 而是和下游渠道约定的信息, 例如第一列显示'账期'
	 * 									此时, '列'信息中的keyCode就不是'交易信息表'的字段,而是specialColumnMap中的key
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, String>> createList4File(List<SettleFileColumn> settleFileColumns, List<Map<String, Object>> settleTransDetailList, Map<String, Object> specialColumnMap) throws Exception {
		logger.info(">>> >>> >>>　创建生成文件内容需要的信息结构体 [交易信息&特殊信息]");
		//声明日期格式
		SimpleDateFormat sdf = null;
		//声明信息结构体
		List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
		//遍历'写入文件的清算交易信息' [List<Map<String, Object>> settleTransDetailList]
		Iterator<Map<String, Object>> detailListIter = settleTransDetailList.iterator();
		while(detailListIter.hasNext()) {
			//'行'结构
			Map<String, String> fileRow = new HashMap<String, String>();
			//交易信息item
			Map<String, Object> item = detailListIter.next();
			//遍历 文件'列'信息
			Iterator<SettleFileColumn> fileColumnsIter = settleFileColumns.iterator();
			while(fileColumnsIter.hasNext()) {
				//'列'实体
				SettleFileColumn column = fileColumnsIter.next();
				//表头
				String columnTile = column.getFileColumnTitle();
				//交易信息item中对应的字段
				String keyCode = column.getFileColumnKeyCode();
				//交易信息item字段的数据类型[数据类型 1:字符串, 2:数字, 3:日期]
				Integer dataType = column.getDataType();
				//交易信息value
				Object valueObj = null;
				String valueStr = "";
				//判断是否是特殊信息
				if(column.getIsSpecialColumn() == 0) {//普通信息
					valueObj = item.get(keyCode);
				} else {//特殊信息
					if(specialColumnMap != null && specialColumnMap.size() > 0) {
						//获取特殊信息value [从specialColumnMap中获取, specialColumnMap是由调用者传入的约定内容]
						valueObj = specialColumnMap.get(keyCode);
					} else {
						throw new SettleException("specialColumnMap 指定的特殊列信息 为null!");
					}
				}
				if(valueObj == null) {
					valueStr = "";
				} else {
					switch(dataType) {
						//string和number不做格式处理
						case 1 : 
						case 2 : 
							//存入'行'
							valueStr = String.valueOf(valueObj);
							break;
						//date按对应'格式'转化为string
						case 3 : 
							//内容
							Date date = (Date) valueObj;
							//格式
							String format = column.getDataFormat();
							//转化
							sdf = new SimpleDateFormat(format); 
							//存入'行'
							valueStr = sdf.format(date);
							break;
						default ://未查询到对应dataType抛出异常 []
							throw new SettleException("异常:此列信息的数据类型未定义, '"+ dataType +"'未定义, 数据类型必须是[ 1:字符串, 2:数字, 3:日期]其中之一");
					}
				}
				fileRow.put(columnTile, valueStr);
			}
			fileList.add(fileRow);
		}
		return fileList;
	}
	/***
	 * 创建下游对账文件需要的信息结构体
	 * @param settleFileColumns		文件'列'信息
	 * @param specialColumnMap		文件的特殊列信息Map, 此类信息不是从'对账文件的交易信息'获取的, 而是和下游渠道约定的信息, 例如第一列显示'账期'
	 * 								此时, '列'信息中的keyCode就不是'交易信息表'的字段,而是specialColumnMap中的key
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, String>> createList4File(List<SettleFileColumn> settleFileColumns, Map<String, Object> specialColumnMap) throws Exception {
		logger.info(">>> >>> >>>　创建生成文件内容需要的信息结构体 [特殊信息]");
		//声明日期格式
		SimpleDateFormat sdf = null;
		//声明信息结构体
		List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
		//'行'结构
		Map<String, String> fileRow = new HashMap<String, String>();
		//遍历 文件'列'信息
		Iterator<SettleFileColumn> fileColumnsIter = settleFileColumns.iterator();
		while(fileColumnsIter.hasNext()) {
			//'列'实体
			SettleFileColumn column = fileColumnsIter.next();
			//表头
			String columnTile = column.getFileColumnTitle();
			//交易信息item中对应的字段
			String keyCode = column.getFileColumnKeyCode();
			//交易信息item字段的数据类型[数据类型 1:字符串, 2:数字, 3:日期]
			Integer dataType = column.getDataType();
			//交易信息valu
			Object valueObj = null;
			String valueStr = "";
			//判断是否是特殊信息
			if(column.getIsSpecialColumn() == 0) {//普通信息
				throw new SettleException("请调用此方法的'重载', 传入 需要写入文件的'清算'交易信息 !");
			} else {//特殊信息
				if(specialColumnMap != null && specialColumnMap.size() > 0) {
					//获取特殊信息value [从specialColumnMap中获取, specialColumnMap是由调用者传入的约定内容]
					valueObj = specialColumnMap.get(keyCode);
				} else {
					throw new SettleException("specialColumnMap 指定的特殊列信息 为null!");
				}
			}
			
			if(valueObj == null) {
				valueStr = "";
			} else {
				switch(dataType) {
					//string和number不做格式处理
					case 1 : 
					case 2 : 
						//存入'行'value
						valueStr = String.valueOf(valueObj);
						break;
					//date按对应'格式'转化为string
					case 3 : 
						//内容
						Date date = (Date) valueObj;
						//格式
						String format = column.getDataFormat();
						//转化
						sdf = new SimpleDateFormat(format); 
						//存入'行'value
						valueStr = sdf.format(date);
						break;
					default ://未查询到对应dataType抛出异常 []
						throw new SettleException("异常:此列信息的数据类型未定义, '"+ dataType +"'未定义, 数据类型必须是[ 1:字符串, 2:数字, 3:日期]其中之一");
				}
			}
			//存入'行'
			fileRow.put(columnTile, valueStr);
		}
		fileList.add(fileRow);
		return fileList;
	}
}
