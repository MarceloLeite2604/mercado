package org.marceloleite.mercado.controller;

import java.math.BigDecimal;

import org.marceloleite.mercado.CurrencyPair;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;

public class Main {

	public static void main(String[] args) {
		controller();
		// mailOrderExecutor();
	}

	@SuppressWarnings("unused")
	private static void mailOrderExecutor() {
		Account account = new Account();
		account.setOwner("Marcelo Leite");
		account.setEmail("marceloleite2604@gmail.com");
		Order buyOrder = Order.builder()
				.setCreated(ZonedDateTimeUtils.now())
				.setCurrencyPair(CurrencyPair.retrieveByPair(Currency.REAL, Currency.BITCOIN))
				.setQuantity(new BigDecimal("0.0567"))
				.setLimitPrice(new BigDecimal("700.00"))
				.build();
		MailOrderExecutor.getInstance()
				.placeOrder(buyOrder, null, account);

	}

	@SuppressWarnings("unused")
	private static void controller() {
		new Controller().start();
	}
}
