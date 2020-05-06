package com.dmg.lobbyserver.model.bean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenricator extends Authenticator {
		String u = null;
		String p = null;
		public MyAuthenricator(String u,String p){
			this.u=u;
			this.p=p;
		}
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(u,p);
		}
	}