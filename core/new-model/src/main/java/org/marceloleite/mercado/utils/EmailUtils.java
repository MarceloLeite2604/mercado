package org.marceloleite.mercado.utils;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.marceloleite.mercado.cdi.MercadoApplicationContextAware;
import org.marceloleite.mercado.commons.properties.SystemProperty;
import org.marceloleite.mercado.commons.utils.EncryptUtils;
import org.marceloleite.mercado.dao.interfaces.PropertyDAO;
import org.marceloleite.mercado.model.Property;

public final class EmailUtils {

	private static PropertyDAO propertyDAO = MercadoApplicationContextAware.getBean(PropertyDAO.class,
			"PropertyDatabaseDAO");

	private EmailUtils() {
	}

	public static String retrieveUsername() {
		Property property = retrieveProperty(SystemProperty.EMAIL_USERNAME.getName());
		return EncryptUtils.decrypt(property.getValue());
	}

	public static String retrievePasswrod() {
		Property property = retrieveProperty(SystemProperty.EMAIL_PASSWORD.getName());
		return EncryptUtils.decrypt(property.getValue());
	}

	private static Property retrieveProperty(String name) {
		Property property = propertyDAO.findByName(name);

		if (property == null) {
			throw new RuntimeException("Could not find property \"" + name + "\".");
		}

		if (property.getValue() == null) {
			throw new RuntimeException("The property \"" + name + "\" does not have a value.");
		}
		return property;
	}

	public static MimeMultipart createGraphicMimeMultipart(File fileLocation) {

		MimeMultipart multipart = new MimeMultipart("related");

		BodyPart messageBodyPart = new MimeBodyPart();
		String htmlText = "<img src=\"cid:image\">";
		try {
			messageBodyPart.setContent(htmlText, "text/html");
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			DataSource dataSource = new FileDataSource(fileLocation);

			messageBodyPart.setDataHandler(new DataHandler(dataSource));
			messageBodyPart.setHeader("Content-ID", "<image>");

			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException exception) {
			throw new RuntimeException("Error while creating graphic mime multipart.", exception);
		}

		return multipart;
	}

	public MimeMultipart createGraphicMimeMultipart(String fileLocation) {
		return createGraphicMimeMultipart(new File(fileLocation));
	}
}
