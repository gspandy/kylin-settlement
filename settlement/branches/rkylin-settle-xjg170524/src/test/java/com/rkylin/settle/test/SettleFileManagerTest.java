/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleFileManager;
import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileQuery;

public class SettleFileManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleFileManager")
	private SettleFileManager settleFileManager;


	public void testNewSettleFile() {
		SettleFile SettleFile = new SettleFile();
//		SettleFile.setAccountingNo("1111111");
		settleFileManager.saveSettleFile(SettleFile);
	}


	public void testUpdateSettleFile(){
		SettleFile SettleFile = new SettleFile();
		SettleFile.setFileId(2);
//		SettleFile.setAccountingNo("222z");
		settleFileManager.saveSettleFile(SettleFile);
	}
	

	public void testDeleteSettleFile(){
		settleFileManager.deleteSettleFileById(99L);
	}
	

	public void testDeleteSettleFileByQuery(){
		SettleFileQuery query = new SettleFileQuery();
//		query.setAccountingNo("1111111");
		settleFileManager.deleteSettleFile(query);
	}


	public void testFindSettleFileById(){
		SettleFileQuery query = new SettleFileQuery();
//		query.setAccountingNo("1111111");
		int size = settleFileManager.queryList(query).size();
		System.out.println(size);
	}
}
