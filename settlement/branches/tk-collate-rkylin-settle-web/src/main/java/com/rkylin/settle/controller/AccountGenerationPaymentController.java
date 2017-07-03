package com.rkylin.settle.controller;

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

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.service.CrpsApiService;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.GenerationPaymentManager;
import com.rkylin.settle.manager.TransDaysSummaryManager;
import com.rkylin.settle.pojo.GenerationPayment;
import com.rkylin.settle.pojo.GenerationPaymentQuery;
import com.rkylin.settle.pojo.TransDaysSummary;
import com.rkylin.settle.service.AccGenterationPaymentService;
import com.rkylin.settle.util.PagerModel;
import com.rkylin.settle.util.StringUtils;
import com.rkylin.settle.util.toExcel2007;

/**
 * @Description: 交易结果管理
 * @author Yang
 * @Create Time: 2015-6-12下午12:59:48
 * @version V1.00
 */
@Controller
@RequestMapping("/accGenPay")
@Scope("prototype")
public class AccountGenerationPaymentController extends BaseController {
	@Autowired
	private AccGenterationPaymentService accGenterationPaymentService;
	@Autowired
	private GenerationPaymentManager generationPaymentManager;
	@Autowired
	private TransDaysSummaryManager transDaysSummaryManager;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	Properties userProperties;
	@Autowired
	private CrpsApiService crpsApiService;
	
