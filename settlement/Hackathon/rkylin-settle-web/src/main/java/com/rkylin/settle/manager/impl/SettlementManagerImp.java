package com.rkylin.settle.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettlementDao;
import com.rkylin.settle.manager.SettlementManager;
import com.rkylin.settle.pojo.SettleBatchManage;

@Component("settlementManager")
public class SettlementManagerImp implements SettlementManager {

	@Autowired
	@Qualifier("settlementDao")
	private SettlementDao settlementDao;


	@Override
	public List<SettleBatchManage> selectbatchno(SettleBatchManage paraMap) {
		return settlementDao.selectbatchno(paraMap);
	}
}
