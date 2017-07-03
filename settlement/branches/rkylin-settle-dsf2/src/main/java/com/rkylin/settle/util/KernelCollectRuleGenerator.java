package com.rkylin.settle.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class KernelCollectRuleGenerator {
	private static String splitStr = "\t";
	private static String encode = "GBK";
	private static List<String> mechantCodeList = null;
	private static Map<String, String> payChannelIdMap = null;
	private static Map<String, String> funcCodeMap = null;
	private static StringBuffer sqlModule = new StringBuffer();
	private static String mtkernelFuncCode2 = "F6010,F6011,F6012,F6013,F6014,F6016";//实收 代收付 成功
	private static String mtkernelFuncCode3 = "F6113,F6114,F6116";//实收 代收付 失败
	private static String mtkernelFuncCode4 = "F6015";//实收 充值 成功
	private static String mtkernelFuncCode5 = "F30263,F30283";//实收 退票 退款
	
	static {
		mechantCodeList = new ArrayList<String>();
		mechantCodeList.add("M000001");	//丰年
		mechantCodeList.add("M000003");	//会唐
		mechantCodeList.add("M000004");	//课栈
		mechantCodeList.add("M000005");	//君融贷
		mechantCodeList.add("M000006");	//食全食美
		mechantCodeList.add("M000007");	//绵庄
		mechantCodeList.add("M00000X");	//融数
		mechantCodeList.add("M000011");	//展酷
		mechantCodeList.add("M000012");	//指尖
		mechantCodeList.add("M000013");	//卖家云
		mechantCodeList.add("M000014");	//通信运维
		mechantCodeList.add("M000016");	//天下房仓
		mechantCodeList.add("M000017");	//企业白条
		mechantCodeList.add("M000020");	//帮帮助学
		mechantCodeList.add("M000024");	//领客科技
		mechantCodeList.add("M000025");	//悦视觉
		mechantCodeList.add("M000026");	//汽车融资
		mechantCodeList.add("M000027");	//旅游分期
		mechantCodeList.add("M666666");	//融数钱包
		
		payChannelIdMap = new HashMap<String, String>();
		payChannelIdMap.put("畅捷支付", "S01");
		payChannelIdMap.put("连连", "04");
		payChannelIdMap.put("联动", "05");
		payChannelIdMap.put("鹏元", "");
		payChannelIdMap.put("平安", "Y01");
		payChannelIdMap.put("通联", "01");
		payChannelIdMap.put("银企民生", "Y02");
		
		funcCodeMap = new HashMap<String, String>();
		funcCodeMap.put("实时代收", "40131");
		funcCodeMap.put("代收", "4013");
		funcCodeMap.put("代付", "4014");
		funcCodeMap.put("一分钱代付", "4014_1");
		funcCodeMap.put("充值", "4015");
		funcCodeMap.put("提现", "4016");
		funcCodeMap.put("信用消费", "4011");
		funcCodeMap.put("储蓄消费", "4012");
		funcCodeMap.put("代收差错", "4013");
		funcCodeMap.put("代付差错", "4014");
		funcCodeMap.put("提现差错", "4016");
		funcCodeMap.put("退票", "5024");
		funcCodeMap.put("退款", "4017");
		
		sqlModule.append("INSERT INTO `settle`.`SETTLE_COLLECT_RULE` (");
		sqlModule.append("`PROFIT_RULE_NAME`,");
	    sqlModule.append("`ROOT_INST_CD`,");
		sqlModule.append("`PAY_CHANNEL_ID`,");
		sqlModule.append("`FUNC_CODE`,");
		sqlModule.append("`KERNEL_FUNC_CODE`,");
		sqlModule.append("`ACCOUNT_NAME1`,");
		sqlModule.append("`FIN_ACCOUNT_ID1`,");
		sqlModule.append("`ACCOUNT_NAME2`,");
		sqlModule.append("`FIN_ACCOUNT_ID2`,");
		sqlModule.append("`OBLIGATE1`,");
		sqlModule.append("`COLLECT_TYPE`,");
		sqlModule.append("`STATUS_ID` ");
		sqlModule.append("	)");
		sqlModule.append("	VALUES ");
		sqlModule.append(" (");
		sqlModule.append("	'{RULE_NAME}',");
		sqlModule.append("	'{ROOT_INST_CD}',");
		sqlModule.append("	'{PAY_CHANNEL_ID}',");
		sqlModule.append("	'{FUNC_CODE}',");
		sqlModule.append("	'{KERNEL_FUNC_CODE}',");
		sqlModule.append("	'{ACCOUNT_NAME1}',");
		sqlModule.append("	'{FIN_ACCOUNT_ID1}',");
		sqlModule.append("	'{ACCOUNT_NAME2}',");
		sqlModule.append("	'{FIN_ACCOUNT_ID2}',");
		sqlModule.append("	'{OBLIGATE1}',");
		sqlModule.append("	'{COLLECT_TYPE}',");
		sqlModule.append("	'1' ");
		sqlModule.append(");");
	}
	public static void main(String[] args) throws Exception {
		String filePath1 = "D:\\mydata\\kernel_func.txt";
		String filePath2 = "D:\\mydata\\kernel_item.txt";
		doGenerator(filePath1, filePath2);
	}
	
	public static void doGenerator(String filePath1, String filePath2) throws Exception {
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		List<Map<String, String>> list1 = txtreader(file1, splitStr);
		List<Map<String, String>> list2 = txtreader(file2, splitStr);
		createCollectRule(list1, list2);
	}
	
	/**
	 * 构造记账规则信息
	 */
	public static void createCollectRule(List<Map<String, String>> list1, List<Map<String, String>> list2) throws Exception {
		File targetFile = new File("D:/mydata/COLLECT_RULE_SQL.sql");
		FileWriter fw = new FileWriter(targetFile);
		if(!targetFile.exists())targetFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(String merChantCode : mechantCodeList) {
			for(int i = 0; i < list1.size(); i += 2) {
				Map<String, String> map1 = list1.get(i);
				Map<String, String> map2 = list1.get(i + 1);
				Map<String, String> collectInfoMap = new HashMap<String, String>();
				
				if(!map1.get("L_0").equals(map2.get("L_0"))) {
					System.err.println("异常: " + map1.get("L_0") + "和" + map2.get("L_0") + "不一致, 请检查文件内容!");
					continue;
				}
				
				String f = map1.get("L_0");
				String name1 = map1.get("L_2");
				String name2 = map2.get("L_2");
				String funcName = map1.get("L_2");
				funcName = funcName.substring(1, funcName.indexOf(")"));
				name1 = editName(name1);
				name2 = editName(name2);

//				String ruleRemark = merChantCode + "-" + funcName + "-" + f;
//				System.out.println(ruleRemark +" : " + name1 + ", " + name2);				
				collectInfoMap.put("ROOT_INST_CD", merChantCode);
				collectInfoMap.put("FUNC_CD", funcCodeMap.get(funcName));
				collectInfoMap.put("FUNC_NAME", funcName);
				collectInfoMap.put("KERNEL_FUNC_CD", f);
				collectInfoMap.put("COLLECT_TYPE", getCollectType(f));
//System.out.println("merChantCode : " + merChantCode);
//System.out.println("name1 : " + name1);
//System.out.println("name2 : " + name2);
				List<Map<String, String>> accountList1 = queryItem(list2, merChantCode, name1, "位置1");
				List<Map<String, String>> accountList2 = queryItem(list2, merChantCode, name2, "位置2");
//System.out.println("accountList1 : " + accountList1.size());
//System.out.println("accountList1 : " + accountList2.size());					
				if(accountList1.size() > 1 && accountList2.size() > 1) {
					System.err.println(merChantCode + funcName + f + " 异常: 借记 和 贷记 出现多对多关系! 程序无法自动匹配!");
					continue;
				}
				
				if(accountList2.size() >= 1) {
					if(accountList1.size() <= 0) {
						System.err.println(merChantCode + funcName + f + " 异常: 借记 和 贷记 出现多对多关系! 程序无法自动匹配!");
						continue;
					}
					Map<String, String> accountMap1 = accountList1.get(0);
					for(Map<String, String> accountMap2 : accountList2) {
						String sql = getRuleInfo(accountMap1, accountMap2, collectInfoMap);
						bw.write(sql + "\r\n");
						System.out.println("        " + sql);
					}
				} else if(accountList1.size() >= 1) {
					if(accountList2.size() <= 0) {
						System.err.println(merChantCode + funcName + f + " 异常: 借记 和 贷记 出现多对多关系! 程序无法自动匹配!");
						continue;	
					}
					Map<String, String> accountMap2 = accountList2.get(0);
					for(Map<String, String> accountMap1 : accountList1) {
						String sql = getRuleInfo(accountMap1, accountMap2, collectInfoMap);
						bw.write(sql + "\r\n");
						System.out.println("        " + sql);
					}
				}
			}
		}
		bw.close();
        fw.close();
	}
	
	public static String getRuleInfo(Map<String, String> accountMap1, Map<String, String> accountMap2, Map<String, String> collectInfoMap) {
		String ROOT_INST_CD = accountMap2.get("L_5") != null ? accountMap2.get("L_5"):accountMap1.get("L_5");
		String PAY_CHANNEL_ID = "";
		if(accountMap2.get("L_7") != null) {
			PAY_CHANNEL_ID = payChannelIdMap.get(accountMap2.get("L_7"));
		} else if(accountMap1.get("L_7") != null ) {
			PAY_CHANNEL_ID = payChannelIdMap.get(accountMap1.get("L_7"));
		} else {
			PAY_CHANNEL_ID = "";
		}
		String OBLIGATE1 = "";
		if(accountMap2.get("L_6") != null) {
			OBLIGATE1 = accountMap2.get("L_6");
		} else if(accountMap1.get("L_6") != null) {
			OBLIGATE1 = accountMap1.get("L_6");
		} else {
			OBLIGATE1 = "";
		}
		String FUNC_CODE = collectInfoMap.get("FUNC_CD");
		String KERNEL_FUNC_CODE = collectInfoMap.get("KERNEL_FUNC_CD");
		String ACCOUNT_NAME1 = accountMap1.get("L_4");
		String FIN_ACCOUNT_ID1 = accountMap1.get("L_1");	
		String ACCOUNT_NAME2 = accountMap2.get("L_4");
		String FIN_ACCOUNT_ID2 = accountMap2.get("L_1");
		String COLLECT_TYPE = collectInfoMap.get("COLLECT_TYPE");
		String RULE_NAME = ROOT_INST_CD + (accountMap2.get("L_7")!=null?accountMap2.get("L_7"):"")+collectInfoMap.get("FUNC_NAME")+collectInfoMap.get("COLLECT_TYPE");
//		System.out.println("~~~~~~~~~~~~~~~~~~   " + accountMap2.get("L_7"));		
		String sql = sqlModule.toString();
		sql = sql.replace("{RULE_NAME}", RULE_NAME);
		sql = sql.replace("{ROOT_INST_CD}", ROOT_INST_CD);
		sql = sql.replace("{PAY_CHANNEL_ID}", PAY_CHANNEL_ID);
		sql = sql.replace("{FUNC_CODE}", FUNC_CODE);
		sql = sql.replace("{KERNEL_FUNC_CODE}", KERNEL_FUNC_CODE);
		sql = sql.replace("{ACCOUNT_NAME1}", ACCOUNT_NAME1);
		sql = sql.replace("{FIN_ACCOUNT_ID1}", FIN_ACCOUNT_ID1);
		sql = sql.replace("{ACCOUNT_NAME2}", ACCOUNT_NAME2);
		sql = sql.replace("{FIN_ACCOUNT_ID2}", FIN_ACCOUNT_ID2);
		sql = sql.replace("{OBLIGATE1}", OBLIGATE1);
		sql = sql.replace("{COLLECT_TYPE}", COLLECT_TYPE);
		return sql;
	}
	
	public static String editName(String name) {
		if(name.indexOf(")") > -1) name = name.substring(name.indexOf(")") + 1);
		if(name.indexOf("-") > -1) name = name.substring(0, name.indexOf("-"));
		return name;
	}
	
	public static List<Map<String, String>> queryItem(List<Map<String, String>> list2, String merChantCode, String name, String itemName) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(Map<String, String> map : list2) {
			String theMerChantCode = map.get("L_5");
			String theName = map.get("L_4");
			
			if(theName.indexOf(name) > -1) {
				if(theMerChantCode == null || theMerChantCode.equals(merChantCode)) {
					list.add(map);
				}
			}
		}
		return list;
	}
	/**
	 * 打印list中的信息
	 * @param list
	 */
	public static void testPrintList(List<Map<String, String>> list) {
		for(Map<String, String> map : list) {
			System.out.print("{");
			Set<String> keySet = map.keySet();
			for(String key : keySet) {
				System.out.print(key + ":" + map.get(key) + "; ");
			}
			System.out.println("}");
		}
	}
	public static List<Map<String, String>> txtreader(File file,String splitString) throws Exception {
		//声明文件输入流
		BufferedReader inputFile = null;
		int nLoop = 0;
		//文件信息结构体
		List<Map<String, String>> list =  new LinkedList<Map<String, String>>();
		//声明数据输入流
		DataInputStream in = null;
		try{
			//实例化数据输入流
			in = new DataInputStream(new FileInputStream(file));
			//初始化文件输入流
			inputFile = new BufferedReader(new InputStreamReader(in, encode));
			//
			Map<String, String> keyMap = null;
			//行信息
			String line = null;
			//行信息数组
			String[] linearr = null;
			String splitStringZ = "";
			
    		while(true){
    			linearr = null;
    			keyMap = new HashMap<String, String>();
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
    				}
    			}
//    			System.out.println(">>> >>> " + line);
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
	public static String getCollectType(String f) {		
		if(f.indexOf("F5") >= 0) {
			return "1";
		} else {
			if(mtkernelFuncCode2.indexOf(f) >= 0) {
				return "2";
			} else if(mtkernelFuncCode3.indexOf(f) >= 0) {
				return "3";
			} else if(mtkernelFuncCode4.indexOf(f) >= 0) {
				return "4";
			} else if(mtkernelFuncCode5.indexOf(f) >= 0) {
				return "5";
			} else {
				return "-1";
			}
		}
	}
}
