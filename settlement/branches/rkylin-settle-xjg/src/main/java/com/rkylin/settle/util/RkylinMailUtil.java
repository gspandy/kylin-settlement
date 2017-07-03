package com.rkylin.settle.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
/**
 * 
 * @author  zhenpc@chanjet.com
 * @version 2015年3月30日 下午2:54:42
 */

public class RkylinMailUtil {
	/**
	 * 发送普通邮件
	 * @param subject
	 * @param content
	 * @param to
	 * @param cc
	 * @throws Exception
	 */
	public static void sendMail(String subject, String content, String[] to,String[] cc) throws Exception{
		Properties prop=new Properties();
    	prop.put("mail.host","smtp.163.com" );
    	prop.put("mail.transport.protocol", "smtp");
    	prop.put("mail.smtp.auth", true);
    	//使用java发送邮件5步骤
    	//1.创建sesssion
    	Session session=Session.getInstance(prop);
    	//开启session的调试模式，可以查看当前邮件发送状态
    	session.setDebug(true);
    	//2.通过session获取Transport对象（发送邮件的核心API）
    	Transport ts=session.getTransport();
    	//3.通过邮件用户名密码链接
    	ts.connect("ws-sun", "Bin840716");
    	//4.创建邮件
    	Message msg=createSimpleMail(session,subject,content,to,cc);
    	//5.发送电子邮件
    	ts.sendMessage(msg, msg.getAllRecipients());
	}
	/**
	 * 编辑普通邮件内容
	 * @param session
	 * @param subject
	 * @param content
	 * @param to
	 * @param cc
	 * @return
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static MimeMessage createSimpleMail(Session session,String subject, String content, String[] to ,String[] cc) throws AddressException,MessagingException{
    	//创建邮件对象
    	MimeMessage mm=new MimeMessage(session);
    	//设置发件人
    	mm.setFrom(new InternetAddress("ws-sun@163.com"));
    	for (int i=0;i<to.length;i++) {
	    	//设置收件人
	    	mm.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
    	}
    	for (int i=0;i<cc.length;i++) {
	    	//设置抄送人
	    	mm.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
    	}
    	mm.setSubject(subject);
    	mm.setContent(content, "text/html;charset=gbk");
    	return mm;
    }
	/**
	 * 编辑带附件的邮件内容
	 * @param session
	 * @param subject
	 * @param content
	 * @param attachment
	 * @param to
	 * @param cc
	 * @return
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static MimeMessage createMultipartMail(Session session, String subject, String content, File attachment, String[] to ,String[] cc) throws AddressException,MessagingException,UnsupportedEncodingException {
    	//创建邮件对象
    	MimeMessage mm=new MimeMessage(session);
    	//设置发件人
    	mm.setFrom(new InternetAddress("ws-sun@163.com"));
    	for (int i=0;i<to.length;i++) {
	    	//设置收件人
	    	mm.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
    	}
    	for (int i=0;i<cc.length;i++) {
	    	//设置抄送人
	    	mm.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
    	}
    	mm.setSubject(subject);
    	// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件  
        Multipart multipart = new MimeMultipart(); 
        // 设置邮件的文本内容  
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setText(content);
        multipart.addBodyPart(contentPart);
        // 添加附件  
        BodyPart messageBodyPart = new MimeBodyPart();  
        DataSource source = new FileDataSource(attachment);
        // 添加附件的内容  
        messageBodyPart.setDataHandler(new DataHandler(source));
        // 添加附件的标题  
        // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码  
        messageBodyPart.setFileName(MimeUtility.encodeText(attachment.getName()));
        multipart.addBodyPart(messageBodyPart);
        // 将multipart对象放到message中  
        mm.setContent(multipart);  
        // 保存邮件  
        mm.saveChanges();
    	return mm;
    }
	/**
	 * 发送带附件的邮件
	 * @param subject		邮件标题
	 * @param content		邮件正文
	 * @param attachment	附件文件
	 * @param to			收件人
	 * @param cc			抄送
	 * @throws Exception
	 */
	public static void sendAttachmentMail(String subject, String content, File attachment, String[] to,String[] cc) throws Exception{
		Properties prop=new Properties();
    	prop.put("mail.host","smtp.163.com" );
    	prop.put("mail.transport.protocol", "smtp");
    	prop.put("mail.smtp.auth", true);
    	//使用java发送邮件5步骤
    	//1.创建sesssion
    	Session session=Session.getInstance(prop);
    	//开启session的调试模式，可以查看当前邮件发送状态
    	session.setDebug(true);
    	//2.通过session获取Transport对象（发送邮件的核心API）
    	Transport ts=session.getTransport();
    	//3.通过邮件用户名密码链接
    	ts.connect("ws-sun", "Bin840716");
    	//4.创建邮件
    	Message msg=createMultipartMail(session, subject, content, attachment, to, cc);
    	//5.发送电子邮件
    	ts.sendMessage(msg, msg.getAllRecipients());
	}
}
