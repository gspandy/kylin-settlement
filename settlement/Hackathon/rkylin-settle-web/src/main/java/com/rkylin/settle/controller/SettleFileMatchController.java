package com.rkylin.settle.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.service.SettleFileMatchService;
import com.rop.utils.StringUtils;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleFileMatchController
 * 类描述：文件分配管理
 * 创建人：CLF
 * 创建时间：2015年9月15日 下午2:35:23
 * 修改人：
 * 修改时间：2015年9月15日 下午2:35:23
 * 修改备注：
 * @version
 */
@Controller
@RequestMapping("/filematch")
@Scope("prototype")
public class SettleFileMatchController extends BaseController {
	@Autowired
	private SettleFileMatchService settleFileMatchService;
	
    @RequestMapping("/match_manager")
    public String openMatch() {
        return "/settlement/file/match";
    }
    
    /***
     * 分配查询
     * @param query
     * @throws Exception
     */
    @RequestMapping("/search_ajax")
    public void searchAjax(Integer id) throws Exception {
        try {
            List<SettleFileColumn> fileColumnList = settleFileMatchService.findByFileSubId(id);
            Map<String,Object> map = new HashMap<String, Object>();
            if(!fileColumnList.isEmpty()){
                map.put("dataList", fileColumnList);
                map.put("result", "ok");
            }else{
                map.put("result", "error");
                map.put("msg", "系统异常");
            }
            writeJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /***
     * 分配操作
     * @param query
     * @throws Exception
     */
    @RequestMapping("/match_operation_ajax")
    public void updateAjax(String matchVal,Integer id) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean flag = false;
        
        if(!StringUtils.isEmpty(matchVal) && id!=null){
            flag = settleFileMatchService.updateByMatch(matchVal,id);
        }
        String msg = "";
        
        if(flag) {
            msg = "分配成功!";
        } else {
            msg = "分配失败!";
        }
        
        resultMap.put("isSuccess", flag);
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
