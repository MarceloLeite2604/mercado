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
		/* balances = new EnumMap<>(Currency.class); */
	}

	public void deposit(CurrencyAmount currencyAmountToDeposit) {
		CurrencyAmount currencyAmount = Optional.ofNullable(get(currencyAmountToDeposit.getCurrency()))
				.orElse(new CurrencyAmount(currencyAmountToDeposit.getCurrency(), 0.0));
		currencyAmount.setAmount(currencyAmount.getAmount() + currencyAmountToDeposit.getAmount());
		put(currencyAmount.getCurrency(), currencyAmount);
	}

	public void withdraw(CurrencyAmount currencyAmountToWithdraw) {
		CurrencyAmount currencyAmount = Optional.ofNullable(get(currencyAmountToWithdraw.getCurrency()))
				.orElse(new CurrencyAmount(currencyAmountToWithdraw.getCurrency(), 0.0));
		currencyAmount.setAmount(currencyAmount.getAmount() - currencyAmountToWithdraw.getAmount());
		put(currencyAmount.getCurrency(), currencyAmount);
	}

	/*public Map<Currency, CurrencyAmount> getBalances() {
		return balances;
	}*/
}
