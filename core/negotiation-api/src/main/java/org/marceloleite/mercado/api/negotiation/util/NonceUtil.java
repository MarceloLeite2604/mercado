package org.marceloleite.mercado.api.negotiation.util;

import javax.inject.Inject;

import org.marceloleite.mercado.commons.properties.SystemProperty;
import org.marceloleite.mercado.dao.interfaces.PropertyDAO;
import org.marceloleite.mercado.model.Property;

public class NonceUtil {

	private static NonceUtil instance;

	@Inject
	private PropertyDAO propertyDAO;

	private NonceUtil() {
	}

	public long next() {
		Property nonceProperty = propertyDAO.findByName(SystemProperty.NONCE.getName());

		if (nonceProperty == null) {
			throw new RuntimeException("Could not find property \"" + SystemProperty.NONCE.getName() + "\".");
		}
		long nonce = Long.parseLong(nonceProperty.getValue());
		nonceProperty.setValue(Long.toString(++nonce));
		propertyDAO.save(nonceProperty);
		return nonce;
	}

	public static NonceUtil getInstance() {
		if (instance == null) {
			instance = new NonceUtil();
		}
		return instance;
	}
}
