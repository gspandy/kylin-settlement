/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoQuery;

public interface ParameterInfoManager {
	void saveParameterInfo(ParameterInfo parameterInfo);

	ParameterInfo findParameterInfoById(Long id);
	
	List<ParameterInfo> queryList(ParameterInfoQuery query);
	
	void deleteParameterInfoById(Long id);
	
	void deleteParameterInfo(ParameterInfoQuery query);
}
