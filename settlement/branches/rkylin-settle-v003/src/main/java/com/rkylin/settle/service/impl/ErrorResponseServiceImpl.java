package com.rkylin.settle.service.impl;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.response.ErrorResponse;
import com.rkylin.settle.service.IErrorResponseService;


@Service("iErrorResponseService")
public class ErrorResponseServiceImpl implements IErrorResponseService {
	@Autowired
	private Properties errorCodeProperties;
	
	public Properties getErrorCodeProperties() {
		return errorCodeProperties;
	}

	public void setErrorCodeProperties(Properties errorCodeProperties) {
		this.errorCodeProperties = errorCodeProperties;
	}

	@Override
	public ErrorResponse getErrorResponse(String code) {
		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setCallResult(false);

		errorResponse.setCode(code);
		errorResponse.setMsg(errorCodeProperties.getProperty(code));

		return errorResponse;
	}

	@Override
	public ErrorResponse getErrorResponse(String code, String msg) {
		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setCallResult(false);
		
		errorResponse.setCode(code);
		errorResponse.setMsg(msg);
		
		return errorResponse;
	}

	@Override
	public ErrorResponse getErrorResponse(String code, String msg,
			String subCode, String subMsg) {
		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setCallResult(false);

		errorResponse.setCode(code);
		errorResponse.setMsg(msg);
		errorResponse.setSubCode(subCode);
		errorResponse.setSubMsg(subMsg);

		return errorResponse;
	}

	@Override
	public ErrorResponse getAccountErrorResponse(String code) {
		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setCallResult(false);

		errorResponse.setCode(code);
		//errorResponse.setMsg(accountErrorCodeProperties.getProperty(code));

		return errorResponse;
	}

	@Override
	public ErrorResponse getAccountErrorResponse(String code, String msg) {
		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setCallResult(false);
		
		errorResponse.setCode(code);
		errorResponse.setMsg(msg);
		
		return errorResponse;
	}
	
	
}
