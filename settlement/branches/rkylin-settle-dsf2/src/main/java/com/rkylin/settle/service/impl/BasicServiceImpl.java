package com.rkylin.settle.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicServiceImpl {
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(BasicServiceImpl.class);
	/***
	 * 编辑对账返回值
	 * @param resultMap		返回值Map
	 * @param code			信息编码	1:成功, 0:失败, -1:异常, 其他 ... ...
	 * @param msg			信息内容
	 * @return
	 */
	protected Map<String, Object> editResultMap(Map<String, Object> resultMap, String code, String msg) {
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		return resultMap;
	}
}
