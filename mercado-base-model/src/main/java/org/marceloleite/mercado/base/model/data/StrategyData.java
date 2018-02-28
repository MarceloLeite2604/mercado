package org.marceloleite.mercado.base.model.data;

import java.util.List;

import org.marceloleite.mercado.commons.Currency;

public class StrategyData {

	private AccountData accountData;

	// private String name;

	private Currency currency;

	private List<ParameterData> strategyParameterDatas;

	private List<VariableData> strategyVariableDatas;
	
	private List<ClassData> strategyClassDatas;

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

	public List<ParameterData> getStrategyParameterDatas() {
		return strategyParameterDatas;
	}

	public void setStrategyParameterDatas(List<ParameterData> strategyParameterDatas) {
		this.strategyParameterDatas = strategyParameterDatas;
	}

	public List<VariableData> getStrategyVariableDatas() {
		return strategyVariableDatas;
	}

	public void setStrategyVariableDatas(List<VariableData> strategyVariableDatas) {
		this.strategyVariableDatas = strategyVariableDatas;
	}

	public List<ClassData> getStrategyClassDatas() {
		return strategyClassDatas;
	}

	public void setStrategyClassDatas(List<ClassData> strategyClassDatas) {
		this.strategyClassDatas = strategyClassDatas;
	}
}
