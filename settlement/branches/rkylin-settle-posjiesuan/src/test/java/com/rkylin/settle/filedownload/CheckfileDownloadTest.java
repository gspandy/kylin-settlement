package com.rkylin.settle.filedownload;

import static org.junit.Assert.fail;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.test.BaseJUnit4Test;
import com.rkylin.settle.util.SettlementUtil;

public class CheckfileDownloadTest extends BaseJUnit4Test {

	@Autowired
	SettlementUtil settlementUtil;
	
	@Autowired
	CheckfileDownload checkfileDownload;

	@Autowired
	LianDongFileDownload lianDongFileDownload;
	
	@Test
	public void testTlFileDown() {
	    //丰年代收付-通联对账文件下载
//		checkfileDownload.tlFileDown("M000001", "ZF","2015-08-07");
		//会唐代收付-通联对账文件下载
//		checkfileDownload.tlFileDown("M000003", "ZF","2015-08-07");
		//会唐网关支付-通联对账文件下载
//		checkfileDownload.tlFileDown("M000003", "WG","2015-08-07");
		
		checkfileDownload.merAcctBalance("M00000X",null);
//		checkfileDownload.merAcctBalance("M000005","200222000008705000");
		
//		lianDongFileDownload.merAcctBalance("M00000X", "ZF");
//		lianDongFileDownload.lianDongFileDown("2016-01-01", "M00000X", "ZF");
	}

	public void testSendToTlt() {
		fail("Not yet implemented");
	}


	public void testSendXml() {
		fail("Not yet implemented");
	}


	public void testSend() {
		fail("Not yet implemented");
	}


	public void testGetHttpsURLConnection() {
		fail("Not yet implemented");
	}
	public static void main(String[] args) throws  DocumentException {
		  String xml="<?xml version='1.0' encoding='GBK'?><AIPG>"
  +"<INFO>"
    +"<TRX_CODE>300000</TRX_CODE>"
    +"<VERSION>03</VERSION>"
    +"<DATA_TYPE>2</DATA_TYPE>"
    +"<REQ_SN>200222000008786-1452649731740</REQ_SN>"
    +"<RET_CODE>0000</RET_CODE>"
    +"<ERR_MSG>查询成功</ERR_MSG>"
    +"<SIGNED_MSG>41b732b10753fca9200a12f948e054d98a538d37c42fe27fe01063b4769086816a2cae06fb2afc412a3284d36b1707242d9ab4c5f2980fe9df7b0457fcbfa5d239839456bb3dbe71103a033ec3aa475f0fc662b67adfba7d184a13abc10e04698d801c93c4433f35712b7e23e99f427e7892a95b23b79eae76c9571ed0974b18b60ca124fa41919bd34a038979f06bfdc44f7274d732bcb4f1ef26aa2a9ac4c3b7a413b6e41749a2d0586c9bf780dcb2526f9543f1bbae86ee91e2e9a42b181e4ce567683d312e0e278850ca1ab8afc9a9f321c0d2f4ff9bb2931fb7f9b97c330b8cbca6ff43b981fcfa40279311e815676a121f75da66a53588e1c9ff840e1e</SIGNED_MSG>"
  +"</INFO>"
  +"<ACQUERYREP>"
    +"<ACNODE>"
      +" <ACCTNO>200222000008786000</ACCTNO>"
      +" <ACCTNAME>北京会唐世纪科技有限公司</ACCTNAME>"
      +" <BALANCE>48752059</BALANCE>"
      +" <USABLEBAL>0</USABLEBAL>"
      +" <BALBY>2</BALBY>"
      +" <DEPOSIT>1</DEPOSIT>"
      +" <WITHDRAW>1</WITHDRAW>"
      +" <TRANSFERIN>1</TRANSFERIN>"
      +" <TRANSFEROUT>1</TRANSFEROUT>"
      +" <PAYABLE>1</PAYABLE>"
      +" <DEFCLR>0</DEFCLR>"
    +"</ACNODE>"
  +"</ACQUERYREP>"
+"</AIPG>\n"+"1211\n"+"0";
		  xml = xml.substring(0, xml.indexOf("</AIPG>")+7);
		  Document dom=DocumentHelper.parseText(xml);
		  Element root=dom.getRootElement();
		  String weighTime=root.element("ACQUERYREP").element("ACNODE").element("BALANCE").getText();
		  System.out.println(weighTime);
		 }
	
}
