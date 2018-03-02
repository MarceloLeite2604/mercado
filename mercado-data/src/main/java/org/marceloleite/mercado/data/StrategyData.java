package org.marceloleite.mercado.data;

import java.util.List;

import org.marceloleite.mercado.commons.Currency;

public class StrategyData {

	private AccountData accountData;

	// private String name;

	private Currency currency;

	private List<ClassData> classDatas;

	public StrategyData() {
		super();
	}

	public AccountData getAccountData() {
		return accountData;
	}

	/*public String getName() {
		return name;
	}*/

	/*public void setName(String name) {
		this.name = name;
	}*/

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setAccountData(AccountData accountData) {
		this.accountData = accountData;
	}

	public List<ClassData> getClassDatas() {
		return classDatas;
	}

	public void setClassDatas(List<ClassData> classDatas) {
		this.classDatas = classDatas;
	}
}
