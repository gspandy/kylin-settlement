package com.rkylin.settle.util;

import java.io.Serializable;
import java.util.List;

public class FtpFilesInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * ftp文件目录
	 */
	private String ftpDirectory;
	/**
	 * 文件名称List
	 */
	private List<String> fileNameList;
	
	public List<String> getFileNameList() {
		return fileNameList;
	}
	public void setFileNameList(List<String> fileNameList) {
		this.fileNameList = fileNameList;
	}
	public String getFtpDirectory() {
		return ftpDirectory;
	}
	public void setFtpDirectory(String ftpDirectory) {
		this.ftpDirectory = ftpDirectory;
	}
}
