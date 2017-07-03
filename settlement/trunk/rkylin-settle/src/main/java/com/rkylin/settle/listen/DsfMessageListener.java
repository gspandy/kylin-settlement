package com.rkylin.settle.listen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.database.BaseDao;
import com.rkylin.order.mixservice.SettlementToOrderService;
import com.rkylin.order.pojo.OrderPayment;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.OrgInfoManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransInvoiceManager;
import com.rkylin.settle.manager.SettleTransSummaryManager;
import com.rkylin.settle.pojo.OrgInfo;
import com.rkylin.settle.pojo.OrgInfoQuery;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;
import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;
import com.rkylin.settle.service.SettleTransDetailService;
import com.rkylin.settle.util.LogicConstantUtil;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.wheatfield.api.TransOrderDubboService;
import com.rongcapital.activemq.api.po.ReturnObject;
import com.rongcapital.activemq.api.service.ActivemqSenderService;
import com.rongcapital.mtaegis.po.MultTransOrderInfoReturn;
import com.rongcapital.mtaegis.po.TransOrderInfo;
import com.rongcapital.mtaegis.response.CommonResponse;
import com.rongcapital.mtaegis.response.MultBaseResultReturnResponse;
import com.rongcapital.mtaegis.response.UpdateOrderRes;
import com.rongcapital.mtaegis.service.MultCollectionResultReturnApi;
import com.rongcapital.mtaegis.service.MultWithholdAndWithdrowResultReturnApi;
import com.rongcapital.mtaegis.service.TransOrderInfoApi;
import com.rongcapital.mtaegis.service.WriteOffApi;

/**
 * 监听代收付结果的MQ并将结果返回给账户系统
 * @author youyu
 *
 */
@Component("dsfMessageListener")
public class DsfMessageListener extends BaseDao implements MessageListener{ 
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(DsfMessageListener.class);
	
	@Autowired
	private SettleTransInvoiceManager settleTransInvoiceManager;//结算表Manager
	
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;//交易信息Manager
	
	@Autowired
	private SettleTransSummaryManager settleTransSummaryManager;//汇总表Manager
	
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager; //参数表manager
	
	@Autowired
	private SettlementToOrderService settlementToOrderService;//返回代收付结果给订单系统
	
	@Autowired
	private TransOrderInfoApi transOrderInfoApi;//返回一分钱代收付结果给账户系统
	
	@Autowired
	private MultWithholdAndWithdrowResultReturnApi multWithholdAndWithdrowResultReturnApi;//返回代付结果给账户系统
	
	@Autowired
	private MultCollectionResultReturnApi multCollectionResultReturnApi;//返回代收结果给账户系统
	
	@Autowired
	private WriteOffApi writeOffApi;//冲正/抹账接口
	    
	@Autowired
	private SettleTransDetailService settleTransDetailService;   
	
	@Autowired
	private RedisIdGenerator redisIdGenerator;
	
	@Autowired
	private SettlementUtil settlementUtil;
	
	@Autowired
	private OrgInfoManager orgInfoManager;//多渠道ORG_INFO标的manager
	
	@Autowired
	private LogicConstantUtil logicConstantUtil;
	
	@Autowired
	private TransOrderDubboService transOrderDubboService;//返回代收付结果给账户系统
	
	@Autowired
	private ActivemqSenderService activemqSenderService;				//发送activeMQ接口(架构组)
	
	public void onMessage(Message msg) {
	   logger.info(">>>>>开始监听到ActiveMQ队列中代收付的消息！");
	   String orderNo = null;
	   try {
			MapMessage message  = (MapMessage)msg;
			orderNo = message.getString("orderNo");
			int returnStatusId = message.getInt("statusId");
			String errorMsg = message.getString("errorMsg");
			String orgCode = message.getString("orgCode");
			
			//获取协议号和渠道号
			logger.info("即将从多渠道查询org_num和channel_home,参数org_code="+orgCode);
			String orgNumAndChannelHome = this.getOrgNumAndChannelHome(orgCode);
//			String orgNumAndChannelHome = orgCode;
			
			logger.info(">>>监听MQ队列,orderNo="+orderNo+",returnStatusId="+returnStatusId+",errorMsg="+errorMsg);
			int dflag = 0;
			if(13 == returnStatusId){
				dflag = 4;
			}else if(15 == returnStatusId){
				dflag = 6;
			}else{
				dflag = 99;
			}
			logger.info(">>>根据MQ队列的orderNo="+orderNo+"查询结算表开始");
			SettleTransInvoiceQuery query = new SettleTransInvoiceQuery();
			query.setOrderNo(orderNo);
			List<SettleTransInvoice> settleTransInvoiceList = settleTransInvoiceManager.queryList(query);
			logger.info(">>>根据MQ队列的orderNo="+orderNo+"查询结算表结束,共查到"+settleTransInvoiceList.size()+"条信息");
			if(settleTransInvoiceList ==null || settleTransInvoiceList.size()==0){
				return;
			}
			SettleTransInvoice invoiceBean = null;
			for(SettleTransInvoice settleTransInvoice : settleTransInvoiceList){//只循环1次?
				invoiceBean = this.updateInvoiceProcessResult(settleTransInvoice, returnStatusId,errorMsg,orgNumAndChannelHome);
				break;
			}
			//去重
			if(invoiceBean !=null && SettleConstants.DSF_HAS_WRITE != invoiceBean.getStatusId()){
			   Map<String, List<SettleTransDetail>> sendToOrderList = null;
			   try{
				   sendToOrderList = this.updateSummeryStatusId(invoiceBean, returnStatusId, dflag, errorMsg);
				   invoiceBean.setStatusId(SettleConstants.DSF_HAS_WRITE);
				   settleTransInvoiceManager.updateSettleTransInvoice(invoiceBean);
			   }catch(Exception e){
				   logger.error("------------------"+e);
			   }
			   this.returnAccount(sendToOrderList);//发送账户
			   //this.returnToOrder(sendToOrderList);//发送订单
			   this.returnMq(sendToOrderList);
			}else{
				logger.info("监听MQ队列,statusId=6已经回写过数据,此处去重去重,不再处理,orderNo="+invoiceBean.getOrderNo()+"statusId="+invoiceBean.getStatusId());
			}
	      }catch (Exception e){
	          logger.error("监听代收付队列处理逻辑异常！！！！！orderNo="+orderNo+",错误信息:"+e.getMessage());
	      }
	      logger.info(">>>>>结束监听到ActiveMQ队列中代收付的消息！orderNo="+orderNo);
	} 
	
