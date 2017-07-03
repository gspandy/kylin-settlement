package com.rkylin.settle.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxReader {

	public Map<String,List<Map<String,Object>>> file_reader(File file) throws Exception {
		Map<String,List<Map<String,Object>>> rtnMap = new HashMap<String,List<Map<String,Object>>>();
		List<Map<String,Object>> subList = new ArrayList<Map<String,Object>>();
		Map<String,Object> subMap = new HashMap<String,Object>();

		InputStream is = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFCell cell = null;
		for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
			XSSFSheet st = wb.getSheetAt(sheetIndex);
			for (int rowIndex = 0; rowIndex <= st.getLastRowNum(); rowIndex++) {
				XSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				int cellNum = row.getLastCellNum();
				for (int k = 0; k < cellNum; k++) {
					cell = row.getCell(k);
					subMap.put("L_"+k, row.getCell(k)==null?" ":row.getCell(k));
				}System.out.println(subMap);
				subList.add(subMap);
				subMap = new HashMap<String,Object>();
			}
			rtnMap.put(st.getSheetName(), subList);
			subList = new ArrayList<Map<String,Object>>();
		}
		return rtnMap;
	}
	
	public static void main(String[] args) {
    	XlsxReader xlsxReader = new XlsxReader();
    	try {
			System.out.println(xlsxReader.file_reader(new File("Z:\\下载\\20160527_TongLian_MWPayment_M000012.xlsx")));
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
}
