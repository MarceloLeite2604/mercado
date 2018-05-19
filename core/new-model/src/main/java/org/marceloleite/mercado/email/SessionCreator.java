package org.marceloleite.mercado.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.marceloleite.mercado.utils.EmailUtils;

public class SessionCreator {

	public static Properties properties;

	static {
		properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
	}

	private SessionCreator() {
	}

	public static Session create() {
		return Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EmailUtils.retrieveUsername(), EmailUtils.retrievePasswrod());
			}
		});
	}

}
