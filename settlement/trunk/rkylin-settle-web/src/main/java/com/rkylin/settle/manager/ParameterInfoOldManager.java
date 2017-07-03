/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoOld;
import com.rkylin.settle.pojo.ParameterInfoOldQuery;

public interface ParameterInfoOldManager {
	void saveParameterInfo(ParameterInfoOld parameterInfo);

	ParameterInfoOld findParameterInfoById(Long id);
	
	List<ParameterInfoOld> queryList(ParameterInfoOldQuery query);
	
	void deleteParameterInfoById(Long id);
	
	void deleteParameterInfo(ParameterInfoOldQuery query);
}
