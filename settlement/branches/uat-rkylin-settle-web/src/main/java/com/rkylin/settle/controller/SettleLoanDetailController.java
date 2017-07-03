package com.rkylin.settle.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.settle.pojo.SettleLoanDetail;
import com.rkylin.settle.pojo.SettleLoanDetailQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.service.SettleLoanDetailService;
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
@RequestMapping("/loandetail")
@Scope("prototype")
public class SettleLoanDetailController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(SettleLoanDetailController.class);
	@Autowired
	private SettleTransDetailService settleTransDetailService;
	@Autowired
	private SettleLoanDetailService settleLoanDetailService;
	@Autowired
	private SettleWebInterface settleWebInterface;
	@Autowired
	private RedisIdGenerator redisIdGenerator;
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleLoanDetailQuery query) throws Exception {
		try {
			PagerModel<SettleLoanDetail> pagerModel = settleLoanDetailService.query(query);
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 打开查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/loan_manager")
	public String openQuery() {
		return "/settlement/trans/loan/query";
	}
	/***
     * 打开详情页面
     * @param id
     * @throws Exception
     */
    @RequestMapping("/detail_open_view")
    public ModelAndView openView(Long id) {
        ModelAndView modelAndView = new ModelAndView("/settlement/trans/loan/view");
        modelAndView.addObject("settleLoanDetail", settleLoanDetailService.findById(id));
        return modelAndView;
    }
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/detail_open_edit")
	public ModelAndView openEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/trans/loan/edit");
		modelAndView.addObject("settleLoanDetail", settleLoanDetailService.findById(id));
		return modelAndView;
	}
	/***
	 * 修改
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/edit_ajax")
	public void editAjax(SettleLoanDetail settleLoanDetail) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer resultNum = settleLoanDetailService.edit(settleLoanDetail);
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
	 * 打开读取‘账户系统’交易信息
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
			e.printStackTrace();
		}
		try {
			settleWebInterface.getAccountTransOrderInfos(checkStartTime, checkEndTime);
		} catch (Exception e1) {
			logger.error(">>> >>> 异常: 调用 清结算接口, 读取‘账户系统’交易信息异常!");
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
		    logger.error("读取‘账户系统’交易信息失败", e);
		}
	}
	/***
	 * 存储过程获取T-1‘账户系统’交易信息
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
			logger.error(">>> >>> >>> >>> 异常: 调用存储过程获取 账户系统交易信息!");
		}
		try {
			writeText(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> >>> >>> 异常: AJAX返回值异常!");
		}
	}
	/**
	 * 生成代付数据
	 * @param ids
	 */
	@RequestMapping("/do_correct")
	public void doCorrect() {
	    logger.info("生成代付数据");
		Map<String, Object> messageMap = null;
		try {
			messageMap = settleWebInterface.doSettleInvoice();
		} catch (Exception e) {
			e.printStackTrace();
		    logger.error("生成代付数据失败", e);
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
		    logger.error("生成代付数据，调用接口失败，返回值为空", e);
		}
	}
	/***
	 * 发送代付交易
	 * @param ids
	 */
	@RequestMapping("/do_accof_or_refund")
	public void doAccofOrRefund() {
		Map<String, Object> messageMap = null;
		try {
			messageMap = settleWebInterface.doInvoiceSettle();
		} catch (Exception e) {
			e.printStackTrace();
		    logger.error("发送代付交易失败", e);
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
		    logger.error("发送代付交易，调用接口失败，返回值为空", e);
		}
	}
	/***
	 * 清分
	 */
	@RequestMapping("/profit")
	public void doProfit() {
		String resultMsg = "";
		try {
			settleWebInterface.doProfitForLoan();
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
	 * 清分
	 */
	@RequestMapping("/billDate")
	public void doBill() {
		String resultMsg = "";
		try {
			settleWebInterface.getLoanTransOrderInfos(null);
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
		try {
			settleWebInterface.doProfigBalanceForLoan();
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
     * 打开读取‘多渠道’交易信息
     * @param query
     * @throws Exception
     */
    @RequestMapping("/multi_gate_open_view")
    public String openGetMultiGate() {
        return "/settlement/trans/detail/multi_gate_view";
    }
    /***
     * 打开读取‘多渠道’交易信息
     * @param query
     * @throws Exception
     */
    @RequestMapping("/multi_gate_ajax")
    public void getMultiGate(Date accountDate) {
        try {
            Map<String,String> json = new HashMap<String,String>();
            if(accountDate==null){
                json.put("result", "error");
                json.put("msg", "账期不允许为空");
                writeJson(json);
            }else{
                //从多渠道读取交易信息并存入'清算'DB
                Map<String, Object> map = settleWebInterface.getTransDetailFromMultiGate(accountDate);
                //刷新页面
                SettleTransDetailQuery query = new SettleTransDetailQuery();
                query.setAccountDate(accountDate);
                query.setDataFrom(3);
                PagerModel<SettleTransDetail> pagerModel = settleTransDetailService.query(query);
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
     * 头寸交易发送
     */
    @RequestMapping("/cancel_bill")
    public void doCancelBill(Integer[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = settleWebInterface.doInvoiceSettleForCash();
            if(!map.containsKey("error")){
                map.put("result", "success");
            }
        } catch (Exception e) {
        	e.printStackTrace();
            map.put("result", "error");
            map.put("msg", "头寸交易发送异常，请联系管理员！");
            logger.error("头寸交易发送失败",e);
        }
        try {
            writeJson(map);
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("转换JSON失败",e);
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
}
