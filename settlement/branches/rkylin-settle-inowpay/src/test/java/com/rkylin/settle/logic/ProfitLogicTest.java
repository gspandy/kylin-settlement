package com.rkylin.settle.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.test.BaseJUnit4Test;

public class ProfitLogicTest extends BaseJUnit4Test {

    @Autowired
    ProfitLogic profitLogic;
    
    @Test
    public void testGetAccountTransOrderInfos() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //profitLogic.getAccountTransOrderInfos(sdf.parse("2015-04-25 00:00:00"),sdf.parse("2015-04-25 23:59:59") );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
