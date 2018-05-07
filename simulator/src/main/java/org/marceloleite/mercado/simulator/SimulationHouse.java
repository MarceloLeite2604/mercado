package org.marceloleite.mercado.simulator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.OrderExecutor;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Wallet;

public class SimulationHouse implements House {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(SimulationHouse.class);

	private static final double DEFAULT_COMISSION_PERCENTAGE = 0.007;

	private List<Account> accounts;

	private Map<String, Wallet> comissionWallets;

	private double comissionPercentage;

	private Map<Currency, TemporalTicker> temporalTickers;

	private OrderExecutor orderExecutor;

	private SimulationHouse(Builder builder) {
		this.accounts = builder.accounts;
		this.comissionWallets = new HashMap<>();
		this.comissionPercentage = Optional.ofNullable(builder.comissionPercentage)
				.orElse(DEFAULT_COMISSION_PERCENTAGE);
		this.orderExecutor = Optional.ofNullable(builder.orderExecutor)
				.orElse(new SimulationOrderExecutor());
	}

	public List<Account> getAccounts() {
		return new ArrayList<>(accounts);
	}

	public Map<Currency, TemporalTicker> getTemporalTickers() {
		return new EnumMap<>(temporalTickers);
	}

	@Override
	public void process(TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersByTimeInterval) {
		for (Entry<TimeInterval, Map<Currency, TemporalTicker>> entry : temporalTickersByTimeInterval.entrySet()) {
			this.temporalTickers = entry.getValue();
			executeAccountsStrategies(entry.getKey());
		}

	}

	private void executeAccountsStrategies(TimeInterval timeInterval) {
		getAccounts().forEach(account -> executeStrategiesFor(account, timeInterval));
	}

	private void executeStrategiesFor(Account account, TimeInterval timeInterval) {
		account.getStrategies()
				.forEach(strategy -> strategy.getExecutor().execute(timeInterval, account, this));
	}

	@Override
	public double getComissionPercentage() {
		return comissionPercentage;
	}

	@Override
	public OrderExecutor getOrderExecutor() {
		return orderExecutor;
	}

	@Override
	public void afterFinish() {
		getAccounts().forEach(account -> account.getStrategies()
				.forEach(strategy -> strategy.getExecutor().afterFinish()));
	}

	@Override
	public TemporalTicker getTemporalTickerFor(Currency currency) {
		return temporalTickers.get(currency);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private List<Account> accounts;
		private double comissionPercentage;
		private OrderExecutor orderExecutor;

		private Builder() {
		}

		public Builder accounts(List<Account> accounts) {
			this.accounts = accounts;
			return this;
		}

		public Builder comissionPercentage(double comissionPercentage) {
			this.comissionPercentage = comissionPercentage;
			return this;
		}

		public Builder orderExecutor(OrderExecutor orderExecutor) {
			this.orderExecutor = orderExecutor;
			return this;
		}

		public SimulationHouse build() {
			return new SimulationHouse(this);
		}
	}

	@Override
	public void beforeStart() {
	}

	@Override
	public Wallet getCommissionWalletFor(Account account) {
		return comissionWallets.getOrDefault(account.getOwner(), new Wallet());
	}
}
