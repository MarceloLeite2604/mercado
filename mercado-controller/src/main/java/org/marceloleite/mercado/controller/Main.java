package org.marceloleite.mercado.controller;

import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {

	public static void main(String[] args) {
		controller();
		// mailOrderExecutor();
	}

	private static void mailOrderExecutor() {
		try {
		Account account = new Account("Marcelo Leite");
		account.setEmail("marceloleite2604@gmail.com");
		BuyOrder buyOrder = new BuyOrderBuilder().toExecuteOn(ZonedDateTimeUtils.now())
				.buying(new CurrencyAmount(Currency.BITCOIN, 0.0567)).paying(new CurrencyAmount(Currency.REAL, 700.00))
				.build();
		new MailOrderExecutor().executeBuyOrder(buyOrder, null, account);
		} finally {
			EntityManagerController.getInstance().close();
		}
		
	}

	@SuppressWarnings("unused")
	private static void controller() {
		try {
			new Controller().start();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
}