	/**
	 * 返回代付结果给账户系统
	 * @param detailBean 交易表的bean
	 */
	public void returnAccount(Map<String, List<SettleTransDetail>> detailList){
		try{
			List<com.rkylin.wheatfield.bean.TransOrderInfo> param1List = new ArrayList<com.rkylin.wheatfield.bean.TransOrderInfo>();//代收付1期
			List<TransOrderInfo> param2DfList = new ArrayList<TransOrderInfo>();//代付2期
			List<TransOrderInfo> param2DsList = new ArrayList<TransOrderInfo>();//代收2期
			this.splitAccountData(detailList, param1List, param2DfList, param2DsList);//拆分交易表的数据，决定具体调用账户的哪个接口进行代收付结果的推送
			this.dealDsfResultTo1Account(param1List);//一期
		    this.dealDfResultTo2Account(param2DfList);//二期代付、提现、一分钱代付
		    this.dealDsResultTo2Account(param2DsList);//二期代收
		    this.removeSuccessToHis();//移入历史
		}catch(Exception e){
			logger.error("返回代付结果给账户系统发生异常，error="+e.getMessage());
		}
	}
	
	/**
	 * 返回代付结果给MQ
	 * @param detailBean 交易表的bean
	 */
	public void returnMq(Map<String, List<SettleTransDetail>> detailList){
		//mq发送返回值
		ReturnObject<String> returnObject = null;
		String mqKey = null;
		String verifySign = null;
		List<SettleTransDetail> subDetailList = new ArrayList<SettleTransDetail>();
		JSONObject jsonArray = null;
		try{
			if (detailList.containsKey("1")) {
				subDetailList.addAll(detailList.get("1"));
			}
			if (detailList.containsKey("2")) {
				subDetailList.addAll(detailList.get("2"));
			}
			
			for (SettleTransDetail bean:subDetailList) {
				mqKey = this.getCollectMqKey("DSFRES_MQKET");
				Map<String, Object> map4MQ = new HashMap<String, Object>();
				map4MQ.put("orderNo", bean.getOrderNo());
				map4MQ.put("statusId", bean.getReadStatusId());
				map4MQ.put("ErrMsg", bean.getErrorMsg());
				map4MQ.put("msgType", "100888");
				jsonArray = JSONObject.fromObject(map4MQ);
				returnObject = activemqSenderService.sendTopicSingle(mqKey, jsonArray.toString());
			}
		}catch(Exception e){
			logger.error("返回代付结果给账户系统发生异常，error="+e.getMessage());
		}
	}
	
	/**
	 * 根据账户系统返回的结果更新交易表的dflag
	 * @param detailBean 交易表的bean
	 * @param commonResponse 账户系统的返回结果对象
	 */
	public void updateDetailByAccountReturn(UpdateOrderRes  updateOrderRes){
		if(updateOrderRes==null){
			logger.info(">>>根据账户系统返回的结果更新交易表的commonResponse方法的入参为空!");
			return;
		}
		try{
			String merchantCode = null;
			String orderNo = null;
			String roleCode = null;
			String result =null;
			String[] resultArray = null;
			Set<String> failSet = updateOrderRes.getFailOrderSet();
			Iterator<String> iterator = failSet.iterator();
			while(iterator.hasNext()){
				result = iterator.next();//机构号_订单号_角色号
				resultArray = result.split(",");
				if(resultArray !=null && resultArray.length>0){
					if(StringUtils.isNotBlank(resultArray[0])){
						merchantCode = resultArray[0];
					}
					if(StringUtils.isNotBlank(resultArray[1])){
						orderNo = resultArray[1];
					}
					if(StringUtils.isNotBlank(resultArray[2])){
						roleCode = resultArray[2];
					}
				}
				
				SettleTransDetailQuery query = new SettleTransDetailQuery();
				query.setOrderNo(orderNo);
				query.setMerchantCode(merchantCode);
				query.setRoleCode(roleCode);
				List<SettleTransDetail>  detailList = settleTransDetailManager.queryList(query);
				for(SettleTransDetail detail : detailList){
					if(detail.getDflag() == 6){
						detail.setDflag(61);
				    }else if(detail.getDflag() == 4){
				    	detail.setDflag(41);
					}else{
						continue;
					}
					settleTransDetailManager.updateSettleTransDetail(detail);
				}
			}
			
		}catch(Exception e){
			//捕获异常 未处理
		}
	}
	
	/**
	 * 将代收付的结果更新到结算表
	 */
	public SettleTransInvoice updateInvoiceProcessResult(SettleTransInvoice settleTransInvoice,int returnStatusId,String errorMsg,String orgNumAndChannelHome){
		if(settleTransInvoice == null){
			logger.info("将代收付的结果更新到结算表的参数settleTransInvoice为空！！！！！");
			return settleTransInvoice ;
		}
		logger.info(">>>>>>开始将代收付的结果更新到结算表,orderNo="+settleTransInvoice.getOrderNo()+",代收付返回的结果="+returnStatusId);
		try{
			if(SettleConstants.DSF_HAS_RETURN != settleTransInvoice.getStatusId()){
				//更新结算表
			    settleTransInvoice.setProcessResult(String.valueOf(returnStatusId));//13失败,15成功
			    settleTransInvoice.setRemark(errorMsg);
			    settleTransInvoice.setStatusId(SettleConstants.DSF_HAS_RETURN);
			    settleTransInvoice.setGeneSeq(orgNumAndChannelHome);
				settleTransInvoiceManager.updateSettleTransInvoice(settleTransInvoice);
			}else{
				logger.info("代收付的结果更新到结算表时发现状态是5已经返回过了,此处不再处理,orderNo="+settleTransInvoice.getOrderNo()+"statusId="+settleTransInvoice.getStatusId());
			}
		}catch(Exception e){
			logger.error(">>>>>将代收付结果更新到结算表异常！！！！！！"+"orderNo="+settleTransInvoice.getOrderNo()+",代收付返回的结果="+returnStatusId+",异常信息:"+e.getMessage());
		}
		logger.info(">>>>>>结束将代收付的结果更新到结算表,orderNo="+settleTransInvoice.getOrderNo()+",代收付返回的结果="+returnStatusId);
		return settleTransInvoice;
	}
	
