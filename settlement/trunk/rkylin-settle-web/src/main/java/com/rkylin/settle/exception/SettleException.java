package com.rkylin.settle.exception;


import com.rkylin.exception.BaseException;
import com.rkylin.settle.enumtype.SettleExceptionEnum;


public class SettleException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public SettleException(String defineCode) {
		super(defineCode);
	}

	public SettleException(SettleExceptionEnum exp) {
		super(exp.getDefineCode(), exp.getDefineMsg());
	}

}
