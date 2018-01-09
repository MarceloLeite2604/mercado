package org.marceloleite.mercado.simulator.structure;

import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;

public class AccountData {

	private String owner;

	private BalanceData balanceData;

	private List<DepositData> depositDatas;

	private List<BuyOrderData> buyOrderDatas;

	private List<SellOrderData> sellOrderDatas;

	private Map<Currency, List<String>> currenciesStrategies;

	public AccountData(String owner, BalanceData balanceData, List<DepositData> depositsData,
			List<BuyOrderData> buyOrdersData, List<SellOrderData> sellOrdersData,
			Map<Currency, List<String>> currenciesStrategies) {
		super();
		this.owner = owner;
		this.balanceData = balanceData;
		this.depositDatas = depositsData;
		this.buyOrderDatas = buyOrdersData;
		this.sellOrderDatas = sellOrdersData;
		this.currenciesStrategies = currenciesStrategies;
	}

	public AccountData() {
		this(null, null, null, null, null, null);
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

	public List<SellOrderData> getSellOrderDatas() {
		return sellOrderDatas;
	}

	public void setSellOrderDatas(List<SellOrderData> sellOrderDatas) {
		this.sellOrderDatas = sellOrderDatas;
	}

	public Map<Currency, List<String>> getCurrenciesStrategies() {
		return currenciesStrategies;
	}

	public void setCurrenciesStrategies(Map<Currency, List<String>> currenciesStrategies) {
		this.currenciesStrategies = currenciesStrategies;
	}
}