/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileQuery;

public interface SettleFileManager {
	int saveSettleFile(SettleFile settleFile);

	SettleFile findSettleFileById(Long id);
	
	List<SettleFile> queryList(SettleFileQuery query);
	
	int deleteSettleFileById(Long id);
	
	void deleteSettleFile(SettleFileQuery query);
	
	List<SettleFile> queryPage(SettleFileQuery query);
    
    int countByExample(SettleFileQuery query);
}
