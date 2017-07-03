package com.rkylin.settle.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUpload {
	private FTPClient ftp;

	/**
	 * @param path
	 *            上传到ftp服务器哪个路径下
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
	public boolean connect(String path, String addr, int port, String username,
			String password) {
		try {
			boolean result = false;
			ftp = new FTPClient();
			int reply;
			ftp.connect(addr, port);
			if (!ftp.login(username, password)) {
				return result;
			}
			if (!ftp.setFileType(FTPClient.BINARY_FILE_TYPE)) {
				return result;
			}
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			if (!ftp.changeWorkingDirectory(path)) {
				return result;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * @param file 上传的文件或文件夹
	 * @throws Exception
	 */
	public boolean upload(File file) {
		return upload(null, file);
	}
	
	/**
	 * @param file 上传的文件或文件夹
	 * @throws Exception
	 */
	public boolean upload(String FtpFileName, File file) {
		try {
			if (file.isDirectory()) {
				ftp.makeDirectory(file.getName());
				ftp.changeWorkingDirectory(file.getName());
				String[] files = file.list();
				for (int i = 0; i < files.length; i++) {
					File file1 = new File(file.getPath() + "//" + files[i]);
					if (file1.isDirectory()) {
						upload(FtpFileName, file1);
						ftp.changeToParentDirectory();
					} else {
						File file2 = new File(file.getPath() + "//" + files[i]);
						FileInputStream input = new FileInputStream(file2);
						ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
						ftp.enterLocalPassiveMode();
						ftp.storeFile(file2.getName(), input);
						input.close();
					}
				}
			} else {
				String targetFileName = FtpFileName == null ? file.getName() : FtpFileName;
				FileInputStream input = new FileInputStream(file);
				ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
				ftp.enterLocalPassiveMode();
				ftp.storeFile(targetFileName, input);			
				input.close();
			}
			return true;
		} catch (Exception e) {
			System.out.println("FTP___________" + e.getMessage());
			return false;
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (Exception ioe) {
				}
			}
		}
	}
	
	/**
	 * @param fileArr 上传的文件数组
	 * @throws Exception
	 */
	public boolean upload(List<File> fileList) {
		try {
			FileInputStream input = null;
			ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			for(File file : fileList) {
				if(file == null || !file.exists()) {
					continue;
				}
				input = new FileInputStream(file);
				ftp.storeFile(file.getName(), input);			
				input.close();
			}
			return true;
		} catch (Exception e) {
			System.out.println("FTP___________" + e.getMessage());
			return false;
		} finally {
			try {
				if (ftp != null && ftp.isConnected()) ftp.disconnect();
			} catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
