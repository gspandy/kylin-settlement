/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.service.CollateService;
import com.rkylin.settle.util.SettlementUtil;


public class CyServiceTest extends BaseJUnit4Test {
	@Autowired
	@Qualifier("collateService")
	CollateService collateService;
	@Autowired
	@Qualifier("settlementUtil")
	SettlementUtil settlementUtil;
	@Test
	public void test() throws Exception {
System.out.println(">>> >>> >>> >>> start >>> >>> >>> >>> >>> >>> >>> >>> start >>> >>> >>> >>> >>> >>> >>> >>> start >>> >>> >>> >>> >>> >>> >>> >>> start >>> >>> >>> >>>");	
//		SettlementUtil settlementUtil = new SettlementUtil();	
		String invoicedate = String.valueOf(settlementUtil.getAccountDate("yyyy-MM-dd", 0, "String"));
		String accountDate = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", -1, "String"));
		String accountDate2 = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", 0, "String"));
		
		System.out.println(invoicedate);
		System.out.println(accountDate);
		System.out.println(accountDate2);
System.out.println(" <<< <<< <<< <<< end   <<< <<< <<< <<< <<< <<< <<< <<< end   <<< <<< <<< <<< <<< <<< <<< <<< end   <<< <<< <<< <<< <<< <<< <<< <<< end   <<< <<< <<< <<<");
	}
}
