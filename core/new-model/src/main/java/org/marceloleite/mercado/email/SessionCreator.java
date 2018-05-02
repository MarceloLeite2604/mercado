package org.marceloleite.mercado.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.marceloleite.mercado.commons.properties.SystemProperty;
import org.marceloleite.mercado.commons.utils.EncryptUtils;
import org.marceloleite.mercado.dao.interfaces.PropertyDAO;
import org.marceloleite.mercado.model.Property;

public class SessionCreator {
	
	private static SessionCreator instance;
	
	private Session session;

	public Properties properties;
	
	private PropertyDAO propertyDAO;
	
	private SessionCreator() {
		super();
		createProperties();
		createSession();
	}
	
	public Session getSession() {
		return session;
	}

	private void createSession() {
		session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(UsernameRetriever.getInstance().retrieveUsername(), retrievePassword());
			}
		});
	}

	private void createProperties() {
		properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
	}

	private Property retrieveProperty(String name) {
		Property property = propertyDAO.findByName(name);

		if (property == null) {
			throw new RuntimeException(
					"Could not find property \"" + name + "\".");
		}

		if (property.getValue() == null) {
			throw new RuntimeException("Property \"" + name + "\" does not have a value.");
		}
		return property;
	}

	private String retrievePassword() {
		Property property = retrieveProperty(SystemProperty.EMAIL_PASSWORD.getName());
		return EncryptUtils.getInstance().decrypt(property.getValue());
	}
	
	public static SessionCreator getInstante() {
		if (instance == null ) {
			instance = new SessionCreator();
		}
		return instance;
	}
}
