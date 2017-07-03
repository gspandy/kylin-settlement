package com.rkylin.settle.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.jcraft.jsch.Logger;

public class FtpDownload {
	private FTPClient ftp;

	/**
	 * @param addr
	 *            地址
	 * @param port
	 *            端口号
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public boolean connect(String addr, int port, String username,
			String password) {
		try {
			boolean result = false;
			ftp = new FTPClient();
			int reply;
			if (port != -1 ) {
				ftp.connect(addr, port);
			} else {
				ftp.connect(addr);
			}
			if (!ftp.login(username, password)) {
				return result;
			}
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			return true;
		} catch (Exception e) {
			return false;
		} 
	}

	/**
	 * 
	 * @param remotePath
	 *            ftp上的路径
	 * @param fileName
	 *            ftp上的文件名
	 * @param localPath
	 *            本地路径
	 * @param localfileName
	 *            本地的文件名
	 * @throws Exception
	 */
	public boolean download(String remotePath,String fileName,String localPath,String localfileName) {
		try {
			ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录 
			
			File filepath = new File(localPath);
			if  (!filepath.exists()) {
				filepath.mkdirs();
			}
			
    		ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
    		ftp.enterLocalPassiveMode();
    		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
	        FTPFile[] fs = ftp.listFiles();
	        for(FTPFile ff:fs){ 
	            if(ff.getName().equals(fileName)){
	            	if ("".equals(localfileName)) {
	            		File localFile = new File(localPath+File.separator+fileName);
	            		OutputStream is = new FileOutputStream(localFile);
		                ftp.retrieveFile(fileName, is); 
		                is.flush();
		                is.close(); 
	            	} else {
	            		File localFile = new File(localPath+File.separator+localfileName);
	            		OutputStream is = new FileOutputStream(localFile);
		                ftp.retrieveFile(fileName, is); 
		                is.flush();
		                is.close(); 
	            	}
	    			//ftp.logout(); 
	    			return true;
	            } 
	        }
			//ftp.logout(); 
			return false;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
	        
	    }
	}
	
	public boolean disConnect() {
		if (ftp.isConnected()) {
            try {
            	ftp.logout();
            	ftp.disconnect();
            } catch (Exception ioe) {
        		return false;
            }
        }
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FtpDownload ftpDownload = new FtpDownload();
		boolean rtnflg = ftpDownload.connect("opftp.rongcapital.cn",21,"duizhang","D%9&uiZh5#(8a117ngJ");
		System.out.println(rtnflg);
	}
}
