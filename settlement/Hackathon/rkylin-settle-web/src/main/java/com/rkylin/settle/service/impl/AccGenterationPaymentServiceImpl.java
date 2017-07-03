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

import org.apache.http.HttpRequest;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.GenerationPaymentManager;
import com.rkylin.settle.pojo.GenerationPayment;
import com.rkylin.settle.pojo.GenerationPaymentQuery;
import com.rkylin.settle.service.AccGenterationPaymentService;
import com.rkylin.settle.util.PagerModel;
import com.rkylin.settle.util.StringUtils;
import com.rkylin.settle.util.toExcel2007;

@Service("accGenterationPaymentService")
public class AccGenterationPaymentServiceImpl implements AccGenterationPaymentService {
	private static Logger logger = LoggerFactory.getLogger(AccGenterationPaymentServiceImpl.class);
	@Autowired
	private GenerationPaymentManager generationPaymentManager;
	
	public PagerModel<GenerationPayment> query(GenerationPaymentQuery query) {
		//创建分页Model
		PagerModel<GenerationPayment> pagerModel = new PagerModel<GenerationPayment>();
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
			pagerModel.setList(generationPaymentManager.queryPage(query));
			pagerModel.setTotal(generationPaymentManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	
	public List<GenerationPayment> select(GenerationPaymentQuery query) {
		return generationPaymentManager.queryList(query);
	}

	@Override
	public boolean queryAndOutputExcl(HttpServletRequest request, GenerationPaymentQuery query) throws Exception {
		List<GenerationPayment> list;
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
		String fileName = "accGenPay" + date.getTime() + ".xlsx";
		//生成中标识
		String isLoading = " 文件生成中";
		//生成文件目录对象
		File folder = new File(folderPath);
		//excl工具类入参
		Map configMap = new HashMap();
		//数据实例(对象)
		GenerationPayment genPay;
		//文件头信息
		List<Map<String, Object>> headList = new ArrayList<Map<String, Object>>();
		//文件内容信息
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		//数据实例(行)
		Map<String, Object> itemMap;
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
			list = this.select(query);
			//编辑文件头信息(表头) 【ROW&COL的索引从0开始】
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "0");
			itemMap.put("VALUE", "ID");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "1");
			itemMap.put("VALUE", "用户ID");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "2");
			itemMap.put("VALUE", "业务代码");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "3");
			itemMap.put("VALUE", "管理机构代码");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "4");
			itemMap.put("VALUE", "订单号");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "5");
			itemMap.put("VALUE", "订单类型");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "6");
			itemMap.put("VALUE", "账号");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "7");
			itemMap.put("VALUE", "账号名");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "8");
			itemMap.put("VALUE", "开户行名称");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "9");
			itemMap.put("VALUE", "金额（元）");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "10");
			itemMap.put("VALUE", "发送类型");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "11");
			itemMap.put("VALUE", "状态");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "12");
			itemMap.put("VALUE", "错误编码");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "13");
			itemMap.put("VALUE", "账号属性");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "14");
			itemMap.put("VALUE", "银行代码");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "15");
			itemMap.put("VALUE", "账期");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "16");
			itemMap.put("VALUE", "备注");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "17");
			itemMap.put("VALUE", "记录创建时间");
			headList.add(itemMap);
			itemMap = new HashMap<String, Object>();
			itemMap.put("ROW", "0");
			itemMap.put("COL", "18");
			itemMap.put("VALUE", "记录更新时间");
			headList.add(itemMap);
			//编辑excl工具类入参
			//模板文件位置
			configMap.put("MODEL_FILE", model_file + File.separator + SettleConstants.MY_DOWNLOAD_MODEL);
			//生成文件位置
			configMap.put("FILE", folderPath + fileName + isLoading);
			//excl的sheet页名称 非空串 & 唯一
			configMap.put("SHEET", "失败代付");
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
				genPay = list.get(i);
				//行
				itemMap = new HashMap<String, Object>();
				itemMap.put("F_1",String.valueOf(genPay.getGeneId()));
				itemMap.put("F_2",StringUtils.isEmpty(genPay.getUserId())?"未定义":genPay.getUserId());
				itemMap.put("F_3",StringUtils.isEmpty(genPay.getBussinessCode())?"未定义":genPay.getBussinessCode());
				itemMap.put("F_4",StringUtils.isEmpty(genPay.getRootInstCd())?"未定义":genPay.getRootInstCd());
				itemMap.put("F_5",StringUtils.isEmpty(genPay.getOrderNo())?"未定义":genPay.getOrderNo());
				itemMap.put("F_6",genPay.getOrderType()==2?"提现":genPay.getOrderType()==6?"代付":genPay.getOrderType()==5?"代收":genPay.getOrderType()==7?"贷款还款":genPay.getOrderType()+"(未定义)");
				itemMap.put("F_7",StringUtils.isEmpty(genPay.getAccountNo())?"未定义":genPay.getAccountNo());
				itemMap.put("F_8",StringUtils.isEmpty(genPay.getAccountName())?"未定义":genPay.getAccountName());
				itemMap.put("F_9",StringUtils.isEmpty(genPay.getOpenBankName())?"未定义":genPay.getOpenBankName());
				//金额格式化
				itemMap.put("F_10",amountFormat.format((new BigDecimal(genPay.getAmount())).divide(new BigDecimal(100)).doubleValue()));
				itemMap.put("F_11","1".equals(String.valueOf(genPay.getSendType()))?"支付失败":"0".equals(String.valueOf(genPay.getSendType()))?"支付成功":"支付中");
				itemMap.put("F_12",String.valueOf(genPay.getStatusId()));
				itemMap.put("F_13",StringUtils.isEmpty(genPay.getErrorCode())?"未定义":genPay.getErrorCode());
				itemMap.put("F_14","1".equals(genPay.getAccountProperty())?"对公":"对私");
				itemMap.put("F_15",StringUtils.isEmpty(genPay.getBankCode())?"未定义":genPay.getBankCode());
				itemMap.put("F_18",sdf1.format(genPay.getAccountDate()));
				itemMap.put("F_17",StringUtils.isEmpty(genPay.getRemark())?"未定义":genPay.getRemark());
				itemMap.put("F_16",sdf1.format(genPay.getCreatedTime()));
				itemMap.put("F_19",sdf1.format(genPay.getUpdatedTime()));
				fileList.add(itemMap);
			}
			//删除 提示用户正在生成的文件
			file.delete();
			//excl工具类创建文件并编辑
			toExcel2007.WriteDetailSheet(fileList ,configMap ,null);
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
