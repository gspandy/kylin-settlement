/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.GenerationPayment;
import com.rkylin.settle.pojo.GenerationPaymentQuery;

public interface GenerationPaymentManager {
	void saveGenerationPayment(GenerationPayment generationPayment);

	GenerationPayment findGenerationPaymentById(Long id);
	
	List<GenerationPayment> queryList(GenerationPaymentQuery query);
	
	void deleteGenerationPaymentById(Long id);
	
	void deleteGenerationPayment(GenerationPaymentQuery query);
	
	List<GenerationPayment> queryPage(GenerationPaymentQuery query);
	
	Integer countByExample(GenerationPaymentQuery query);
}
