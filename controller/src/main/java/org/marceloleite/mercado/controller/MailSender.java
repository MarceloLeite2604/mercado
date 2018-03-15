package org.marceloleite.mercado.controller;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.marceloleite.mercado.commons.encryption.Encrypt;
import org.marceloleite.mercado.commons.properties.SystemProperty;
import org.marceloleite.mercado.databaseretriever.persistence.daos.PropertyDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class MailSender {

	public static Properties properties;

	static {
		properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
	}

	public void sendEmail(List<String> toAddressesList, List<String> ccAddressesList, List<String> bccAddressesList,
			String subject, String content) {
		String username = retrieveUsername();
		Session session = Session.getInstance(properties, new Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, retrievePassword());
			}
		});
		
		Address[] toAddresses = createAddresses(toAddressesList);
		Address[] ccAddresses = createAddresses(ccAddressesList);
		Address[] bccAddresses = createAddresses(bccAddressesList);

		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, toAddresses);
			message.setRecipients(Message.RecipientType.CC, ccAddresses);
			message.setRecipients(Message.RecipientType.BCC, bccAddresses);
			message.setSubject(subject);
			message.setText(content);

			Transport.send(message);
		} catch (MessagingException exception) {
			exception.printStackTrace();
		}
	}

	private Address[] createAddresses(List<String> addressesList) {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			if (addressesList != null) {
				addressesList.forEach(address -> stringBuilder.append(address + ","));
			}

			return InternetAddress.parse(stringBuilder.toString());
		} catch (AddressException exception) {
			throw new RuntimeException("Error while creating mail addresses.", exception);
		}
	}

	private String retrieveUsername() {
		PropertyPO propertyPO = retrieveProperty(SystemProperty.EMAIL_USERNAME.getName());
		return propertyPO.getValue();
	}

	private String retrievePassword() {
		PropertyPO propertyPO = retrieveProperty(SystemProperty.EMAIL_PASSWORD.getName());
		String encryptedPassword = propertyPO.getValue();
		return new Encrypt().decrypt(encryptedPassword);
	}

	private PropertyPO retrieveProperty(String propertyName) {
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

}
