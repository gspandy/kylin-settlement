package com.rkylin.settle.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.service.SettleBalanceEntryService;
import com.rkylin.settle.util.OutputFileUtil;

@Controller
@RequestMapping("/outputFileController")
@Scope("prototype")
public class OutputFileController extends BaseController {
	@Autowired
	private SettleBalanceEntryService settleBalanceEntryService;
	
	@RequestMapping("/op_txt_balance_entry")
	public ResponseEntity<byte[]> opTxtBalanceEntry(String ids) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyyMMdd");
		Date date = new Date();
		
		List<SettleBalanceEntry> settleBalanceEntryList = settleBalanceEntryService.queryByIds(ids);
		
		String s = File.separator;
		
		String path = s + "mydata" + s + "duizhang" + s + "web" + s + sdf.format(date);
		
		String fileName = "" + date.getTime() + Math.random() * 1000 / 1;
		
		String downLoadName = new String("对账结果.txt".getBytes("UTF-8"),"iso-8859-1");
		
		try {
			OutputFileUtil.outputTxtFileByObjectList(path, fileName, settleBalanceEntryList, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpHeaders headers = new HttpHeaders();    
		
		headers.setContentDispositionFormData("attachment", downLoadName);
		
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(path, fileName + ".txt")),    
                headers, HttpStatus.CREATED);
	}
}
