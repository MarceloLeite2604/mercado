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
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.simulator.order.SellOrderBuilder.SellOrder;
import org.marceloleite.mercado.simulator.strategy.Strategy;
import org.marceloleite.mercado.simulator.structure.AccountData;
import org.marceloleite.mercado.xml.reader.AccountsReader;

public class House {

	private static final Logger LOGGER = LogManager.getLogger(House.class);

	private static final double DEFAULT_COMISSION_PERCENTAGE = 0.007;

	private List<Account> accounts;

	private Map<String, Balance> comissionBalance;

	private double comissionPercentage;

	private Map<Currency, TemporalTickerPO> temporalTickers;

	private Map<Currency, TemporalTickerVariation> temporalTickerVariations;

	private TemporalTickerRetriever temporalTickerRetriever;

	private House(List<Account> accounts, Map<String, Balance> comissionBalance, double comissionPercentage,
			Map<Currency, TemporalTickerPO> temporalTickers,
			Map<Currency, TemporalTickerVariation> temporalTickerVariations,
			TemporalTickerRetriever temporalTickerRetriever) {
		this.accounts = accounts;
		this.comissionBalance = comissionBalance;
		this.comissionPercentage = comissionPercentage;
		this.temporalTickers = temporalTickers;
		this.temporalTickerVariations = temporalTickerVariations;
	}

	public House() {
		super();
		comissionBalance = new HashMap<>();
		comissionPercentage = DEFAULT_COMISSION_PERCENTAGE;
		this.temporalTickerRetriever = new TemporalTickerRetriever();
		this.temporalTickers = new EnumMap<>(Currency.class);
		this.temporalTickerVariations = new EnumMap<>(Currency.class);
		this.accounts = retrieveAccounts();
	}

	public House(House house) {
		this(new ArrayList<>(house.getAccounts()), new HashMap<>(house.getComissionBalance()),
				house.getComissionPercentage(), new EnumMap<>(house.getTemporalTickers()),
				new EnumMap<>(house.getTemporalTickerVariations()), new TemporalTickerRetriever());
	}

	public List<Account> getAccounts() {
		return new ArrayList<>(accounts);
	}

	public Map<String, Balance> getComissionBalance() {
		return new HashMap<>(comissionBalance);
	}

	public Map<Currency, TemporalTickerPO> getTemporalTickers() {
		return new EnumMap<>(temporalTickers);
	}

