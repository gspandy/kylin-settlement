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

import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;
import com.rkylin.settle.service.SettleProfitKeyService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleProfitKeyController
 * 类描述：清分规则管理
 * 创建人：CLF
 * 创建时间：2015年9月7日 下午3:53:47
 * 修改人：
 * 修改时间：2015年9月7日 下午3:53:47
 * 修改备注：
 * @version
 */
@Controller
@RequestMapping("/profitkey")
@Scope("prototype")
public class SettleProfitKeyController extends BaseController {
	@Autowired
	private SettleProfitKeyService settleProfitKeyService;
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleProfitKeyQuery query) throws Exception {
		try {
			PagerModel<SettleProfitKey> pagerModel = settleProfitKeyService.query(query);
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
	@RequestMapping("/key_manager")
	public String openQuery() {
		return "/settlement/profit/key/query";
	}
	/***
     * 打开详情页面
     * @param id
     * @throws Exception
     */
    @RequestMapping("/key_open_view")
    public ModelAndView openView(Long id) {
        ModelAndView modelAndView = new ModelAndView("/settlement/profit/key/view");
        modelAndView.addObject("settleProfitKey", settleProfitKeyService.findById(id));
        return modelAndView;
    }
    /***
     * 打开新增页面
     * @param query
     * @throws Exception
     */
    @RequestMapping("/key_open_add")
    public String openAdd() {
        return "/settlement/profit/key/add";
    }
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/key_open_edit")
	public ModelAndView openEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/profit/key/edit");
		modelAndView.addObject("settleProfitKey", settleProfitKeyService.findById(id));
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
        resultNum = settleProfitKeyService.delById(id);
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
	public void editAjax(SettleProfitKey settleProfitKey) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer resultNum = 0;
		if(settleProfitKey.getProfitKeyId()!=null){
		    resultNum = settleProfitKeyService.saveOrEdit(settleProfitKey);
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
    public void saveAjax(SettleProfitKey settleProfitKey) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer resultNum = settleProfitKeyService.saveOrEdit(settleProfitKey);
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
