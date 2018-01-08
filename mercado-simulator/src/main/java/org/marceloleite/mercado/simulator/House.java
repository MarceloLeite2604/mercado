package org.marceloleite.mercado.simulator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.retriever.TemporalTickerRetriever;
import org.marceloleite.mercado.retriever.exception.NoTemporalTickerForPeriodException;
import org.marceloleite.mercado.simulator.strategy.Strategy;
import org.marceloleite.mercado.simulator.structure.AccountData;
import org.marceloleite.mercado.xml.reader.AccountsReader;

public class House {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(House.class);

	private static final double DEFAULT_COMISSION_PERCENTAGE = 0.007;

	private List<Account> accounts;

	private Map<String, Balance> comissionBalance;

	private double comissionPercentage;

	private Map<Currency, TemporalTickerPO> temporalTickers;

	private TemporalTickerRetriever temporalTickerRetriever;

	public House() {
		super();
		comissionBalance = new HashMap<>();
		comissionPercentage = DEFAULT_COMISSION_PERCENTAGE;
		this.temporalTickerRetriever = new TemporalTickerRetriever();
		this.temporalTickers = new EnumMap<>(Currency.class);
		this.accounts = retrieveAccounts();
	}

	private List<Account> retrieveAccounts() {
		List<Account> accounts = new ArrayList<>();
		List<AccountData> accountsData = new AccountsReader().readAccounts();
		for (AccountData accountData : accountsData) {
			accounts.add(new Account(accountData));
		}
		return accounts;
	}

	public static double getDefaultComissionPercentage() {
		return DEFAULT_COMISSION_PERCENTAGE;
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
				TemporalTickerPO temporalTickerPO;
				try {
					temporalTickerPO = temporalTickerRetriever.retrieve(currency,
							timeInterval, false);
				} catch (NoTemporalTickerForPeriodException e) {
					temporalTickerPO = null;
				}
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
		double comissionAmount = currencyAmountToBuy.getAmount() * comissionPercentage;
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), comissionAmount);
	}
}
