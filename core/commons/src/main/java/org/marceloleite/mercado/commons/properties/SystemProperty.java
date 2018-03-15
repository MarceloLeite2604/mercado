package org.marceloleite.mercado.commons.properties;

public enum SystemProperty implements Property {
	
	EMAIL_USERNAME("system.email.username", true),
	EMAIL_PASSWORD("system.email.password", true),
	NONCE("system.negotiationApi.nonce", false);

	private String name;

	private String value;

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
	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
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
