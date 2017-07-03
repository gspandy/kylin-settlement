/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.GenerationPayment;
import com.rkylin.settle.pojo.GenerationPaymentQuery;

public interface GenerationPaymentDao {
	int countByExample(GenerationPaymentQuery example);
	
	int deleteByExample(GenerationPaymentQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(GenerationPayment record);
	
	int insertSelective(GenerationPayment record);
	
	List<GenerationPayment> selectByExample(GenerationPaymentQuery example);
	
	GenerationPayment selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(GenerationPayment record);
	
	int updateByPrimaryKey(GenerationPayment record);
	
	List<GenerationPayment> selectByPreExample(GenerationPaymentQuery example);
}
