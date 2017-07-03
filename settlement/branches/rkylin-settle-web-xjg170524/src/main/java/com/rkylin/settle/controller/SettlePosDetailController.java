package com.rkylin.settle.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.allinpay.ets.client.StringUtil;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.service.SettleTransDetailService;
import com.rkylin.settle.settleInterface.SettleWebInterface;
import com.rkylin.settle.util.PagerModel;

/**
 * @Description: 交易结果管理
 * @author Yang
 * @Create Time: 2015-6-12下午12:59:48
 * @version V1.00
 */
@Controller
@RequestMapping("/posdetail")
@Scope("prototype")
public class SettlePosDetailController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(SettleTransDetailController.class);
	@Autowired
	private SettleTransDetailService settleTransDetailService;
	@Autowired
	private SettleWebInterface settleWebInterface;
	@Autowired
	private RedisIdGenerator redisIdGenerator;
	
	/***
	 * 分页查询
	 * @param query
	 * @throws Exceptionmulti_gate_aja
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleTransDetailQuery query) throws Exception {
		try {
//			PagerModel<SettleTransDetail> pagerModel = settleTransDetailService.query(query);
//			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 打开查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/detail_manager")
	public String openQuery() {
		return "/settlement/trans/detail/query";
	}
	/***
	 * 打开查询页面带业务按钮
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/detail_manager_settler")
	public String openQueryBySettler() {
		return "/settlement/trans/detail/query_settler";
	}
	/***
     * 打开详情页面
     * @param id
     * @throws Exception
     */
    @RequestMapping("/detail_open_view")
    public ModelAndView openView(Long id) {
        ModelAndView modelAndView = new ModelAndView("/settlement/trans/detail/view");
        modelAndView.addObject("settleTransDetail", settleTransDetailService.findById(id));
        return modelAndView;
    }
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/detail_open_edit")
	public ModelAndView openEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/trans/detail/edit");
		modelAndView.addObject("settleTransDetail", settleTransDetailService.findById(id));
		return modelAndView;
	}
	
	/***
	 * 打开代收付修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/detail_open_dsf_edit")
	public ModelAndView openDsfEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/trans/detail/dsf_edit");
		modelAndView.addObject("settleTransDetail", settleTransDetailService.findById(id));
		return modelAndView;
	}
	
	/***
	 * 修改
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/edit_ajax")
	public void editAjax(SettleTransDetail settleTransDetail) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer resultNum = settleTransDetailService.edit(settleTransDetail);
		Boolean isSuccess = false;
		String msg = "";
		
		if(resultNum > 0) {
			isSuccess = true;
			msg = "修改成功!";
		} else {
			isSuccess = false;
			msg = "修改失败!";
		}
		
		resultMap.put("isSuccess", isSuccess);
		resultMap.put("msg", msg);
		
		try {
			writeJson(resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		    logger.error("返回值JSON序列化失败", e);
		}
	}
	
	/***
	 * 打开读取‘账户系统’交易信息
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/openGetAccTraOrdInfo")
	public String openGetAccTraOrdInfo() {
		return "/settlement/trans/detail/get_acc_tra_ord_info";
	}
	/***
	 * 打开读取‘账户系统一期’交易信息
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/getAccOldTraOrdInfo")
	public void getAccOldTraOrdInfo(String requestDateStrBegin, String requestDateStrEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date checkStartTime = null;
		Date checkEndTime = null;
		try {
			checkStartTime = sdf.parse(requestDateStrBegin);
			checkEndTime = sdf.parse(requestDateStrEnd);
		} catch (ParseException e) {
			logger.error(">>> >>> 异常: 调用 清结算接口, 读取‘账户系统一期’交易信息异常!", e);
			e.printStackTrace();
		}
		try {
			settleWebInterface.getAccountOldTransOrderInfos(checkStartTime, checkEndTime);
		} catch (Exception e1) {
			logger.error(">>> >>> 异常: 调用 清结算接口, 读取‘账户系统一期’交易信息异常!", e1);
			e1.printStackTrace();
		}
		try {
			SettleTransDetailQuery query = new SettleTransDetailQuery();
			query.setRequestDateStrBegin(requestDateStrBegin);
			query.setRequestDateStrEnd(requestDateStrEnd);
			PagerModel<SettleTransDetail> pagerModel = settleTransDetailService.query(query);
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		    logger.error("读取‘账户系统一期’交易信息失败", e);
		}
	}
	/***
	 * 打开读取‘账户系统二期’交易信息
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/getAccTraOrdInfo")
	public void getAccTraOrdInfo(String requestDateStrBegin, String requestDateStrEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date checkStartTime = null;
		Date checkEndTime = null;
		try {
			checkStartTime = sdf.parse(requestDateStrBegin);
			checkEndTime = sdf.parse(requestDateStrEnd);
		} catch (ParseException e) {
			logger.error(">>> >>> 异常: 调用 清结算接口, 读取‘账户系统二期’交易信息异常!", e);
			e.printStackTrace();
		}
		try {
			settleWebInterface.getAccountTransOrderInfos(checkStartTime, checkEndTime);
		} catch (Exception e1) {
			logger.error(">>> >>> 异常: 调用 清结算接口, 读取‘账户系统二期’交易信息异常!", e1);
			e1.printStackTrace();
		}
		try {
			SettleTransDetailQuery query = new SettleTransDetailQuery();
			query.setRequestDateStrBegin(requestDateStrBegin);
			query.setRequestDateStrEnd(requestDateStrEnd);
			PagerModel<SettleTransDetail> pagerModel = settleTransDetailService.query(query);
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		    logger.error("读取‘账户系统二期’交易信息失败", e);
		}
	}
	/***
	 * 存储过程获取T-1‘账户系统一期’交易信息
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/getAccOldTraOrdInfoBySP")
	public void getAccOldTraOrdInfoBySP() {
		String msg = "执行完成, 无异常!";
		try {
			settleWebInterface.readAccOldDate();
		} catch (Exception e) {
			e.printStackTrace();
			msg = "失败! 执行方法异常!";
			logger.error(">>> >>> >>> >>> 异常: 调用存储过程获取 账户系统 一期 交易信息!", e);
		}
		try {
			writeText(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> >>> >>> 异常: AJAX返回值异常!", e);
		}
	}
	/***
	 * 存储过程获取T-1‘账户系统 二期’交易信息
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/getAccTraOrdInfoBySP")
	public void getAccTraOrdInfoBySP() {
		String msg = "执行完成, 无异常!";
		try {
			settleWebInterface.readAccDate();
		} catch (Exception e) {
			e.printStackTrace();
			msg = "失败! 执行方法异常!";
			logger.error(">>> >>> >>> >>> 异常: 调用存储过程获取 账户系统 二期 交易信息!", e);
		}
		try {
			writeText(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> >>> >>> 异常: AJAX返回值异常!", e);
		}
	}
	/**
	 * 冲正
	 * @param ids
	 */
	@RequestMapping("/do_correct")
	public void doCorrect(Integer[] ids) {
		List<SettleTransDetail> settleTransDetailList = settleTransDetailService.queryByIds(ids);
		Map<String, Object> messageMap = null;
		try {
			messageMap = settleTransDetailService.doCorrectByAccount(settleTransDetailList);	
		} catch (Exception e) {
			e.printStackTrace();
		    logger.error("冲正交易失败", e);
		}
		try {
		    String msg = "";
		    if(messageMap != null){
		        msg = String.valueOf(messageMap.get("msg"));     
		    }else{
		        msg = "系统异常，接口返回值为空";
		    }
			writeText(msg);
		} catch (Exception e) {
			e.printStackTrace();
		    logger.error("冲正交易，调用接口失败，返回值为空", e);
		}
	}
	/***
	 * 抹账 || 交易后退款
	 * @param ids
	 */
	@RequestMapping("/do_accof_or_refund")
	public void doAccofOrRefund(Integer[] ids) {
		List<SettleTransDetail> settleTransDetailList = settleTransDetailService.queryByIds(ids);
		Map<String, Object> messageMap = null;
		try {
			messageMap = settleTransDetailService.doAccOfOrRefundByAccount(settleTransDetailList);
		} catch (Exception e) {
			e.printStackTrace();
		    logger.error("抹账 || 交易后退款交易失败", e);
		}
		try {
		    String msg = "";
		    if(messageMap != null){
		        msg = String.valueOf(messageMap.get("msg"));
		    }else{
		        msg = "系统异常，接口返回值为空";
		    }
			writeText(msg);
		} catch (Exception e) {
			e.printStackTrace();
		    logger.error("抹账 || 交易后退款交易，调用接口失败，返回值为空", e);
		}
	}
	/***
	 * 清分
	 */
	@RequestMapping("/profit")
	public void doProfit() {
		String resultMsg = "";
		try {
			settleWebInterface.doProfit();
			resultMsg = "success";
		} catch (Exception e) {
			resultMsg = "fail";
			e.printStackTrace();
		}
		try {
			writeText(resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 分润结算
	 */
	@RequestMapping("/profit_balance")
	public void doProfitBalance(String[] ids) {
		String resultMsg = "";
		Map<String, Object> resultMap = null;
		String code = null;
		String msg = null;
		try {
			resultMap = settleWebInterface.doProfigBalance(ids);
			code = String.valueOf(resultMap.get("code"));
			msg = String.valueOf(resultMap.get("msg"));
			resultMsg = msg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			writeText(resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
     * 打开读取‘多渠道’交易信息
     * @param query
     * @throws Exception
     */
    @RequestMapping("/order_open_view")
    public String openGetOrder() {
        return "/settlement/trans/pos/order_view";
    }
    /***
     * 打开读取‘多渠道’交易信息
     * @param query
     * @throws Exception
     */
    @RequestMapping("/order_ajax")
    public void getOrder(Date accountDate) {
        try {
            Map<String,String> json = new HashMap<String,String>();
            if(accountDate==null){
                json.put("result", "error");
                json.put("msg", "账期不允许为空");
                writeJson(json);
            }else{
                //从多渠道读取交易信息并存入'清算'DB
                Map<String, Object> map = settleWebInterface.getPosDetailFromOrder(accountDate);
//                //刷新页面
//                SettleTransDetailQuery query = new SettleTransDetailQuery();
//                query.setAccountDate(accountDate);
//                query.setDataFrom(3);
//                PagerModel<SettleTransDetail> pagerModel = settleTransDetailService.query(query);
                  PagerModel<SettleTransDetail> pagerModel = new PagerModel<SettleTransDetail>();
                //code值  1:成功, 0:失败, -1:异常
                if("-1".equals(map.get("code").toString()) && map.get("msg").toString().length()>50){
                    pagerModel.setMsg("读取‘多渠道’交易信息,部分数据发生错误");
                }else{
                    pagerModel.setMsg(map.get("msg").toString());
                }
                returnJsonList(pagerModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /***
     * 挂账
     */
    @RequestMapping("/bill")
    public void doBill(Integer[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = settleTransDetailService.doBill(ids);
            if(!map.containsKey("error")){
                map.put("result", "success");
            }
        } catch (Exception e) {
        	e.printStackTrace();
            map.put("result", "error");
            map.put("msg", "挂账操作异常，请联系管理员！");
            logger.error("挂账失败",e);
        }
        try {
            writeJson(map);
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("转换JSON失败",e);
        }
    }
    /***
     * 取消挂账
     */
    @RequestMapping("/cancel_bill")
    public void doCancelBill(Integer[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = settleTransDetailService.doCancelBill(ids);
            if(!map.containsKey("error")){
                map.put("result", "success");
            }
        } catch (Exception e) {
        	e.printStackTrace();
            map.put("result", "error");
            map.put("msg", "取消挂账操作异常，请联系管理员！");
            logger.error("取消挂账失败",e);
        }
        try {
            writeJson(map);
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("转换JSON失败",e);
        }
    }
    
    /***
	 * 打开代收付失败单的查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/dsf_manager")
	public String openDsfQuery() {
		return "/settlement/trans/detail/dsf_query";
	}
	
	/***
	 * 代收付分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_dsf_ajax")
	public void queryDsfAjax(SettleTransDetailQuery query) throws Exception {
		try {
			if(query.getDflag() == null){
				query.setDflag(33);//33查询全部 
			}
			if("".equals(query.getRequestDateStrBegin())){
				query.setRequestDateStrBegin(null);
			}
			if("".equals(query.getRequestDateStrEnd())){
				query.setRequestDateStrEnd(null);
			}
			
			query.setReadStatusId(1);
			query.setOrderType(0);
			query.setNotStatusId(99);
			if(StringUtils.isBlank(query.getFuncCode())){
				query.setFuncCodes(new String[]{"4013","4014","4014_1","4016"});
			}
			
			
		/*	if("40130".equals(query.getFuncCode())){//贷款还款,是特殊的代收，funcCode跟代收一样,但是busi是1
				query.setFuncCode("4013");
				query.setBusinessType("1");
			}*/
			
			PagerModel<SettleTransDetail> pagerModel = settleTransDetailService.query(query);
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /*
     * 表单提交日期绑定
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    
    /**
     * 代收付查询总金额
     * @param ids
     */
	@RequestMapping("/query_total_amount")
	public void queryTotalAmount(Integer[] ids) {
		String resultMsg = "";
		Long amount = 0L;
		try {
			List<SettleTransDetail>  settleTransDetailList= settleTransDetailService.queryByIds(ids);
			for(SettleTransDetail bean : settleTransDetailList){
				amount +=  bean.getAmount();
			}
			resultMsg = String.valueOf(amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			writeText(resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * 发送代收付系统
     * @param ids
     */
	@RequestMapping("/summaryByIds")
	public void summaryByIds(Integer[] ids) {
		String resultMsg = "";

		try {
			logger.info("通过交易表的ID汇总代收付数据,开始调用后台服务");
			settleWebInterface.summaryByIds(ids);
			logger.info("通过交易表的ID汇总代收付数据,结束调用后台服务");
			resultMsg = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			writeText(resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 代收付结果推送给订单系统
	 * @param ids
	 */
	@RequestMapping("/send_order")
	public void manualSendOrder(Integer[] ids) {
		String resultMsg = "";
		try {
			settleWebInterface.manualSendOrder(ids);
			resultMsg = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			writeText(resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /***
	 * 代收付手工汇总或发送
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/dsf_manual_summary_or_send")
	public String dsfSummaryOrSend(){
		return "/settlement/trans/detail/dsf_manual_summary_or_send";
	}
	
	/**
	 * 汇总
	 * @param inRootInstCds 机构号
	 * @param inFlag 标记  "0"代付和提现,"1"代收和有T0标记的代付
	 */
	@RequestMapping("/summary")
	public void summaryDetail(String inRootInstCds,String inFlag) {
		String resultMsg = "";
		try {
			String[] rootInstCds = null;
			String[] funcCodes = null;
			
			if("2".equals(inFlag)){
				funcCodes = new String[1];
				funcCodes[0] = "4013";
			}
			if("4".equals(inFlag)){
				funcCodes = new String[1];
				funcCodes[0] = "4014_1";
			}
			
			if(inRootInstCds.contains(",")){
				rootInstCds =  inRootInstCds.split(",");
			}else{
				rootInstCds = new String[1];
				rootInstCds[0] =  inRootInstCds;
			}
			logger.info("inFlag="+inFlag+",+rootInstCds="+rootInstCds[0]+",funcCodes="+funcCodes);
			settleWebInterface.manualSendToDsf(null,inFlag,rootInstCds,funcCodes);//null,0,M000005,null
			resultMsg = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			writeText(resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 退票失败的或异常的手工重发给账户系统
	 * @param ids
	 */
	@RequestMapping("/send_account_tp")
	public void manualSendAccountTP(Integer[] ids) {
		String resultMsg = "";
		try {
			settleWebInterface.manualSendAccountTP(ids,new Integer[]{1,99});
			resultMsg = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			writeText(resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
   /***
	 * 账户跟多渠道对账页面跳转
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/collecte_acc_and_multi_query")
	public String collecteAccAndMultiQuery(){
		return "/settlement/trans/detail/collecte_acc_and_multi_query";
	}
	
	/**
	 * 账户跟多渠道对账
	 * @param ids
	 */
	@RequestMapping("/collecte_acc_and_multi")
	public void collecteAccAndMulti(String inRootInstCd, Date accountDate) throws Exception {
		String resultMsg = "";
		if(StringUtil.isEmpty(inRootInstCd)) {writeText("机构号不能为空!"); return;}
		if(accountDate == null) {writeText("账期不能为空!"); return;}
		if("all".equalsIgnoreCase(inRootInstCd)) {inRootInstCd = null;}
		
		try {
			settleWebInterface.collecteAccAndMulti(inRootInstCd, accountDate);
			resultMsg = "success";
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("账户跟多渠道对账 异常:", e);
		}
		writeText(resultMsg);
	}
	
    /***
	 * 从代收付系统读取结果
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/dsf_result_query")
	public String dsfResultQuery(){
		return "/settlement/trans/detail/dsf_result_query";
	}
	
	/***
	 * 打开查询还款交易手续费并更新订单系统页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/open_select_repay_update_order")
	public String openSelectRepayUpdateOrder(){
		return "/settlement/trans/pos/select_repay_update_order";
	}
	/***
	 * 执行查询还款交易手续费并更新订单系统页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/select_repay_update_order")
	public void selectRepayUpdateOrder(String accountDate)  throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
    	try{
	    	if(StringUtils.isEmpty(accountDate)){
	        	throw new Exception("执行查询还款交易手续费并更新订单系统页面异常!");
	        }	
	    	SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		settleWebInterface.selectPosTransFeeAndUpdateOrder(format.parse(accountDate));
    		resultMap.put("msg", "success");
    	}catch(Exception e){
    		logger.error("testYFQSummary发生异常，e="+e);
    	}
		try {
			writeJson(resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
