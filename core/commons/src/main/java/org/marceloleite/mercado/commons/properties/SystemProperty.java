package org.marceloleite.mercado.commons.properties;

public enum SystemProperty implements PropertyDefinition {

	EMAIL_USERNAME("system.email.username", true),
	EMAIL_PASSWORD("system.email.password", true),
	NONCE("system.negotiationApi.nonce", false);

	private String name;

	private boolean encrypted;

	private SystemProperty(String name, boolean encrypted) {
		this.name = name;
		this.encrypted = encrypted;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

	@Override
	public boolean isEncrypted() {
		return encrypted;
	}

}
