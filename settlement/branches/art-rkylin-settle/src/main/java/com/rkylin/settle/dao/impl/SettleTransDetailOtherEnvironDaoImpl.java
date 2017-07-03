/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.common.OtherEnvironBaseDao;
import com.rkylin.settle.dao.SettleTransDetailOtherEnvironDao;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;

@Repository("settleTransDetailOtherEnvironDao")
public class SettleTransDetailOtherEnvironDaoImpl extends OtherEnvironBaseDao implements SettleTransDetailOtherEnvironDao {
	
	@Override
	public int countByExample(SettleTransDetailQuery example) {
		return super.getSqlSession().selectOne("SettleTransDetailMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleTransDetailQuery example) {
		return super.getSqlSession().delete("SettleTransDetailMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleTransDetailMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleTransDetail record) {
		return super.getSqlSession().insert("SettleTransDetailMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleTransDetail record) {
		return super.getSqlSession().insert("SettleTransDetailMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleTransDetail> selectByExample(SettleTransDetailQuery example) {
		return super.getSqlSession().selectList("SettleTransDetailMapper.selectByExample", example);
	}
	
	@Override
	public SettleTransDetail selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleTransDetailMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleTransDetail record) {
		return super.getSqlSession().update("SettleTransDetailMapperCu.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleTransDetail record) {
		return super.getSqlSession().update("SettleTransDetailMapper.updateByPrimaryKey", record);
	}
	
	/***
	 * 自定义
	 */
	@Override
	public List<Map<String,Object>> selectListMapByExample(SettleTransDetailQuery example){
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectListMapByExample", example);
	}
	
	//TODO 自定义其他环境查询交易信息
	@Override
	public List<Map<String, Object>> selectListMapOEByExample(SettleTransDetailQuery example) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectListMapOEByExample", example);
	}
	
	@Override
	public int updateTransStatusId(Map<String, Object> map) {
		return super.getSqlSession().update("SettleTransDetailMapperCu.updateTransStatusId", map);
	}
	
	@Override
	public List<Map<String,Object>> selectCollateTransInfo(Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectCollateTransInfo", map);
	}
	
	@Override
	public List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectProfitTransInfo", map);
	}

    @Override
    public int updateByAccountOrderInfo(SettleTransDetail record) {
        return super.getSqlSession().update("SettleTransDetailMapperCu.updateByAccountOrderInfo", record);
    }
    
    @Override
    public List<Map<String,Object>> selectBillTransInfo(Map<String, Object> map) {
    	return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectBillTransInfo", map);
    }
    
    @Override
	public List<Map<String,Object>> selectDsfTransInfo(Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectDsfTransInfo", map);
	}
    
    @Override
    public int batchInsertSelective(List<SettleTransDetail> settleTransDetailList) {
    	return super.getSqlSession().insert("SettleTransDetailMapperCu.batchInsertSelective", settleTransDetailList);
    }

	@Override
	public int updateMTAegisCorrectOrder(List<Integer> transDetailIds) {
		return super.getSqlSession().update("SettleTransDetailMapperCu.updateMTAegisCorrectOrder", transDetailIds);
	}

	@Override
	public int updateMTAegisWipeOutTheAccount(List<Integer> transDetailIds) {
		return super.getSqlSession().update("SettleTransDetailMapperCu.updateMTAegisWipeOutTheAccount", transDetailIds);
	}

	@Override
	public int updateMTAegisSendBack(List<Integer> transDetailIds) {
		return super.getSqlSession().update("SettleTransDetailMapperCu.updateMTAegisSendBack", transDetailIds);
	}
	
	@Override
	public int updatePayChannelIdBy4015Trans() {
		return super.getSqlSession().update("SettleTransDetailMapperCu.updatePayChannelIdBy4015Trans");
	}
	
	@Override
	public List<Integer> queryTransDetailIdsByOrderNo(List<String> orderNoList) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.queryTransDetailIdsByOrderNo", orderNoList);
	}
	
	@Override
	public int updateTransAmount(Map<String, Object> map) {
		return super.getSqlSession().update("SettleTransDetailMapperCu.updateTransAmount", map);
	}
	
	@Override
	public int updatePayChannelIdByoriginalTrans(Map<String, Object> map) {
		return super.getSqlSession().update("SettleTransDetailMapperCu.updatePayChannelIdByoriginalTrans", map);
	}
	
	@Override
	public List<Map<String,Object>> collectAmountByExample(SettleTransDetailQuery query) {
		List<Map<String, Object>> list = super.getSqlSession().selectList("SettleTransDetailMapperCu.collectAmountByExample", query);
		if(list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	
	@Override
	public List<Map<String,Object>> collectShouldAmountByExample(SettleTransDetailQuery query) {
		List<Map<String, Object>> list = super.getSqlSession().selectList("SettleTransDetailMapperCu.collectShouldAmountByExample", query);
		if(list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	@Override
	public List<Map<String,Object>> collecteListMap(Map<String, Object> map){
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectCollateDetail", map);
	}
	
    @Override
	public List<String> selectRootInstCds(Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectRootInstCds", map);
	}

	@Override
	public List<SettleTransDetail> selectByIdsArr(Map<String, Object> paramMap) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectByIdsArr", paramMap);
	}
	
	@Override
	public int selectDsfTransCount(Map<String, Object> paramMap) {
		return super.getSqlSession().selectOne("SettleTransDetailMapperCu.selectDsfTransCount", paramMap);
	}

	@Override
	public int updateAccountInfoToDetailInfo(Map<String, Object> map) {
		int resultInt = 0;
		int resultInt2 = 0;
		
		/*
		 * 同步 除连连快捷外的 手续费和渠道,协议
		 */
		try {
			resultInt = super.getSqlSession().update("SettleTransDetailMapperCu.updateAccountInfoToDetailInfo", map);
		} catch (Exception e) {
			e.printStackTrace();
			resultInt = 0;
		}
		/*
		 * 同步 连连快捷 手续费和渠道,协议
		 */
		try {
			resultInt2 = super.getSqlSession().update("SettleTransDetailMapperCu.updateAccountInfoToDetailInfo04", map);
		} catch (Exception e) {
			e.printStackTrace();
			resultInt2 = 0;
		}
		
		return resultInt + resultInt2;
	}
	
	@Override
	public List<Map<String, Object>> summaryFeeAmount(Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.summaryFeeAmount", map);
	}
	
	@Override
	public List<Map<String, Object>> selectByUnKnowStatus(Map<String, Object> paramMap) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectByUnKnowStatus", paramMap);
	}

	@Override
	public int countBySHPTPage(Map<String, Object> map) {
		return super.getSqlSession().selectOne("SettleTransDetailMapperCu.countBySHPTPage", map);
	}

	@Override
	public List<SettleTransDetail> selectBySHPTPage(Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectBySHPTPage", map);
	}
	
	@Override
	public List<SettleTransDetail> selectAllBySHPT(Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransDetailMapperCu.selectAllBySHPT", map);
	}
	
	@Override
	public int batchUpdateSettleTransDetailDflag(Map<String, Object> map) {
		return super.getSqlSession().update("SettleTransDetailMapperCu.batchUpdateSettleTransDetailDflag", map);
	}
}
