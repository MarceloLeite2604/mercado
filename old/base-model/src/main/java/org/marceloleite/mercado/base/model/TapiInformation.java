package org.marceloleite.mercado.base.model;

import org.marceloleite.mercado.data.TapiInformationData;

public class TapiInformation {

	private String id;

	private String secret;
	
	public TapiInformation() {
		super();
	}

	public TapiInformation(TapiInformationData tapiInformationData) {
		this.id = new String(tapiInformationData.getId());
		this.secret = new String(tapiInformationData.getSecret());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public TapiInformationData retrieveData() {
		TapiInformationData tapiInformationData = new TapiInformationData();
		tapiInformationData.setId(id);
		tapiInformationData.setSecret(secret);
		return tapiInformationData;
	}
}
