package org.marceloleite.mercado.controller;

import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.House;
import org.marceloleite.mercado.OrderExecutor;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Wallet;

public class ControllerHouse implements House {

	private static final double DEFAULT_COMISSION_PERCENTAGE = 0.007;

	@Inject
	@Named("TradeDatabaseSiteDAO")
	private TemporalTickerDAO temporalTickerDAO;

	private Map<Currency, TemporalTicker> temporalTickersByCurrency;

	private Map<String, Wallet> comissionWallets;

	private OrderExecutor orderExecutor;
	
	public ControllerHouse() {
		super();
		this.orderExecutor = MailOrderExecutor.getInstance();
	}
	
	@Override
	public OrderExecutor getOrderExecutor() {
		return orderExecutor;
	}

	@Override
	public double getComissionPercentage() {
		return DEFAULT_COMISSION_PERCENTAGE;
	}

	@Override
	public void beforeStart() {
	}

	@Override
	public void process(TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickers) {
		temporalTickersByCurrency = new EnumMap<>(Currency.class);
		
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
		return temporalTickersByCurrency.get(currency);
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

}
