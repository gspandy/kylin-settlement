package com.rkylin.settle.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TxtWriter {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map configMap = new HashMap();
		configMap.put("FILE", "C:\\test\\p2p2.csv");
		List detailList = new ArrayList();
		Map subMap = new HashMap();
		subMap.put("F_1", "测试1");
		subMap.put("F_2", "测试2");
		subMap.put("F_3", "测试3");
		detailList.add(subMap);
		subMap = new HashMap();
		subMap.put("F_1", "测试1");
		subMap.put("F_2", "测试2");
		subMap.put("F_3", "测试3");
		detailList.add(subMap);
		//configMap.put("REPORT-HEAD", detailList);
		configMap.put("REPORT-TAIL", detailList);
		try {
			TxtWriter.WriteTxt(null,configMap,",","UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public static int WriteTxt(List detailList ,Map configMap ,String splitstr,String encode)
    throws Exception {
    	if(configMap == null){
    		throw new IOException("配置参数错误");
    	}
    	
    	String filename = (String)configMap.get("FILE");;
    	List reportHead = (List)configMap.get("REPORT-HEAD");
    	List reportTail = (List)configMap.get("REPORT-TAIL");

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter rw = null;
		File f = null;
		try {
		f = new File(filename);			
		if(!f.exists()){
			f.createNewFile();
		}			
 
		if (encode == null || "".equals(encode)) {
			encode = "GBK";
		}
		fos = new FileOutputStream(filename);
		osw = new OutputStreamWriter(fos ,encode);			  
		rw = new BufferedWriter(osw);

		Map subMap = null;
		int k = 0;
		StringBuffer buf = new StringBuffer();
		StringBuffer lineBuf = new StringBuffer();
		if (reportHead !=null && reportHead.size() != 0) {
			for(int i=0;i<reportHead.size();i++){
				subMap = (Map)reportHead.get(i);
				lineBuf.delete(0,lineBuf.length());

				for(k=0;k<subMap.size();k++){
					buf.delete(0,buf.length());
					buf.append("F_");
					buf.append((k+1));
					if(k==0){
						if (i!=0) {
							lineBuf.append("\r\n");
						}
					}else{
						lineBuf.append(splitstr);
					}
					lineBuf.append(subMap.get(buf.toString()));
				}
				rw.write(lineBuf.toString());
			}
		}

		if (detailList !=null && detailList.size() != 0) {
			for(int i=0;i<detailList.size();i++){
				subMap = (Map)detailList.get(i);
				lineBuf.delete(0,lineBuf.length());

				for(k=0;k<subMap.size();k++){
					buf.delete(0,buf.length());
					buf.append("F_");
					buf.append((k+1));
					if(k==0){
						if ((reportHead ==null || reportHead.size() == 0) && i==0) {
						}else {
							lineBuf.append("\r\n");
						}
					}else{
						lineBuf.append(splitstr);
					}
					lineBuf.append(subMap.get(buf.toString()));
				}
				rw.write(lineBuf.toString());
			}
		}
		
		if (reportTail !=null && reportTail.size() != 0) {
			for(int i=0;i<reportTail.size();i++){
				subMap = (Map)reportTail.get(i);
				lineBuf.delete(0,lineBuf.length());

				for(k=0;k<subMap.size();k++){
					buf.delete(0,buf.length());
					buf.append("F_");
					buf.append((k+1));
					if(k==0){
						if (((reportHead ==null || reportHead.size() == 0) &&
								(detailList ==null || detailList.size() == 0)) && i==0) {
							
						} else {
							lineBuf.append("\r\n");
						}
					}else{
						lineBuf.append(splitstr);
					}
					lineBuf.append(subMap.get(buf.toString()));
				}
				rw.write(lineBuf.toString());
			}
		}
		rw.flush();
		rw.close();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			try{
				if(rw != null){
					rw.close();
				}
				if(osw != null){
					osw.close();
				}
				if(fos != null){
					fos.close();
				}
			} catch (Exception e1) {
				throw new Exception(e1.getMessage());
			}
		}
        return 1;
    }
}
