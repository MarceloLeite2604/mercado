package org.marceloleite.mercado.controller;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.marceloleite.mercado.base.model.Balance;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.OrderExecutor;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.retriever.TemporalTickerRetriever;

public class ControllerHouse implements House {

	private static final MercadoBigDecimal DEFAULT_COMISSION_PERCENTAGE = new MercadoBigDecimal("0.007");

	private TemporalTickerRetriever temporalTickerRetriever;

	private Map<Currency, TemporalTicker> temporalTickersByCurrency;

	private Map<String, Balance> comissionBalance;

	private OrderExecutor orderExecutor;
	
	public ControllerHouse() {
		super();
		this.orderExecutor = new MailOrderExecutor();
		this.temporalTickerRetriever = new TemporalTickerRetriever();
	}
	
	public void setTemporalTickerRetriever(TemporalTickerRetriever temporalTickerRetriever) {
		this.temporalTickerRetriever = temporalTickerRetriever;
	}

	@Override
	public Map<Currency, TemporalTicker> getTemporalTickers() {
		return new EnumMap<>(temporalTickersByCurrency);
	}

	@Override
	public OrderExecutor getOrderExecutor() {
		return orderExecutor;
	}

	@Override
	public MercadoBigDecimal getComissionPercentage() {
		return DEFAULT_COMISSION_PERCENTAGE;
	}

	@Override
	public Map<String, Balance> getComissionBalance() {
		return new HashMap<>(comissionBalance);
	}

	@Override
	public void updateTemporalTickers(TimeInterval timeInterval) {
		temporalTickersByCurrency = new EnumMap<>(Currency.class);

		for (Currency currency : Currency.values()) {
			/* TODO: Watch out with BGOLD. */
			if (currency.isDigital() && currency != Currency.BGOLD) {
				TemporalTicker temporalTicker = temporalTickerRetriever.retrieve(currency, timeInterval);
				temporalTickersByCurrency.put(currency, temporalTicker);
			}
		}
	}

}
