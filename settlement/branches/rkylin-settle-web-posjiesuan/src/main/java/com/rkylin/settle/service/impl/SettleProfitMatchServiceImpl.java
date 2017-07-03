package com.rkylin.settle.service.impl;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.controller.BaseController;
import com.rkylin.settle.manager.SettleProfitRuleManager;
import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleProfitRuleQuery;
import com.rkylin.settle.service.SettleProfitMatchService;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleProfitRuleServiceImpl
 * 类描述：清分规则明细业务逻辑
 * 创建人：CLF
 * 创建时间：2015年8月31日 下午2:48:31
 * 修改人：
 * 修改时间：2015年8月31日 下午2:48:31
 * 修改备注：
 * @version
 */
@Service("settleProfitMatchService")
public class SettleProfitMatchServiceImpl implements SettleProfitMatchService {
    private Logger logger = LoggerFactory.getLogger(BaseController.class);
	@Autowired
	private SettleProfitRuleManager settleProfitRuleManager;
	
    /***
     * 查询key对应的、未分配的规则明细，根据keyId
     * 未分配是指：detailId为-1
     * @param Integer
     * @return
     */
    @Override
    public List<SettleProfitRule> findByprofitDetailId(String profitdetailId) {
        return settleProfitRuleManager.findByprofitDetailId(profitdetailId);
    }
    /***
     * 根据分配页面操作数据，重新分配规则-明细
     * @param String matchVal(profitDetailId:subId:createdTime:是否分配,profitDetailId:subId:createdTime:是否分配)
     * @return
     */
    @Override
    public boolean updateByMatch(String matchVal,String profitDetailId){
        try {
            String[] matchs = matchVal.split(",");
            SettleProfitRuleQuery query = new SettleProfitRuleQuery();
            for(String rule:matchs){
                String[] ruleStr = rule.split(":");
                //规则明细ID
                query.setProfitDetailId(ruleStr[0]);
                //子ID
                query.setSubId(ruleStr[1]);
                //创建时间
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(Long.parseLong(ruleStr[2]));
                query.setCreatedTime(cal.getTime());
                //更新后的规则明细ID
                if("true".equals(ruleStr[3])){
                    query.setObligate1(profitDetailId);
                }else{
                    query.setObligate1("-1");
                }
            settleProfitRuleManager.updateProfitDetailIdByMatch(query);
            }
            return true;
        } catch (Exception e) {
            logger.error("清分规则分配失败",e);
            return false;
        }
    }
}
