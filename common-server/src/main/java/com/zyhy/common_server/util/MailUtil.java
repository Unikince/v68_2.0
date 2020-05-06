package com.zyhy.common_server.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil implements Runnable {
	private String email;// 收件人邮箱
	private String code;// 激活码
	private String MINE = "must.eq.2o5@163.com";
	private String MINEPWD = "ww13205076919";
	private String HOST = "smtp.163.com";
	public MailUtil(String email, String code) {
		this.email = email;
		this.code = code;
	}
	
	public void run() {
		
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", HOST);
		properties.setProperty("mail.smtp.auth", "true");

		try {
			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			properties.put("mail.smtp.ssl.enable", "true");
			properties.put("mail.smtp.ssl.socketFactory", sf);
			Session session = Session.getDefaultInstance(properties, new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(MINE, MINEPWD);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(MINE));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject("收到一条新的消息");
			String content = code;
			message.setContent(content, "text/html;charset=UTF-8");
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