	public Map<Currency, TemporalTickerVariation> getTemporalTickerVariations() {
		return new EnumMap<>(temporalTickerVariations);
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

	public void updateTemporalTickers(TimeInterval timeInterval) {
		TemporalTickerPO previousTemporalTicker;
		for (Currency currency : Currency.values()) {
			/* TODO: Watch you with BGOLD. */
			if (currency.isDigital() && currency != Currency.BGOLD) {
				TemporalTickerPO temporalTickerPO;
				/* try { */
				temporalTickerPO = temporalTickerRetriever.retrieve(currency, timeInterval, false);
				/*
				 * } catch (NoTemporalTickerForPeriodException e) { temporalTickerPO = null; }
				 */
				previousTemporalTicker = temporalTickers.get(currency);
				TemporalTickerVariation temporalTickerVariation = null;
				/* temporalTickers.put(currency, temporalTickerPO); */
				if (temporalTickerPO != null) {
					temporalTickers.put(currency, temporalTickerPO);
					temporalTickerVariation = new TemporalTickerVariation(previousTemporalTicker, temporalTickerPO);
					/*
					 * TemporalTickerVariation temporalTickerVariation = new
					 * TemporalTickerVariation( previousTemporalTicker, temporalTickerPO);
					 * temporalTickerVariations.put(currency, temporalTickerVariation);
					 */
				}
				temporalTickerVariations.put(currency, temporalTickerVariation);
			}
		}
	}

	public void executeTemporalEvents(TimeInterval currentTimeInterval) {
		updateTemporalTickers(currentTimeInterval);
		for (Account account : accounts) {
			account.checkTimedEvents(currentTimeInterval);
			checkStrategies(currentTimeInterval, account);
			checkBuyOrders(currentTimeInterval, account);
			checkSellOrders(currentTimeInterval, account);
		}
	}

	private void checkStrategies(TimeInterval currentTimeInterval, Account account) {
		Map<Currency, List<Strategy>> currenciesStrategies = account.getCurrenciesStrategies();
		for (List<Strategy> strategies : currenciesStrategies.values()) {
			strategies.forEach(strategy -> strategy.check(currentTimeInterval, account, this));
		}
	}

	private void checkBuyOrders(TimeInterval currentTimeInterval, Account account) {
		List<BuyOrder> buyOrders = account.getBuyOrdersTemporalController().retrieve(currentTimeInterval);
		for (BuyOrder buyOrder : buyOrders) {
			buyOrder.updateOrder(temporalTickers);
			CurrencyAmount currencyAmountToPay = buyOrder.getCurrencyAmountToPay();
			if (hasBalance(account, currencyAmountToPay)) {
				LOGGER.info(currentTimeInterval + ": Executing " + buyOrder + " on \"" + account.getOwner()
						+ "\" account.");
				CurrencyAmount currencyAmountCommission = calculateComission(buyOrder);
				LOGGER.debug("Commission amount is " + currencyAmountCommission + ".");
				CurrencyAmount currencyAmountToDeposit = calculateDeposit(buyOrder, currencyAmountCommission);
				LOGGER.debug("Amount to withdraw is " + currencyAmountToPay + ".");
				depositComission(currencyAmountCommission, account);
				account.getBalance().withdraw(currencyAmountToPay);
				LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
				account.getBalance().deposit(currencyAmountToDeposit);
			} else {
				LOGGER.info("Account \"" + account.getOwner() + "\" does not have enough "
						+ currencyAmountToPay.getCurrency() + " balance to execute buy order. Cancelling.");
			}
		}
	}

	private boolean hasBalance(Account account, CurrencyAmount amountToHave) {
		Currency currency = amountToHave.getCurrency();
		CurrencyAmount balanceAmount = account.getBalance().get(currency);
		return (balanceAmount.getAmount() >= amountToHave.getAmount());
	}

	private void checkSellOrders(TimeInterval currentTimeInterval, Account account) {
		List<SellOrder> sellOrders = account.getSellOrdersTemporalController().retrieve(currentTimeInterval);
		for (SellOrder sellOrder : sellOrders) {
			sellOrder.updateOrder(temporalTickers);
			CurrencyAmount currencyAmountToSell = sellOrder.getCurrencyAmountToSell();
			if (hasBalance(account, currencyAmountToSell)) {
				LOGGER.info("Executing " + sellOrder + " on \"" + account.getOwner() + "\" account.");
				CurrencyAmount currencyAmountCommission = calculateCommission(sellOrder);
				CurrencyAmount currencyAmountToDeposit = calculateDeposit(sellOrder, currencyAmountCommission);
				LOGGER.debug("Commission amount is " + currencyAmountCommission + ".");
				depositComission(currencyAmountCommission, account);
				LOGGER.debug("Amount to withdraw is " + currencyAmountToSell + ".");
				account.getBalance().withdraw(currencyAmountToSell);
				LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
				account.getBalance().deposit(currencyAmountToDeposit);
			} else {
				LOGGER.info("Account \"" + account.getOwner() + "\" does not have enough "
						+ currencyAmountToSell.getCurrency() + " balance to execute sell order. Cancelling.");
			}
		}
	}

	private void depositComission(CurrencyAmount currencyAmountComission, Account account) {
		Balance balance = comissionBalance.getOrDefault(account.getOwner(), new Balance());
		balance.deposit(currencyAmountComission);
		comissionBalance.put(account.getOwner(), balance);
	}

	private CurrencyAmount calculateDeposit(BuyOrder buyOrder, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		double amountToDeposit = currencyAmountToBuy.getAmount() - currencyAmountComission.getAmount();
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), amountToDeposit);
	}

	private CurrencyAmount calculateDeposit(SellOrder sellOrder, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToReceive = sellOrder.getCurrencyAmountToReceive();
		double amountToDeposit = currencyAmountToReceive.getAmount() - currencyAmountComission.getAmount();
		return new CurrencyAmount(currencyAmountToReceive.getCurrency(), amountToDeposit);
	}

	private CurrencyAmount calculateComission(BuyOrder buyOrder) {
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		double comissionAmount = currencyAmountToBuy.getAmount() * comissionPercentage;
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), comissionAmount);
	}

	private CurrencyAmount calculateCommission(SellOrder sellOrder) {
		CurrencyAmount currencyAmountToReceive = sellOrder.getCurrencyAmountToReceive();
		double comissionAmount = currencyAmountToReceive.getAmount() * comissionPercentage;
		return new CurrencyAmount(currencyAmountToReceive.getCurrency(), comissionAmount);
	}
}
