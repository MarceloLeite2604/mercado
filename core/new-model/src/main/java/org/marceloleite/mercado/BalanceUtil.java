package org.marceloleite.mercado;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.MercadoBigDecimal;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Balance;

public class BalanceUtil {

	private static final Logger LOGGER = LogManager.getLogger(BalanceUtil.class);

	private static BalanceUtil instance;

	private BalanceUtil() {
	}

	public void withdraw(Account account, CurrencyAmount currencyAmount) {
		LOGGER.debug("Withdrawing " + currencyAmount + " from " + account.getOwner() + " account.");
		Currency currency = currencyAmount.getCurrency();
		BigDecimal balance = account.getBalanceFor(currency);
		CurrencyAmount balanceCurrencyAmount = new CurrencyAmount(currency, balance);
		LOGGER.debug("Previous balance was " + balanceCurrencyAmount + ".");
		balanceCurrencyAmount.setAmount(balanceCurrencyAmount.getAmount()
				.subtract(currencyAmount.getAmount()));
		LOGGER.debug("New balance is " + balanceCurrencyAmount + ".");
		account.setBalanceFor(currency, balanceCurrencyAmount);
	}

	public void deposit(Account account, CurrencyAmount currencyAmount) {
		LOGGER.debug("Depositing " + currencyAmount + " on " + account.getOwner() + " account.");
		Currency currency = currencyAmount.getCurrency();
		BigDecimal balance = account.getBalanceFor(currency);
		CurrencyAmount balanceCurrencyAmount = new CurrencyAmount(currency, balance);
		LOGGER.debug("Previous balance was " + balanceCurrencyAmount + ".");
		balanceCurrencyAmount.setAmount(balanceCurrencyAmount.getAmount()
				.add(currencyAmount.getAmount()));
		LOGGER.debug("New balance is " + balanceCurrencyAmount + ".");
		account.setBalanceFor(currency, balanceCurrencyAmount);
	}

	public static BalanceUtil getInstance() {
		if (instance == null) {
			instance = new BalanceUtil();
		}
		return instance;
	}

	public void deposit(House house, Account account, CurrencyAmount currencyAmount) {
		LOGGER.debug("Depositing " + currencyAmount + " on house comission.");
		
		Currency currency = currencyAmount.getCurrency();
		Balance balance = house.getComissionBalanceFor(account.getOwner(), currency);
		CurrencyAmount balanceCurrencyAmount = balance.asCurrencyAmount();
		LOGGER.debug("Previous balance was " + balanceCurrencyAmount + ".");
		balanceCurrencyAmount.setAmount(balanceCurrencyAmount.getAmount().add(currencyAmount.getAmount()));
		LOGGER.debug("New balance is " + balanceCurrencyAmount + ".");
		house.setComissionBalanceFor(account.getOwner(), balanceCurrencyAmount);
	}
}
