package org.marceloleite.mercado.api.negotiation.util;

import org.marceloleite.mercado.commons.properties.SystemProperty;
import org.marceloleite.mercado.databaseretriever.persistence.daos.PropertyDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class NonceGenerator {

	private static final PropertyPO propertyPOToEnquire = new PropertyPO();
	
	static {
		propertyPOToEnquire.setName(SystemProperty.NONCE.getName());
		propertyPOToEnquire.setValue(null);
	}

	private static NonceGenerator INSTANCE;

	private PropertyDAO propertyDAO;

	public NonceGenerator() {
		this.propertyDAO = null;
	}

	public long nextNonce() {
		createPropertyRetriever();
		PropertyPO noncePropertyPO = propertyDAO.findById(propertyPOToEnquire);
		long nonce = 0l;
		if (noncePropertyPO != null) {
			nonce = Long.parseLong(noncePropertyPO.getValue());
		} else {
			noncePropertyPO = new PropertyPO();
			noncePropertyPO.setName(SystemProperty.NONCE.getName());
		}
		noncePropertyPO.setValue(Long.toString(++nonce));
		propertyDAO.merge(noncePropertyPO);
		return nonce;
	}

	private void createPropertyRetriever() {
		if (propertyDAO == null) {
			propertyDAO = new PropertyDAO();
		}
	}

	public static NonceGenerator getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new NonceGenerator();
		}
		return INSTANCE;
	}
}
