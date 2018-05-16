package org.marceloleite.mercado.model;

import java.math.BigDecimal;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;

public class Wallet extends HashSet<Balance> {

	private static final Logger LOGGER = LogManager.getLogger(Wallet.class);

	private Account account;

	private static final long serialVersionUID = 1L;

	public Wallet() {
		super();
	}

	public Wallet(Account account) {
		super();
		this.account = account;
		for (Currency currency : Currency.values()) {
			Balance balance = new Balance();
			balance.setAccount(account);
			balance.setCurrency(currency);
			balance.setAmount(BigDecimal.ZERO);
			add(balance);
		}
	}

	public void updateBalance(CurrencyAmount currecyAmount) {
		findOrCreateBalance(currecyAmount);
	}

	public void setBalanceFor(Currency currency, BigDecimal amount) {
		findOrCreateBalance(new CurrencyAmount(currency, amount));
	}

	private Balance findOrCreateBalance(CurrencyAmount currencyAmount) {
		Balance balance = findBalance(currencyAmount.getCurrency());

		if (balance == null) {
			balance = createNewBalance(currencyAmount);
			add(balance);
		}

		return balance;
	}

	private Balance findBalance(Currency currency) {
		Balance balance = stream().filter(analysedBalance -> analysedBalance.getCurrency()
				.equals(currency))
				.findFirst()
				.orElse(null);
		return balance;
	}

	private Balance createNewBalance(CurrencyAmount currencyAmount) {
		Balance balance;
		balance = new Balance();
		balance.setCurrency(currencyAmount.getCurrency());
		balance.setAmount(currencyAmount.getAmount());
		balance.setAccount(account);
		return balance;
	}

	public Balance getBalanceFor(Currency currency) {
		return findOrCreateBalance(currency);
	}

	private Balance findOrCreateBalance(Currency currency) {
		return findOrCreateBalance(new CurrencyAmount(currency, new BigDecimal("0")));
	}

	public boolean hasPositiveBalanceOf(Currency currency) {
		return (getBalanceFor(currency).getAmount()
				.compareTo(BigDecimal.ZERO) > 0);
	}

	public boolean hasBalanceFor(CurrencyAmount currencyAmount) {
		return (getBalanceFor(currencyAmount.getCurrency()).getAmount()
				.compareTo(currencyAmount.getAmount()) >= 0);
	}

	public void withdraw(CurrencyAmount currencyAmount) {
		LOGGER.debug("Withdrawing " + currencyAmount + " from " + account.getOwner() + " account.");
		Currency currency = currencyAmount.getCurrency();
		Balance balance = getBalanceFor(currency);
		LOGGER.debug("Previous balance was " + balance + ".");
		balance.setAmount(balance.getAmount()
				.subtract(currencyAmount.getAmount()));
		LOGGER.debug("New balance is " + balance + ".");
		updateBalance(balance.asCurrencyAmount());
	}

	public void deposit(CurrencyAmount currencyAmount) {
		LOGGER.debug("Depositing " + currencyAmount + " on " + account.getOwner() + " account.");
		Currency currency = currencyAmount.getCurrency();
		Balance balance = getBalanceFor(currency);
		LOGGER.debug("Previous balance was " + balance + ".");
		balance.setAmount(balance.getAmount()
				.add(currencyAmount.getAmount()));
		LOGGER.debug("New balance is " + balance + ".");
		updateBalance(balance.asCurrencyAmount());
	}

	public void adjustReferences(Account account) {
		this.account = account;
		forEach(balance -> balance.setAccount(account));
	}
}
