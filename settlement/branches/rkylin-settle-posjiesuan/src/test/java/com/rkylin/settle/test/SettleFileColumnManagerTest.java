/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleFileColumnManager;
import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.pojo.SettleFileColumnQuery;

public class SettleFileColumnManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleFileColumnManager")
	private SettleFileColumnManager settleFileColumnManager;


	public void testNewSettleFileColumn() {
		SettleFileColumn SettleFileColumn = new SettleFileColumn();
//		SettleFileColumn.setAccountingNo("1111111");
		settleFileColumnManager.saveSettleFileColumn(SettleFileColumn);
	}


	public void testUpdateSettleFileColumn(){
		SettleFileColumn SettleFileColumn = new SettleFileColumn();
		SettleFileColumn.setFileColumnId(2);
//		SettleFileColumn.setAccountingNo("222z");
		settleFileColumnManager.saveSettleFileColumn(SettleFileColumn);
	}
	

	public void testDeleteSettleFileColumn(){
		settleFileColumnManager.deleteSettleFileColumnById(99L);
	}
	

	public void testDeleteSettleFileColumnByQuery(){
		SettleFileColumnQuery query = new SettleFileColumnQuery();
//		query.setAccountingNo("1111111");
		settleFileColumnManager.deleteSettleFileColumn(query);
	}


	public void testFindSettleFileColumnById(){
		SettleFileColumnQuery query = new SettleFileColumnQuery();
//		query.setAccountingNo("1111111");
		int size = settleFileColumnManager.queryList(query).size();
		System.out.println(size);
	}
}
