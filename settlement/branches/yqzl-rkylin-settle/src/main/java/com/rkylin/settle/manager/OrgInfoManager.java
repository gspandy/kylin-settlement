/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.OrgInfo;
import com.rkylin.settle.pojo.OrgInfoQuery;

public interface OrgInfoManager {
	void saveOrgInfo(OrgInfo orgInfo);

	OrgInfo findOrgInfoById(Long id);
	
	List<OrgInfo> queryList(OrgInfoQuery query);
	
	void deleteOrgInfoById(Long id);
	
	void deleteOrgInfo(OrgInfoQuery query);
}
