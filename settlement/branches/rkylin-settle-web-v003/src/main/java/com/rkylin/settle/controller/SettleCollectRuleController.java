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

import com.rkylin.settle.pojo.SettleCollectRule;
import com.rkylin.settle.pojo.SettleCollectRuleQuery;
import com.rkylin.settle.service.SettleCollectRuleService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleRuleController
 * 类描述：汇总规则管理
 * 创建人：Yang
 * 修改备注：
 * @version
 */
@Controller
@RequestMapping("/collect_rule")
@Scope("prototype")
public class SettleCollectRuleController extends BaseController {
	@Autowired
	private SettleCollectRuleService settleCollectRuleService;
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleCollectRuleQuery query) throws Exception {
		try {
			PagerModel<SettleCollectRule> pagerModel = settleCollectRuleService.query(query);
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
		return "/settlement/collect_rule/query";
	}
	/***
     * 打开详情页面
     * @param id
     * @throws Exception
     */
    @RequestMapping("/rule_open_view")
    public ModelAndView openView(Long id) {
        ModelAndView modelAndView = new ModelAndView("/settlement/rule/view");
        modelAndView.addObject("settleRule", settleCollectRuleService.findById(id));
        return modelAndView;
    }
    /***
     * 打开新增页面
     * @param query
     * @throws Exception
     */
    @RequestMapping("/rule_open_add")
    public String openAdd() {
        return "/settlement/collect_rule/add";
    }
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/rule_open_edit")
	public ModelAndView openEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/collect_rule/edit");
		modelAndView.addObject("settleCollectRule", settleCollectRuleService.findById(id));
		return modelAndView;
	}
	/***
     * 删除
     * @param query
     * @throws Exception
     */
    @RequestMapping("/del_ajax")
    public void delAjax(Long id) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int resultNum = 0;
        if(id!=null)
        resultNum = settleCollectRuleService.delById(id);
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
	public void editAjax(SettleCollectRule settleCollectRule) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer resultNum = 0;
		if(settleCollectRule.getId() != null){
		    resultNum = settleCollectRuleService.saveOrEdit(settleCollectRule);
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
    public void saveAjax(SettleCollectRule settleCollectRule) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer resultNum = settleCollectRuleService.saveOrEdit(settleCollectRule);
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
