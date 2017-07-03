package com.rkylin.settle.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;
import com.rkylin.settle.service.SettleTransBillService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleTransBillController
 * 类描述：挂账交易信息管理
 * 创建人：CLF
 * 创建时间：2015年8月27日 下午1:47:23
 * 修改人：
 * 修改时间：2015年8月27日 下午1:47:23
 * 修改备注：
 * @version
 */
@Controller
@RequestMapping("/transbill")
@Scope("prototype")
public class SettleTransBillController extends BaseController {
	@Autowired
	private SettleTransBillService settleTransBillService;
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleTransBillQuery query) throws Exception {
		try {
			PagerModel<SettleTransBill> pagerModel = settleTransBillService.query(query);
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
	@RequestMapping("/bill_manager")
	public String openQuery() {
		return "/settlement/trans/bill/query";
	}
	/***
     * 打开详情页面
     * @param id
     * @throws Exception
     */
    @RequestMapping("/bill_open_view")
    public ModelAndView openView(Long id) {
        ModelAndView modelAndView = new ModelAndView("/settlement/trans/bill/view");
        modelAndView.addObject("settleTransBill", settleTransBillService.findById(id));
        return modelAndView;
    }
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/bill_open_edit")
	public ModelAndView openEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/trans/bill/edit");
		modelAndView.addObject("settleTransBill", settleTransBillService.findById(id));
		return modelAndView;
	}
	/***
	 * 修改
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/edit_ajax")
	public void editAjax(SettleTransBill settleTransBill) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer resultNum = settleTransBillService.edit(settleTransBill);
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
}