	/**
	 * 将代收付结果更新到汇总表
	 * @param batchNo
	 * @param returnStatusId
	 */
	public Map<String, List<SettleTransDetail>> updateSummeryStatusId(SettleTransInvoice invoiceBean,int returnStatusId,int dflag,String errorMsg){
		Map<String, List<SettleTransDetail>> rtnMap = new HashMap<String, List<SettleTransDetail>>();
		List<SettleTransDetail> sendToAccount1List = new ArrayList<SettleTransDetail>();
		List<SettleTransDetail> sendToAccount2List = new ArrayList<SettleTransDetail>();
		List<SettleTransDetail> sendToOrderList = new ArrayList<SettleTransDetail>();
		if(invoiceBean == null){
			logger.info("将代收付结果更新到汇总表时updateSummeryStatusId的参数invoiceBean为空");
			return null;
		}
		String batchNo = null;
		String orgNumAndChannelHome = null;
		try{
			batchNo = invoiceBean.getBatchNo();
			orgNumAndChannelHome = invoiceBean.getGeneSeq();
			
			logger.info(">>>MQ的监听类中,开始根据结算表的batchNo从汇总表查询数据,batchNo="+batchNo);
			SettleTransSummaryQuery summeryQuery = new SettleTransSummaryQuery();
			summeryQuery.setBatchNo(batchNo);
			List<SettleTransSummary> summaryList = settleTransSummaryManager.queryList(summeryQuery);
			if(summaryList !=null){
				logger.info(">>>MQ的监听类中,根据结算表的batchNo从汇总表查询数据,batchNo="+batchNo+",共查询到："+summaryList.size()+"条");
			}else{
				logger.info(">>>MQ的监听类中,根据结算表的batchNo从汇总表查询数据,batchNo="+batchNo+",共查询到0条");
			}
		
			SettleTransDetail detailBean = null;
			//冲正List
//			List<SettleTransDetail> czList = new ArrayList<SettleTransDetail>(); 
			//抹账List
//			List<SettleTransDetail> mzList = new ArrayList<SettleTransDetail>();  
			//获取会计账期
			String settleDate = String.valueOf(settlementUtil.getAccountDate("yyyy-MM-dd", 0, ""));
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			
			for(SettleTransSummary summaryBean : summaryList){
				summaryBean.setStatusId(returnStatusId);
				try{
					detailBean = ((DsfMessageListener) AopContext.currentProxy()).updateSummaryAndDetailOne(summaryBean, dflag, errorMsg,orgNumAndChannelHome);
				}catch(Exception e){
					logger.error("错误信息:"+e.getMessage());
				}
				
				Calendar c = Calendar.getInstance();
				int hour = c.get(Calendar.HOUR_OF_DAY); 
				int minute = c.get(Calendar.MINUTE);
				if(((hour==18 && minute>=30)||hour>18)||(dflag == 6 && detailBean !=null)){//18:30之后成功失败的都立即发给订单或者18:30之前的成功的立即发给订单
					// sendToOrderList.add(detailBean);
					try{
						/*if(detailBean.getDflag() == 4){
							//true=冲正   else=抹账
							if(detailBean.getAccountDate().equals(sdf.format(settleDate))){
								czList.add(detailBean);
							}else{
								mzList.add(detailBean);
							}
						}*/
						if (detailBean.getDataFrom()==0) {// 账户1期
							if (rtnMap.containsKey("1")) {
								sendToAccount1List = rtnMap.get("1");
								sendToAccount1List.add(detailBean);
							} else {
								sendToAccount1List.add(detailBean);
								rtnMap.put("1", sendToAccount1List);
							}
						} else if (detailBean.getDataFrom()==4) {// 账户2期
							if (rtnMap.containsKey("2")) {
								sendToAccount2List = rtnMap.get("2");
								sendToAccount2List.add(detailBean);
							} else {
								sendToAccount2List.add(detailBean);
								rtnMap.put("2", sendToAccount2List);
							}
						}
					}catch(Exception e){
						logger.error("错误信息:"+e.getMessage());
					}
				}
			}
		/*	if(czList !=null && czList.size()>0){
				doCorrectByAccount(czList); 
			}
			if(mzList !=null && mzList.size()>0){
				doAccOfOrRefundByAccount(mzList);
			}*/
			
		}catch(Exception e){
			logger.error(">>>>>将代收付结果更新到汇总表updateSummeryStatusId发生异常!!!!!!batchNo="+batchNo);
		}
		return rtnMap;
	}
	
	//更新汇总表和交易表(单条)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public SettleTransDetail updateSummaryAndDetailOne(SettleTransSummary summaryBean,int dflag,String errorMsg,String orgNumAndChannelHome) throws Exception{
		SettleTransDetail detailBean = null;

