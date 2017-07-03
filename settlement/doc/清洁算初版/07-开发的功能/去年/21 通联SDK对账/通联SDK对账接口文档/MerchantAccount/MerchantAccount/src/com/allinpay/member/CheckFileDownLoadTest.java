package com.allinpay.member;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;





import org.bouncycastle.util.encoders.Base64;
/**
 * 对账文件下载Test
 * 
 * 如果有任何对代码的修改,请按下面的格式注明修改的内容.
 * 
 * 序号 时间 作者 修改内容
 * 
 * 1. 2011-3-9 Robin created this class.
 * 错误代码描述：
 * ERRORCODE:001 ERRORDES:系统繁忙.请稍候............
 * ERRORCODE:002 ERRORDES:请传入有效的商户号,结算日期,signMsg
 * ERRORCODE:003 ERRORDES:结算日期格式错误(格式为yyyy-MM-dd)
 * ERRORCODE:004 ERRORDES:商户号不存在或者MD5key没有设置
 * ERRORCODE:005 ERRORDES:摘要信息验证有误
 * ERRORCODE:006 ERRORDES:没有相应的对账信息
 * 
 * 
 */

public class CheckFileDownLoadTest {
	public static  final String FIRST_TEXT="http://ceshi.allinpay.com/member/checkfiledown/CheckFileDownLoad/checkfileDownLoad.do";
	public static  final String FIRST_PRODUCT="https://service.allinpay.com/member/checkfiledown/CheckFileDownLoad/checkfileDownLoad.do";
	/**
	 * 二期生产地址
	 */
	
	
	public static  final String SECOND_PRODUCT="https://merchant.allinpay.com/ms/onlinebill/download";
	public static  final String SECOND_TEST="http://116.236.252.102:8019/ms/onlinebill/download";
	
	/**
	 * 一期  与二期的商户号
     */
	public static  final String FIRST_mchtCd="100020091218001";
	public static  final String SECOND_mchtCd="008310107420054";
	
	public static  final String SECOND_mchtCd_test="008310107420099";
	
	/**
	 *  一期有账目日期   与二期有账目记录日期
	 * @param args
	 */
	public static  final String FIRST_settleDate="2016-07-31";
	public static  final String SECOND_settleDate="2016-08-11";
	
	public static  final String SECOND_settleDate_test="2016-08-26";
	
	
	/*
	 * 得到HttpURLConnection对象 
	 * @author: Robin 
	 * @param : URL 
	 * @return:HttpURLConnection 
	 * @date : 2011-03-10
	 */

	public HttpURLConnection getHttpsURLConnection(URL url) {

		try {
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			
			if ("https".equals(url.getProtocol())) // 如果是https协议
			{
				HttpsURLConnection httpsConn = (HttpsURLConnection) httpConnection;
				TrustManager[] managers = { new MyX509TrustManager() };// 证书过滤
				SSLContext sslContext;
				sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, managers, new SecureRandom());
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				httpsConn.setSSLSocketFactory(ssf);
				httpsConn.setHostnameVerifier(new MyHostnameVerifier());// 主机名过滤
				return httpsConn;

			} 
			return httpConnection;
	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;

	}
	/*
	 * 编写证书过滤器
	 * @author: Robin 
	 * @date : 2011-03-10
	 */
	public class MyX509TrustManager implements X509TrustManager
	{

		/**
		 * 该方法体为空时信任所有客户端证书
		 * 
		 * @param chain
		 * @param authType
		 * @throws CertificateException
		 */
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
		/**
		 * 该方法体为空时信任所有服务器证书
		 * 
		 * @param chain
		 * @param authType
		 * @throws CertificateException
		 */

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		/**
		 * 返回信任的证书
		 * @return
		 */
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
	/*
	 * 编写主机名过滤器
	 * @author: Robin 
	 * @date : 2011-03-10
	 */
	private class MyHostnameVerifier implements HostnameVerifier
	{

		/**
		 * 返回true时为通过认证 当方法体为空时，信任所有的主机名
		 * 
		 * @param hostname
		 * @param session
		 * @return
		 */
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}

	}
	/*
	 * 验证签名信息
	 * @author: Robin 
	 * @param : String , String  
	 * @return: boolean
	 * @date : 2011-03-10
	 */

