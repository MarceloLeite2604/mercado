package org.marceloleite.mercado;

import java.util.Map;
import java.util.TreeMap;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Balance;
import org.marceloleite.mercado.model.TemporalTicker;

public interface House {

	void beforeStart();

	void process(TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickers);

	void afterFinish();

	TemporalTicker getTemporalTickerFor(Currency currency);

	OrderExecutor getOrderExecutor();

	public double getComissionPercentage();

	Balance getComissionBalanceFor(String owner, Currency currency);

	void setComissionBalanceFor(String owner, CurrencyAmount balanceCurrencyAmount);
}
