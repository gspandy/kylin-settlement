/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleTransDetailDao;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;

@Component("settleTransDetailManager")
public class SettleTransDetailManagerImpl implements SettleTransDetailManager {
	
	@Autowired
	@Qualifier("settleTransDetailDao")
	private SettleTransDetailDao settleTransDetailDao;
	
	@Override
	public int countSettleTransDetail(SettleTransDetailQuery settleTransDetail) {
		return settleTransDetailDao.countByExample(settleTransDetail);
	}
	
	@Override
	public void saveSettleTransDetail(SettleTransDetail settleTransDetail) {
		settleTransDetailDao.insertSelective(settleTransDetail);
	}
	
	@Override
	public void updateSettleTransDetail(SettleTransDetail settleTransDetail) {
		settleTransDetailDao.updateByPrimaryKeySelective(settleTransDetail);
	}
	
	@Override
	public SettleTransDetail findSettleTransDetailById(Long id) {
		return settleTransDetailDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransDetail> queryList(SettleTransDetailQuery query) {
		return settleTransDetailDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransDetailById(Long id) {
		settleTransDetailDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransDetail(SettleTransDetailQuery query) {
		settleTransDetailDao.deleteByExample(query);
	}
	@Override
	public List<Map<String,Object>> queryListMap(SettleTransDetailQuery query){
		return settleTransDetailDao.selectListMapByExample(query);
	}
	
	@Override
	public void updateTransStatusId(Map<String, Object> map) {
		settleTransDetailDao.updateTransStatusId(map);
	}
	
	@Override
	public List<Map<String,Object>> selectCollateTransInfo(Map<String, Object> map) {
		return settleTransDetailDao.selectCollateTransInfo(map);
	}
	
	@Override
	public List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map) {
		return settleTransDetailDao.selectProfitTransInfo(map);
	}

    @Override
    public int updateByAccountOrderInfo(SettleTransDetail settleTransDetail) {
        return settleTransDetailDao.updateByAccountOrderInfo(settleTransDetail);
    }
    
    @Override
    public int updatePayChannelIdByoriginalTrans(Map<String, Object> map) {
    	return settleTransDetailDao.updatePayChannelIdByoriginalTrans(map);
    }
    
    @Override
    public List<Map<String,Object>> selectBillTransInfo(Map<String, Object> map) {
    	return settleTransDetailDao.selectBillTransInfo(map);
    }
    
    @Override
	public List<Map<String,Object>> selectDsfTransInfo(Map<String, Object> map) {
		return settleTransDetailDao.selectDsfTransInfo(map);
	}
    
    @Override
    public void batchSaveSettleTransDetail(List<SettleTransDetail> settleTransDetailList) {
    	settleTransDetailDao.batchInsertSelective(settleTransDetailList);
    }

	@Override
	public int updateMTAegisCorrectOrder(List<Integer> transDetailIds) {
		return settleTransDetailDao.updateMTAegisCorrectOrder(transDetailIds);
	}

	@Override
	public int updateMTAegisWipeOutTheAccount(List<Integer> transDetailIds) {
		return settleTransDetailDao.updateMTAegisWipeOutTheAccount(transDetailIds);
	}

	@Override
	public int updateMTAegisSendBack(List<Integer> transDetailIds) {
		return settleTransDetailDao.updateMTAegisSendBack(transDetailIds);
	}
	
	@Override
	public int updatePayChannelIdBy4015Trans() {
		return settleTransDetailDao.updatePayChannelIdBy4015Trans();
	}
	
	@Override
	public List<Integer> queryTransDetailIdsByOrderNo(List<String> orderNoList) {
		return settleTransDetailDao.queryTransDetailIdsByOrderNo(orderNoList);
	}
	
	@Override
	public void updateTransAmount(Map<String, Object> map) {
		settleTransDetailDao.updateTransAmount(map);
	}
	
	@Override
	public List<Map<String, Object>> collectAmountByExample(SettleTransDetailQuery query) {
		return settleTransDetailDao.collectAmountByExample(query);
	}
	
	@Override
	public List<Map<String, Object>> collectShouldAmountByExample(SettleTransDetailQuery query) {
		return settleTransDetailDao.collectShouldAmountByExample(query);
	}
	
	@Override
	public List<Map<String,Object>> queryCollecteListMap(Map<String, Object> map) {
		return settleTransDetailDao.collecteListMap(map);
	}
	
	@Override
	public List<String> selectRootInstCds(Map<String, Object> map) {
		return settleTransDetailDao.selectRootInstCds(map);
	}

	@Override
	public List<SettleTransDetail> selectByIdsArr(Map<String, Object> paramMap) {
		return settleTransDetailDao.selectByIdsArr(paramMap);
	}
	
	@Override
	public int selectDsfTransCount(Map<String, Object> paramMap) {
		return settleTransDetailDao.selectDsfTransCount(paramMap);
	}

	@Override
	public int updateAccountInfoToDetailInfo(Map<String, Object> map) {
		return settleTransDetailDao.updateAccountInfoToDetailInfo(map);
	}

	@Override
	public List<Map<String, Object>> selectByUnKnowStatus(Map<String, Object> map) {
		return settleTransDetailDao.selectByUnKnowStatus(map);
	}
	
	@Override
	public List<Map<String, Object>> summaryFeeAmount(Map<String, Object> map) {
		return settleTransDetailDao.summaryFeeAmount(map);
	}

	@Override
	public int countBySHPTPage(Map<String, Object> map) {
		return settleTransDetailDao.countBySHPTPage(map);
	}

	@Override
	public List<SettleTransDetail> selectBySHPTPage(Map<String, Object> map) {
		return settleTransDetailDao.selectBySHPTPage(map);
	}
	
	@Override
	public List<SettleTransDetail> selectAllBySHPT(Map<String, Object> map) {
		return settleTransDetailDao.selectAllBySHPT(map);
	}
	
	@Override
	public int batchUpdateSettleTransDetailDflag(Map<String, Object> map) {
		return settleTransDetailDao.batchUpdateSettleTransDetailDflag(map);
	}
}

