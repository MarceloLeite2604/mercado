package org.marceloleite.mercado.base.model;

import java.util.ArrayList;
import java.util.Currency;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.marceloleite.mercado.base.model.converter.CurrencyAmountToBalanceDataConverter;
import org.marceloleite.mercado.base.model.order.MinimalAmounts;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
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
		CurrencyAmount retrievedCurrency = getOrDefault(currency, new CurrencyAmount(currency, new MercadoBigDecimal("0.0")));
		LOGGER.debug("Previous balance was " + retrievedCurrency + ".");
		retrievedCurrency.setAmount(retrievedCurrency.getAmount().add(currencyAmount.getAmount()));
		LOGGER.debug("New balance is " + retrievedCurrency + ".");
		put(currency, retrievedCurrency);
	}

	public void withdraw(CurrencyAmount currencyAmount) {
		LOGGER.debug("Withdrawing " + currencyAmount + ".");
		Currency currency = currencyAmount.getCurrency();
		CurrencyAmount retrievedCurrency = Optional.ofNullable(get(currency)).orElse(new CurrencyAmount(currency, new MercadoBigDecimal("0.0")));
		LOGGER.debug("Previous balance was " + retrievedCurrency + ".");
		retrievedCurrency.setAmount(retrievedCurrency.getAmount().subtract(currencyAmount.getAmount()));
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
		return (currencyAmountBalance.getAmount().compareTo(currencyAmount.getAmount()) >= 0 );
	}
	
	public boolean hasMinimalAmount(Currency currency) {
		CurrencyAmount currencyAmountBalance = get(currency);
		MercadoBigDecimal minimalAmount = MinimalAmounts.retrieveMinimalAmountFor(currency);
		return (currencyAmountBalance.getAmount().compareTo(minimalAmount) >= 0);
	}

	public boolean hasPositiveBalance(Currency currency) {
		
		MercadoBigDecimal amount = get(currency).getAmount().setScale(currency.getScale());
		return (amount.compareTo(MercadoBigDecimal.ZERO) > 0);
	}
}
