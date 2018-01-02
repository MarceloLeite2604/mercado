package org.marceloleite.mercado.simulator;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.structure.CurrencyMonitoring;
import org.marceloleite.mercado.xmlstructures.BuyOrder;
import org.marceloleite.mercado.xmlstructures.Deposit;
import org.marceloleite.mercado.xmlstructures.TemporalController;

public class Account {
	
	private String owner;
	
	private Balance balance;
	
	private TemporalController<Deposit> depositsTemporalController;

	private TemporalController<BuyOrder> buyOrdersTemporalController;
	
	private Map<Currency, Double> basePrices;
	
	private Map<Currency, CurrencyMonitoring> currenciesMonitoring;

	public Account(String owner) {
		super();
		this.owner = owner;
		this.balance = new Balance();
		this.currenciesMonitoring = new EnumMap<>(Currency.class);
		this.depositsTemporalController = new TemporalController<>();
		this.buyOrdersTemporalController = new TemporalController<>();
	}
	
	public void addCurrencyMonitoring(CurrencyMonitoring currencyMonitoring) {
		currenciesMonitoring.put(currencyMonitoring.getCurrency(), currencyMonitoring);
	}
	
	public void addDeposit(Deposit deposit, LocalDateTime time) {
		depositsTemporalController.add(time, deposit);
	}

	public void addBuyOrder(BuyOrder buyOrder, LocalDateTime time) {
		buyOrdersTemporalController.add(time, buyOrder);
	}
	
	public Map<Currency, CurrencyMonitoring> getCurrenciesMonitoring() {
		return currenciesMonitoring;
	}
	
	public Map<Currency, Double> getBasePrices() {
		return basePrices;
	}
	
	public String getOwner() {
		return owner;
	}

}
