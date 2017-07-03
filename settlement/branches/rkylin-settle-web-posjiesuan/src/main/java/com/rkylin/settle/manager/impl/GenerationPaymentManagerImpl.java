/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.GenerationPaymentDao;
import com.rkylin.settle.manager.GenerationPaymentManager;
import com.rkylin.settle.pojo.GenerationPayment;
import com.rkylin.settle.pojo.GenerationPaymentQuery;

@Component("generationPaymentManager")
public class GenerationPaymentManagerImpl implements GenerationPaymentManager {
	
	@Autowired
	@Qualifier("generationPaymentDao")
	private GenerationPaymentDao generationPaymentDao;
	
	@Override
	public void saveGenerationPayment(GenerationPayment generationPayment) {
		if (generationPayment.getGeneId() == null) {
			generationPaymentDao.insertSelective(generationPayment);
		} else {
			generationPaymentDao.updateByPrimaryKeySelective(generationPayment);
		}
	}
	
	@Override
	public GenerationPayment findGenerationPaymentById(Long id) {
		return generationPaymentDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<GenerationPayment> queryList(GenerationPaymentQuery query) {
		return generationPaymentDao.selectByExample(query);
	}
	
	@Override
	public void deleteGenerationPaymentById(Long id) {
		generationPaymentDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteGenerationPayment(GenerationPaymentQuery query) {
		generationPaymentDao.deleteByExample(query);
	}

	@Override
	public List<GenerationPayment> queryPage(GenerationPaymentQuery query) {
		return generationPaymentDao.selectByPreExample(query);
	}

	@Override
	public Integer countByExample(GenerationPaymentQuery query) {
		return generationPaymentDao.countByExample(query);
	}
}

