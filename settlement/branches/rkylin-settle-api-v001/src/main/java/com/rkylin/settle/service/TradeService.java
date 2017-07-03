package com.rkylin.settle.service;

import java.util.Map;

import com.rkylin.settle.pojo.SettleTransDetailApi;

public interface TradeService {
	Map<String,String> saveAccountTrans(SettleTransDetailApi settleTransDetail);
}
