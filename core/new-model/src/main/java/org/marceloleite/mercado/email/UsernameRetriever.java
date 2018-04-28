package org.marceloleite.mercado.email;

import org.marceloleite.mercado.commons.encryption.Encrypt;
import org.marceloleite.mercado.commons.properties.SystemProperty;
import org.marceloleite.mercado.dao.interfaces.PropertyDAO;
import org.marceloleite.mercado.model.Property;

public class UsernameRetriever {
	
	private static UsernameRetriever instance;

	private static String username;
	
	private PropertyDAO propertyDAO;

	public String retrieveUsername() {
		if (username == null) {
			Property property = retrieveProperty(SystemProperty.EMAIL_USERNAME.getName());
			username = new Encrypt().decrypt(property.getValue());
		}
		return username;
	}

	private Property retrieveProperty(String name) {
		Property property = propertyDAO.findByName(name);

		if (property == null) {
			throw new RuntimeException(
					"Could not find property \"" + name + "\".");
		}

		if (property.getValue() == null) {
			throw new RuntimeException("The property \"" + name + "\" does not have a value.");
		}
		return property;
	}
	
	public static UsernameRetriever getInstance() {
		if (instance == null ) {
			instance = new UsernameRetriever();
		}
		return instance;
	}
}
