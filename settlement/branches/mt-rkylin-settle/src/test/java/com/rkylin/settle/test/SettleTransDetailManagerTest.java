/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;

public class SettleTransDetailManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleTransDetailManager")
	private SettleTransDetailManager settleTransDetailManager;


	public void testNewSettleTransDetail() {
		SettleTransDetail SettleTransDetail = new SettleTransDetail();
//		SettleTransDetail.setAccountingNo("1111111");
		settleTransDetailManager.saveSettleTransDetail(SettleTransDetail);
	}


	public void testUpdateSettleTransDetail(){
		SettleTransDetail SettleTransDetail = new SettleTransDetail();
//		SettleTransDetail.setId(2l);
//		SettleTransDetail.setAccountingNo("222z");
		settleTransDetailManager.saveSettleTransDetail(SettleTransDetail);
	}
	

	public void testDeleteSettleTransDetail(){
		settleTransDetailManager.deleteSettleTransDetailById(99L);
	}
	

	public void testDeleteSettleTransDetailByQuery(){
		SettleTransDetailQuery query = new SettleTransDetailQuery();
//		query.setAccountingNo("1111111");
		settleTransDetailManager.deleteSettleTransDetail(query);
	}


	public void testFindSettleTransDetailById(){
		SettleTransDetailQuery query = new SettleTransDetailQuery();
//		query.setAccountingNo("1111111");
		int size = settleTransDetailManager.queryList(query).size();
		System.out.println(size);
	}
}
