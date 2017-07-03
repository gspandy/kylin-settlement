/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;

public class SettleParameterInfoManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleParameterInfoManager")
	private SettleParameterInfoManager settleParameterInfoManager;


	public void testNewSettleParameterInfo() {
		SettleParameterInfo SettleParameterInfo = new SettleParameterInfo();
//		SettleParameterInfo.setAccountingNo("1111111");
		settleParameterInfoManager.saveSettleParameterInfo(SettleParameterInfo);
	}


	public void testUpdateSettleParameterInfo(){
		SettleParameterInfo SettleParameterInfo = new SettleParameterInfo();
//		SettleParameterInfo.setId(2l);
//		SettleParameterInfo.setAccountingNo("222z");
		settleParameterInfoManager.saveSettleParameterInfo(SettleParameterInfo);
	}
	

	public void testDeleteSettleParameterInfo(){
		settleParameterInfoManager.deleteSettleParameterInfoById(99L);
	}
	

	public void testDeleteSettleParameterInfoByQuery(){
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
//		query.setAccountingNo("1111111");
		settleParameterInfoManager.deleteSettleParameterInfo(query);
	}


	public void testFindSettleParameterInfoById(){
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
//		query.setAccountingNo("1111111");
		int size = settleParameterInfoManager.queryList(query).size();
		System.out.println(size);
	}
}
