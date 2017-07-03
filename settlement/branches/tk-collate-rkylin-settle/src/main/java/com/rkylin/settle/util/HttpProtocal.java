package com.rkylin.settle.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public class HttpProtocal {
	private static Logger logger = Logger.getLogger(HttpProtocal.class);

	public Map httpMerInfoPro(String url, Map sendParams) throws Exception {
		StringBuffer contentBuffer = new StringBuffer();
		Map resMap = new HashMap();
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// while(sendParams.keySet().iterator().hasNext()){
		// String key = (String)sendParams.keySet().iterator().next();
		// String paramsVal = (String)sendParams.get(key);
		// paramsVal = java.net.URLEncoder.encode(paramsVal, "UTF-8");
		// //构造键值对参数
		// NameValuePair[] data = { new NameValuePair(key, paramsVal)};
		// // 把参数值放入postMethod中
		// postMethod.setRequestBody(data);
		//
		// }
		// 记录发送出去的数据
		logger.info("发送出去的报文" + postMethod.getPath());
		int statusCode = client.executeMethod(postMethod);
		System.out.println(" status code:" + statusCode);
		InputStream in = postMethod.getResponseBodyAsStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				postMethod.getResponseCharSet()));
		String inputLine = null;
		while ((inputLine = reader.readLine()) != null) {
			contentBuffer.append(inputLine);
			System.out.println("input line:" + inputLine);
		}
		if (statusCode == HttpURLConnection.HTTP_OK) {
			// resMap =
			// com.umpay.api.paygate.v40.Plat2Mer_v40.getResDataByMeta("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html><head><META NAME=\"MobilePayPlatform\" CONTENT=\"amount=1&amt_type=RMB&gate_id=CMB&mer_date=20130609&mer_id=7428&mer_priv=9900&order_id=428289&pay_date=20130609&refund_amt=0&ret_code=0000&ret_msg=操作成功&settle_date=20130609&sign_type=RSA&trade_no=1306091425368870&trade_state=TRADE_SUCCESS&version=4.0&sign=OxmMVT+hkLWznL6SZHYm6etBwl7vPn2zpXsS5qbdHJbyc6zpCmKJteSgv/dA5viigvA0kCLwtdEVpE5RXdHGnKKy9wVMo9f/Aty9EpBP72XbLUwIWJS+S/jtwufQTDxlDElDwNiYYplvJlVbu80h6sq3slM2x0AiVzQ/cZesqLc=\"></head><body></body></html>");//(contentBuffer.toString());
			resMap = com.umpay.api.paygate.v40.Plat2Mer_v40
					.getResData(contentBuffer.toString());
		}
		in.close();

		System.out.println("contentBuffer.toString():"
				+ contentBuffer.toString());
		logger.info("url,接收到的报文:" + url + "," + contentBuffer.toString());
		return resMap;
	}

	public StringBuffer httpMerInfoProStrB(String url, Map sendParams,String filepath)
			throws Exception {
		StringBuffer contentBuffer = new StringBuffer();
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		// 记录发送出去的数据
		logger.info("发送出去的报文" + postMethod.getPath());
		int statusCode = client.executeMethod(postMethod);
		System.out.println(" status code:" + statusCode);
		InputStream in = postMethod.getResponseBodyAsStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				postMethod.getResponseCharSet()));
		String inputLine = null;

		RandomAccessFile file = new RandomAccessFile(filepath, "rw");

		while ((inputLine = reader.readLine()) != null) {
			contentBuffer.append(inputLine);
			System.out.println("input line:" + inputLine);

			file.writeUTF(inputLine);
			// file.writeBytes("\r\n");
			String crlf = System.getProperty("line.separator");
			file.writeBytes(crlf);

		}
		file.close();
		in.close();

		// System.out.println("contentBuffer.toString():"+
		// contentBuffer.toString());
		// logger.info("url,接收到的报文:"+url+","+contentBuffer.toString());
		return contentBuffer;
	}
}
