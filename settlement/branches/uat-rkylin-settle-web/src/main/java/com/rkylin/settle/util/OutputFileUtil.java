package com.rkylin.settle.util;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.rkylin.settle.constant.SettleConstants;

public class OutputFileUtil {
	/***
	 * 根据实体的toString生成txt文件
	 * @param filePath
	 * @param fileName
	 * @param objectList
	 * @param encoding
	 * @throws Exception
	 */
	public static void outputTxtFileByObjectList(String filePath, String fileName, List<?> objectList, String encoding) throws Exception {
		//文件目录
		File folder = new File(filePath);
		//文件名称
		File file = new File(filePath + File.separator + fileName + ".txt");
		//如果文件目录不存在,创建此目录
		if(!folder.exists()) folder.mkdirs();
		//字符串缓冲区
		StringBuffer sb = new StringBuffer();
		//拼接文件内容
		Iterator<?> objectIter = objectList.iterator();
		while(objectIter.hasNext()) {
			sb.append(objectIter.next().toString() + "\r\n");
		}
		//写入文件内容并生成文件
		FileUtils.writeStringToFile(file, sb.toString(), encoding);
	}
	/**
	 * 下载已上传的下游对账文件 - 获取文件名称
	 * @param merchantName
	 * @param channelName
	 * @param transType
	 * @param accountDate
	 * @return
	 */
	public String[] getFilenameListFromFileUPath(String merchantName, String channelName, String transType, String accountDate) {
		/**
		 * 获取服务器上传下游对账文件的备份路径的信息
		 */
		File folder = new File(SettleConstants.FILE_UP_PATH + File.separator + "beifen");
		return folder.list(new FileUpPathFilenameFilter(merchantName, channelName, transType, accountDate));
	}
	/**
	 * 下载上游对账文件到本地 - 获取文件名称
	 * @param merchantName
	 * @param channelName
	 * @param transType
	 * @param accountDate
	 * @return
	 */
	public String[] getFilenameListFromFileDPath(String merchantName, String channelName, String transType, String accountDate) throws Exception {
		/**
		 * 通过账期 获取文件父目录
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		/**
		 * 目录名称 = 账期 + 1天
		 */
		Date date = sdf.parse(accountDate);
		date.setTime(date.getTime() + 24 * 60 * 60 * 1000);
		/**
		 * 获取下载上游对账文件父目录信息
		 */
		String accountDateStr = sdf.format(date);
		File folder = new File(SettleConstants.FILE_PATH + File.separator + accountDateStr);
		return folder.list(new FileUpPathFilenameFilter(merchantName, channelName, transType, accountDate));
	}
	/**
	 * 下载已上传的下游对账文件 - 文件名称过滤器
	 * @author CaoYang
	 */
	private class FileUpPathFilenameFilter implements FilenameFilter {
		private String merchantName;		//机构简称
		private String channelName;			//渠道简称
		private String transType;			//交易类型
		private String accountDate;			//账期
		//构造器
		public FileUpPathFilenameFilter(String merchantName, String channelName, String transType, String accountDate) {
			this.merchantName = merchantName != null && !"".equals(merchantName.trim()) ? merchantName : "";
			this.channelName = channelName != null && !"".equals(channelName.trim()) ? channelName : "";
			this.transType = transType != null && !"".equals(transType.trim()) ? transType : "";
			this.accountDate = accountDate != null && !"".equals(accountDate.trim()) ? accountDate : "";
		}
		//过滤方法
		public boolean accept(File dir, String name) {
			return 	name.indexOf(merchantName) > -1
					&& name.indexOf(channelName) > -1
					&& name.indexOf(transType) > -1
					&& name.indexOf(accountDate) > -1;
		}
	}
	/**
	 * 我的下载 - 获取文件对象数组
	 * @param transType
	 * @param accountDate
	 * @return
	 */
	public Map<String, String>[] getFilenameListFromMyDLPath(File folder, String transType, String accountDate) throws Exception {
		/**
		 * 获取下载上游对账文件父目录信息 只获取.xlsx文件
		 */
		File[] fileArr = folder.listFiles(new FileUpPathFilenameFilter(null, ".xlsx", transType, accountDate));
		HashMap<String, String>[] hashMaps = new HashMap[fileArr.length];
		Map<String, String>[] mapArr = hashMaps;
		/**
		 * 按最后修改时间排序倒序(冒泡)
		 */
		Long iFile = null;
		Long jFile = null;
		File temp = null;
		for(int i = 0; i < fileArr.length; i++) {
			for(int j = i + 1; j < fileArr.length; j++) {
				iFile = new Long(fileArr[i].lastModified());
				jFile = new Long(fileArr[j].lastModified());
				if(iFile < jFile) {
					temp = fileArr[i];
					fileArr[i] = fileArr[j];
					fileArr[j] = temp;
				}
			}
		}
		for(int i = 0; i < fileArr.length; i ++) {
			mapArr[i] = new HashMap<String, String>();
			mapArr[i].put("name", fileArr[i].getName());
			mapArr[i].put("lastModified", String.valueOf(fileArr[i].lastModified()));
		}
		return mapArr;
	}
	
	public static void main(String[] args) {
//		/**
//		 * 下载已上传的下游对账文件 - 文件名称过滤器
//		 * @author CaoYang
//		 */
//		class FileUpPathFilenameFilter implements FilenameFilter {
//			private String merchantName;		//机构简称
//			private String channelName;			//渠道简称
//			private String transType;			//交易类型
//			private String accountDate;			//账期
//			//构造器
//			public FileUpPathFilenameFilter(String merchantName, String channelName, String transType, String accountDate) {
//				this.merchantName = merchantName != null && !"".equals(merchantName.trim()) ? merchantName : "";
//				this.channelName = channelName != null && !"".equals(channelName.trim()) ? channelName : "";
//				this.transType = transType != null && !"".equals(transType.trim()) ? transType : "";
//				this.accountDate = accountDate != null && !"".equals(accountDate.trim()) ? accountDate : "";
//			}
//			//过滤方法
//			public boolean accept(File dir, String name) {
//				return 	name.indexOf(merchantName) > -1
//						&& name.indexOf(channelName) > -1
//						&& name.indexOf(transType) > -1
//						&& name.indexOf(accountDate) > -1;
//			}
//		}
//		/**
//		 * 获取下载上游对账文件父目录信息
//		 */
//		String filePath = "D:\\Rkylin\\笔记\\周报\\00羊\\";	//目录
//    	File folder = new File(filePath);
//		File[] fileArr = folder.listFiles(new FileUpPathFilenameFilter(null, null, "啊啊啊", "160122"));
//		/**
//		 * 按最后修改时间排序
//		 */
//		Long iFile = null;
//		Long jFile = null;
//		File temp = null;
//		for(int i = 0; i < fileArr.length; i++) {
//			for(int j = i + 1; j < fileArr.length; j++) {
//				iFile = new Long(fileArr[i].lastModified());
//				jFile = new Long(fileArr[j].lastModified());
//				if(iFile < jFile) {
//					temp = fileArr[i];
//					fileArr[i] = fileArr[j];
//					fileArr[j] = temp;
//				}
//			}
//		}
//		
//		for(File f : fileArr) {
//			System.out.println(f.getName() + " --- --- " + f.lastModified());
//		}
	}
}