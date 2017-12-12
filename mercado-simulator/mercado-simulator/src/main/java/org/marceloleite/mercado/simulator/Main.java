package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.util.ObjectToJsonFormatter;
import org.marceloleite.mercado.commons.util.StringToLocalDateTimeFormatter;
import org.marceloleite.mercado.consumer.model.Currency;
import org.marceloleite.mercado.modeler.persistence.TemporalTicker;
import org.marceloleite.mercado.nnew.TemporalTickerRetriever;
import org.marceloleite.mercado.simulator.configuration.Configuration;
import org.marceloleite.mercado.simulator.configuration.Property;

/**
 * Hello world!
 *
 */
public class Main {

	private static final double COMISSION_PERCENTAGE = 0.007;

	public static void main(String[] args) {
		Configuration configuration = Configuration.getInstance();

		Balance balance = new Balance();

		balance = buyFromOtherCurrencyAmount(balance, Currency.BITCOIN, new CurrencyAmount(Currency.REAL, 250.0), new StringToLocalDateTimeFormatter().format("07/09/2017 21:47:15"));
		balance = buyFromOtherCurrencyAmount(balance, Currency.BITCOIN, new CurrencyAmount(Currency.REAL, 750.0), new StringToLocalDateTimeFormatter().format("29/11/2017 13:32:05"));
		
		/*System.out.println(new ObjectToJsonFormatter().format(balance));*/
		
		TemporalTicker temporalTicker = retrieveTemporalTicker(Currency.BITCOIN, LocalDateTime.now());
		System.out.println(balance.getBalances().get(Currency.BITCOIN).getAmount()* temporalTicker.getLast());

	}

	private static Balance buyFromDesiredCurrencyAmount(Balance balance, CurrencyAmount currencyAmountToBuy, LocalDateTime time) {
		TemporalTicker temporalTicker = retrieveTemporalTicker(currencyAmountToBuy.getCurrency(), time);
		double cost = currencyAmountToBuy.getAmount() * temporalTicker.getLast();
		double comission = currencyAmountToBuy.getAmount() * COMISSION_PERCENTAGE;
		balance.add(Currency.REAL, -cost);
		balance.add(currencyAmountToBuy.getCurrency(), currencyAmountToBuy.getAmount() - comission);
		
		return balance;
	}
	
	private static Balance buyFromOtherCurrencyAmount(Balance balance, Currency desiredCurrency, CurrencyAmount currencyAmountToSpend, LocalDateTime time) {
		
		if ( currencyAmountToSpend.getCurrency() != Currency.REAL ) {
			throw new RuntimeException("Not implemented yet.");
			
		}
		
		TemporalTicker temporalTicker = retrieveTemporalTicker(desiredCurrency, time);
		double quantityToBuy = currencyAmountToSpend.getAmount()/temporalTicker.getLast();
		double comission = quantityToBuy * COMISSION_PERCENTAGE;
		balance.add(Currency.REAL, -currencyAmountToSpend.getAmount());
		balance.add(desiredCurrency, quantityToBuy - comission);
		
		return balance;
	}

	private static TemporalTicker retrieveTemporalTicker(Currency currency, LocalDateTime time) {
		TemporalTickerRetriever temporalTickerRetriever = new TemporalTickerRetriever();
		Duration duration = Duration.ofSeconds(60);
		LocalDateTime previousSecond = LocalDateTime.from(time)
			.minus(duration);

		List<TemporalTicker> temporalTickers = temporalTickerRetriever.retrieve(currency, previousSecond, time,
				duration);
		return temporalTickers.get(0);
	}
}
