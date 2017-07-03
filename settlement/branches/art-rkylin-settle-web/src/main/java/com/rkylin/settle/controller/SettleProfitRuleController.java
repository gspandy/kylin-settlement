package com.rkylin.settle.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleProfitRuleQuery;
import com.rkylin.settle.service.SettleProfitRuleService;
import com.rkylin.settle.util.PagerModel;
import com.rop.utils.StringUtils;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleProfitRuleController
 * 类描述：清分规则明细管理
 * 创建人：CLF
 * 创建时间：2015年9月7日 下午3:57:15
 * 修改人：
 * 修改时间：2015年9月7日 下午3:57:15
 * 修改备注：
 * @version
 */
@Controller
@RequestMapping("/profitrule")
@Scope("prototype")
public class SettleProfitRuleController extends BaseController {
	@Autowired
	private SettleProfitRuleService settleProfitRuleService;
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleProfitRuleQuery query) throws Exception {
		try {
			PagerModel<SettleProfitRule> pagerModel = settleProfitRuleService.query(query);
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
	@RequestMapping("/rule_manager")
	public String openQuery() {
		return "/settlement/profit/rule/query";
	}
    /***
     * 打开新增页面
     * @param query
     * @throws Exception
     */
    @RequestMapping("/rule_open_add")
    public String openAdd() {
        return "/settlement/profit/rule/add";
    }
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/rule_open_edit")
	public ModelAndView openEdit(String profitDetailId,String subId) {
		ModelAndView modelAndView = new ModelAndView("/settlement/profit/rule/edit");
		SettleProfitRuleQuery query = new SettleProfitRuleQuery();
        query.setProfitDetailId(profitDetailId);
        query.setSubId(subId);
		modelAndView.addObject("settleProfitRule", settleProfitRuleService.findById(query));
		return modelAndView;
	}
	/***
     * 删除
     * @param query
     * @throws Exception
     */
    @RequestMapping("/del_ajax")
    public void delAjax(String profitDetailId,String subId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        SettleProfitRule record = new SettleProfitRule();
        int resultNum = 0;
        if(!StringUtils.isEmpty(profitDetailId) && !StringUtils.isEmpty(subId)){
            record.setProfitDetailId(profitDetailId);
            record.setSubId(subId);
            resultNum = settleProfitRuleService.delById(record);
        }
        Boolean isSuccess = false;
        String msg = "";
        
        if(resultNum > 0) {
            isSuccess = true;
            msg = "删除成功!";
        } else {
            isSuccess = false;
            msg = "删除失败!";
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
	 * 修改
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/edit_ajax")
	public void editAjax(SettleProfitRule settleProfitRule) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer resultNum = 0;
		if(!StringUtils.isEmpty(settleProfitRule.getProfitDetailId()) && !StringUtils.isEmpty(settleProfitRule.getSubId())){
		    resultNum = settleProfitRuleService.edit(settleProfitRule);
		}
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
     * 新增
     * @param query
     * @throws Exception
     */
    @RequestMapping("/save_ajax")
    public void saveAjax(SettleProfitRule settleProfitRule) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer resultNum = settleProfitRuleService.save(settleProfitRule);
        Boolean isSuccess = false;
        String msg = "";
        
        if(resultNum > 0) {
            isSuccess = true;
            msg = "添加成功!";
        } else {
            isSuccess = false;
            msg = "添加失败!";
        }
        
        resultMap.put("isSuccess", isSuccess);
        resultMap.put("msg", msg);
        
        try {
            writeJson(resultMap);
        } catch(Exception e) {
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
}
