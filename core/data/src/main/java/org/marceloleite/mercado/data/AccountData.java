package org.marceloleite.mercado.data;

import java.util.List;

public class AccountData {

	private String owner;

	private TapiInformationData tapiInformationData;
	
	private String email;

	private List<BalanceData> balanceDatas;

	private List<DepositData> depositDatas;

	private List<BuyOrderData> buyOrderDatas;

	private List<SellOrderData> sellOrderDatas;

	private List<StrategyData> strategyDatas;

	public AccountData(String owner, List<BalanceData> balanceDatas, List<DepositData> depositsData,
			List<BuyOrderData> buyOrdersData, List<SellOrderData> sellOrdersData, List<StrategyData> strategyDatas) {
		super();
		this.owner = owner;
		this.balanceDatas = balanceDatas;
		this.depositDatas = depositsData;
		this.buyOrderDatas = buyOrdersData;
		this.sellOrderDatas = sellOrdersData;
		this.strategyDatas = strategyDatas;
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

	public TapiInformationData getTapiInformationData() {
		return tapiInformationData;
	}

	public void setTapiInformationData(TapiInformationData tapiInformationData) {
		this.tapiInformationData = tapiInformationData;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<BalanceData> getBalanceDatas() {
		return balanceDatas;
	}

	public void setBalanceDatas(List<BalanceData> balanceDatas) {
		balanceDatas.forEach(balanceData -> balanceData.setAccountData(this));
		this.balanceDatas = balanceDatas;
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

	public List<StrategyData> getStrategyDatas() {
		return strategyDatas;
	}

	public void setStrategyDatas(List<StrategyData> strategyDatas) {
		this.strategyDatas = strategyDatas;
	}
}