	/***
	 * 打开页面的链接
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/openQueryInfo")
	public String openQueryInfo() throws Exception {
		return "/account/generation_payment/query_settler";
	}
	
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(GenerationPaymentQuery query) throws Exception {
		try {
			PagerModel<GenerationPayment> pagerModel = accGenterationPaymentService.query(query);
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}
	
	/***
	 * 全部到处excl
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_and_output_excl")
	public void queryAndOutputExcl(GenerationPaymentQuery query) throws Exception {
		logger.info(">>> >>> >>> >>> 开始 到处全部信息excl文件");
		boolean isSuccess = false;
		try {
			isSuccess = accGenterationPaymentService.queryAndOutputExcl(request, query);
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
			logger.error("全部到处excl异常:", e);
		}
		if(isSuccess) {
			writeText("导出成功! 请点击【我的下载】下载导出文件！");
		} else {
			writeText("导出失败!");
		}
		logger.info("<<< <<< <<< <<< 结束 到处全部信息excl文件 执行结果:" + isSuccess);
	}
	/***
	 * 通过ID字符串查询交易信息
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/create_gen_pay_file")
	@Transactional("account_db")
	public void getGenPayTrans(String idsStr)   {
		try{
		List<GenerationPayment> generationPaymentList = new ArrayList<GenerationPayment>();
		List<GenerationPayment> generationPaymentList1 = new ArrayList<GenerationPayment>();
		String[] idArray = idsStr.split(",");
		List<TransDaysSummary> transDaysSummaryList = new ArrayList<TransDaysSummary>();
		TransDaysSummary transDaysSummary = null;
		TransDaysSummary transDaysSummaryI = null;
		GenerationPayment generationPayment = null;
		GenerationPayment generationPaymentU = null;
		GenerationPayment generationPaymentI = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String idS = null;
		String rootCd = null;
		for (int i = 0; i < idArray.length;i++) {
			idS = idArray[i];
			generationPayment = generationPaymentManager.findGenerationPaymentById(Long.parseLong(idS));
			if (generationPayment != null) {
				if (generationPayment.getOrderType() != 2 && generationPayment.getOrderType() != 6) {
					writeText("只能重付提现或代付交易！");
					return;
				}
				if (rootCd != null) {
					if (!rootCd.equals(generationPayment.getRootInstCd())) {
						writeText("只能重付同机构的提现或代付交易！");
						return;
					}
				}
				if (1 != generationPayment.getSendType()) {
					writeText("只能重付同机构失败的提现或代付交易！");
					return;
				}
				if (generationPayment.getStatusId() == 3) {
					writeText("已经入账的交易不能再付！");
					return;
				}
				rootCd = generationPayment.getRootInstCd();
				generationPaymentList.add(generationPayment);
			}
			generationPayment = null;
		}
		if (generationPaymentList.size() ==0) {
			writeText("请选择同机构失败的提现或代付交易！");
			return;
		}
		for (int i = 1; i <= generationPaymentList.size(); i++) {
			generationPayment = generationPaymentList.get(i-1);
			
			transDaysSummary = transDaysSummaryManager.findTransDaysSummaryById(generationPayment.getOrderNo());
			if (transDaysSummary == null) {
				writeText("选择交易中存在有问题的交易，Summary不存在！");
				return;
			}
			transDaysSummaryList.add(transDaysSummary);
			transDaysSummary = null;
		}

		String batch = "QJS_" + formatter.format(new Date());
		String sumId = null;
		//插入汇总表
		for (int i = 1; i <= transDaysSummaryList.size(); i++) {
			transDaysSummary = transDaysSummaryList.get(i-1);
			
			sumId = formatter.format(new Date()) + i;
			transDaysSummaryI = transDaysSummary;
			for (int j = 1; j <= generationPaymentList.size(); j++) {
				generationPayment = generationPaymentList.get(j-1);
				if (transDaysSummary.getTransSumId().equals(generationPayment.getOrderNo())) {
					generationPaymentI = new GenerationPayment();
					generationPaymentI.setGeneId(null);
					generationPaymentI.setRequestNo(generationPayment.getRequestNo());
					generationPaymentI.setBussinessCode(generationPayment.getBussinessCode());
					generationPaymentI.setRootInstCd(generationPayment.getRootInstCd());
					generationPaymentI.setOrderNo(sumId);
					generationPaymentI.setOrderType(generationPayment.getOrderType());
					generationPaymentI.setGeneSeq(generationPayment.getGeneSeq());
					generationPaymentI.setUserId(generationPayment.getUserId());
					generationPaymentI.setBankCode(generationPayment.getBankCode());
					generationPaymentI.setAccountType(generationPayment.getAccountType());
					generationPaymentI.setAccountNo(generationPayment.getAccountNo());
					generationPaymentI.setAccountName(generationPayment.getAccountName());
					generationPaymentI.setAccountProperty(generationPayment.getAccountProperty());
					generationPaymentI.setProvince(generationPayment.getProvince());
					generationPaymentI.setCity(generationPayment.getCity());
					generationPaymentI.setOpenBankName(generationPayment.getOpenBankName());
					generationPaymentI.setPayBankCode(generationPayment.getPayBankCode());
					generationPaymentI.setAmount(generationPayment.getAmount());
					generationPaymentI.setCurrency(generationPayment.getCurrency());
					generationPaymentI.setCertificateNumber(generationPayment.getCertificateNumber());
					generationPaymentI.setCertificateType(generationPayment.getCertificateType());
					generationPaymentI.setProcessResult(batch);
					generationPaymentI.setSendType(2);
					generationPaymentI.setErrorCode(generationPayment.getErrorCode());
					generationPaymentI.setStatusId(1);
					generationPaymentI.setAccountDate(generationPayment.getAccountDate());
					generationPaymentI.setRemark(generationPayment.getRemark());
					
					generationPaymentI.setErrorCode(null);
					generationPaymentList1.add(generationPaymentI);
					generationPaymentManager.saveGenerationPayment(generationPaymentI);
					
					generationPaymentU = new GenerationPayment();
					generationPaymentU = generationPayment;
					generationPaymentU.setGeneId(generationPayment.getGeneId());
//					generationPaymentU.setStatusId(3);
					generationPaymentU.setStatusId(1);
					generationPaymentU.setRemark("手动再付，此交易作废！"+generationPaymentI.getOrderNo());
					generationPaymentU.setUpdatedTime(null);
					
					generationPaymentManager.saveGenerationPayment(generationPaymentU);
				}
			}
			transDaysSummaryI.setTransSumId(sumId);//汇总订单号
			transDaysSummaryI.setUpdatedTime(null);
			transDaysSummaryI.setCreatedTime(null);
			transDaysSummaryManager.saveTransDaysSummary(transDaysSummaryI);
		}
		
		List<OrderDetail> orderDeBatchList =  new ArrayList<OrderDetail>();
		//所有发送到代收付成功的代收付交易
		List<GenerationPayment> genPaySuccessAllList = new ArrayList<GenerationPayment>();
		//每limitCount条生成一个批次号，并发到代收付系统（如果某一批发送失败，继续发送下一批）
		for (int i = 1; i <= generationPaymentList1.size(); i++) {
			generationPayment = generationPaymentList1.get(i-1);
			//根据传入的汇总数据，封装成要传入代收付系统的数据OrderDetail
			OrderDetail orderDetail = getOrderDeByTransOrder(generationPayment);
			orderDetail.setAccountDate(generationPayment.getAccountDate());
			
			genPaySuccessAllList.add(generationPayment);
			orderDetail.setRequestNo(generationPayment.getProcessResult());
			orderDeBatchList.add(orderDetail);
			//所有数据组装完成后再去调用代收付系统，否则继续循环组装；
			if (i!=generationPaymentList.size())  {
				continue; 
			}
			//调用代收付系统接口
			OrderDetail orderDeBatch = null;
			try{
				orderDeBatch = crpsApiService.transDetailFromOrderList(orderDeBatchList);
			} catch (Exception e) {
				e.printStackTrace();
				writeText("上传代收付系统异常！"+e.getMessage());
				throw new RuntimeException();
			}
			String retCode = orderDeBatch.getRetCode();
			String errorMsg = orderDeBatch.getErrMsg();
			//处理失败，继续处理剩下的数据
			if (!"100000".equals(retCode)) {
				writeText("代收付系统处理失败！["+retCode+"]["+ errorMsg +"]");
				throw new RuntimeException();
			}
		}
		writeText("通知代收付系统成功！");
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * //根据传入的汇总数据，封装成要传入代收付系统的数据OrderDetail
	 * @param generationPaymentList
	 * @return
	 */
	private OrderDetail getOrderDeByTransOrder(GenerationPayment generationPayment)   {
		try{
//		for (GenerationPayment generationPayment:generationPaymentList) {
			OrderDetail orderDetail = new OrderDetail(); 
//			orderDetail.setRequestNo(batch);
			orderDetail.setBussinessCode(generationPayment.getBussinessCode());
			orderDetail.setBussinessType(1);//业务类型ID:0实时;1非实时
			orderDetail.setRootInstCd(generationPayment.getRootInstCd());
			orderDetail.setUserId(generationPayment.getUserId());
			String certificateType = generationPayment.getCertificateType();
			if(certificateType!=null && !"".equals(certificateType)){
				orderDetail.setCertificateType(Integer.parseInt(certificateType));
			}
			orderDetail.setCertificateNumber(generationPayment.getCertificateNumber());
			orderDetail.setAccountNo(generationPayment.getAccountNo());
			orderDetail.setAccountName(generationPayment.getAccountName());
			String accountProp = "10"; //定义此变量仅用于记录日志
			orderDetail.setAccountProp(accountProp); //对私
			if("1".equals(generationPayment.getAccountProperty())){
				accountProp="20";
				orderDetail.setAccountProp(accountProp);// 对公
			}
			orderDetail.setAccountType(Integer.parseInt(generationPayment.getAccountType()));
			orderDetail.setBankCode(generationPayment.getBankCode());
			orderDetail.setBankName(generationPayment.getOpenBankName());
			orderDetail.setPayBankCode(generationPayment.getPayBankCode());
//			orderDetail.setPayBankName(generationPayment.getp);
			orderDetail.setProvince(generationPayment.getProvince());
			orderDetail.setCity(generationPayment.getCity());
			orderDetail.setOrderNo(generationPayment.getOrderNo());
			orderDetail.setRemark(generationPayment.getRemark());
			Integer orderType = generationPayment.getOrderType();
			int orderrType = 1; //定义此变量仅用于记录日志
			orderDetail.setOrderType(orderrType); //代收
			if (orderType==SettleConstants.ORDER_WITHDRAW || orderType==SettleConstants.ORDER_WITHHOLD) {
				orderrType = 2;
				orderDetail.setOrderType(orderrType); //代付
			}
			orderDetail.setAmount(generationPayment.getAmount());
			orderDetail.setCurrency(generationPayment.getCurrency());
			//orderDetail.setPurpose("fileupdata");
			return orderDetail;
		}catch(Exception e){
			throw new RuntimeException("getOrderDeByTransOrder error");
		}
	}
}
