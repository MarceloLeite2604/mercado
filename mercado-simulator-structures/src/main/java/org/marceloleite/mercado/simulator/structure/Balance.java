package org.marceloleite.mercado.simulator.structure;

import java.util.EnumMap;
import java.util.Optional;

import javax.xml.bind.annotation.XmlRootElement;

import org.marceloleite.mercado.commons.Currency;

@XmlRootElement
public class Balance extends EnumMap<Currency, CurrencyAmount> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * @XmlJavaTypeAdapter(BalanceXmlAdapter.class) private Map<Currency,
	 * CurrencyAmount> balances;
	 */

	public Balance() {
		super(Currency.class);
	}

	public void deposit(CurrencyAmount currencyAmount) {
		Currency currency = currencyAmount.getCurrency();
		CurrencyAmount retrievedCurrency = getOrDefault(currency, new CurrencyAmount(currency, 0.0));
		retrievedCurrency.setAmount(retrievedCurrency.getAmount() + currencyAmount.getAmount());
		put(currency, currencyAmount);
	}

	public void withdraw(CurrencyAmount currencyAmountToWithdraw) {
		CurrencyAmount currencyAmount = Optional.ofNullable(get(currencyAmountToWithdraw.getCurrency()))
				.orElse(new CurrencyAmount(currencyAmountToWithdraw.getCurrency(), 0.0));
		currencyAmount.setAmount(currencyAmount.getAmount() - currencyAmountToWithdraw.getAmount());
		put(currencyAmount.getCurrency(), currencyAmount);
	}

	/*
	 * public Map<Currency, CurrencyAmount> getBalances() { return balances; }
	 */
}
