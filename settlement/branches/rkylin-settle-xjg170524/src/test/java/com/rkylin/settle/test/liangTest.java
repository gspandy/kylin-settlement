package com.rkylin.settle.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.logic.CollateLogic;
import com.rkylin.settle.service.SettleService;

public class liangTest extends BaseJUnit4Test {
	@Autowired
	private CollateLogic collageLogic;
	@Autowired
	private SettleService settleService;

	public void testQuerySettleTransDetail() throws ParseException{
		List<Map<String, Object>> resultlist = new ArrayList<Map<String,Object>>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date accountDate = sdf.parse("2015-07-17");
		try{
		//resultlist = collageLogic.querySettleTransDetail(accountDate, "01");
			//resultlist= collageLogic.querySettleTransAccount(accountDate, "01", "M000001");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(resultlist.size()>0){
			System.out.println("*******ORDER_NO********"+ resultlist.get(0).get("ORDER_NO"));
			System.out.println("*******STATUS_ID********"+ resultlist.get(0).get("STATUS_ID"));
		}else{
			System.out.print("********no data!!!*******");
		}
	}
	@Test
	public void testCollage() throws ParseException{
		try{
			/*settleService.collage("01");*/
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
