package com.rkylin.settle.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class TxtReader {
	
	private String encode = "GBK";
	
	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TxtReader txtReader = new TxtReader();
		try {
			txtReader.setEncode("GBK");
			txtReader.txtreader(new File("C:\\test\\p2p.csv"),",");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/**
 * Txt文件读入
 * @author sunrb
 *
 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> txtreader(File file,String splitString) throws Exception {
		BufferedReader inputFile = null;
		//FileReader fr = null;
		int nLoop = 0;
		List list =  new LinkedList();
		DataInputStream in = null;
		try{
			in = new DataInputStream(new FileInputStream(file));
			//fr = new FileReader(file);
			inputFile = new BufferedReader(new InputStreamReader(in,this.encode));
			
			Map keyMap = null;
			String line = null;
			String[] linearr = null;
			String splitStringZ = "";
			
    		while(true){
    			linearr = null;
    			keyMap = new HashMap();
    			line = inputFile.readLine();
    			if (line == null || line.trim().equals("")) {
    		        break;
    			}

    			// 特殊字符转换
    			splitStringZ = "";
    			for (int jj =0;jj<splitString.length();jj++) {
    				char sc = splitString.charAt(jj);
	    			if (sc == '|' || sc == ',' || sc == '$') {
	    				splitStringZ = splitStringZ + "\\"+sc;
	    			} else {
	    				splitStringZ = splitStringZ + sc;
	    			}
    			}
    			if (line.indexOf(splitString) >= 0 ) {
    				linearr = line.split(splitStringZ);
    				 for (int linei=0;linei<linearr.length;linei++) {
    	    			keyMap.put("L_"+linei, linearr[linei]);
    				 }
    			} else {
    				keyMap.put("L_0", line);
    			}

    			if(keyMap != null){
    				if(keyMap.get("L_0") != null){
	    				list.add(keyMap);
	    				nLoop++;
    				} else {
        				//文件这行为空
    				}
    			}
    		}

    		if(list.size() > 0){  
				nLoop++;
    		}

		}catch(Exception e){
			throw new Exception("文件读取异常!" + e.getMessage());

		}finally{
    		try{
    			if(in != null){
    				in.close();
    			}

    			if(inputFile != null){
    				inputFile.close();
    			}
    		}catch(Exception e){
    			throw new Exception("文件关闭异常!" + e.getMessage());   			
    		}    		
    	}
		return list;
	}
	
}
