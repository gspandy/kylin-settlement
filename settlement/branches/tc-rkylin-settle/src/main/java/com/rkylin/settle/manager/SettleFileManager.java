/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileQuery;

public interface SettleFileManager {
	void saveSettleFile(SettleFile settleFile);

	void updateSettleFile(SettleFile settleFile);
	
	SettleFile findSettleFileById(Long id);
	
	List<SettleFile> queryList(SettleFileQuery query);
	
	void deleteSettleFileById(Long id);
	
	void deleteSettleFile(SettleFileQuery query);
}
