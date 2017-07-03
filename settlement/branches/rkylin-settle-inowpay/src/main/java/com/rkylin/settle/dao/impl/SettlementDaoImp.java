package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettlementDao;
import com.rkylin.settle.pojo.SettleBatchManage;

@Repository("settlementDao")
public class SettlementDaoImp  extends BaseDao implements SettlementDao {
	
	@Override
	public List<SettleBatchManage> selectbatchno(SettleBatchManage paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectbatchno", paraMap);
	}

	@Override
	public void batInsKerDetRel(List<Map<String, Object>> list) {
		super.getSqlSession().insert("SettlementMapper.batInsKerDetRel", list);
	}
}
