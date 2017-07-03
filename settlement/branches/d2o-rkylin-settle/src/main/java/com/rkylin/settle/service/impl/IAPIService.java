package com.rkylin.settle.service.impl;

import java.util.Map;

import com.rkylin.settle.response.Response;


public interface IAPIService {

	public Response doJob(Map<String, String[]> paramMap,String methodName);

}
