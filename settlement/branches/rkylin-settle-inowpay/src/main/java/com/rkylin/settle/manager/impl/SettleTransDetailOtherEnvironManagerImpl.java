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

import com.rkylin.settle.dao.SettleTransDetailOtherEnvironDao;
import com.rkylin.settle.manager.SettleTransDetailOtherEnvironManager;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;

@Component("settleTransDetailOtherEnvironManager")
public class SettleTransDetailOtherEnvironManagerImpl implements SettleTransDetailOtherEnvironManager {
	
	@Autowired
	@Qualifier("settleTransDetailOtherEnvironDao")
	private SettleTransDetailOtherEnvironDao otherEnvironDao;
	
	@Override
	public int countSettleTransDetail(SettleTransDetailQuery settleTransDetail) {
		return otherEnvironDao.countByExample(settleTransDetail);
	}
	
	@Override
	public void saveSettleTransDetail(SettleTransDetail settleTransDetail) {
		otherEnvironDao.insertSelective(settleTransDetail);
	}
	
	@Override
	public void updateSettleTransDetail(SettleTransDetail settleTransDetail) {
		otherEnvironDao.updateByPrimaryKeySelective(settleTransDetail);
	}
	
	@Override
	public SettleTransDetail findSettleTransDetailById(Long id) {
		return otherEnvironDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransDetail> queryList(SettleTransDetailQuery query) {
		return otherEnvironDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransDetailById(Long id) {
		otherEnvironDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransDetail(SettleTransDetailQuery query) {
		otherEnvironDao.deleteByExample(query);
	}
	@Override
	public List<Map<String,Object>> queryListMap(SettleTransDetailQuery query){
		return otherEnvironDao.selectListMapByExample(query);
	}
	
	@Override
	public List<Map<String, Object>> queryListMapOE(SettleTransDetailQuery query) {
		return otherEnvironDao.selectListMapOEByExample(query);
	}
	
	@Override
	public void updateTransStatusId(Map<String, Object> map) {
		otherEnvironDao.updateTransStatusId(map);
	}
	
	@Override
	public List<Map<String,Object>> selectCollateTransInfo(Map<String, Object> map) {
		return otherEnvironDao.selectCollateTransInfo(map);
	}
	
	@Override
	public List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map) {
		return otherEnvironDao.selectProfitTransInfo(map);
	}

    @Override
    public int updateByAccountOrderInfo(SettleTransDetail settleTransDetail) {
        return otherEnvironDao.updateByAccountOrderInfo(settleTransDetail);
    }
    
    @Override
    public int updatePayChannelIdByoriginalTrans(Map<String, Object> map) {
    	return otherEnvironDao.updatePayChannelIdByoriginalTrans(map);
    }
    
    @Override
    public List<Map<String,Object>> selectBillTransInfo(Map<String, Object> map) {
    	return otherEnvironDao.selectBillTransInfo(map);
    }
    
    @Override
	public List<Map<String,Object>> selectDsfTransInfo(Map<String, Object> map) {
		return otherEnvironDao.selectDsfTransInfo(map);
	}
    
    @Override
    public void batchSaveSettleTransDetail(List<SettleTransDetail> settleTransDetailList) {
    	otherEnvironDao.batchInsertSelective(settleTransDetailList);
    }

	@Override
	public int updateMTAegisCorrectOrder(List<Integer> transDetailIds) {
		return otherEnvironDao.updateMTAegisCorrectOrder(transDetailIds);
	}

	@Override
	public int updateMTAegisWipeOutTheAccount(List<Integer> transDetailIds) {
		return otherEnvironDao.updateMTAegisWipeOutTheAccount(transDetailIds);
	}

	@Override
	public int updateMTAegisSendBack(List<Integer> transDetailIds) {
		return otherEnvironDao.updateMTAegisSendBack(transDetailIds);
	}
	
	@Override
	public int updatePayChannelIdBy4015Trans() {
		return otherEnvironDao.updatePayChannelIdBy4015Trans();
	}
	
	@Override
	public List<Integer> queryTransDetailIdsByOrderNo(List<String> orderNoList) {
		return otherEnvironDao.queryTransDetailIdsByOrderNo(orderNoList);
	}
	
	@Override
	public void updateTransAmount(Map<String, Object> map) {
		otherEnvironDao.updateTransAmount(map);
	}
	
	@Override
	public List<Map<String, Object>> collectAmountByExample(SettleTransDetailQuery query) {
		return otherEnvironDao.collectAmountByExample(query);
	}
	
	@Override
	public List<Map<String, Object>> collectShouldAmountByExample(SettleTransDetailQuery query) {
		return otherEnvironDao.collectShouldAmountByExample(query);
	}
	
	@Override
	public List<Map<String,Object>> queryCollecteListMap(Map<String, Object> map) {
		return otherEnvironDao.collecteListMap(map);
	}
	
	@Override
	public List<String> selectRootInstCds(Map<String, Object> map) {
		return otherEnvironDao.selectRootInstCds(map);
	}

	@Override
	public List<SettleTransDetail> selectByIdsArr(Map<String, Object> paramMap) {
		return otherEnvironDao.selectByIdsArr(paramMap);
	}
	
	@Override
	public int selectDsfTransCount(Map<String, Object> paramMap) {
		return otherEnvironDao.selectDsfTransCount(paramMap);
	}

	@Override
	public int updateAccountInfoToDetailInfo(Map<String, Object> map) {
		return otherEnvironDao.updateAccountInfoToDetailInfo(map);
	}

	@Override
	public List<Map<String, Object>> selectByUnKnowStatus(Map<String, Object> map) {
		return otherEnvironDao.selectByUnKnowStatus(map);
	}
	
	@Override
	public List<Map<String, Object>> summaryFeeAmount(Map<String, Object> map) {
		return otherEnvironDao.summaryFeeAmount(map);
	}

	@Override
	public int countBySHPTPage(Map<String, Object> map) {
		return otherEnvironDao.countBySHPTPage(map);
	}

	@Override
	public List<SettleTransDetail> selectBySHPTPage(Map<String, Object> map) {
		return otherEnvironDao.selectBySHPTPage(map);
	}
	
	@Override
	public List<SettleTransDetail> selectAllBySHPT(Map<String, Object> map) {
		return otherEnvironDao.selectAllBySHPT(map);
	}
	
	@Override
	public int batchUpdateSettleTransDetailDflag(Map<String, Object> map) {
		return otherEnvironDao.batchUpdateSettleTransDetailDflag(map);
	}
}

