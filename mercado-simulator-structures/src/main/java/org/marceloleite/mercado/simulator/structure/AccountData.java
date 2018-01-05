package org.marceloleite.mercado.simulator.structure;

import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;

public class AccountData {

	private String owner;

	private BalanceData balanceData;

	private List<DepositData> depositDatas;

	private List<BuyOrderData> buyOrderDatas;

	private Map<Currency, List<String>> currenciesStrategies;

	public AccountData(String owner, BalanceData balanceData, List<DepositData> depositDatas,
			List<BuyOrderData> buyOrderDatas, Map<Currency, List<String>> currenciesStrategies) {
		super();
		this.owner = owner;
		this.balanceData = balanceData;
		this.depositDatas = depositDatas;
		this.buyOrderDatas = buyOrderDatas;
		this.currenciesStrategies = currenciesStrategies;
	}

	public AccountData() {
		this(null, null, null, null, null);
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public BalanceData getBalanceData() {
		return balanceData;
	}

	public void setBalanceData(BalanceData balanceData) {
		this.balanceData = balanceData;
	}

	public List<DepositData> getDepositDatas() {
		return depositDatas;
	}

	public void setDepositDatas(List<DepositData> depositDatas) {
		this.depositDatas = depositDatas;
	}

	public List<BuyOrderData> getBuyOrderDatas() {
		return buyOrderDatas;
	}

	public void setBuyOrderDatas(List<BuyOrderData> buyOrderDatas) {
		this.buyOrderDatas = buyOrderDatas;
	}

	public Map<Currency, List<String>> getCurrenciesStrategies() {
		return currenciesStrategies;
	}

	public void setCurrenciesStrategies(Map<Currency, List<String>> currenciesStrategies) {
		this.currenciesStrategies = currenciesStrategies;
	}
}