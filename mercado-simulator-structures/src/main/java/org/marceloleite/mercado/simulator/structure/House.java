package org.marceloleite.mercado.simulator.structure;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.additional.TemporalTickerGenerator;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class House {

	private static final double DEFAULT_COMISSION_PERCENTAGE = 0.007;

	private List<CurrencyTrade> currencyTrades;
	
	private List<Account> accounts;

	private Map<String, Balance> comissionBalance;

	private double comissionPercentage;

	private Map<Currency, TemporalTickerPO> temporalTickers;

	private TemporalTickerGenerator temporalTickerGenerator;

	public House() {
		super();
		currencyTrades = new ArrayList<>();
		comissionBalance = new HashMap<>();
		comissionPercentage = DEFAULT_COMISSION_PERCENTAGE;
		this.temporalTickerGenerator = new TemporalTickerGenerator();
		this.temporalTickers = new EnumMap<>(Currency.class);
		this.accounts = new AccountsReader().readAccounts();
	}

	public static double getDefaultComissionPercentage() {
		return DEFAULT_COMISSION_PERCENTAGE;
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

	public void executeBuyOrder(Account account, BuyOrder buyOrder) {
		buyOrder.updateOrder(temporalTickers);
		Balance balance = account.getBalance();
		balance.withdraw(buyOrder.getCurrencyAmountToPay());
		balance.deposit(buyOrder.getCurrencyAmountToBuy());
	}

	public void updateTemporalTickers(TimeInterval timeInterval) {
		for (Currency currency : Currency.values()) {
			if (currency.isDigital()) {
				List<TemporalTickerPO> temporalTickersRetrieved = temporalTickerGenerator.generate(currency,
						timeInterval);
				if (temporalTickersRetrieved == null || temporalTickersRetrieved.size() != 0) {
					LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
					throw new RuntimeException("Could not retrieve temporal ticker for " + currency.getAcronym()
							+ " currency for period " + localDateTimeToStringConverter.convert(timeInterval.getStart())
							+ " to " + localDateTimeToStringConverter.convert(timeInterval.getEnd()) + ".");
				}
				TemporalTickerPO temporalTickerPO = temporalTickersRetrieved.get(0);
				temporalTickers.put(currency, temporalTickerPO);
			}
		}
	}

	public void executeTemporalEvents(TimeInterval currentTimeInterval) {
		updateTemporalTickers(currentTimeInterval);
		for (Account account : accounts) {
			account.checkTimedEvents(currentTimeInterval);
			Map<Currency, List<Strategy>> currenciesStrategies = account.getCurrenciesStrategies();
			for (Currency currency : currenciesStrategies.keySet()) {
				List<Strategy> strategies = currenciesStrategies.get(currency);
				for (Strategy strategy : strategies) {
					strategy.check(account, this);
				}
			}
			checkBuyOrders(currentTimeInterval, account);
		}
	}
	
	private void checkBuyOrders(TimeInterval currentTimeInterval, Account account) {
		List<BuyOrder> buyOrders = account.getBuyOrdersTemporalController().retrieve(currentTimeInterval.getEnd());
		for (BuyOrder buyOrder : buyOrders) {
			executeBuyOrder(account, buyOrder);
			buyOrder.updateOrder(temporalTickers);
			CurrencyAmount currencyAmountComission = calculateComission(buyOrder);
			CurrencyAmount currencyAmountToDeposit = calculateDeposit(buyOrder, currencyAmountComission);
			depositComission(currencyAmountComission, account);
			account.getBalance().withdraw(buyOrder.getCurrencyAmountToPay());
			account.getBalance().deposit(currencyAmountToDeposit);
		}

	}

	private void depositComission(CurrencyAmount currencyAmountComission, Account account) {
		Balance balance = comissionBalance.getOrDefault(account.getOwner(), new Balance());
		balance.deposit(currencyAmountComission);
	}

	private CurrencyAmount calculateDeposit(BuyOrder buyOrder, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		double amountToDeposit = currencyAmountToBuy.getAmount() - currencyAmountComission.getAmount();
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), amountToDeposit);
	}

	private CurrencyAmount calculateComission(BuyOrder buyOrder) {
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		double comissionAmount = currencyAmountToBuy.getAmount()*comissionPercentage;
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), comissionAmount);
	}
}
