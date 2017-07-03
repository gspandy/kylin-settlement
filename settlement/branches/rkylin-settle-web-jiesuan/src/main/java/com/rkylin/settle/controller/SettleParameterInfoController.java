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

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.service.SettleParameterInfoService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleParameterInfoController
 * 类描述：系统参数信息管理
 * 创建人：CLF
 * 创建时间：2015年10月13日 下午3:07:17
 * 修改人：
 * 修改时间：2015年10月13日 下午3:07:17
 * 修改备注：
 * @version
 */
@Controller
@RequestMapping("/parameterinfo")
@Scope("prototype")
public class SettleParameterInfoController extends BaseController {
	@Autowired
	private SettleParameterInfoService settleParameterInfoService;
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleParameterInfoQuery query) throws Exception {
		try {
			PagerModel<SettleParameterInfo> pagerModel = settleParameterInfoService.query(query);
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
	@RequestMapping("/query_manager")
	public String openQuery() {
		return "/settlement/parameterinfo/query";
	}
    /***
     * 打开新增页面
     * @param query
     * @throws Exception
     */
    @RequestMapping("/open_add")
    public String openAdd() {
        return "/settlement/parameterinfo/add";
    }
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/open_edit")
	public ModelAndView openEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/parameterinfo/edit");
		modelAndView.addObject("settleParameterInfo", settleParameterInfoService.findById(id));
		return modelAndView;
	}
	/***
	 * 修改
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/edit_ajax")
	public void editAjax(SettleParameterInfo settleParameterInfo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer resultNum = 0;
		if(settleParameterInfo.getParameterId()!=null){
		    resultNum = settleParameterInfoService.saveOrEdit(settleParameterInfo);
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
    public void saveAjax(SettleParameterInfo settleParameterInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer resultNum = settleParameterInfoService.saveOrEdit(settleParameterInfo);
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
    /**
     * ajax 获取机构号
     */
    @RequestMapping("/get_root_inst_cd_select")
    public void getRootInstCdSelect() {
    	try {
	    	SettleParameterInfoQuery query = new SettleParameterInfoQuery();
	    	query.setParameterType(SettleConstants.PARAMETER_DOWN_MERCHANT);
	    	query.setStatusId(1);
			writeJson(settleParameterInfoService.select(query));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:获取机构号", e);
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
