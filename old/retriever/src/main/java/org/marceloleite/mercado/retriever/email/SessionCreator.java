package org.marceloleite.mercado.retriever.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.marceloleite.mercado.commons.encryption.Encrypt;
import org.marceloleite.mercado.commons.properties.SystemProperty;
import org.marceloleite.mercado.databaseretriever.persistence.daos.PropertyDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class SessionCreator {

	public static Properties properties;

	static {
		properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
	}

	private static Session session;

	public static Session createSession() {
		if (session == null) {
			session = Session.getInstance(properties, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(UsernameRetriever.retrieveUsername(), retrievePassword());
				}
			});
		}
		return session;
	}

	private static PropertyPO retrieveProperty(String propertyName) {
		PropertyPO propertyPoToEnquire = new PropertyPO();
		propertyPoToEnquire.setName(propertyName);
		PropertyPO propertyPO = new PropertyDAO().findById(propertyPoToEnquire);

		if (propertyPO == null) {
			throw new RuntimeException(
					"Could not find property \"" + propertyPoToEnquire.getName() + "\" on database.");
		}

		if (propertyPO.getValue() == null) {
			throw new RuntimeException("The property \"" + propertyPoToEnquire.getName() + "\" does not have a value.");
		}
		return propertyPO;
	}

	private static String retrievePassword() {
		PropertyPO propertyPO = retrieveProperty(SystemProperty.EMAIL_PASSWORD.getName());
		String encryptedPassword = propertyPO.getValue();
		return new Encrypt().decrypt(encryptedPassword);
	}
}
