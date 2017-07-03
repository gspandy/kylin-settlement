package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleBatchManage;

public interface SettlementDao {

	List<SettleBatchManage> selectbatchno(SettleBatchManage paraMap);

}
