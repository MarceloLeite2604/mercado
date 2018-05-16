package org.marceloleite.mercado.email;

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
}
