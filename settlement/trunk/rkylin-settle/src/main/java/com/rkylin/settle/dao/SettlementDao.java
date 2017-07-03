package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleBatchManage;

public interface SettlementDao {
	public List<SettleBatchManage> selectbatchno(SettleBatchManage paraMap);
	public void batInsKerDetRel(List<Map<String, Object>> list);
}
