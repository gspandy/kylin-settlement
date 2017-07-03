package com.rkylin.settle.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.request.WheatfieldUserWithdrowRequest;
import com.Rop.api.request.WheatfieldUserWithholdRequest;
import com.Rop.api.response.WheatfieldUserWithdrowResponse;
import com.Rop.api.response.WheatfieldUserWithholdResponse;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.service.CrpsApiService;
import com.rkylin.settle.common.SessionUtils;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.GenerationPaymentManager;
import com.rkylin.settle.manager.TransDaysSummaryManager;
import com.rkylin.settle.manager.TransOrderInfoManager;
import com.rkylin.settle.pojo.GenerationPayment;
import com.rkylin.settle.pojo.TransDaysSummary;
import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoQuery;
import com.rkylin.settle.service.AccTransOrderInfoService;
import com.rkylin.settle.util.PagerModel;

/**
 * @Description: 交易结果管理
 * @author Yang
 * @Create Time: 2015-6-12下午12:59:48
 * @version V1.00
 */
@Controller
@RequestMapping("/accTranOrderInfo")
@Scope("prototype")
public class AccountTransOrderInfoController extends BaseController {
	@Autowired
	private AccTransOrderInfoService accTransOrderInfoService;
	@Autowired
	private GenerationPaymentManager generationPaymentManager;
	@Autowired
	private TransDaysSummaryManager transDaysSummaryManager;
	@Autowired
	private TransOrderInfoManager transOrderInfoManager;
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
		return "/account/trans_order_info/query_settler";
	}
	
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(TransOrderInfoQuery query) throws Exception {
		try {
			PagerModel<TransOrderInfo> pagerModel = accTransOrderInfoService.query(query);
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			List<TransOrderInfo> transorderinfoList = new ArrayList<TransOrderInfo>();
			String appKey_AC = userProperties.getProperty("ACAPP_KEY");
			String appSecret_AC =userProperties.getProperty("ACAPP_SECRET");
			String url_AC=userProperties.getProperty("ACAPP_URL");
			String[] idArray = idsStr.split(",");
			TransOrderInfo transorderinfo = null;
			TransOrderInfo transorderinfoU = null;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String idS = null;
			String rootCd = null;
			for (int i = 0;i<idArray.length;i++) {
				idS = idArray[i];
				transorderinfo=transOrderInfoManager.findTransOrderInfoById(Long.parseLong(idS));
				if (transorderinfo != null) {
					if (!"4014".equals(transorderinfo.getFuncCode()) && !"4016".equals(transorderinfo.getFuncCode())) {
						writeText("只能生成支付交易！");
						return;
					}
					if (rootCd != null) {
						if (!rootCd.equals(transorderinfo.getMerchantCode())) {
							writeText("只能生成同机构的支付交易！");
							return;
						}
					}
					if (transorderinfo.getStatus() != 5) {
						writeText("只能生成同机构失败的支付交易！");
						return;
					}
					rootCd = transorderinfo.getMerchantCode();
					transorderinfoList.add(transorderinfo);
				}
				transorderinfo = null;
			}
			if (transorderinfoList.size() ==0) {
				writeText("请选择同机构失败的支付交易！");
				return;
			}
	
			String orderNo = null;
			String requestNo = "";
			//插入交易表
			for (int i = 1; i <= transorderinfoList.size(); i++) {
				transorderinfo = transorderinfoList.get(i-1);
				
				orderNo = formatter.format(new Date()) + i;
				
				if ("4014".equals(transorderinfo.getFuncCode())) {
					DefaultRopClient ropClient = new DefaultRopClient(url_AC, appKey_AC,
							appSecret_AC, SettleConstants.FILE_XML);
					WheatfieldUserWithholdRequest acRequest=new WheatfieldUserWithholdRequest();
					acRequest.setRequestno(transorderinfo.getRequestNo());
					acRequest.setRequesttime(transorderinfo.getRequestTime());
					acRequest.setTradeflowno(transorderinfo.getTradeFlowNo());
					acRequest.setOrderpackageno(transorderinfo.getOrderNo());
					acRequest.setOrderno(orderNo);
					acRequest.setOrderdate(transorderinfo.getOrderDate());
					acRequest.setOrderamount(transorderinfo.getOrderAmount());
					acRequest.setOrdercount(transorderinfo.getOrderCount());
					acRequest.setFunccode(transorderinfo.getFuncCode());
					acRequest.setIntermerchantcode(transorderinfo.getInterMerchantCode());
					acRequest.setMerchantcode(transorderinfo.getMerchantCode());
					acRequest.setUserid(transorderinfo.getUserId());
					acRequest.setAmount(transorderinfo.getAmount());
					acRequest.setFeeamount(transorderinfo.getFeeAmount());
					acRequest.setUserfee(transorderinfo.getUserFee());
					acRequest.setProfit(transorderinfo.getProfit());
					acRequest.setBusitypeid(transorderinfo.getBusiTypeId());
					acRequest.setPaychannelid(transorderinfo.getPayChannelId());
					acRequest.setBankcode(transorderinfo.getBankCode());
					acRequest.setUseripaddress(transorderinfo.getUserIpAddress());
					acRequest.setStatus(1);
					acRequest.setErrorcode(transorderinfo.getErrorCode());
					acRequest.setErrormsg(transorderinfo.getErrorMsg());
					acRequest.setRemark(transorderinfo.getRemark());
					
					acRequest.setProductid(transorderinfo.getErrorCode());
	
					try {
						WheatfieldUserWithholdResponse acResponse=ropClient.execute(acRequest, SessionUtils.sessionGet(url_AC, appKey_AC,appSecret_AC));
						if(!"true".equals(acResponse.getIs_success())) {
							requestNo = requestNo + "," + transorderinfo.getRequestNo();
							System.out.println(acResponse.getMsg());
						} else {
							//更新原交易
							transorderinfoU = new TransOrderInfo();
							transorderinfoU.setRequestId(transorderinfo.getRequestId());
							//transorderinfoU.setStatus(8);
							transorderinfoU.setRemark("qjs_tuikuan");
							transOrderInfoManager.saveTransOrderInfo(transorderinfoU);;
						}
					} catch (ApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if ("4016".equals(transorderinfo.getFuncCode())) {
					DefaultRopClient ropClient = new DefaultRopClient(url_AC, appKey_AC,
							appSecret_AC, SettleConstants.FILE_XML);
					WheatfieldUserWithdrowRequest acRequest = new WheatfieldUserWithdrowRequest();
					
					acRequest.setRequestno(transorderinfo.getRequestNo());
					acRequest.setRequesttime(transorderinfo.getRequestTime());
					acRequest.setTradeflowno(transorderinfo.getTradeFlowNo());
					acRequest.setOrderpackageno(transorderinfo.getOrderNo());
					acRequest.setOrderno(orderNo);
					acRequest.setOrderdate(transorderinfo.getOrderDate());
					acRequest.setOrderamount(transorderinfo.getOrderAmount());
					acRequest.setOrdercount(transorderinfo.getOrderCount());
					acRequest.setFunccode(transorderinfo.getFuncCode());
					acRequest.setIntermerchantcode(transorderinfo.getInterMerchantCode());
					acRequest.setMerchantcode(transorderinfo.getMerchantCode());
					acRequest.setUserid(transorderinfo.getUserId());
					acRequest.setAmount(transorderinfo.getAmount());
					acRequest.setUserfee(transorderinfo.getUserFee());
					acRequest.setProfit(transorderinfo.getProfit());
					acRequest.setBusitypeid(transorderinfo.getBusiTypeId());
					acRequest.setPaychannelid(transorderinfo.getPayChannelId());
					acRequest.setBankcode(transorderinfo.getBankCode());
					acRequest.setUseripaddress(transorderinfo.getUserIpAddress());
					acRequest.setStatus(1);
					acRequest.setErrorcode(transorderinfo.getErrorCode());
					acRequest.setErrormsg(transorderinfo.getErrorMsg());
					acRequest.setRemark(transorderinfo.getRemark());
		            
		            try {
		            	WheatfieldUserWithdrowResponse acResponse=ropClient.execute(acRequest, SessionUtils.sessionGet(url_AC, appKey_AC,appSecret_AC));
						if(!"true".equals(acResponse.getIs_success())) {
							requestNo = requestNo + "," + transorderinfo.getRequestNo();
							System.out.println(acResponse.getMsg());
						} else {
							//更新原交易
							transorderinfoU = new TransOrderInfo();
							transorderinfoU.setRequestId(transorderinfo.getRequestId());
							transorderinfoU.setStatus(8);
							transorderinfoU.setRemark("qjs_tuikuan");
							transOrderInfoManager.saveTransOrderInfo(transorderinfoU);
						}
					} catch (ApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			String rtmMsg = "";
			if ("".equals(requestNo)) {
				rtmMsg = requestNo;
				writeText("生成支付交易成功！一共提交："+transorderinfoList.size()+"笔，且全部生成成功");
			} else {
				rtmMsg = requestNo.substring(1);
				writeText("生成支付交易成功！一共提交："+transorderinfoList.size()+"笔，其中失败订单号为:"+rtmMsg);
			}
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
	/***
	 * 通过ID字符串查询交易信息
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/create_gen_pay_file2")
	@Transactional("account_db")
	public void sendGenPayTrans(String idsStr)   {
		try{
			List<TransOrderInfo> transorderinfoList = new ArrayList<TransOrderInfo>();
			String[] idArray = idsStr.split(",");
			TransOrderInfo transorderinfo = null;
			TransOrderInfo transorderinfoU = null;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
			String idS = null;
			String rootCd = null;
			String sum_order = "";
			for (int i = 0;i<idArray.length;i++) {
				idS = idArray[i];
				transorderinfo=transOrderInfoManager.findTransOrderInfoById(Long.parseLong(idS));
				if (transorderinfo != null) {
					if (!"4014".equals(transorderinfo.getFuncCode()) && !"4016".equals(transorderinfo.getFuncCode())) {
						writeText("只能发送支付交易！");
						return;
					}
					if (rootCd != null) {
						if (!rootCd.equals(transorderinfo.getMerchantCode())) {
							writeText("只能发送同机构的支付交易！");
							return;
						}
					}
					if (transorderinfo.getStatus() != 1) {
						writeText("只能生成同机构未发送的支付交易！");
						return;
					}
					rootCd = transorderinfo.getMerchantCode();
					sum_order = sum_order + "'"+transorderinfo.getOrderPackageNo()+"',";
					transorderinfoList.add(transorderinfo);
				}
				transorderinfo = null;
			}
			if (transorderinfoList.size() ==0) {
				writeText("请选择同机构失败的支付交易！");
				return;
			}
	
			List<GenerationPayment> generationPaymentList = new ArrayList<GenerationPayment>();
			List<GenerationPayment> generationPaymentList1 = new ArrayList<GenerationPayment>();
			TransDaysSummary transDaysSummaryI = null;
			GenerationPayment generationPayment = null;
			GenerationPayment generationPaymentI = null;

			Map<String,String> opjP = new HashMap<String,String>();
			Map<String,Object> opj = new HashMap<String,Object>();
			opjP.put("ORDERNO", sum_order.substring(0, sum_order.length()-1));
			List<Map<String,Object>> allList = transDaysSummaryManager.findDateAll(opjP);
			String sumId = "";
			String batch = "QJS_" + formatter.format(new Date());
			int countI = 0;
			
			// 修改交易状态为发送中
			for (int i = 1;i <= transorderinfoList.size();i++) {
				transorderinfo = transorderinfoList.get(i-1);
				for (int j = 1;j <=allList.size();j++) {
					opj = allList.get(j-1);
					if (transorderinfo.getOrderPackageNo().equals(opj.get("ssummaryorders"))) {
						transDaysSummaryI = new TransDaysSummary();
						sumId = formatter.format(new Date()) + i+j;
						
						transDaysSummaryI.setTransSumId(sumId);//汇总订单号
						transDaysSummaryI.setRootInstCd(opj.get("srootinstcd").toString());
						transDaysSummaryI.setUserId(opj.get("suserid").toString());
						transDaysSummaryI.setOrderType(opj.get("sordertype").toString());
						transDaysSummaryI.setAccountDate(formatter1.parse(opj.get("saccountdate").toString()));
						transDaysSummaryI.setSummaryOrders(transorderinfo.getOrderNo());
						transDaysSummaryI.setSummaryAmount(Long.valueOf((opj.get("ssummaryamount").toString())).longValue());
//						transDaysSummaryI.setStatusId(opj.get("sstatusid"));
						
						transDaysSummaryManager.saveTransDaysSummary(transDaysSummaryI);
						
						generationPaymentI = new GenerationPayment();
						generationPaymentI.setRequestNo(opj.get("grequestno")==null ? null : opj.get("grequestno").toString());
						generationPaymentI.setBussinessCode(opj.get("gbussinesscode").toString());
						generationPaymentI.setRootInstCd(opj.get("grootinstcd").toString());
						generationPaymentI.setOrderNo(sumId);
						generationPaymentI.setOrderType(Integer.parseInt(opj.get("gordertype").toString()));
						generationPaymentI.setGeneSeq(opj.get("ggeneseq")==null ? null : opj.get("ggeneseq").toString());
						generationPaymentI.setUserId(opj.get("guserid").toString());
						generationPaymentI.setBankCode(opj.get("gbankcode")==null ? null : opj.get("gbankcode").toString());
						generationPaymentI.setAccountType(opj.get("gaccounttype")==null ? null : opj.get("gaccounttype").toString());
						generationPaymentI.setAccountNo(opj.get("gaccountno")==null ? null : opj.get("gaccountno").toString());
						generationPaymentI.setAccountName(opj.get("gaccountname")==null ? null : opj.get("gaccountname").toString());
						generationPaymentI.setAccountProperty(opj.get("gaccountproperty")==null ? null : opj.get("gaccountproperty").toString());
						generationPaymentI.setProvince(opj.get("gprovince")==null ? null : opj.get("gprovince").toString());
						generationPaymentI.setCity(opj.get("gcity")==null ? null : opj.get("gcity").toString());
						generationPaymentI.setOpenBankName(opj.get("gopenbankname")==null ? null : opj.get("gopenbankname").toString());
						generationPaymentI.setPayBankCode(opj.get("gpaybankcode")==null ? null : opj.get("gpaybankcode").toString());
						generationPaymentI.setAmount(Long.valueOf((opj.get("gamount").toString())).longValue());
						generationPaymentI.setCurrency(opj.get("gcurrency")==null ? null : opj.get("gcurrency").toString());
						generationPaymentI.setCertificateNumber(opj.get("gcertificatetype")==null ? null : opj.get("gcertificatetype").toString());
						generationPaymentI.setCertificateType(opj.get("gcertificatenumber")==null ? null : opj.get("gcertificatenumber").toString());
						generationPaymentI.setProcessResult(batch);
						generationPaymentI.setSendType(2);
//						generationPaymentI.setErrorCode("");
						generationPaymentI.setStatusId(1);
						generationPaymentI.setAccountDate(formatter1.parse(opj.get("gaccountdate").toString()));
						generationPaymentI.setRemark(opj.get("gremark")==null ? null : opj.get("gremark").toString());
						
						generationPaymentList1.add(generationPaymentI);
						generationPaymentManager.saveGenerationPayment(generationPaymentI);
						
						transorderinfoU = new TransOrderInfo();
						transorderinfoU.setRequestId(transorderinfo.getRequestId());
						transorderinfoU.setStatus(7);
						transOrderInfoManager.saveTransOrderInfo(transorderinfoU);
						countI++;
						break;
					}
				}
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
			if (countI == transorderinfoList.size()) {
				writeText("通知代收付系统成功！");
			} else {
				writeText("通知代收付系统成功！但是部分之前没有支付过的交易无法再次支付");
			}
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
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
