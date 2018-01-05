package org.marceloleite.mercado.simulator.conversor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.simulator.structure.AccountData;
import org.marceloleite.mercado.simulator.structure.DepositData;

public class Account {

	private String owner;

	private Balance balance;

	private TemporalController<DepositData> depositsTemporalController;

	private TemporalController<BuyOrder> buyOrdersTemporalController;

	private Map<Currency, List<Strategy>> currenciesStrategies;

	public Account(AccountData accountData) {
		super();
		this.owner = accountData.getOwner();
		this.balance = new Balance(accountData.getBalanceData());
		this.depositsTemporalController = new TemporalController<>(accountData.getDepositDatas());
		this.buyOrdersTemporalController = new TemporalController<>(accountData.getBuyOrderDatas());
		this.currenciesStrategies = new EnumMap<>(Currency.class);
	}

	public Account(String owner) {
		this(owner, null, null, null);
	}

	public String getOwner() {
		return owner;
	}

	public Balance getBalance() {
		return balance;
	}

	public TemporalController<DepositData> getDepositsTemporalController() {
		return depositsTemporalController;
	}

	public TemporalController<BuyOrder> getBuyOrdersTemporalController() {
		return buyOrdersTemporalController;
	}

	public Map<Currency, List<Strategy>> getCurrenciesStrategies() {
		return currenciesStrategies;
	}

	public void checkTimedEvents(TimeInterval currentTimeInterval) {
		checkDeposits(currentTimeInterval);
	}

	

	private void checkDeposits(TimeInterval currentTimeInterval) {
		List<DepositData> deposits = depositsTemporalController.retrieve(currentTimeInterval.getEnd());
		for (DepositData deposit : deposits) {
			balance.deposit(deposit.getCurrencyAmount());
		}
	}

}