package com.rkylin.settle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.service.SettleParameterInfoService;

@Service("settleParameterInfoService")
public class SettleParameterInfoServiceImpl implements SettleParameterInfoService {
	private Logger logger = LoggerFactory.getLogger(SettleParameterInfoService.class);
	
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;
	
	@Override
	public Map<String, String> selectPayChannelIdInfo() {
		return this.selectPayChannelIdInfo(null);
	}

	@Override
	public Map<String, String> selectPayChannelIdInfo(String payChannelId) {
		logger.info(">>> >>> >>> >>> 开始:查询PayChannelIdInfo 入参:" + payChannelId);
		//属性信息List
		List<SettleParameterInfo> list = null;
		//属性信息Map key渠道号 value渠道名称
		Map<String, String> map = null;
		//query对象
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		//渠道信息查询
		query.setParameterType(SettleConstants.PARAMETER_TYPE_UNKNOW_STATUS);
		query.setParameterCode(payChannelId);
		query.setStatusId(1);
		list = settleParameterInfoManager.queryList(query);
		//查询结果验证
		if(list == null || list.size() < 1) {return null;}
		//封装结构信息map
		map = new HashMap<String, String>();
		for(SettleParameterInfo spi : list) {
			map.put(spi.getParameterCode(), spi.getObligate3());
		}
		logger.info("<<< <<< <<< <<< 结束:查询PayChannelIdInfo");
		return map;
	}

	@Override
	public Map<String, String> selectMerchantCdIdInfo() {
		return this.selectMerchantCdIdInfo(null);
	}

	@Override
	public Map<String, String> selectMerchantCdIdInfo(String merchantCd) {
		logger.info(">>> >>> >>> >>> 开始:查询merchantCd 入参:" + merchantCd);
		//属性信息List
		List<SettleParameterInfo> list = null;
		//属性信息Map key渠道号 value渠道名称
		Map<String, String> map = null;
		//query对象
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		//渠道信息查询
		query.setParameterType(SettleConstants.PARAMETER_DOWN_MERCHANT);
		query.setParameterCode(merchantCd);
		query.setStatusId(1);
		list = settleParameterInfoManager.queryList(query);
		//查询结果验证
		if(list == null || list.size() < 1) {return null;}
		//封装结构信息map
		map = new HashMap<String, String>();
		for(SettleParameterInfo spi : list) {
			map.put(spi.getParameterCode(), spi.getObligate3());
		}
		logger.info("<<< <<< <<< <<< 结束:查询merchantCd");
		return map;
	}

	@Override
	public Map<String, String> selectFuncCodeInfo() {
		return this.selectFuncCodeInfo(null);
	}

	@Override
	public Map<String, String> selectFuncCodeInfo(String funcCode) {
		logger.info(">>> >>> >>> >>> 开始:查询funcCode 入参:" + funcCode);
		//属性信息List
		List<SettleParameterInfo> list = null;
		//属性信息Map key渠道号 value渠道名称
		Map<String, String> map = null;
		//query对象
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		//渠道信息查询
		query.setParameterType(SettleConstants.PARAMETER_TYPE_FUN_INFO);
		query.setParameterCode(funcCode);
		query.setStatusId(1);
		list = settleParameterInfoManager.queryList(query);
		//查询结果验证
		if(list == null || list.size() < 1) {return null;}
		//封装结构信息map
		map = new HashMap<String, String>();
		for(SettleParameterInfo spi : list) {
			map.put(spi.getParameterCode(), spi.getParameterValue());
		}
		logger.info("<<< <<< <<< <<< 结束:查询funcCode");
		return map;
	}
}
