package com.rkylin.settle.service;

public interface PositionService {
	/**
	 * 生成头寸信息
	 * @return
	 * @throws Exception
	 */
	String createPositionInfo() throws Exception;
	/**
	 * 生成头寸信息
	 * @param parameterCode
	 * @return
	 * @throws Exception
	 */
	String createPositionInfo(String pstRootInstCd, String pstFuncCode, String pstUserId, String pstProductId) throws Exception;
}