	public static boolean verifySign(String src, String mac) {
		try {
			src = SecurityUtil.MD5Encode(src);
			String rootPath = CheckFileDownLoadTest.class.getResource("/").getFile()
					.toString();
			rootPath = rootPath.substring(1, rootPath.length() - 4)
					+ "libs/TLCert-prod.cer";
			boolean result=com.allinpay.member.SecurityUtil.verifyByRSA(
					rootPath, src.getBytes("utf-8"), Base64
							.decode(mac));//"c:/cert/TLCert.cer"
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	/**
	 * 主函数
	 * @param args
	 */
	public static void main(String[] args) {

		//创建测试对象CheckFileDownLoadTest
		CheckFileDownLoadTest test = new CheckFileDownLoadTest();
		//创建变量srcStr-对账文件source
		StringBuffer srcStr = new StringBuffer();
		//创建变量mac-签名信息
		String mac = "";
		//定义标志位 0表示初始位 1表示读取签名位
		int flag = 0;
		//定义 访问地址,商户号,结算日期,md5key的值
		//http://192.168.1.81
		//https://service.allinpay.com
		//String serverHost = "https://service.allinpay.com";//接入生产环境的地址为https://service.allinpay.com
		//String mchtCd = "100000000000004";  //商户号
		//String settleDate = "2011-11-18"; //格式为yyyy-MM-dd
		//String md5key = "1234567890";        //md5key
		
//		String serverHost = "http://192.168.103.54";//接入生产环境的地址为https://service.allinpay.com
//		String mchtCd = "100020091218001";  //商户号
//		String settleDate = "2013-11-05"; //格式为yyyy-MM-dd
//		String md5key = "1234567890";        //md5key
//
//		// 得到摘要(MD5Encode函数的传入参数为商户号+结算日期+md5key)
//		String signMsg = SecurityUtil.MD5Encode(mchtCd + settleDate + md5key);
//		System.out.print(signMsg);
//		System.out.println("------------");
		//   
		String serverUrl =SECOND_PRODUCT;// ;  SECOND_TEST
		String mchtCd = SECOND_mchtCd;//;    SECOND_mchtCd_test
		String settleDate ="2016-08-11";// "2016-08-11"; // 格式为 yyyy-MM-dd   SECOND_settleDate_test
		String md5key = "1234567890"; // md5key   
		String fileAsString = ""; // 签名信息前的对账文件内容 
		String fileSignMsg = ""; // 文件签名信息
		boolean isVerified = false; // 验证签名结果
		
		try {
			String signMsg = SecurityUtil.MD5Encode(mchtCd + settleDate + md5key);
			// 建立连接   
			URL url = new URL(serverUrl + "?mchtCd=" + mchtCd+ "&settleDate=" + settleDate + "&signMsg=" + signMsg);    
			
			
			
//			URL url = new URL(
//					serverHost
//							+ "/member/checkfiledown/CheckFileDownLoad/checkfileDownLoad.do?mchtCd="
//							+ mchtCd + "&settleDate=" + settleDate
//							+ "&signMsg=" + signMsg);
			
			
			
			System.out.println("url:"+url.toString());
			HttpURLConnection httpConn = test.getHttpsURLConnection(url);
			httpConn.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String lines;
			while ((lines = reader.readLine()) != null) {

				if (flag == 1) {
					mac = lines;
					break;
				}
				if (lines.length() > 0) {
					srcStr.append(lines + "\r\n");
				} else {
					flag = 1;
				}

			}
			//关闭BufferedReader
			reader.close();
			// 断开连接
			httpConn.disconnect();
			
			//将对账文件信息和签名信息输出到控制台
			System.out.print(srcStr.toString());
			//System.out.println("hello");
			System.out.print(mac);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 验证签名的结果输出到控制台
		System.out.println("");	
		System.out.println("");
		boolean verifybool=false;
		if(srcStr!=null && srcStr.length()>0 && mac!=null && mac.length()>0){
			verifybool=verifySign(srcStr.toString(), mac);
		}
		 
		if(!verifybool){
			System.out.print("**不能进行对账文件的操作**");
		}else{
			System.out.print("**可以进行对账文件的操作**");
		}

	}

}
