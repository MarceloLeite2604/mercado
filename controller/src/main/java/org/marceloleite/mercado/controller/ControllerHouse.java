package org.marceloleite.mercado.controller;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.OrderExecutor;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Wallet;

public class ControllerHouse implements House {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(ControllerHouse.class);

	private static final double DEFAULT_COMISSION_PERCENTAGE = 0.007;
	
	private List<Account> accounts;

	@Inject
	@Named("TradeDatabaseSiteDAO")
	private TemporalTickerDAO temporalTickerDAO;
	
	@Inject
	private MailOrderExecutor mailOrderExecutor;

	private Map<Currency, TemporalTicker> temporalTickers;

	private Map<String, Wallet> comissionWallets;

	private OrderExecutor orderExecutor;
	
	private double comissionPercentage;
	
	private ControllerHouse(Builder builder) {
		this.accounts = builder.accounts;
		this.comissionWallets = new HashMap<>();
		this.comissionPercentage = Optional.ofNullable(builder.comissionPercentage)
				.orElse(DEFAULT_COMISSION_PERCENTAGE);
		this.orderExecutor = Optional.ofNullable(builder.orderExecutor)
				.orElse(new ControllerOrderExecutor());
		this.temporalTickers = new EnumMap<>(Currency.class);
	}
	
	List<Account> getAccounts() {
		return new ArrayList<>(accounts);
	}
	
	@Override
	public OrderExecutor getOrderExecutor() {
		return orderExecutor;
	}

	@Override
	public double getComissionPercentage() {
		return comissionPercentage;
	}

	@Override
	public void beforeStart() {
	}

	@Override
	public void process(TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickers) {
//		this.temporalTickers = new EnumMap<>(Currency.class);
		
//				for (Currency currency : Currency.values()) {
//					/* TODO: Watch out with BGOLD. */
//					if (currency.isDigital() && currency != Currency.BGOLD) {
//						TemporalTicker temporalTicker = temporalTickerRetriever.retrieve(currency, timeInterval);
//						temporalTickersByCurrency.put(currency, temporalTicker);
//					}
//				}
	}

	@Override
	public void afterFinish() {
	}

	@Override
	public TemporalTicker getTemporalTickerFor(Currency currency) {
		return temporalTickers.get(currency);
	}

	@Override
	public Wallet getCommissionWalletFor(Account account) {
		return comissionWallets.get(account.getOwner());
	}

//	@Override
//	public void updateTemporalTickers(TimeInterval timeInterval) {
//		temporalTickersByCurrency = new EnumMap<>(Currency.class);
//
//		for (Currency currency : Currency.values()) {
//			/* TODO: Watch out with BGOLD. */
//			if (currency.isDigital() && currency != Currency.BGOLD) {
//				TemporalTicker temporalTicker = temporalTickerRetriever.retrieve(currency, timeInterval);
//				temporalTickersByCurrency.put(currency, temporalTicker);
//			}
//		}
//	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private List<Account> accounts;
		private Double comissionPercentage;
		private OrderExecutor orderExecutor;

		private Builder() {
		}

		public Builder accounts(List<Account> accounts) {
			this.accounts = accounts;
			return this;
		}

		public Builder comissionPercentage(Double comissionPercentage) {
			this.comissionPercentage = comissionPercentage;
			return this;
		}

		public Builder orderExecutor(OrderExecutor orderExecutor) {
			this.orderExecutor = orderExecutor;
			return this;
		}

		public ControllerHouse build() {
			return new ControllerHouse(this);
		}
	}

}