		//获取多渠道ChannelHome 和 清结算 PayChannelId 对应关系
		List<SettleParameterInfo> payChannelIdList = logicConstantUtil.getMultiGateChannelHome2PayChannelId();
		try{
			settleTransSummaryManager.updateSettleTransSummary(summaryBean);//更新汇总表
			logger.info(">>>>更新汇总表的单条记录成功,orderNo="+summaryBean.getOrderNo());
			SettleTransDetailQuery detailQuery = new SettleTransDetailQuery();
			detailQuery.setMerchantCode(summaryBean.getRootInstCd());// 机构号加订单号确定唯一
			detailQuery.setOrderNo(summaryBean.getOrderNo());
			detailQuery.setDflag(1);
			List<SettleTransDetail> detailList = settleTransDetailManager.queryList(detailQuery);
			if(detailList !=null && detailList.size()>0){
				detailBean = detailList.get(0);
				detailBean.setDflag(dflag);
				if(detailBean.getDflag() == 4 || detailBean.getDflag() == 40){//失败
					detailBean.setReadStatusId(5);
				}else if(detailBean.getDflag() == 6 || detailBean.getDflag() == 60){//成功
					detailBean.setReadStatusId(4);
				}
				detailBean.setErrorMsg(errorMsg);
				//将orgNumAndChannelHome中的ChannelHome改成payChannelID
				if(orgNumAndChannelHome != null && orgNumAndChannelHome.split(",").length > 1) {
					String[] orgNumAndChannelHomeArr = orgNumAndChannelHome.split(",");
					//协议号
					String orgNum = orgNumAndChannelHomeArr[0];
					//渠道名称
					String ChannelHome = orgNumAndChannelHomeArr[1];
					//渠道ID
					String payChannelId = null;
					//遍历对应关系进行匹配
					for(SettleParameterInfo paraInfo : payChannelIdList) {
						if(ChannelHome.equals(paraInfo.getParameterValue())) {//匹配成功
							payChannelId = paraInfo.getParameterCode();
							break;
						}
					}
					if(payChannelId == null) {//匹配失败,未匹配到对应的payChannelID
						payChannelId = ChannelHome;
						logger.error(">>> >>> 异常: "+ orgNumAndChannelHome +"中的 ChannelHome 未找到对应的 payChannelID");
					}
					orgNumAndChannelHome = orgNum + "," + payChannelId;
				} else {
					logger.error(">>> >>> 异常: 代收付的MQ监听类中更新单条汇总表和交易表发生异常! 入参orgNumAndChannelHome:"+ orgNumAndChannelHome +"有误");
				}
				detailBean.setPayChannelId(orgNumAndChannelHome);
				settleTransDetailManager.updateSettleTransDetail(detailBean);
				logger.info(">>>> 更新交易表的单条记录成功,orderNo="+summaryBean.getOrderNo());
			}
		}catch(Exception e){
			detailBean = null;
			logger.error("代收付的MQ监听类中更新单条汇总表和交易表发生异常!!!!!!orderNo="+summaryBean.getOrderNo()+e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		return detailBean;
	}
	
	/**
	 * 将成功的代收付移到历史表
	 */
	public void removeSuccessToHis(){
		try{
			//通过结算单号将指定的数据移入历史表
			 Map<String, Object> map = new HashMap<String, Object>();
			 Calendar c = Calendar.getInstance();
			 int hour = c.get(Calendar.HOUR_OF_DAY); 
			 int minute = c.get(Calendar.MINUTE);
			 if((hour==18 && minute>=30) || hour>18){//18:30之后成功失败的都立即发给订单或者18:30之前的成功的立即发给订单
				 map.put("iv_status_id", "13");
				 super.getSqlSession().selectOne("SettleTransInvoiceMapper.setgeneration", map);  
				 logger.info("18:30之后,将失败单移入历史表完成");
				 map.put("iv_status_id", "15");
				 super.getSqlSession().selectOne("SettleTransInvoiceMapper.setgeneration", map);  
				 logger.info("18:30之后,将成功单移入历史表完成");
			 }else{
				 map.put("iv_status_id", "15");
				 super.getSqlSession().selectOne("SettleTransInvoiceMapper.setgeneration", map);  
				 logger.info("18:30之前,将成功单移入历史表完成");
			 }
	         
		}catch(Exception e){
			logger.error(">>>>>removeSuccessToHis将成功的代收付移到历史表异常！！！！！！"+e.getMessage());
		}
	}
	

	/**
	 * 将代收付结果返回给订单系统并更新交易表dflag状态
	 */
	public void returnToOrder(Map<String, List<SettleTransDetail>> sendToOrderList){
		
		List<OrderPayment> orderPaymentList = new ArrayList<OrderPayment>();
		List<SettleTransDetail> orderList = new ArrayList<SettleTransDetail>();
		

		if (sendToOrderList.containsKey("1")) {
			orderList.addAll(sendToOrderList.get("1"));
		}
		if (sendToOrderList.containsKey("2")) {
			orderList.addAll(sendToOrderList.get("2"));
		}
		
		
		if(orderList !=null && orderList.size()>0){
			String orderStatusId = null;
			OrderPayment orderPayment = null;
			for(SettleTransDetail detailBean : orderList){
				//不是1分钱代付的交易返回给订单
				if(!SettleConstants.BATCH_DF_YFQ_FUN_CODE.equals(detailBean.getFuncCode())){
					orderPayment = new OrderPayment();
					orderPayment.setPaymentId(detailBean.getOrderNo());
					if(detailBean.getDflag() == 6 || detailBean.getDflag() == 60){
						orderStatusId = "0";//成功
					}else if(detailBean.getDflag() == 4 || detailBean.getDflag() == 40){
						orderStatusId = "1";//失败
					}
					orderPayment.setStatusId(orderStatusId);
					orderPayment.setRetMsg(detailBean.getRemark());
					orderPaymentList.add(orderPayment);
					
//					if(detailBean.getDflag() == 6){
//						detailBean.setDflag(60);
//					}else if(detailBean.getDflag() == 4){
//						detailBean.setDflag(40);
//					}else{
//						continue;
//					}
//					settleTransDetailManager.updateSettleTransDetail(detailBean);
				}
			} 
		}
		
		Map<String,Object> resultmap = null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paymentList", orderPaymentList);
		//订单系统只返回处理失败的list
		try{
			if(orderPaymentList !=null && orderPaymentList.size()>0){
				resultmap = settlementToOrderService.updateBatchPaymentStatus(map);
			}
			
		}catch(Exception e){
			logger.error("代收付结果返回给订单系统发生异常，errorMsg = "+e.getMessage());
		}
		
		if(resultmap != null && !resultmap.isEmpty()){
			List<OrderPayment> resultList =  (List<OrderPayment>) resultmap.get("paymentList");
			String orderNo = null;
			if(resultList !=null && resultList.size()>0){
//				for(OrderPayment orderBean : resultList){
//					orderNo = orderBean.getPaymentId();
//					SettleTransDetailQuery detailQuery = new SettleTransDetailQuery();
//					detailQuery.setOrderNo(orderNo);
//					detailQuery.setOrderType(0);
//					List<SettleTransDetail> detailBeanList = settleTransDetailManager.queryList(detailQuery);
//					if(detailBeanList !=null && detailBeanList.size()>0){
//						SettleTransDetail detailBean = detailBeanList.get(0);
//						if(detailBean.getDflag() == 60){
//							detailBean.setDflag(61);
//					    }else if(detailBean.getDflag() == 40){
//							detailBean.setDflag(41);
//						}else{
//							continue;
//						}
//						settleTransDetailManager.updateSettleTransDetail(detailBean);
//					}
//				}
			}
		}
	}
	
	/***
	 * 调用账户系统接口抹账
	 * @return
	 */
	public void doAccOfOrRefundByAccount(List<SettleTransDetail> settleTransDetailList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Iterator<SettleTransDetail> settleTransDetailIter = settleTransDetailList.iterator();
		List<String> queryProfitFuncCodes = settleTransDetailService.queryProfitFuncCodes();
		CommonResponse commonResponse = null;//mtaegis返回值
		TransOrderInfo transOrderInfo = null;//mtaegis交易结构体
		String code = null;
		String msg = null; 
		
		while(settleTransDetailIter.hasNext()) {
			/*
			 * '账户系统'交易信息结构体
			 */
			transOrderInfo = null;
			/*
			 * '清算系统'交易信息结构体
			 */
			SettleTransDetail settleTransDetail = settleTransDetailIter.next();
			String userId = settleTransDetail.getUserId();
			String productId = settleTransDetail.getProductId();
			String merchantCode = settleTransDetail.getMerchantCode();
			Long orderAmount = settleTransDetail.getAmount();
			Integer orderCount = 1;
			Date orderDate = new Date();
			String funcCode = settleTransDetail.getFuncCode();
			String orderNo = settleTransDetail.getOrderNo(); 
			String newOrderNo = "Q"+redisIdGenerator.createRequestNo();
			String roleCode = settleTransDetail.getRoleCode();
			String userIpAddress = settleTransDetail.getUserIpAddress();
			
				/**
				 * 
				 * @Description : TODO(抹帐 dubbo提供接口)
				 * @Param : 
				 * @Return : 
				 * @Creator : liuhuan
				 * @CreateTime : 2015年9月15日 下午4:46:10
				 * @Updator : 
				 * @UpdateTime :
				 */
				try {
					//ErrorResponse errorResponse = payIntOutSer.wipeAccountForDubbo(newOrderNo, orderNo, merchantCode);
					//boolean isSuccess = errorResponse.isIs_success();
					transOrderInfo = new TransOrderInfo();
					transOrderInfo.setOrderNo(newOrderNo);
					transOrderInfo.setOriginalOrderId(orderNo);
					transOrderInfo.setOrderAmount(orderAmount);
					transOrderInfo.setRootInstCd(merchantCode);
					transOrderInfo.setRoleCode(roleCode);
					transOrderInfo.setDealProductCode(SettleConstants.ACC_OF_DPC);
					transOrderInfo.setUserIpAddress(userIpAddress);
					transOrderInfo.setOrderCount(1);
					transOrderInfo.setProductId(productId);
					transOrderInfo.setOrderDate(orderDate);
					transOrderInfo.setUserId(userId);
					commonResponse = writeOffApi.execute(transOrderInfo);
					code = commonResponse.getCode();
					msg = commonResponse.getMsg();
					logger.info("调用[mtaegis抹帐接口]返回值: {code:"+ code +", msg:"+ msg +"}");
					boolean isSuccess = "1".equals(code);
					if(isSuccess){
						//接口请求失败,修改状态为21抹账成功
						settleTransDetail.setDeliverStatusId(21);
					}else{
						//接口请求失败,修改状态为20抹账失败
						settleTransDetail.setDeliverStatusId(20); 
					}
					settleTransDetailManager.updateSettleTransDetail(settleTransDetail);
				} catch (Exception e) { 
					logger.error("抹账接口异常，异常信息==="+e);  
				}
			}
	}
	
	/***
	 * 调用账户系统接口冲正
	 * @return
	 */
	public void doCorrectByAccount(List<SettleTransDetail> settleTransDetailList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Iterator<SettleTransDetail> settleTransDetailIter = settleTransDetailList.iterator();
		TransOrderInfo transOrderInfo = null;
		CommonResponse commonResponse = null;
		String code = "";
		String msg = "";
		while(settleTransDetailIter.hasNext()) {
			/**
		     * @Description : TODO(商户扣款冲正dubbo提供接口)
		     * @Params : funcCode :交易编码 商户冲正 10011
		     *           orderNo : 新订单号
		     *           userIpAddress : 用户IP地址
		     *           orderPackageNo : 订单包号 ----原定单号
		     *           rootInstCd : 机构码
		     * @Return : 
		     * @CreateTime : 2015年9月15日 下午1:51:34
		     * @Updator : 
		     * @UpdateTime :
		     */
			SettleTransDetail settleTransDetail = settleTransDetailIter.next();
			String orderNo = settleTransDetail.getOrderNo(); 
			String newOrderNo = "Q" + redisIdGenerator.createRequestNo();
			String userIpAddress = settleTransDetail.getUserIpAddress();
			Long orderAmount = settleTransDetail.getAmount();
			String roleCode = settleTransDetail.getRoleCode();
			String merchantCode = settleTransDetail.getMerchantCode();
			String productId = settleTransDetail.getProductId();
			Date nowDate = new Date();
			String userId = settleTransDetail.getUserId();
			try {
//				ErrorResponse errorResponse = payAccSerApi.antideductForDubbo(funcCode, newOrderNo, userIpAddress, orderNo, rootInstCd);
//				boolean isSuccess = errorResponse.isIs_success();
				transOrderInfo = new TransOrderInfo();
				transOrderInfo.setOrderNo(newOrderNo);
				transOrderInfo.setOriginalOrderId(orderNo);
				transOrderInfo.setOrderAmount(orderAmount);
				transOrderInfo.setRootInstCd(merchantCode);
				transOrderInfo.setRoleCode(roleCode);
				transOrderInfo.setDealProductCode(SettleConstants.ACC_OF_DPC);
				transOrderInfo.setUserIpAddress(userIpAddress);
				transOrderInfo.setOrderCount(1);
				transOrderInfo.setProductId(productId);
				transOrderInfo.setOrderDate(nowDate);
				transOrderInfo.setUserId(userId);
				commonResponse = writeOffApi.execute(transOrderInfo);
				code = commonResponse.getCode();
				msg = commonResponse.getMsg();
				logger.info("调用[mtaegis抹帐接口]返回值: {code:"+ code +", msg:"+ msg +"}");
				boolean isSuccess = "1".equals(code);
				if(isSuccess){
					//接口请求成功,修改状态为11冲正成功
					settleTransDetail.setDeliverStatusId(11);
				}else{
					//接口请求失败,修改状态为10冲正失败
					settleTransDetail.setDeliverStatusId(10); 
				}
				settleTransDetailManager.updateSettleTransDetail(settleTransDetail);
			} catch (Exception e) {
				logger.error("冲正接口异常，异常信息==="+e);
			} 
			
		}
	}
	
	/**
	 * 通过orgCode查多渠道的ORG_INFO表
	 * @param orgCode 
	 * @return
	 */
	public String getOrgNumAndChannelHome(String orgCode){
		String OrgNumAndChannelHome = null;
		try{
			OrgInfoQuery query = new OrgInfoQuery();
			query.setOrgCode(orgCode);
			List<OrgInfo> list = orgInfoManager.queryList(query);
			if(list != null && list.size() > 0){
				OrgInfo orgInfo = list.get(0);
				OrgNumAndChannelHome =  orgInfo.getOrgNum() +","+ orgInfo.getChannelHome();
			}
		}catch(Exception e){
			logger.error("通过orgCode查询多渠道的ORG_INFO表发生异常！！！！，异常信息=="+e);
		}
		return OrgNumAndChannelHome;
	}
	/**
	 * 拆分交易表的数据，决定具体调用账户的哪个接口进行代收付结果的推送
	 * @param detailwhereMap
	 */
	public void splitAccountData(Map<String, List<SettleTransDetail>> detailList,List<com.rkylin.wheatfield.bean.TransOrderInfo> param1List,List<TransOrderInfo> param2DfList,List<TransOrderInfo> param2DsList){
		try {
			if(detailList == null || detailList.size()==0){
				logger.info(">>>>返回代付结果给账户系统的方法入参detailList为空！！！！！");
				return;
			}
			List<SettleTransDetail> subList = new ArrayList<SettleTransDetail>();
			TransOrderInfo transOrderInfo = new TransOrderInfo();
			com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfoOne = null;
			if(detailList.containsKey("1")){ // 账户1期数据
				subList = detailList.get("1");
				for(SettleTransDetail detailBean : subList){
					transOrderInfoOne = new com.rkylin.wheatfield.bean.TransOrderInfo();
					transOrderInfoOne.setMerchantCode(detailBean.getMerchantCode());
					transOrderInfoOne.setOrderNo(detailBean.getOrderNo());
					transOrderInfoOne.setAmount(detailBean.getAmount());
					if(detailBean.getDflag() == 4||detailBean.getDflag() == 40){
						transOrderInfoOne.setErrorMsg(detailBean.getErrorMsg());
						transOrderInfoOne.setErrorCode(detailBean.getErrorCode());
						transOrderInfoOne.setStatus(5);//5失败
					}else if(detailBean.getDflag() == 6||detailBean.getDflag() == 60){
						transOrderInfoOne.setStatus(4);//4成功
					}else{
						logger.info(detailBean.getTransDetailId() + "一期代付结果状态异常！");
						continue;
					}
					transOrderInfoOne.setRemark(String.valueOf(detailBean.getTransDetailId()));
					logger.info("账户一期接口的请求内部参数：订单号="+transOrderInfoOne.getOrderNo()+",金额="+transOrderInfoOne.getAmount());
					param1List.add(transOrderInfoOne);
				}
			}
			if (detailList.containsKey("2")){ // 账户2期数据
				subList = detailList.get("2");
				for(SettleTransDetail detailBean : subList){
					transOrderInfo = new TransOrderInfo();
					transOrderInfo.setOrderDate(settlementUtil.getCurrentTime());
					transOrderInfo.setOrderNo(settlementUtil.createBatchNo("ST"));
					transOrderInfo.setOriginalOrderId(detailBean.getOrderNo());
					transOrderInfo.setRootInstCd(detailBean.getMerchantCode());
					if(StringUtils.isNotBlank(detailBean.getRoleCode())){
						transOrderInfo.setRoleCode(detailBean.getRoleCode());
					}
					if(detailBean.getDflag() == 4 || detailBean.getDflag() == 40){
						transOrderInfo.setErrorMsg(detailBean.getErrorMsg());
						transOrderInfo.setErrorCode(detailBean.getErrorCode());
						transOrderInfo.setStatusId(5);//5失败
					}else if(detailBean.getDflag() == 6 || detailBean.getDflag() == 60){
						transOrderInfo.setStatusId(4);//4成功
					}else{
						logger.info(detailBean.getTransDetailId() + "一分钱代付结果状态异常！");
						continue;
					}
					transOrderInfo.setUserIpAddress(detailBean.getUserIpAddress());
					transOrderInfo.setRemark(String.valueOf(detailBean.getTransDetailId()));
					
					if(SettleConstants.BATCH_DF_FUN_CODE.equals(detailBean.getFuncCode()) 
							|| SettleConstants.BATCH_TX_FUN_CODE.equals(detailBean.getFuncCode())
							|| SettleConstants.BATCH_DF_YFQ_FUN_CODE.equals(detailBean.getFuncCode())){
						transOrderInfo.setDealProductCode("MPROD_00_RS_001");
						logger.info("二期，订单号="+transOrderInfo.getOrderNo()+",原始订单号："+transOrderInfo.getOriginalOrderId()+"订单包号："+transOrderInfo.getOrderPackageNo());
						param2DfList.add(transOrderInfo);
					}else if (SettleConstants.BATCH_DS_FUN_CODE.equals(detailBean.getFuncCode())){
						transOrderInfo.setDealProductCode("MPROD_00_RS_002");
						logger.info("二期，订单号="+transOrderInfo.getOrderNo()+",原始订单号："+transOrderInfo.getOriginalOrderId()+"订单包号："+transOrderInfo.getOrderPackageNo());
						param2DsList.add(transOrderInfo);
					}else{
						logger.info("二期，原始订单号：="+transOrderInfo.getOriginalOrderId()+"的funcCode不是代收付业务的funcCode");
					}
				}
			}
		}catch (Exception e) {
			logger.error("拆分交易表的数据，决定具体调用账户的哪个接口进行代收付结果的推送发生异常,error="+e);
		}
	}
	
	/**
	 * 处理账户一期的代收付结果，推送给账户
	 * @param paramList
	 */
	public void dealDsfResultTo1Account(List<com.rkylin.wheatfield.bean.TransOrderInfo> param1List){
		try{
			int countList = 0;
			com.rkylin.wheatfield.model.CommonResponse  rtnResponse = null;
			List<com.rkylin.wheatfield.bean.TransOrderInfo> paramTmpList = new ArrayList<com.rkylin.wheatfield.bean.TransOrderInfo>();//代付2期
			if(param1List !=null && param1List.size()>0){
				countList = param1List.size();
				Integer size = 150;//每批数量
				Integer totalIndex = countList / size + 1;//总批数
				Integer batchIndex = 1;//当前批数
				Integer fromIndex = null;//开始索引 包含
				Integer toIndex = null;//结束索引 不包含
				while(totalIndex >= batchIndex) {
					try{
						fromIndex = (batchIndex - 1) * size;
						toIndex = totalIndex == batchIndex ? countList : batchIndex * size;
						paramTmpList = param1List.subList(fromIndex, toIndex);
						if(paramTmpList==null){
							continue;
						}
						logger.info("一期数据调用账户系统代收付结果推送接口，共推送"+paramTmpList.size()+"条，开始");
						
						logger.info("transOrderDubboService="+transOrderDubboService);
						if(paramTmpList.size()>0){
							logger.info("参数:merchantCode="+paramTmpList.get(0).getMerchantCode()+";orderNo="+paramTmpList.get(0).getOrderNo()+";amount="+paramTmpList.get(0).getAmount());
						}
						rtnResponse = transOrderDubboService.notifyTransOrderResults(paramTmpList);//代付结果推送给账户
						logger.info("一期数据调用账户系统代收付结果推送接口，共推送"+paramTmpList.size()+"条，结束");
						if (rtnResponse!=null && !"".equals(rtnResponse)){
							if ("1".equals(rtnResponse.getCode())) {
								logger.info("一期数据调用账户系统代收付结果推送接口，共推送"+paramTmpList.size()+"条，账户系统返回成功！");
								this.updateSend1AccountSuccess(paramTmpList);
							} else {
								this.updateSend1AccountFail(paramTmpList);
							}
						}else{
							logger.info("一期数据调用账户系统代收付结果推送接口，共推送"+paramTmpList.size()+"条，账户系统返回的对象为空！！！！！");
						}
					}catch(Exception e){
						logger.error("代付结果推送账户发生异常,error_msg:"+e);
						this.updateSend1AccountFail(paramTmpList);
					}
					batchIndex ++;
				}
			}
		}catch(Exception e){
			logger.error("组织账户一期代收付结果即将发往账户的过程中发生异常"+e);
		}
	}
	
	/**
	 * 将账户二期的代收的代收付结果推送给账户
	 */
	public void dealDsResultTo2Account(List<TransOrderInfo> param2DsList){
		try{
			int countList = 0;
			MultBaseResultReturnResponse rtnDsResponse = null;
			List<MultTransOrderInfoReturn> rtnDsList = null;
			List<TransOrderInfo> paramTmpList = new ArrayList<TransOrderInfo>();//代付2期
			if(param2DsList !=null && param2DsList.size()>0){
				countList = param2DsList.size();
				Integer size = 150;//每批数量
				Integer totalIndex = countList / size + 1;//总批数
				Integer batchIndex = 1;//当前批数
				Integer fromIndex = null;//开始索引 包含
				Integer toIndex = null;//结束索引 不包含
				while(totalIndex >= batchIndex) {
					try{
						String orderPackageNo = settlementUtil.createBatchNo("SP");
						fromIndex = (batchIndex - 1) * size;
						toIndex = totalIndex == batchIndex ? countList : batchIndex * size;
						paramTmpList = param2DsList.subList(fromIndex, toIndex);
						Iterator<TransOrderInfo> ite = paramTmpList.iterator();
						while(ite.hasNext()){
							TransOrderInfo tmpTransOrderInfo = ite.next();
							tmpTransOrderInfo.setOrderCount(paramTmpList.size());
							tmpTransOrderInfo.setOrderPackageNo(orderPackageNo);
							logger.info("二期，订单号="+tmpTransOrderInfo.getOrderNo()+",原始订单号："+tmpTransOrderInfo.getOriginalOrderId()+"订单包号："+tmpTransOrderInfo.getOrderPackageNo());
						}
						logger.info("二期代收结果推送给账户系统，开始》》》》》》,paramRList.size="+paramTmpList.size());
						rtnDsResponse = multCollectionResultReturnApi.execute(paramTmpList);//代收结果推送给账户
						logger.info("二期代收结果推送给账户系统，结束》》》》》》,paramRList.size="+paramTmpList.size());
						if (rtnDsResponse!=null && !"".equals(rtnDsResponse)){
							logger.info("二期代付结果推送给账户系统,账户系统返回的code="+rtnDsResponse.getCode());
							if (!"1".equals(rtnDsResponse.getCode())) {
								rtnDsList = rtnDsResponse.getErrList();
								if(rtnDsList !=null && rtnDsList.size()>0){
									this.updateDflagByAccountFail(paramTmpList, rtnDsList);//部分失败
									logger.info("二期代收结果推送给账户系统，账户系统有部分处理失败，共"+rtnDsList.size());
								}else{
									this.updateDflagByAccountFail(paramTmpList, null);//全部失败
									logger.info("二期代收结果推送给账户系统，账户系统有部分处理失败，但是返回的失败信息的列表为空或0条");
								}
							}else{
								logger.info("二期代收结果推送给账户系统成功");
								this.updateDflagByAccountSuccess(paramTmpList);//全部成功
							}
						}else{
							this.updateDflagByAccountFail(paramTmpList, null);//全部失败
							logger.info("二期代收结果推送给账户系统，账户系统返回的对象为空");
						}
					}catch(Exception e){
						logger.error("代收结果推送账户发生异常,error_msg:"+e.getMessage());
						this.updateDflagByAccountFail(paramTmpList, null);
					}
					batchIndex ++;
				}
			}
		}catch(Exception e){
			logger.error("组织账户二期代收结果即将发往账户的过程中发生异常"+e);
		}
	}
	
	/**
	 * 将账户二期的代付、一分钱代付、提现的代收付结果推送给账户
	 */
	public void dealDfResultTo2Account(List<TransOrderInfo> param2DfList){
		MultBaseResultReturnResponse rtnDfResponse = null;
		List<MultTransOrderInfoReturn> rtnDfList = null;
		List<TransOrderInfo> paramTmpList = new ArrayList<TransOrderInfo>();//代付2期
		int countList = 0;
		try{
			if(param2DfList !=null && param2DfList.size()>0){
				countList = param2DfList.size();
				Integer size = 150;//每批数量
				Integer totalIndex = countList / size + 1;//总批数
				Integer batchIndex = 1;//当前批数
				Integer fromIndex = null;//开始索引 包含
				Integer toIndex = null;//结束索引 不包含
				while(totalIndex >= batchIndex){
					try{
						String orderPackageNo = settlementUtil.createBatchNo("SP");
						fromIndex = (batchIndex - 1) * size;
						toIndex = totalIndex == batchIndex ? countList : batchIndex * size;
						paramTmpList = param2DfList.subList(fromIndex, toIndex);
						Iterator<TransOrderInfo> ite = paramTmpList.iterator();
						while(ite.hasNext()){
							TransOrderInfo tmpTransOrderInfo = ite.next();
							tmpTransOrderInfo.setOrderCount(paramTmpList.size());
							tmpTransOrderInfo.setOrderPackageNo(orderPackageNo);
							logger.info("二期，订单号="+tmpTransOrderInfo.getOrderNo()+",原始订单号："+tmpTransOrderInfo.getOriginalOrderId()+"订单包号："+tmpTransOrderInfo.getOrderPackageNo());
						}
						logger.info("二期代付结果推送给账户系统，开始》》》》》》,paramPList.size="+paramTmpList.size());
						rtnDfResponse = multWithholdAndWithdrowResultReturnApi.execute(paramTmpList);//代付结果推送给账户
						logger.info("二期代付结果推送给账户系统，结束》》》》》》,paramPList.size="+paramTmpList.size());
						if (rtnDfResponse!=null && !"".equals(rtnDfResponse)) {
							logger.info("二期代付结果推送给账户系统,账户系统返回的code="+rtnDfResponse.getCode());
							if (!"1".equals(rtnDfResponse.getCode())) {
								rtnDfList = rtnDfResponse.getErrList();
								if(rtnDfList !=null && rtnDfList.size()>0){
									logger.info("二期代付结果推送给账户系统，账户系统有部分处理失败，共"+rtnDfList.size());
									this.updateDflagByAccountFail(paramTmpList, rtnDfList);//部分失败
								}else{
									this.updateDflagByAccountFail(paramTmpList, null);//全部失败
									logger.info("二期代付结果推送给账户系统，账户系统有部分处理失败，但是返回的失败信息的列表为空或0条");
								}
							}else{
								logger.info("二期代付结果推送给账户系统成功");
								this.updateDflagByAccountSuccess(paramTmpList);//全部成功
							}
						}else{
							logger.info("二期代付结果推送给账户系统，账户系统返回的对象为空");
							this.updateDflagByAccountFail(paramTmpList, null);//全部失败
						}
					}catch(Exception e){
						logger.error("代付结果推送账户发生异常,error_msg:"+e.getMessage());
						this.updateDflagByAccountFail(paramTmpList, null);
					}
					batchIndex ++;
				}
			}
		}catch(Exception e){
			logger.error("组织账户二期代收付结果即将发往账户的过程中发生异常"+e);
		}
	}
	
	/**
	 * 更新账户一期代收付结果推送给账户失败的数据
	 */
	public void updateSend1AccountFail(List<com.rkylin.wheatfield.bean.TransOrderInfo> paramTmpList){
		try{
			if(paramTmpList ==null){
				logger.info("updateSend1AccountFail的参数paramTmpList==null");
				return;
			}
			SettleTransDetail detailBean = new SettleTransDetail();
			for(com.rkylin.wheatfield.bean.TransOrderInfo transBean : paramTmpList){
				logger.info("remark="+transBean.getRemark());
				detailBean.setTransDetailId(Integer.parseInt(transBean.getRemark()));
				if(transBean.getStatus() == 5){
					detailBean.setDflag(40);
				}else if(transBean.getStatus() == 4){
					detailBean.setDflag(60);
				}else{
					logger.info("transBean的status状态不是4或5！");
					continue;
				}
				logger.info("准备更新detail表 detailId="+detailBean.getTransDetailId()+";dflag="+detailBean.getDflag());
				settleTransDetailManager.updateSettleTransDetail(detailBean);
			}
			detailBean = null;
		}catch(Exception e){
			logger.error("更新账户一期代收付结果推送给账户失败的数据发生异常="+e);
		}
	
	}
	
	/**
	 * 更新账户一期代收付结果推送给账户成功的数据
	 * 注意：数据量较大的情况下可以考虑批量更新
	 */
	public void updateSend1AccountSuccess(List<com.rkylin.wheatfield.bean.TransOrderInfo> paramTmpList){
		try{
			SettleTransDetail detailBean = new SettleTransDetail();
			for(com.rkylin.wheatfield.bean.TransOrderInfo transBean : paramTmpList){
				detailBean.setTransDetailId(Integer.parseInt(transBean.getRemark()));
				if(transBean.getStatus() == 5){
					detailBean.setDflag(41);
				}else if(transBean.getStatus() == 4){
					detailBean.setDflag(61);
				}else{
					continue;
				}
				settleTransDetailManager.updateSettleTransDetail(detailBean);
			}
			detailBean = null;
		}catch(Exception e){
			logger.error("更新账户一期代收付结果推送给账户成功的数据发生异常，error="+e);
		}
	}
	
	/**
	 * 更新账户二期代收付结果推送给账户成功的数据
	 * @param paramList
	 */
	public void updateDflagByAccountSuccess(List<TransOrderInfo> paramList){
		try{
			SettleTransDetail detail = new SettleTransDetail();
			for(TransOrderInfo transBean :paramList){
				detail.setTransDetailId(Integer.parseInt(transBean.getRemark()));
				if(transBean.getStatusId() == 5){
					detail.setDflag(41);
				}else if(transBean.getStatusId() == 4){
					detail.setDflag(61);
				}else{
					continue;
				}
				settleTransDetailManager.updateSettleTransDetail(detail);
			}
			detail = null;
		}catch(Exception e){
			logger.error("更新账户二期代收付结果推送给账户成功的数据发生异常，error="+e);
		}
	}
	
	/**
	 * 更新账户二期代收付结果推送给账户失败的数据
	 * @param paramList
	 */
	public void updateDflagByAccountFail(List<TransOrderInfo> paramList,List<MultTransOrderInfoReturn> rtnList){
		try{
			SettleTransDetail detail = new SettleTransDetail();
			for(TransOrderInfo transBean :paramList){
				detail.setTransDetailId(Integer.parseInt(transBean.getRemark()));
				if (rtnList != null && rtnList.size()!=0){//部分失败
					for(MultTransOrderInfoReturn bean : rtnList){
						logger.info("orderNo="+bean.getOriginalOrderId()+",二期账户系统返回失败");
						if (transBean.getOriginalOrderId().compareTo(bean.getOriginalOrderId()) == 0) {
							if(transBean.getStatusId() == 5){
								detail.setDflag(40);
							}else if(transBean.getStatusId() == 4){
								detail.setDflag(60);
							}else{
								continue;
							}
							settleTransDetailManager.updateSettleTransDetail(detail);
							break;
						}
					}
				}else{//全部失败
					if(transBean.getStatusId() == 5){
						detail.setDflag(40);
					}else if(transBean.getStatusId() == 4){
						detail.setDflag(60);
					}else{
						continue;
					}
					settleTransDetailManager.updateSettleTransDetail(detail);
				}
			}
		}catch(Exception e){
			logger.error("更新账户二期代收付结果推送给账户失败的数据发生异常，error="+e);
		}
	}
	/**
	 * 开始查询 activeMQ的key
	 * @param parameterCode
	 * @return
	 * @throws Exception
	 */
	private String getCollectMqKey(String parameterCode) throws Exception {
		logger.info(">>> >>> >>> 开始:查询 activeMQ的key");
		String mqKey = null;
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(SettleConstants.PARAMETER_TYPE_COLLECT_MQKEY);
		query.setParameterCode(parameterCode);
		List<SettleParameterInfo> settleParameterInfoList = null;
		try {
			settleParameterInfoList = settleParameterInfoManager.queryList(query);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:查询 activeMQ的key", e);
			throw e;
		}
		if(settleParameterInfoList == null || settleParameterInfoList.size() < 1) {
			logger.error("异常:查询 activeMQ的key, settleParameterInfoList is null or settleParameterInfoList.size() < 1");
			throw new Exception("settleParameterInfoList is null or settleParameterInfoList.size() < 1");
		}
		mqKey = settleParameterInfoList.get(0).getParameterValue();
		logger.info("<<< <<< <<< 结束:查询 activeMQ的key为" + mqKey);
		return mqKey;
	}
}