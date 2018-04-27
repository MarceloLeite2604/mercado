package org.marceloleite.mercado.email;

import org.marceloleite.mercado.commons.encryption.Encrypt;
import org.marceloleite.mercado.commons.properties.SystemProperty;
import org.marceloleite.mercado.databaseretriever.persistence.daos.PropertyDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class UsernameRetriever {

	private static String username;

	public static String retrieveUsername() {
		if (username == null) {
			PropertyPO propertyPO = retrieveProperty(SystemProperty.EMAIL_USERNAME.getName());
			String encryptedUsername = propertyPO.getValue();
			username = new Encrypt().decrypt(encryptedUsername);
		}
		return username;
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
}
