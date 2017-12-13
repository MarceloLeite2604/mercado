package org.marceloleite.mercado.simulator;

import java.time.LocalDateTime;
import java.util.Map;

import org.marceloleite.mercado.commons.util.ObjectToJsonFormatter;
import org.marceloleite.mercado.commons.util.StringToLocalDateTimeFormatter;
import org.marceloleite.mercado.consumer.model.Currency;
import org.marceloleite.mercado.nnew.PriceRetriever;

/**
 * Hello world!
 *
 */
public class Main {

	public static void main(String[] args) {

		House simulator = new House();
		
		StringToLocalDateTimeFormatter stringToLocalDateTimeFormatter = new StringToLocalDateTimeFormatter();
		
		Deposit deposit = new Deposit();
		deposit.setTime(stringToLocalDateTimeFormatter.format("05/09/2017 21:31:00"));
		deposit.setCurrencyAmount(new CurrencyAmount(Currency.REAL, 550.37));
		simulator.deposit(deposit);
		
		deposit = new Deposit();
		deposit.setTime(stringToLocalDateTimeFormatter.format("29/11/2017 10:34:00"));
		deposit.setCurrencyAmount(new CurrencyAmount(Currency.REAL, 1030.0));
		simulator.deposit(deposit);
		
		
		/*simulator.getBalance()
			.deposit(new CurrencyAmount(Currency.REAL, 500.0));
		simulator.getBalance()
			.deposit(new CurrencyAmount(Currency.REAL, 1000.0));*/

		simulator.buyFromOtherCurrencyAmount(Currency.BITCOIN, new CurrencyAmount(Currency.REAL, 250.0),
				new StringToLocalDateTimeFormatter().format("07/09/2017 21:47:15"));
		simulator.buyFromOtherCurrencyAmount(Currency.LITECOIN, new CurrencyAmount(Currency.REAL, 250.0),
				new StringToLocalDateTimeFormatter().format("07/09/2017 21:47:15"));
		simulator.buyFromOtherCurrencyAmount(Currency.BITCOIN, new CurrencyAmount(Currency.REAL, 750.0),
				new StringToLocalDateTimeFormatter().format("29/11/2017 13:32:05"));
		simulator.buyFromOtherCurrencyAmount(Currency.LITECOIN, new CurrencyAmount(Currency.REAL, 250.0),
				new StringToLocalDateTimeFormatter().format("29/11/2017 13:32:05"));

		System.out.println(new ObjectToJsonFormatter().format(simulator.getBalance()));
		System.out.println(new ObjectToJsonFormatter().format(simulator.getComission()));

		Map<Currency, CurrencyAmount> balances = simulator.getBalance()
			.getBalances();

		System.out.println("Balance: ");
		for (Currency currency : Currency.values()) {
			CurrencyAmount currencyAmount = balances.get(currency);
			if (currencyAmount != null) {
				calculateRealValueOf(currencyAmount);
			}
		}

		System.out.println("Comission: ");
		Map<Currency, CurrencyAmount> comission = simulator.getComission()
			.getBalances();
		for (Currency currency : Currency.values()) {
			CurrencyAmount currencyAmount = comission.get(currency);
			if (currencyAmount != null) {
				calculateRealValueOf(currencyAmount);
			}
		}
	}

	private static void calculateRealValueOf(CurrencyAmount currencyAmount) {
		double currentPrice;
		if (currencyAmount.getCurrency() != Currency.REAL) {
			currentPrice = new PriceRetriever().retrieve(currencyAmount.getCurrency(), LocalDateTime.now());
		} else {
			currentPrice = 1;
		}

		CurrencyAmount realCurrencyAmount = new CurrencyAmount(Currency.REAL,
				currencyAmount.getAmount() * currentPrice);
		System.out.println(realCurrencyAmount);
	}

}
