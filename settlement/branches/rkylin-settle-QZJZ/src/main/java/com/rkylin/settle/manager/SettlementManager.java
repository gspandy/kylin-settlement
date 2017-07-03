package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleBatchManage;

public interface SettlementManager {
	public List<SettleBatchManage> selectbatchno(SettleBatchManage paraMap);
	public void batInsKerDetRel(List<Map<String, Object>> list);
}
