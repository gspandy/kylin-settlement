package com.rkylin.settle.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allinpay.ets.client.StringUtil;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettlewebAccountsumCompareManager;
import com.rkylin.settle.pojo.SettlewebAccountsumCompare;
import com.rkylin.settle.pojo.SettlewebAccountsumCompareQuery;
import com.rkylin.settle.service.SettleWebAccountSumCompareService;
import com.rkylin.settle.util.PagerModel;
import com.rkylin.settle.util.StringUtils;
import com.rkylin.settle.util.ToExcel2007S;

@Service("settleWebAccountSumCompareService")
public class SettleWebAccountSumCompareServiceImpl implements SettleWebAccountSumCompareService {
	private static Logger logger = LoggerFactory.getLogger(SettleWebAccountSumCompareServiceImpl.class);
	@Autowired
	private SettlewebAccountsumCompareManager settlewebAccountsumCompareManager;
	/***
	 * 分页条件查询汇总信息表
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettlewebAccountsumCompare> queryPage(SettlewebAccountsumCompareQuery query) {
		//创建分页Model
		PagerModel<SettlewebAccountsumCompare> pagerModel = new PagerModel<SettlewebAccountsumCompare>();
		try {
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settlewebAccountsumCompareManager.queryPage(query));
			pagerModel.setTotal(settlewebAccountsumCompareManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		return pagerModel;
	}
	
	@Override
	public boolean queryAndOutputExcl(HttpServletRequest request, SettlewebAccountsumCompareQuery query) throws Exception {
		List<SettlewebAccountsumCompare> list;
		//工程更目录 + /WEB-INF
		String model_file = request.getSession().getServletContext().getRealPath(File.separator + "WEB-INF");
		//日期格式yyyyMMdd
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//日期格式yyyyMMdd
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//当前时间
		Date date = new Date();
		//当前时间字符串
		String dateStr = sdf.format(date);
		//生成文件目录
		String folderPath = SettleConstants.MY_DOWNLOAD_PATH + dateStr + File.separator;
		//生成文件名称
		String fileName = "SWAccCpr" + date.getTime() + ".xlsx";
		//生成中标识
		String isLoading = " 文件生成中";
		//生成文件目录对象
		File folder = new File(folderPath);
		//excl工具类入参
		Map configMap = new HashMap();
		//数据实例(对象)
		SettlewebAccountsumCompare sac;
		//文件头信息
		List<Map<String, Object>> headList = new ArrayList<Map<String, Object>>();
		//文件内容信息
		List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
		//数据实例(行)
		Map<String, Object> itemMap;
		//数据实例(行)
		Map<String, String> detailMap;
		//金额格式
		DecimalFormat amountFormat = new DecimalFormat();
		amountFormat.applyPattern("##,##0.00");
		//文件不存在时生成 【注:此文件用于提示用户文件正在生成】
		File file = null;
		try { 
			//目录不存在时生成
			if(!folder.exists())  folder.mkdirs();
			//文件不存在时生成 【注:此文件用于提示用户文件正在生成】
			file = new File(folderPath, fileName + isLoading);
			//文件不存在时生成
			if(!file.exists()) file.createNewFile();
			//查询内容数据
			list = settlewebAccountsumCompareManager.queryList(query);
			//编辑文件头信息(表头) 【ROW&COL的索引从0开始】
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "0");
			itemMap.put("VALUE", "ID");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "1");
			itemMap.put("VALUE", "账期");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "2");
			itemMap.put("VALUE", "渠道");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "3");
			itemMap.put("VALUE", "协议");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "4");
			itemMap.put("VALUE", "支付方式");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "5");
			itemMap.put("VALUE", "退款笔数");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "6");
			itemMap.put("VALUE", "退款金额");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "7");
			itemMap.put("VALUE", "支付笔数");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "8");
			itemMap.put("VALUE", "支付金额");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "9");
			itemMap.put("VALUE", "渠道结算额");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "10");
			itemMap.put("VALUE", "手续费");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "11");
			itemMap.put("VALUE", "对账结果");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "12");
			itemMap.put("VALUE", "差额");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "13");
			itemMap.put("VALUE", "备注");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "14");
			itemMap.put("VALUE", "状态");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "15");
			itemMap.put("VALUE", "记录创建时间");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "16");
			itemMap.put("VALUE", "记录更新时间");
			headList.add(itemMap);
			//编辑excl工具类入参
			//模板文件位置
			configMap.put("MODEL_FILE", model_file + File.separator + SettleConstants.MY_DOWNLOAD_MODEL);
			//生成文件位置
			configMap.put("FILE", folderPath + fileName + isLoading);
			//excl的sheet页名称 非空串 & 唯一
			configMap.put("SHEET", "充值对账");
			//内容开始行
			configMap.put("firstDetailRow", "1");
			//内容样式行
			configMap.put("firstStyleRow", "1");
			//sheet页数
			configMap.put("multiSheet", "1");
			//sheet开始索引
			configMap.put("multiSheet_index", "1");
			//定义文件头信息
			configMap.put("REPORT-HEAD", headList);
			//编辑文件内容
			for(int i = 0; i < list.size(); i ++) {
				//数据对象
				sac = list.get(i);
				//行
				detailMap = new HashMap<String, String>();
				detailMap.put("F_1",String.valueOf(sac.getId()));
				detailMap.put("F_2",sdf1.format(sac.getAccountDate()));
				detailMap.put("F_3",StringUtils.isEmpty(sac.getPayChannelId())?"":sac.getPayChannelId());
				detailMap.put("F_4",StringUtils.isEmpty(sac.getMerchantCode())?"":sac.getMerchantCode());
				detailMap.put("F_5",StringUtils.isEmpty(sac.getReadType())?"":sac.getReadType());
				detailMap.put("F_6",String.valueOf(sac.getCount1()));
				detailMap.put("F_7",StringUtil.isEmpty(sac.getAmount1()) ? "0" : "" + amountFormat.format((new BigDecimal(sac.getAmount1())).divide(new BigDecimal(100)).doubleValue()));
				detailMap.put("F_8",String.valueOf(sac.getCount2()));
				detailMap.put("F_9",StringUtil.isEmpty(sac.getAmount2()) ? "0" : "" + amountFormat.format((new BigDecimal(sac.getAmount2())).divide(new BigDecimal(100)).doubleValue()));
				detailMap.put("F_10",StringUtil.isEmpty(sac.getAmount3()) ? "0" : "" + amountFormat.format((new BigDecimal(sac.getAmount3())).divide(new BigDecimal(100)).doubleValue()));
				detailMap.put("F_11",StringUtil.isEmpty(sac.getFeeAmount()) || "null".equals(sac.getFeeAmount()) ? "0" : "" + amountFormat.format((new BigDecimal(sac.getFeeAmount())).divide(new BigDecimal(100)).doubleValue()));
				detailMap.put("F_12",StringUtils.isEmpty(sac.getAccountResult())?"":sac.getAccountResult());
				detailMap.put("F_13",StringUtil.isEmpty(sac.getAmount4()) ? "0" :"" + amountFormat.format((new BigDecimal(sac.getAmount4())).divide(new BigDecimal(100)).doubleValue()));
				detailMap.put("F_14",StringUtils.isEmpty(sac.getRemark())?"":sac.getRemark());
				detailMap.put("F_15",String.valueOf(sac.getStatusId()));
				detailMap.put("F_16",sdf1.format(sac.getCreatedTime()));
				detailMap.put("F_17",sdf1.format(sac.getUpdatedTime()));
				fileList.add(detailMap);
			}
			//删除 提示用户正在生成的文件
			file.delete();
			//excl工具类创建文件并编辑
			ToExcel2007S.WriteDetailSheet(fileList ,configMap ,null);
			//获取excl工具类生成的文件对象
			file = new File(folderPath, fileName + isLoading);
			//copy到用户下载的文件中(File的renameTo不好用, 采用此逻辑)
			FileUtil.copyFile(file, new File(folderPath, fileName));
			//删除原文件
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			if(file != null) file.delete();
		}
		return true;
	}
}
