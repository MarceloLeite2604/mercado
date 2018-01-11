package org.marceloleite.mercado.simulator;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.marceloleite.mercado.simulator.order.BuyOrder;
import org.marceloleite.mercado.simulator.order.SellOrder;
import org.marceloleite.mercado.simulator.strategy.FirstStrategy;
import org.marceloleite.mercado.simulator.strategy.OriginalStrategy;
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
		/* TODO: Remove. */
		for (Account account : accounts) {

			if ("FirstStrategy".equals(account.getOwner())) {
				Strategy strategy = new FirstStrategy();
				strategy.setCurrency(Currency.BITCOIN);
				account.getCurrenciesStrategies().put(Currency.BITCOIN, Arrays.asList(strategy));
			}

			if ("OriginalStrategy".equals(account.getOwner())) {
				Strategy strategy = new OriginalStrategy();
				strategy.setCurrency(Currency.BITCOIN);
				account.getCurrenciesStrategies().put(Currency.BITCOIN, Arrays.asList(strategy));
			}
		}
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
			if (currency.isDigital()) {
				TemporalTickerPO temporalTickerPO;
				try {
					temporalTickerPO = temporalTickerRetriever.retrieve(currency, timeInterval, false);
				} catch (NoTemporalTickerForPeriodException e) {
					temporalTickerPO = null;
				}
				previousTemporalTicker = temporalTickers.get(currency);
				temporalTickers.put(currency, temporalTickerPO);
				if (temporalTickerPO != null) {
					TemporalTickerVariation temporalTickerVariation = new TemporalTickerVariation(
							previousTemporalTicker, temporalTickerPO);
					temporalTickerVariations.put(currency, temporalTickerVariation);
				}
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
			LOGGER.info("Executing " + buyOrder + " on \"" + account.getOwner() + "\" account.");
			CurrencyAmount currencyAmountCommission = calculateComission(buyOrder);
			LOGGER.debug("Commission amount is " + currencyAmountCommission + ".");
			CurrencyAmount currencyAmountToDeposit = calculateDeposit(buyOrder, currencyAmountCommission);
			LOGGER.debug("Amount to withdraw is " + buyOrder.getCurrencyAmountToPay() + ".");
			depositComission(currencyAmountCommission, account);
			account.getBalance().withdraw(buyOrder.getCurrencyAmountToPay());
			LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
			account.getBalance().deposit(currencyAmountToDeposit);
		}
	}

	private void checkSellOrders(TimeInterval currentTimeInterval, Account account) {
		List<SellOrder> sellOrders = account.getSellOrdersTemporalController().retrieve(currentTimeInterval);
		for (SellOrder sellOrder : sellOrders) {
			sellOrder.updateOrder(temporalTickers);
			LOGGER.info("Executing " + sellOrder + " on \"" + account.getOwner() + "\" account.");
			CurrencyAmount currencyAmountCommission = calculateCommission(sellOrder);
			CurrencyAmount currencyAmountToDeposit = calculateDeposit(sellOrder, currencyAmountCommission);
			LOGGER.debug("Commission amount is " + currencyAmountCommission + ".");
			depositComission(currencyAmountCommission, account);
			LOGGER.debug("Amount to withdraw is " + sellOrder.getCurrencyAmountToSell() + ".");
			account.getBalance().withdraw(sellOrder.getCurrencyAmountToSell());
			LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
			account.getBalance().deposit(currencyAmountToDeposit);
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
