package com.rkylin.settle.service;

import javax.servlet.http.HttpServletRequest;

import com.rkylin.settle.pojo.SettlewebAccountsumCompare;
import com.rkylin.settle.pojo.SettlewebAccountsumCompareQuery;
import com.rkylin.settle.util.PagerModel;

public interface SettleWebAccountSumCompareService {
	
	public PagerModel<SettlewebAccountsumCompare> queryPage(SettlewebAccountsumCompareQuery query);
	
	public boolean queryAndOutputExcl(HttpServletRequest request, SettlewebAccountsumCompareQuery query) throws Exception;
}
