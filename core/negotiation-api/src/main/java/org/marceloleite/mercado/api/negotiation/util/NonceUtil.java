package org.marceloleite.mercado.api.negotiation.util;

import org.marceloleite.mercado.cdi.MercadoApplicationContextAware;
import org.marceloleite.mercado.commons.properties.SystemProperty;
import org.marceloleite.mercado.dao.interfaces.PropertyDAO;
import org.marceloleite.mercado.model.Property;
import org.springframework.stereotype.Component;

@Component
public final class NonceUtil {

	private static PropertyDAO propertyDAO = MercadoApplicationContextAware.getBean(PropertyDAO.class,
			"PropertyDatabaseDAO");

	private NonceUtil() {
	}

	public static long next() {
		Property nonceProperty = propertyDAO.findByName(SystemProperty.NONCE.getName());

		if (nonceProperty == null) {
			throw new RuntimeException("Could not find property \"" + SystemProperty.NONCE.getName() + "\".");
		}
		long nonce = Long.parseLong(nonceProperty.getValue());
		nonceProperty.setValue(Long.toString(++nonce));
		propertyDAO.save(nonceProperty);
		return nonce;
	}
}
