package org.marceloleite.mercado.simulator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.OrderExecutor;
import org.marceloleite.mercado.TemporalTickerVariation;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Balance;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategy.StrategyExecutor;

public class SimulationHouse implements House {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(SimulationHouse.class);

	private static final double DEFAULT_COMISSION_PERCENTAGE = 0.007;

	private List<Account> accounts;

	private Map<String, Balance> comissionBalance;

	private double comissionPercentage;

	private Map<Currency, TemporalTicker> temporalTickers;

	private Map<Currency, TemporalTickerVariation> temporalTickerVariations;

	private TemporalTickerDAO temporalTickerDAO;

	private OrderExecutor orderExecutor;

	private SimulationHouse(Builder builder) {
		this.accounts = builder.accounts;
		this.comissionBalance = builder.comissionBalance;
		this.comissionPercentage = builder.comissionPercentage;
		this.temporalTickers = builder.temporalTickers;
		this.temporalTickerVariations = builder.temporalTickerVariations;
		this.orderExecutor = builder.orderExecutor;
	}

//	public SimulationHouse() {
//		super();
//		comissionBalance = new HashMap<>();
//		comissionPercentage = DEFAULT_COMISSION_PERCENTAGE;
//		this.temporalTickers = new EnumMap<>(Currency.class);
//		this.temporalTickerVariations = new EnumMap<>(Currency.class);
//		this.orderExecutor = new SimulationOrderExecutor();
//		this.accounts = retrieveAccounts();
//	}

//	public SimulationHouse(SimulationHouse house) {
//		this(new ArrayList<>(house.getAccounts()), new HashMap<>(house.getComissionBalance()),
//				house.getComissionPercentage(), new EnumMap<>(house.getTemporalTickers()),
//				new EnumMap<>(house.getTemporalTickerVariations()), new TemporalTickerRetriever());
//	}

	public List<Account> getAccounts() {
		return new ArrayList<>(accounts);
	}

	public Map<String, Balance> getComissionBalance() {
		return comissionBalance;
	}

	public Map<Currency, TemporalTicker> getTemporalTickers() {
		return new EnumMap<>(temporalTickers);
	}

	public Map<Currency, TemporalTickerVariation> getTemporalTickerVariations() {
		return new EnumMap<>(temporalTickerVariations);
	}

//	private List<Account> retrieveAccounts() {
//		List<Account> accounts = new ArrayList<>();
//		List<AccountData> accountsData = new AccountsXmlReader().readAccounts();
//		for (AccountData accountData : accountsData) {
//			accounts.add(new Account(accountData));
//		}
//		return accounts;
//	}

//	public static double getDefaultComissionPercentage() {
//		return DEFAULT_COMISSION_PERCENTAGE;
//	}

	public double getComissionPercentage() {
		return comissionPercentage;
	}

	@Override
	public void updateTemporalTickers(TimeInterval timeInterval) {
		TemporalTicker previousTemporalTicker;
		for (Currency currency : Currency.values()) {
			// TODO Watch out with BGOLD.
			if (currency.isDigital() && currency != Currency.BGOLD) {
				TemporalTicker temporalTicker;
				temporalTicker = temporalTickerDAO.findByCurrencyAndStartAndEnd(currency, timeInterval.getStart(), timeInterval.getEnd());
				previousTemporalTicker = temporalTickers.get(currency);
				TemporalTickerVariation temporalTickerVariation = null;
				if (temporalTicker != null) {
					temporalTickers.put(currency, temporalTicker);
					temporalTickerVariation = new TemporalTickerVariation(previousTemporalTicker, temporalTicker);
				}
				temporalTickerVariations.put(currency, temporalTickerVariation);
			}
		}
	}

//	private void updateTemporalTickerVariations(Map<Currency, TemporalTicker> newTemporalTickers) {
//		TemporalTicker previousTemporalTicker;
//		for (Currency currency : Currency.values()) {
//			/* TODO: Watch you with BGOLD. */
//			if (currency.isDigital() && currency != Currency.BGOLD) {
//				TemporalTicker temporalTicker = newTemporalTickers.get(currency);
//				previousTemporalTicker = temporalTickers.get(currency);
//				TemporalTickerVariation temporalTickerVariation = null;
//				if (temporalTicker != null) {
//					newTemporalTickers.put(currency, temporalTicker);
//					temporalTickerVariation = new TemporalTickerVariation(previousTemporalTicker, temporalTicker);
//				}
//				temporalTickerVariations.put(currency, temporalTickerVariation);
//			}
//		}
//	}

//	public void executeTemporalEvents(TimeInterval timeInterval) {
//		updateTemporalTickers(timeInterval);
//		for (Account account : accounts) {
//			account.checkTimedEvents(timeInterval);
//			checkStrategies(timeInterval, account);
//			// checkBuyOrders(currentTimeInterval, account);
//			// checkSellOrders(currentTimeInterval, account);
//		}
//	}

