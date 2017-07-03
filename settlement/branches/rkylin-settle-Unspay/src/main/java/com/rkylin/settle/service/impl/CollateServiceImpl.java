package com.rkylin.settle.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.filedownload.CheckfileDownload;
import com.rkylin.settle.filedownload.CjzfFileDown;
import com.rkylin.settle.filedownload.LianDongFileDownload;
import com.rkylin.settle.filedownload.LycheeDownload;
import com.rkylin.settle.filedownload.MultiFileDown;
import com.rkylin.settle.filedownload.PABB2BDownload;
import com.rkylin.settle.filedownload.UnsPayFileDownload;
import com.rkylin.settle.logic.CollateFileLogic;
import com.rkylin.settle.logic.CollateLogic;
import com.rkylin.settle.logic.ReadFileLogic;
import com.rkylin.settle.service.AccountService;
import com.rkylin.settle.service.CollateService;
import com.rkylin.settle.service.ProfitService;

@Service("collateService")
public class CollateServiceImpl implements CollateService {
	@Autowired
	private CollateLogic collateLogic;			//对账相关逻辑
	@Autowired
	private ReadFileLogic readFileLogic;		//读取文件相关逻辑
	@Autowired
	private CollateFileLogic collateFileLogic;	//写入文件相关逻辑
	@Autowired
	private ProfitService profitService;		//分润相关service
	@Autowired
	private AccountService accountService;		//分润相关service
	@Autowired
	private CheckfileDownload checkfileDownload;   //下载上游对账文件
	@Autowired
	private LianDongFileDownload lianDongfileDownload;   //联动下载上游对账文件
	@Autowired
	private CjzfFileDown cjzfFileDown;				//畅捷下载上游对账文件
	@Autowired
	private MultiFileDown multiFileDown;				//多渠道上游对账文件
	@Autowired
	private PABB2BDownload pABB2BDownload;				//平安B2B对账文件下载
	@Autowired
	private LycheeDownload lycheeDownload;				//快付通对账文件下载
	@Autowired
	private UnsPayFileDownload unsPayDownload;
	
	@Override
	public Map<String, Object> getTransDetailFromMultiGate()
			throws Exception {
		return collateLogic.getTransDetailFromMultiGate();
	}
	@Override
	public Map<String, String> tlFileDown(String rootInstId, String fileType,
			String accountDate) throws Exception {
		return checkfileDownload.tlFileDown(rootInstId, fileType);
	}
	@Override
	public Map<String, Object> readCollateFile(String marchantCode,
			String readType, String accountDate, String fileType,
			String payChannelId) throws Exception {
		return collateFileLogic.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}

	@Override
	public void collage(String payChannelId, String accountType,
			String merchantCode, String bussType) throws Exception {
		accountService.collage(payChannelId, accountType, merchantCode, bussType);
	}
	
	@Override
	public void collage(String payChannelId, String[] accountType,
			String merchantCode, String bussType) throws Exception {
		accountService.collage(payChannelId, accountType, merchantCode, bussType);
	}
	@Override
	public Map<String, Object> createCollateFile(String marchantCode,
			String readType)
			throws Exception {
		return collateFileLogic.createCollateFile(marchantCode, readType);
	}
	@Override
	public Map<String, Object> createCollateFile(String marchantCode,
			String readType, Date accountDate)
			throws Exception {
		
		return collateFileLogic.createCollateFile(marchantCode, readType, accountDate);
	}
	
	@Override
	public Map<String, Object> uploadCollateFile(String marchantCode,
			String readType)
			throws Exception {
		return collateFileLogic.uploadCollateFile(marchantCode, readType);
	}
	@Override
	public Map<String, Object> uploadCollateFile(String marchantCode,
			String readType, Date accountDate)
			throws Exception {
		return collateFileLogic.uploadCollateFile(marchantCode, readType, accountDate);
	}
	@Override
	public void tLFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception {
		if (!SettleConstants.ACCOUNT_COED_WXMOBILE.equals(fileType) 
		&& !SettleConstants.ACCOUNT_COED_TLSDK.equals(fileType)
		&& !SettleConstants.ACCOUNT_COED_WXAPP.equals(fileType)) {
			this.tlFileDown(marchantCode, fileType, accountDate);
		} else {
			multiFileDown.multiFileDown(accountDate, marchantCode, fileType);
		}
		this.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}
	@Override
	public void lLFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception {
		checkfileDownload.lianLianFileDown(accountDate, marchantCode, fileType);
		this.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}
	@Override
	public void lDFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception {
		lianDongfileDownload.lianDongFileDown(accountDate, marchantCode, fileType);
		this.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}
	@Override
	public void cjFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception {
		cjzfFileDown.cjFileDown(marchantCode, fileType);
		this.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}
	
	@Override
	public void collageAccAndMulti(String[] funcCodes,String payChannelId,String readType,String merchantCode,Date accountDate) throws Exception {
		accountService.collageAccAndMulti(funcCodes,payChannelId,readType,merchantCode,accountDate);
	}
	
	@Override
	public void multiFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception {
		multiFileDown.multi_4013C006(accountDate, marchantCode, fileType);
		this.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}
	@Override
	public void multiFileDownAndReadCollateFile_FTP(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception {
		multiFileDown.multiFileDown(marchantCode, fileType);
		this.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}
	@Override
	public void multiFileDownAndReadCollateFileP_FTP(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception {
		multiFileDown.multiFileDown(null, marchantCode, fileType, payChannelId);
		this.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}
	
	@Override
	public void padB2BDownAndReadCollateFile_SFTP(String marchantCode, String readType, String accountDate, String fileType, String payChannelId) throws Exception {
		pABB2BDownload.pABB2BFileDown(accountDate, marchantCode);
		this.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}
	@Override
	public void lycheeDownAndReadCollateFile_FTP(String marchantCode,
			String readType, String accountDate, String fileType,
			String payChannelId) throws Exception {
		lycheeDownload.lycheeFileDown(accountDate, marchantCode);
		this.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}
	
	@Override
	public void unsPayFileDownAndReadCollateFile(String marchantCode, String readType, String accountDate,
			String fileType, String payChannelId) throws Exception {
		unsPayDownload.fileDown(accountDate, marchantCode);
		this.readCollateFile(marchantCode, readType, accountDate, fileType, payChannelId);
	}
}
