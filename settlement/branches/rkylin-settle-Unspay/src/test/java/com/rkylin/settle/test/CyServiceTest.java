/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.crps.service.CrpsApiService;
import com.rkylin.settle.listen.DsfMessageListener;
import com.rkylin.settle.logic.CollateFileLogic;
import com.rkylin.settle.logic.CollateLogic;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.service.AccountService;
import com.rkylin.settle.service.CollateService;
import com.rkylin.settle.service.DsfService;
import com.rkylin.settle.service.impl.DsfServiceImpl;


public class CyServiceTest extends BaseJUnit4Test {
	@Autowired
	@Qualifier("dsfService")
	private DsfService dsfService;
	
	@Autowired
	private CrpsApiService crpsApiService;//代收付的服务
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ActiveMQQueue queueDest;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CollateService collateService;
	
	@Test
	public void test() {
		try {
			dsfService.returnAccount(new Integer[]{1131052}, new Integer[]{6});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
/*	
	public static void main(String[] args) {
		SettleTransDetail dd = new SettleTransDetail();
		dd.setDataFrom(4);
		if("4".equals(4)){
			System.out.println("333");
		}else{
			System.out.println("444");
		}
	}*/
}
