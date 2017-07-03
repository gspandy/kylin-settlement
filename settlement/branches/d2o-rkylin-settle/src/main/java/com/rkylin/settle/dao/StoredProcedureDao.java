package com.rkylin.settle.dao;

import java.util.Map;

public interface StoredProcedureDao {
	/***
	 * 获取T-1日账户(一期)交易信息的存储过程
	 * @param:入参/出参
	 * @return
	 */
	Map<String, Object> proSyncActTrans(Map<String, Object> example);
	/***
	 * 获取T-1日账户(二期)交易信息的存储过程
	 * @param:入参/出参
	 * @return
	 */
	Map<String, Object> proSyncActTrans2(Map<String, Object> example);
}
