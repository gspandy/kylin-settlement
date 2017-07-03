package com.rkylin.settle.service;

import com.rkylin.settle.response.ErrorResponse;



public interface IErrorResponseService {
	
	public ErrorResponse getErrorResponse(String code);

	public ErrorResponse getErrorResponse(String code, String msg);

	public ErrorResponse getErrorResponse(String code, String msg, String subCode, String subMsg);

	public ErrorResponse getAccountErrorResponse(String code);
	
	public ErrorResponse getAccountErrorResponse(String code, String msg);
}
