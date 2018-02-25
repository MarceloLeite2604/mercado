package org.marceloleite.mercado.base.model.data;

import java.util.List;

import org.marceloleite.mercado.commons.Currency;

public class StrategyData {

	private AccountData accountData;

	// private String name;

	private Currency currency;

	private List<StrategyParameterData> strategyParameterDatas;

	private List<StrategyVariableData> strategyVariableDatas;
	
	private List<StrategyClassData> strategyClassDatas;

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

	public List<StrategyParameterData> getStrategyParameterDatas() {
		return strategyParameterDatas;
	}

	public void setStrategyParameterDatas(List<StrategyParameterData> strategyParameterDatas) {
		this.strategyParameterDatas = strategyParameterDatas;
	}

	public List<StrategyVariableData> getStrategyVariableDatas() {
		return strategyVariableDatas;
	}

	public void setStrategyVariableDatas(List<StrategyVariableData> strategyVariableDatas) {
		this.strategyVariableDatas = strategyVariableDatas;
	}

	public List<StrategyClassData> getStrategyClassDatas() {
		return strategyClassDatas;
	}

	public void setStrategyClassDatas(List<StrategyClassData> strategyClassDatas) {
		this.strategyClassDatas = strategyClassDatas;
	}
}
