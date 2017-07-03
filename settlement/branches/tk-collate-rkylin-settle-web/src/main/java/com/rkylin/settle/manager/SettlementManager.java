package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleBatchManage;

public interface SettlementManager {

	List<SettleBatchManage> selectbatchno(SettleBatchManage paraMap);
}
