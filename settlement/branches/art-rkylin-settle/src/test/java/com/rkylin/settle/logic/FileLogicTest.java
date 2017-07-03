package com.rkylin.settle.logic;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.test.BaseJUnit4Test;

public class FileLogicTest extends BaseJUnit4Test {
    @Autowired
    CollateFileLogic collateFileLogic;
    @Test
    public void testUploadCollateFile() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testReadCollateFile() {
        try {
            //丰年代收付-对账文件入库
            collateFileLogic.readCollateFile("M000001", "02", "2015-08-07", "ZF", "01");
            //会唐网关支付-对账文件入库
//            collateFileLogic.readCollateFile("M000003", "01", "2015-08-07", "WG", "01");
            //会唐代收付-对账文件入库
//            collateFileLogic.readCollateFile("M000003", "02", "2015-08-07", "ZF", "01");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testInsertAndUpdateSettleBalanceEntry() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testEditResultMapMapOfStringObjectStringString() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testEditResultMapMapOfStringObjectStringStringListOfStringMapOfStringString() {
        fail("Not yet implemented");
    }
    
}
