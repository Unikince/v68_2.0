package com.dmg.lobbyserver.service.impl;

import com.dmg.lobbyserver.model.bean.MyAuthenricator;
import com.dmg.lobbyserver.service.SendEmailService;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

@Service
public class SendEmailServiceImpl implements SendEmailService {

	@Value("${email.account}")
	private String account;    //登录用户名
	@Value("${email.pass}")
	private String pass;        //登录密码
	@Value("${email.host}")
	private String host;        //服务器地址（邮件服务器）
	@Value("${email.port}")
	private String port;        //端口
	@Value("${email.protocol}")
	private String protocol; //协议

	public void send(String to, String subject, String content){
		Properties prop = new Properties();
		//协议
		prop.setProperty("mail.transport.protocol", protocol);
		//服务器
		prop.setProperty("mail.smtp.host", host);
		//端口
		prop.setProperty("mail.smtp.port", port);
		//使用smtp身份验证
		prop.setProperty("mail.smtp.auth", "true");
		//使用SSL，企业邮箱必需！
		//开启安全协议
		MailSSLSocketFactory sf = null;
		try {
			sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		}
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.socketFactory", sf);

		Session session = Session.getDefaultInstance(prop, new MyAuthenricator(account, pass));
		session.setDebug(true);
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			//发件人
			mimeMessage.setFrom(new InternetAddress(account,"大拇哥互娱科技"));        //可以设置发件人的别名
			//mimeMessage.setFrom(new InternetAddress(account));    //如果不需要就省略
			//收件人
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			//主题
			mimeMessage.setSubject(subject);
			//时间
			mimeMessage.setSentDate(new Date());
			//容器类，可以包含多个MimeBodyPart对象
			Multipart mp = new MimeMultipart();

			//MimeBodyPart可以包装文本，图片，附件
			MimeBodyPart body = new MimeBodyPart();
			//HTML正文
			body.setContent(content, "text/html; charset=UTF-8");
			mp.addBodyPart(body);

			//添加图片&附件
			//body = new MimeBodyPart();
			//body.attachFile(fileStr);
			//mp.addBodyPart(body);

			//设置邮件内容
			mimeMessage.setContent(mp);
			//仅仅发送文本
			//mimeMessage.setText(content);
			mimeMessage.saveChanges();
			Transport.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}