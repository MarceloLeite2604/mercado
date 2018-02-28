package org.marceloleite.mercado.api.negotiation.util;

import org.marceloleite.mercado.databaseretriever.persistence.daos.PropertyDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class NonceGenerator {

	private static final String NONCE_PROPERTY_NAME = "system.negotiationapi.nonce";

	private static final PropertyPO propertyPOToEnquire = new PropertyPO();
	
	static {
		propertyPOToEnquire.setName(NONCE_PROPERTY_NAME);
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
			noncePropertyPO.setName(NONCE_PROPERTY_NAME);
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
