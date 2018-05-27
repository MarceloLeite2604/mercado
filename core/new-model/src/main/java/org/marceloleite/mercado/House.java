package org.marceloleite.mercado;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Wallet;

public interface House {

	void beforeStart();

	void process(TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickers);

	void afterFinish();

	TemporalTicker getTemporalTickerFor(Currency currency);

	OrderExecutor getOrderExecutor();

	public double getComissionPercentage();

	Wallet getCommissionWalletFor(Account account);

	public List<Account> getAccounts();
}
