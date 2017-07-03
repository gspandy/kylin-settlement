package com.rkylin.settle.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleFileColumnManager;
import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.pojo.SettleFileColumnQuery;
import com.rkylin.settle.service.SettleFileMatchService;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleFileMatchServiceImpl
 * 类描述：文件-文件列分配业务逻辑
 * 创建人：CLF
 * 创建时间：2015年9月15日 下午2:47:35
 * 修改人：
 * 修改时间：2015年9月15日 下午2:47:35
 * 修改备注：
 * @version
 */
@Service("settleFileMatchService")
public class SettleFileMatchServiceImpl implements SettleFileMatchService {
    private Logger logger = LoggerFactory.getLogger(SettleFileMatchServiceImpl.class);
	@Autowired
	private SettleFileColumnManager settleFileColumnManager;
	
	/***
     * 查询file对应的、未分配的列明细，根据fileSubId
     * 未分配是指：detailId为-1
     * @param String
     * @return
     */
    @Override
    public List<SettleFileColumn> findByFileSubId(Integer fileSubId) {
        SettleFileColumnQuery query = new SettleFileColumnQuery();
        query.setFileSubId(fileSubId);
        return settleFileColumnManager.queryDefaultAndByFileSubId(query);
    }
    /***
     * 根据分配页面操作数据，重新分配文件-文件列
     * @param String matchVal(fileColumnId:true,fileColumnId:false)
     * @return
     */
    @Override
    public boolean updateByMatch(String matchVal,Integer fileSubId){
        try {
            String[] matchs = matchVal.split(",");
            SettleFileColumn record = new SettleFileColumn();
            for(String rule:matchs){
                String[] ruleStr = rule.split(":");
                //文件列ID
                record.setFileColumnId(Integer.parseInt(ruleStr[0]));
                //更新后的fileSubId
                if("true".equals(ruleStr[1])){
                    record.setFileSubId(fileSubId);
                }else{
                    record.setFileSubId(-1);
                }
                settleFileColumnManager.updateFileSubIdByMatch(record);
            }
            return true;
        } catch (Exception e) {
            logger.error("文件-文件列分配失败",e);
            return false;
        }
    }
}
