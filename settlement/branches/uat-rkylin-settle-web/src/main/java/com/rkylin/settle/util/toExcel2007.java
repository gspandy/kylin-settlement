package com.rkylin.settle.util;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import ch.qos.logback.classic.Logger;

@SuppressWarnings("deprecation")
public class toExcel2007 {
	public static void main(String[] args) {

	}		
    public static int WriteDetailSheet(List detailList ,Map configMap ,OutputStream os)
    throws Exception {
    	if(configMap == null){
    		throw new IOException("配置参数错误");
    	}
    	
    	String modelFilename = (String)configMap.get("MODEL_FILE");			//模板 绝对路径
    	String filename = (String)configMap.get("FILE");					//目标文件 绝对路径
    	String sheetName = (String)configMap.get("SHEET");					//sheet页名字 唯一
    	String firstDetailRow = (String)configMap.get("firstDetailRow");	//详细row 开始索引
    	String multiSheet = (String)configMap.get("multiSheet");			
    	String multiSheet_index = (String)configMap.get("multiSheet_index");
    	String firstStyleRow = (String)configMap.get("firstStyleRow");		//明细row样式 开始索引
    	List reportHead = (List)configMap.get("REPORT-HEAD");
    	List reportTail = (List)configMap.get("REPORT-TAIL");
    	List reportMerge = (List)configMap.get("REPORT-MERGE");
    	
    	int nFirstDetailRow = 0;
    	int nFirstStyleRow = -1;
    	try{
    		nFirstDetailRow = Integer.parseInt(firstDetailRow); 
    		nFirstStyleRow = Integer.parseInt(firstStyleRow);
    	}catch(Exception e){
    		throw new IOException(e.getMessage());
    	}
    	
    	InputStream modelXls = null;	    	
    	XSSFWorkbook wb = null;
    	XSSFSheet sheet = null;
    		    	
    	try{	
    		if(multiSheet != null) {
	    		File file = new File(filename);
	        	if(file != null && !file.exists()){
		    		modelXls = new FileInputStream(modelFilename);
		    		wb = new XSSFWorkbook(modelXls);
		    		for(int nSheetCount=0; nSheetCount < Integer.parseInt(multiSheet); nSheetCount++){
		    			sheet = wb.cloneSheet(0);
		    		}
		    		
			        FileOutputStream fileOut = new FileOutputStream(filename);
			        wb.write(fileOut);
			    	fileOut.close();	
		    	}		
	        	modelXls = new FileInputStream(filename);
    		}else {
    			modelXls = new FileInputStream(modelFilename);
    		}
    		wb = new XSSFWorkbook(modelXls);
    	}catch(Exception e){
    		throw new IOException("打开模板文件失败" + e.getMessage());
    	}
    	if(multiSheet != null) {
    		//sheet = wb.cloneSheet(Integer.parseInt(multiSheet));
    		sheet = wb.getSheetAt(Integer.parseInt(multiSheet_index)-1);
//	    		sheet = wb.cloneSheet(1);
    		wb.setSheetName(Integer.parseInt(multiSheet_index)-1, sheetName);
    	}else{
//	    		sheet = wb.cloneSheet(0);
    		sheet = wb.getSheetAt(0);
    		wb.setSheetName(0, sheetName);
    	}
    	
    	int nRow = 0;
    	int nCol = 0;
    	XSSFRow newRow = null;
    	XSSFRow oldRow = null;
    	XSSFCell newCell = null;
    	XSSFCell oldCell = null;
    	Map map = null;
    	String sVal = null;
    	StringBuffer fBuf = new StringBuffer();
    	
    	List styleList = new LinkedList();
    	Map styleMap = null;
    	int nStyleIndex = 0;	    	
    	
		// 分润明细信息
    	if(detailList == null || detailList.size() == 0){
    		//logger.error("列表为空");
    		//return 0;
    	}else{

    	}
    	try{		    	
    		//读原始的style
	    	// 加格式模板列	   

        	if(detailList != null && detailList.size() != 0){
	    		map = (Map)detailList.get(0);
	    		
	    		if( nFirstStyleRow == -1){
	    			nFirstStyleRow = nFirstDetailRow;
	    		}
	    		try{
		    		for(nRow = nFirstStyleRow; nRow <= nFirstDetailRow; nRow++) {	
			    		oldRow = sheet.getRow( nRow );
			    		styleMap = new HashMap();
			    		
						for(nCol=1;nCol<=map.size();nCol++) {
							if(nCol == 9){
								nCol = nCol+1-1;
							}
//							oldCell = oldRow.getCell((nCol-1));
//				    		fBuf.delete(0, fBuf.length());
//				    		fBuf.append("F_");
//					        fBuf.append(nCol);		    	
//					        
//					        if(map.containsKey(fBuf.toString())){
//					        	styleMap.put(fBuf.toString(), oldCell.getCellStyle());
//					        }
							oldCell = oldRow.getCell((0));
				    		fBuf.delete(0, fBuf.length());
				    		fBuf.append("F_");
					        fBuf.append(nCol);		    	
					        
					        if(map.containsKey(fBuf.toString())){
					        	styleMap.put(fBuf.toString(), oldCell.getCellStyle());
					        }
						}
						styleList.add(styleMap);
		    		}
	    		}catch(Exception e1){
	    			throw new Exception("加载明细行格式失败" + e1.getMessage());
	    		}
        	}
    		
    		//读取报表尾部单元格列
    		if( reportTail != null && reportTail.size() > 0){
    			try{
	    			for(int nRowTail = 0; nRowTail<reportTail.size(); nRowTail++) {
	    				map = (HashMap)reportTail.get(nRowTail);
	    				nRow = -1;
	    				nCol = -1;
	    				try{
	    					nRow = Integer.parseInt((String)map.get("ROW"));
	    					nCol = Integer.parseInt((String)map.get("COL"));
	    				}catch(Exception ex){
		    				nRow = -1;
		    				nCol = -1;	    					
	    				}
	    				if(nRow != -1 && nCol != -1) {
		    				oldRow = sheet.getRow(nRow);
		    				oldCell = oldRow.getCell(nCol);
		    				map.put("CELL-STYLE", oldCell.getCellStyle());
		    				map.put("CELL-TYPE", String.valueOf( oldCell.getCellType() ));
	    				}
	    			}
	    		}catch(Exception e1){
	    			throw new Exception("加载报表尾部单元格失败" + e1.getMessage());
	    		}
    		}
    		/* 设置报表首部 */
    		if( reportHead != null && reportHead.size() > 0){
    			try{
	    			for(int nRowHead = 0; nRowHead<reportHead.size(); nRowHead++) {
	    				map = (HashMap)reportHead.get(nRowHead);
	    				nRow = -1;
	    				nCol = -1;
	    				try{
	    					nRow = Integer.parseInt((String)map.get("ROW"));
	    					nCol = Integer.parseInt((String)map.get("COL"));
	    				}catch(Exception ex){
		    				nRow = -1;
		    				nCol = -1;	    					
	    				}
	    				if(nRow != -1 && nCol != -1) {
		    				oldRow = sheet.getRow(nRow);
		    				oldCell = oldRow.getCell(nCol);
//							oldCell.setEncoding(XSSFCell.ENCODING_UTF_16); 
							sVal = (String)map.get("VALUE");
							
							if(map.containsKey("D")) {
								oldCell.setCellValue(Double.parseDouble(sVal == null ? "0" : sVal));
							}else{
								oldCell.setCellValue(sVal == null ? "" : sVal);
							}
	    				}
	    			}
	    		}catch(Exception e1){
	    			throw new Exception("设置报表头部失败" + e1.getMessage());
	    		}	    			
    		}
    		
			// 填充报表明细 
    		/**/
    		try{
				for(nRow = 0; nRow<detailList.size(); nRow++) {			
		    		map = (HashMap)detailList.get(nRow);
		    		if(map == null || map.size() == 0){
		    			continue;
		    		}
	
		    		oldRow = sheet.getRow( (nFirstStyleRow + nRow));	    		
		    		newRow = sheet.createRow( (nFirstStyleRow + nRow + 1));
		    		
		    		nStyleIndex = 0;
		            if(map.containsKey("FONT-SET")){
		            	try{
		            		nStyleIndex = Integer.parseInt((String)map.get("FONT-SET"));
		            	}catch(Exception ex){
		            		;
		            	}
		            	map.remove("FONT-SET");
		            }
		            
					for(nCol=1;nCol<=map.size();) {
						oldCell = oldRow.getCell((nCol-1));
				        
				        newCell = newRow.createCell((nCol-1));
//				        newCell.setCellStyle((XSSFCellStyle)styleMap.get(String.valueOf(nCol)));
				        newCell.setCellType(oldCell.getCellType() );
				        
			    		fBuf.delete(0, fBuf.length());
			    		fBuf.append("F_");
				        fBuf.append(nCol);		    		
				        
				        styleMap = (Map)styleList.get(nStyleIndex);				        
				        oldCell.setCellStyle((XSSFCellStyle)styleMap.get(fBuf.toString()));
				        
						sVal = (String)map.get(fBuf.toString());
						
						if( "D".equals(configMap.get(String.valueOf(nCol-1)))) {
							oldCell.setCellValue(Double.parseDouble(sVal == null ? "0" : sVal));
						}else{
							oldCell.setCellValue(sVal == null ? "" : sVal);
						}
						
				        nCol++;
					}
		        }
    		}catch(Exception e1){
    			throw new Exception("填充报表明细失败" + e1.getMessage());
    		}					
/**/				
			int lastRow = 0;
			int nloop = 0;
			//删除明细之后的行
			
			//删除新建的行
			if(newRow != null){
				sheet.removeRow(newRow);
				//sheet.shiftRows(nRow, sheet.getLastRowNum(), -1);
			}
			
			if(nRow + nFirstStyleRow <= sheet.getLastRowNum()) {
				try{
					lastRow = nRow + nFirstStyleRow + 1;
					nloop = sheet.getLastRowNum();
					for(;lastRow <= nloop;nloop--) {	
						oldRow = sheet.getRow(nloop);						
						if(oldRow != null){
							sheet.removeRow(oldRow);
							if(nloop < sheet.getLastRowNum()) {
								sheet.shiftRows(nloop, sheet.getLastRowNum(), -1);
							}
						}
					}
	    		}catch(Exception e1){
	    			throw new Exception("追加报表尾行失败" + e1.getMessage());
	    		}					
			}
			
			lastRow = sheet.getLastRowNum();
			//加入报表尾部
			/**/
    		if(reportTail != null && reportTail.size() > 0){
    			try{
	    			for(int nRowTail = 0; nRowTail<reportTail.size(); nRowTail++) {
	    				map = (HashMap)reportTail.get(nRowTail);
	    				nRow = -1;
	    				nCol = -1;
	    				try{
	    					nRow = Integer.parseInt((String)map.get("ROW"));
	    					nCol = Integer.parseInt((String)map.get("COL"));
	    				}catch(Exception ex){
		    				nRow = -1;
		    				nCol = -1;	    					
	    				}
	    				if(nRow != -1 && nCol != -1) {
		    				oldRow = sheet.getRow( (nRow + lastRow - nFirstDetailRow ));
		    				if(oldRow == null){
		    					oldRow = sheet.createRow((nRow+ lastRow - nFirstDetailRow ));
		    				}
		    				oldCell = oldRow.getCell(nCol);
		    				if(oldCell == null){
		    					oldCell = oldRow.createCell(nCol);
		    				}
		    				oldCell.setCellType(Short.parseShort((String)map.get("CELL-TYPE")));
		    				oldCell.setCellStyle((XSSFCellStyle)map.get("CELL-STYLE"));
//		    				oldCell.setEncoding(XSSFCell.ENCODING_UTF_16); 
							sVal = (String)map.get("VALUE");
							
							if(map.containsKey("D")) {
								oldCell.setCellValue(Double.parseDouble(sVal == null ? "0" : sVal));
							}else{
								oldCell.setCellValue(sVal == null ? "" : sVal);
							}
	    				}
	    			}
	    		}catch(Exception e1){
	    			throw new Exception("填充报表尾部失败" + e1.getMessage());
	    		}	
    		}		
    		
    		//合并单元格
    		if(reportMerge != null && reportMerge.size() > 0){
    			try{
    				int nRowFrom = -1;
    				int nRowTo = -1;
    				int nColFrom = -1;
    				int nColTo = -1;
    				for(int nRowTail = 0; nRowTail<reportMerge.size(); nRowTail++) {
	    				map = (Map)reportMerge.get(nRowTail);
	    				nRowFrom = -1;
	    				nRowTo = -1;
	    				nColFrom = -1;
	    				nColTo = -1;		    				
	    				nRowFrom = Integer.parseInt((String)map.get("ROW_FROM"));
	    				nRowTo = Integer.parseInt((String)map.get("ROW_TO"));
	    				nColFrom = Integer.parseInt((String)map.get("COL_FROM"));
	    				nColTo = Integer.parseInt((String)map.get("COL_TO"));
	    				
	    				sheet.addMergedRegion(new CellRangeAddress(nRowFrom ,nRowTo ,nColFrom ,nColTo));
	    			}
	    		}catch(Exception e1){
	    			throw new Exception("合并单元格失败" + e1.getMessage());
	    		}	
    		}	
    		
/**/		if(multiSheet_index != null && multiSheet != null ){
				if( Integer.parseInt(multiSheet_index) == Integer.parseInt(multiSheet) - 1 ){
					wb.removeSheetAt((short)0);
				}
			}		
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new IOException("写入文件失败"+" nRow=" + nRow + "---------nCol=" + nCol+e.getMessage());
    	}	    	
//			wb.removeSheetAt(0);
    	if(os == null) {
	        FileOutputStream fileOut = new FileOutputStream(filename);
	        wb.write(fileOut);
	    	fileOut.close();
    	}else{
    		wb.write(os);
    		os.flush();
    		os.close();
    	}
    	
    	return 0;
    }
}

