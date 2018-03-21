package org.marceloleite.mercado.base.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.converter.CurrencyAmountToBalanceDataConverter;
import org.marceloleite.mercado.base.model.order.MinimalAmounts;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.data.BalanceData;

public class Balance extends EnumMap<Currency, CurrencyAmount> {

	private static final Logger LOGGER = LogManager.getLogger(Balance.class);

	private static final long serialVersionUID = 1L;

	public Balance() {
		super(Currency.class);
	}

	public Balance(List<BalanceData> balanceDatas) {
		super(Currency.class);

		for (BalanceData balanceData : balanceDatas) {
			CurrencyAmount currencyAmount = new CurrencyAmount(balanceData.getCurrency(), balanceData.getAmount());
			put(balanceData.getCurrency(), currencyAmount);
		}
	}

	public Balance(Balance balance) {
		super(balance);
	}

	public void deposit(CurrencyAmount currencyAmount) {
		LOGGER.debug("Depositing " + currencyAmount + ".");
		Currency currency = currencyAmount.getCurrency();
		CurrencyAmount retrievedCurrency = getOrDefault(currency, new CurrencyAmount(currency, 0.0));
		LOGGER.debug("Previous balance was " + retrievedCurrency + ".");
		retrievedCurrency.setAmount(retrievedCurrency.getAmount() + currencyAmount.getAmount());
		LOGGER.debug("New balance is " + retrievedCurrency + ".");
		put(currency, retrievedCurrency);
	}

	public void withdraw(CurrencyAmount currencyAmount) {
		LOGGER.debug("Withdrawing " + currencyAmount + ".");
		Currency currency = currencyAmount.getCurrency();
		CurrencyAmount retrievedCurrency = Optional.ofNullable(get(currency)).orElse(new CurrencyAmount(currency, 0.0));
		LOGGER.debug("Previous balance was " + retrievedCurrency + ".");
		retrievedCurrency.setAmount(retrievedCurrency.getAmount() - currencyAmount.getAmount());
		LOGGER.debug("New balance is " + retrievedCurrency + ".");
		put(currency, retrievedCurrency);
	}

	public List<BalanceData> retrieveData() {
		CurrencyAmountToBalanceDataConverter currencyAmountToBalanceDataConverter = new CurrencyAmountToBalanceDataConverter();
		List<BalanceData> balanceDatas = new ArrayList<>();
		Set<Currency> currencies = keySet();
		if (currencies != null && !currencies.isEmpty()) {
			for (Currency currency : currencies) {
				BalanceData balanceData = currencyAmountToBalanceDataConverter.convertTo(get(currency));
				balanceDatas.add(balanceData);
			}
		}
		return balanceDatas;
	}
	
	public boolean hasBalance(CurrencyAmount currencyAmount) {
		Currency currency = currencyAmount.getCurrency();
		CurrencyAmount currencyAmountBalance = get(currency);
		return (currencyAmountBalance.getAmount() >= currencyAmount.getAmount());
	}
	
	public boolean hasMinimalAmount(Currency currency) {
		CurrencyAmount currencyAmountBalance = get(currency);
		Double minimalAmount = MinimalAmounts.retrieveMinimalAmountFor(currency);
		return (currencyAmountBalance.getAmount() >= minimalAmount);
	}

	public boolean hasPositiveBalance(Currency currency) {
		return (get(currency).getAmount() > 0);
	}
}
