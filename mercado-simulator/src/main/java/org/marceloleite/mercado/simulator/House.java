package org.marceloleite.mercado.simulator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.consumer.model.Currency;
import org.marceloleite.mercado.nnew.PriceRetriever;

public class House {

	private static final double DEFAULT_COMISSION_PERCENTAGE = 0.007;

	private Balance balance;

	private List<CurrencyTrade> currencyTrades;

	private Balance comission;

	private double comissionPercentage;
	
	private List<Deposit> deposits;

	public House() {
		super();
		balance = new Balance();
		currencyTrades = new ArrayList<>();
		comission = new Balance();
		comissionPercentage = DEFAULT_COMISSION_PERCENTAGE;
	}

	public static double getDefaultComissionPercentage() {
		return DEFAULT_COMISSION_PERCENTAGE;
	}

	public Balance getBalance() {
		return balance;
	}

	public List<CurrencyTrade> getCurrencyTrades() {
		return currencyTrades;
	}

	public double getComissionPercentage() {
		return comissionPercentage;
	}

	public void setComissionPercentage(double comissionPercentage) {
		this.comissionPercentage = comissionPercentage;
	}

	public Balance getComission() {
		return comission;
	}

	public void buyFromDesiredCurrencyAmount(CurrencyAmount currencyAmountToBuy, LocalDateTime time) {
		double currencyPrice = new PriceRetriever().retrieve(currencyAmountToBuy.getCurrency(), time);
		double cost = currencyAmountToBuy.getAmount() * currencyPrice;
		double buyComission = currencyAmountToBuy.getAmount() * comissionPercentage;
		CurrencyTrade currencyTrade = new CurrencyTrade();
		currencyTrade.setPrice(currencyPrice);
		currencyTrade.setFrom(new CurrencyAmount(Currency.REAL, cost));
		currencyTrade.setTo(currencyAmountToBuy);
		currencyTrade.setComission(new CurrencyAmount(currencyAmountToBuy.getCurrency(), buyComission));

		update(currencyTrade);
	}

	public void buyFromOtherCurrencyAmount(Currency desiredCurrency, CurrencyAmount currencyAmountToSpend,
			LocalDateTime time) {

		if (currencyAmountToSpend.getCurrency() != Currency.REAL) {
			throw new RuntimeException("Not implemented yet.");

		}

		double currencyPrice = new PriceRetriever().retrieve(desiredCurrency, time);

		double quantityToBuy = currencyAmountToSpend.getAmount() / currencyPrice;
		double buyComission = quantityToBuy * comissionPercentage;

		CurrencyTrade currencyTrade = new CurrencyTrade();
		currencyTrade.setPrice(currencyPrice);
		currencyTrade.setFrom(currencyAmountToSpend);
		currencyTrade.setTo(new CurrencyAmount(desiredCurrency, quantityToBuy));
		currencyTrade.setComission(new CurrencyAmount(desiredCurrency, buyComission));

		update(currencyTrade);
	}

	private void update(CurrencyTrade currencyTrade) {
		currencyTrades.add(currencyTrade);

		balance.deposit(currencyTrade.getTo());
		balance.withdraw(currencyTrade.getFrom());
		comission.deposit(currencyTrade.getComission());
	}

	public void deposit(Deposit deposit) {
		balance.deposit(deposit.getCurrencyAmount());
	}
}
