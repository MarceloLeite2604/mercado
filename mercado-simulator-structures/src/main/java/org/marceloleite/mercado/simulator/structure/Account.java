package org.marceloleite.mercado.simulator.structure;

import java.util.Map;

import org.marceloleite.mercado.commons.Currency;

public class Account {

	private String owner;

	private Balance balance;

	private TemporalController<Deposit> depositsTemporalController;

	private TemporalController<BuyOrder> buyOrdersTemporalController;

	private Map<Currency, Double> basePrices;

	private Map<Currency, CurrencyMonitoring> currenciesMonitoring;

	public Account() {
		this("");
		/*
		 * this.currenciesMonitoring = new EnumMap<>(Currency.class);
		 * 
		 * this.buyOrdersTemporalController = new TemporalController<>();
		 */
	}

	public Account(String owner) {
		super();
		this.owner = owner;
		this.balance = new Balance();
		this.depositsTemporalController = new TemporalController<>();
		/*
		 * this.currenciesMonitoring = new EnumMap<>(Currency.class);
		 * this.depositsTemporalController = new TemporalController<>();
		 * this.buyOrdersTemporalController = new TemporalController<>();
		 */
	}

	/*
	 * public void addCurrencyMonitoring(CurrencyMonitoring currencyMonitoring) {
	 * currenciesMonitoring.put(currencyMonitoring.getCurrency(),
	 * currencyMonitoring); }
	 */

	/*
	 * public void addDeposit(Deposit deposit, LocalDateTime time) {
	 * depositsTemporalController.add(time, deposit); }
	 */

	/*
	 * public void addBuyOrder(BuyOrder buyOrder, LocalDateTime time) {
	 * buyOrdersTemporalController.add(time, buyOrder); }
	 */

	/*
	 * public Map<Currency, CurrencyMonitoring> getCurrenciesMonitoring() { return
	 * currenciesMonitoring; }
	 */

	/*
	 * public Map<Currency, Double> getBasePrices() { return basePrices; }
	 */

	public String getOwner() {
		return owner;
	}

	public Balance getBalance() {
		return balance;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public TemporalController<Deposit> getDepositsTemporalController() {
		return depositsTemporalController;
	}

	public void setDepositsTemporalController(TemporalController<Deposit> depositsTemporalController) {
		this.depositsTemporalController = depositsTemporalController;
	}

}