	public void executeTemporalEvents(
			TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersDataModelsByTimeInterval) {
		for (Entry<TimeInterval, Map<Currency, TemporalTicker>> entry : temporalTickersDataModelsByTimeInterval
				.entrySet()) {
			TimeInterval timeInterval = entry.getKey();
			Map<Currency, TemporalTicker> temporalTickers = entry.getValue();
			// updateTemporalTickers(temporalTickerPOsByCurrency);
			for (Account account : accounts) {
				// account.checkTimedEvents(timeInterval);
				checkStrategies(timeInterval, account);
				// checkBuyOrders(timeInterval, account); checkSellOrders(timeInterval,
				// account);
			}
		}
	}

	private void checkStrategies(TimeInterval timeInterval, Account account) {
		Map<Currency, List<StrategyExecutor>> strategyExecutorsByCurrency = account.getStrategyExecutors();
		for (List<StrategyExecutor> strategyExecutors : strategyExecutorsByCurrency.values()) {
			strategyExecutors.forEach(strategyExecutor -> strategyExecutor.execute(timeInterval, account, this));
		}
	}

//	private void checkBuyOrders(TimeInterval currentTimeInterval, Account account) {
//		List<Order> orders = account.getBuyOrdersTemporalController().retrieve(currentTimeInterval);
//		List<Order> buyOrdersCreated = orders.stream().filter(buyOrder -> buyOrder.getStatus() == OrderStatus.OPEN)
//				.collect(Collectors.toList());
//		if (buyOrdersCreated != null && buyOrdersCreated.size() > 0) {
//			SimulationOrderExecutor orderExecutor = new SimulationOrderExecutor();
//			buyOrdersCreated.forEach(buyOrder -> orderExecutor.executeBuyOrder(buyOrder, this, account));
//		}
//	}

//	private void checkSellOrders(TimeInterval currentTimeInterval, Account account) {
//		List<Order> orders = account.getSellOrdersTemporalController().retrieve(currentTimeInterval);
//		List<Order> sellOrdersCreated = orders.stream().filter(sellOrder -> sellOrder.getStatus() == OrderStatus.OPEN)
//				.collect(Collectors.toList());
//		if (sellOrdersCreated != null && sellOrdersCreated.size() > 0) {
//			SimulationOrderExecutor orderExecutor = new SimulationOrderExecutor();
//			sellOrdersCreated.forEach(sellOrder -> orderExecutor.executeSellOrder(sellOrder, this, account));
//		}
//	}

	@Override
	public OrderExecutor getOrderExecutor() {
		return orderExecutor;
	}

	public void finishTemporalEvents() {
		for (Account account : accounts) {
			for (Entry<Currency, List<StrategyExecutor>> entry : account.getStrategyExecutorsByCurrency().entrySet()) {
				List<StrategyExecutor> strategies = ;
				for (StrategyExecutor strategyExecutor : entry.getValue()) {
					strategyExecutor.afterFinish();
				}
			}
		}
	}

	@Override
	public TemporalTicker getTemporalTickerFor(Currency currency) {
		return temporalTickers.get(currency);
	}
	
	public static Builder builder() {
		return new Builder();
	}

	
	public static class Builder() {
		private List<Account> accounts;
		private Map<String, Balance> comissionBalance;
		private double comissionPercentage;
		private Map<Currency, TemporalTicker> temporalTickers;
		private Map<Currency, TemporalTickerVariation> temporalTickerVariations;
		private OrderExecutor orderExecutor;
		
		private Builder() {
		}
		
		public Builder accounts(List<Account> accounts) {
			this.accounts = accounts;
			return this;
		}
		public Builder comissionBalance(Map<String, Balance> comissionBalance) {
			this.comissionBalance = comissionBalance;
			return this;
		}
		public Builder comissionPercentage(double comissionPercentage) {
			this.comissionPercentage = comissionPercentage;
			return this;
		}
		public Builder temporalTickers(Map<Currency, TemporalTicker> temporalTickers) {
			this.temporalTickers = temporalTickers;
			return this;
		}
		public Builder temporalTickerVariations(Map<Currency, TemporalTickerVariation> temporalTickerVariations) {
			this.temporalTickerVariations = temporalTickerVariations;
			return this;
		}
		
		public Builder orderExecutor(OrderExecutor orderExecutor) {
			this.orderExecutor = orderExecutor;
			return this;
		}
	}
}
