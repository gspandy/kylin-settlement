package com.rkylin.settle.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {
	//日志对象
   protected static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
   
  // private String excelPath = "feeAmountSummary.xlsx";

  /* public static void main(String[] args) throws Exception{
       ExcelUtil excel = new ExcelUtil();
       if(excel.createExcelFile()) {
           System.out.println("data.xlsx is created successfully.");
       }
   }*/
   public File createExcelFile(String[] head,List<String[]> rowList,String excelPath,int columnWidth) {
       Workbook workbook = null;
       File file = null;
       try {
    	   if(head ==null || rowList ==null){
    		   logger.info("生成excel的方法入参head或rowList为null,直接return false!!!!");
    		   return null;
    	   }
    	   System.out.println("excelPath:"+excelPath);
           file = new File(excelPath);
           if(file.exists()){
        	   file.delete();
        	   file.createNewFile();
           }
    	   
           workbook = new XSSFWorkbook();
       }catch(Exception e) {
           System.out.println("It cause Error on CREATING excel workbook: ");
           e.printStackTrace();
       }
       if(workbook != null) {
           Sheet sheet = workbook.createSheet("sheet1");
           Row row0 = sheet.createRow(0);
           for(int i = 0; i < head.length; i++) {
               Cell cell_1 = row0.createCell(i, Cell.CELL_TYPE_STRING);
               CellStyle style = getStyle(workbook);
               cell_1.setCellStyle(style);
               cell_1.setCellValue(head[i]);
               if(columnWidth >0){
            	   sheet.setColumnWidth(i, columnWidth);
               }else{
            	   sheet.autoSizeColumn(i);
               }
           }
           for (int rowNum = 1; rowNum <= rowList.size(); rowNum++) {
               Row row = sheet.createRow(rowNum);
               String[] rowData = rowList.get(rowNum-1);
               if(rowData !=null && rowData.length>0){
            	   for(int i = 0; i < head.length; i++) {
                       Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                       cell.setCellValue(rowData[i]);
                   }  
               }
           }
           FileOutputStream outputStream = null;
           try {
               outputStream = new FileOutputStream(excelPath);
               workbook.write(outputStream);
           } catch (Exception e) {
               System.out.println("It cause Error on WRITTING excel workbook: ");
               e.printStackTrace();
           }finally{
        	   if(outputStream !=null){
        		    try {
						outputStream.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
                    try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
        	   }
           }
         
	       System.out.println(file.getAbsolutePath());
	   }
       return file;
   }
   private CellStyle getStyle(Workbook workbook){
       CellStyle style = workbook.createCellStyle();
       style.setAlignment(CellStyle.ALIGN_CENTER); 
       style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
       // 设置单元格字体
       Font headerFont = workbook.createFont(); // 字体
       headerFont.setFontHeightInPoints((short)14);
       headerFont.setColor(HSSFColor.RED.index);
       headerFont.setFontName("宋体");
       style.setFont(headerFont);
       style.setWrapText(true);
       

       // 设置单元格边框及颜色
       style.setBorderBottom((short)1);
       style.setBorderLeft((short)1);
       style.setBorderRight((short)1);
       style.setBorderTop((short)1);
       style.setWrapText(true);
       return style;
   }
 }