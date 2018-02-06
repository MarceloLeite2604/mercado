package org.marceloleite.mercado.simulator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.retriever.TemporalTickerRetriever;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.simulator.order.OrderExecutor;
import org.marceloleite.mercado.simulator.order.OrderStatus;
import org.marceloleite.mercado.simulator.order.SellOrderBuilder.SellOrder;
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
			/* TODO: Watch out with BGOLD. */
			if (currency.isDigital() && currency != Currency.BGOLD) {
				TemporalTickerPO temporalTickerPO;
				temporalTickerPO = temporalTickerRetriever.retrieve(currency, timeInterval, false);
				previousTemporalTicker = temporalTickers.get(currency);
				TemporalTickerVariation temporalTickerVariation = null;
				if (temporalTickerPO != null) {
					temporalTickers.put(currency, temporalTickerPO);
					temporalTickerVariation = new TemporalTickerVariation(previousTemporalTicker, temporalTickerPO);
				}
				temporalTickerVariations.put(currency, temporalTickerVariation);
			}
		}
	}

	private void updateTemporalTickers(Map<Currency, TemporalTickerPO> temporalTickerPOsByCurrency) {
		TemporalTickerPO previousTemporalTicker;
		for (Currency currency : Currency.values()) {
			/* TODO: Watch you with BGOLD. */
			if (currency.isDigital() && currency != Currency.BGOLD) {
				TemporalTickerPO temporalTickerPO = temporalTickerPOsByCurrency.get(currency);
				previousTemporalTicker = temporalTickers.get(currency);
				TemporalTickerVariation temporalTickerVariation = null;
				if (temporalTickerPO != null) {
					temporalTickers.put(currency, temporalTickerPO);
					temporalTickerVariation = new TemporalTickerVariation(previousTemporalTicker, temporalTickerPO);
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

	public void executeTemporalEvents(TimeDivisionController timeDivisionController) {
		Map<TimeInterval, Map<Currency, TemporalTickerPO>> temporalTickerPOsByTimeInterval = temporalTickerRetriever
				.bulkRetrieve(timeDivisionController);
		for (TimeInterval timeInterval : temporalTickerPOsByTimeInterval.keySet()) {
			Map<Currency, TemporalTickerPO> temporalTickerPOsByCurrency = temporalTickerPOsByTimeInterval
					.get(timeInterval);
			updateTemporalTickers(temporalTickerPOsByCurrency);
			for (Account account : accounts) {
				account.checkTimedEvents(timeInterval);
				checkStrategies(timeInterval, account);
				checkBuyOrders(timeInterval, account);
				checkSellOrders(timeInterval, account);
			}
		}
	}

	public void executeTemporalEvents(
			Map<TimeInterval, Map<Currency, TemporalTickerPO>> temporalTickersPOByTimeInterval) {
		for (TimeInterval timeInterval : temporalTickersPOByTimeInterval.keySet()) {
			Map<Currency, TemporalTickerPO> temporalTickerPOsByCurrency = temporalTickersPOByTimeInterval
					.get(timeInterval);
			updateTemporalTickers(temporalTickerPOsByCurrency);
			for (Account account : accounts) {
				account.checkTimedEvents(timeInterval);
				checkStrategies(timeInterval, account);
				checkBuyOrders(timeInterval, account);
				checkSellOrders(timeInterval, account);
			}
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
		List<BuyOrder> buyOrdersCreated = buyOrders.stream()
				.filter(buyOrder -> buyOrder.getOrderStatus() == OrderStatus.CREATED).collect(Collectors.toList());
		if (buyOrdersCreated != null && buyOrdersCreated.size() > 0) {
			OrderExecutor orderExecutor = new OrderExecutor();
			buyOrdersCreated.forEach(buyOrder -> orderExecutor.executeBuyOrder(buyOrder, this, account));
		}
	}

	private void checkSellOrders(TimeInterval currentTimeInterval, Account account) {
		List<SellOrder> sellOrders = account.getSellOrdersTemporalController().retrieve(currentTimeInterval);
		List<SellOrder> sellOrdersCreated = sellOrders.stream()
				.filter(sellOrder -> sellOrder.getOrderStatus() == OrderStatus.CREATED).collect(Collectors.toList());
		if (sellOrdersCreated != null && sellOrdersCreated.size() > 0) {
			OrderExecutor orderExecutor = new OrderExecutor();
			sellOrdersCreated.forEach(sellOrder -> orderExecutor.executeSellOrder(sellOrder, this, account));
		}
	}
}
