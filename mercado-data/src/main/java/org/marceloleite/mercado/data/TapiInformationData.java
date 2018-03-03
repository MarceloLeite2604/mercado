package org.marceloleite.mercado.data;

public class TapiInformationData {

	private AccountData accountData;

	private String id;

	private String secret;

	public AccountData getAccountData() {
		return accountData;
	}

	public void setAccountData(AccountData accountData) {
		this.accountData = accountData;
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
}
