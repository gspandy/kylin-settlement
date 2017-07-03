package com.rkylin.settle.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rkylin.settle.pojo.SettleKernelEntry;
import com.rkylin.settle.pojo.SettleKernelEntryQuery;
import com.rkylin.settle.service.SettleKernelEntryService;
import com.rkylin.settle.settleInterface.SettleWebInterface;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleKernelEntryController
 * 类描述：上游交易信息查询
 * 创建人：CLF
 * 创建时间：2015年8月24日 下午3:07:50
 * 修改人：
 * 修改时间：2015年8月24日 下午3:07:50
 * 修改备注：
 * @version
 */
@Controller
@RequestMapping("/settle_kernel_entry")
@Scope("prototype")
public class SettleKernelEntryController extends BaseController {
	@Autowired
	private SettleKernelEntryService settleKernelEntryService;
	@Autowired
    private SettleWebInterface settleWebInterface;
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleKernelEntryQuery query) throws Exception {
		try {
			PagerModel<SettleKernelEntry> pagerModel = settleKernelEntryService.query(query);
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
	@RequestMapping("/settle_kernel_entry_manager")
	public String openQuery() {
		return "/settlement/kernel_entry/query";
	}
	/***
     * 打开详情页面
     * @param id
     * @throws Exception
     */
    @RequestMapping("/settle_kernel_entry_open_view")
    public ModelAndView openView(Long id) {
        ModelAndView modelAndView = new ModelAndView("/settlement/kernel_entry/view");
        modelAndView.addObject("settleKernelEntry", settleKernelEntryService.findById(id));
        return modelAndView;
    }
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/settle_kernel_entry_open_edit")
	public ModelAndView openEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/kernel_entry/edit");
		modelAndView.addObject("settleKernelEntry", settleKernelEntryService.findById(id));
		return modelAndView;
	}
	/***
	 * 修改
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/edit_ajax")
	public void editAjax(SettleKernelEntry settleKernelEntry) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer resultNum = settleKernelEntryService.saveOrEdit(settleKernelEntry);
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
		}
	}
	/***
	 * 失败交易重发
	 */
	@RequestMapping("/do_settle_kernel_entry")
	public void doSettleKernelEntry(String idsStr) {
		logger.info(">>> >>> >>> 开始  汇总信息 重新发送到 会计系统 记账 ID["+ idsStr +"]");
		String[] idsArr = idsStr.split(",");
		Map<String, Object> resultMap = null;
		Long id = null;
		String code = "";
		String msg = "";
		String rsMsg = "";
		
		for(String idStr : idsArr) {
			id = Long.parseLong(idStr);
			try {
				resultMap = settleWebInterface.doCollectBySettleKernelEntry(id);
				code = String.valueOf(resultMap.get("code"));
				msg = String.valueOf(resultMap.get("msg"));
				logger.info(">>> >>> ID:"+ id +", 调用清结算服务 doCollectBySettleKernelEntry 返回值 ["
						+ "code:"
						+ code 
						+ ", msg:" 
						+ msg
						+ "]");
				if(!"1".equals(code)) {
					logger.error("记会计账 异常 [ID:"+ id +"]:" + msg);
					rsMsg += "," + idStr;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("记会计账 异常:", e);
				rsMsg += "," + idStr;
			}
		}
		
		if("".equals(rsMsg)) {
			rsMsg = "成功";
		} else {
			rsMsg = "发送完成, 失败ID:[" + rsMsg.substring(1) + "]";
		}
		
		try {
			writeJson(rsMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束  汇总信息 重新发送到 会计系统 记账 ID["+ idsStr +"]");
	}
	/**
	 * 从汇总画面发起汇总
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/do_collect_by_rule")
	public void doCollectByRule(String collectType, String merchantCode, String payChannelId, String funcCode, String accountDateStr) {
		logger.info(">>> >>> >>> 开始 从汇总画面发起汇总 [{collectType:"+collectType+"},{merchantCode:"+ merchantCode +"},{payChannelId:"+ payChannelId +"},{funcCode:"+ funcCode +"},{accountDateStr:"+ accountDateStr +"}]");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date accountDate = sdf.parse(accountDateStr);
			settleWebInterface.doCollectByPage(Integer.parseInt(collectType), accountDate, merchantCode, payChannelId, funcCode);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常 从汇总画面发起汇总 ", e);
		}
		logger.info("<<< <<< <<< 结束 从汇总画面发起汇总 [{collectType:"+collectType+"},{merchantCode:"+ merchantCode +"},{payChannelId:"+ payChannelId +"},{funcCode:"+ funcCode +"},{accountDateStr:"+ accountDateStr +"}]");
	}
	/**
	 * 从交易画面发起汇总
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/do_collect_by_trans")
	public void doCollectByTrans(String collectType, String idsStr) throws Exception {
		logger.info(">>> >>> >>> 开始 从交易画面发起汇总 [{collectType:"+collectType+"},{idsStr:"+ idsStr +"}]");
		try {
			String[] idsStrArr = idsStr.split(",");
			Integer length = idsStrArr.length;
			Integer[] ids = new Integer[length];
			for(int i = 0; i < length; i ++) {
				ids[i] = Integer.parseInt(idsStrArr[i]);
			}
			settleWebInterface.doCollectByPage(Integer.parseInt(collectType), ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常 从汇总画面发起汇总 ", e);
			throw e;
		}
		logger.info("<<< <<< <<< 结束 从交易画面发起汇总 [{collectType:"+collectType+"},{idsStr:"+ idsStr +"}]");
	}
}
