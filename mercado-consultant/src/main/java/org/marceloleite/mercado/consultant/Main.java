package org.marceloleite.mercado.consultant;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class Main {

	public static void main(String[] args) {

		 consultant();
		// checkTrades();
		// zonedDateTime();
	}

	@SuppressWarnings("unused")
	private static void zonedDateTime() {
		
		ZonedDateTime now = ZonedDateTimeUtils.now();
		System.out.println(new ZonedDateTimeToStringConverter().convertTo(now));
	}

	@SuppressWarnings("unused")
	private static void checkTrades() {

		Map<Currency, List<Long>> currencyExceptions = new EnumMap<>(Currency.class);
		currencyExceptions.put(Currency.BITCOIN, Arrays.asList(4926l));
		TradesRetriever tradesRetriever = new TradesRetriever();
		tradesRetriever.setTradesSiteRetrieverStepDuration(Duration.ofDays(1));
		try {
			for (Currency currency : Currency.values()) {
				long lastId = -1l;
				/* TODO: Watch out with BGold. */
				if (currency.isDigital() && currency != Currency.BGOLD) {
					List<Long> exceptions = currencyExceptions.getOrDefault(currency, new ArrayList<>());
					System.out.println("Checking " + currency + " currency.");
					ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
					ZonedDateTime start = zonedDateTimeToStringConverter.convertFrom("01/06/2013 00:00:00");
					ZonedDateTime end = zonedDateTimeToStringConverter.convertFrom("01/01/2018 00:00:00");
					Duration divisionDuration = Duration.ofDays(30);
					TimeDivisionController timeDivisionController = new TimeDivisionController(start, end,
							divisionDuration);
					
					for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
						
						List<Trade> trades = tradesRetriever.retrieve(currency, timeInterval.getStart(),
								timeInterval.getEnd(), false);
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append(timeInterval + ": ");
						if (trades != null) {
							stringBuffer.append(trades.size());
						} else {
							stringBuffer.append(0);
						}
						stringBuffer.append(" trade(s).");
						System.out.println(stringBuffer.toString());
						if (trades != null && trades.size() > 0) {
							trades = trades.stream().sorted(new Comparator<Trade>() {

								@Override
								public int compare(Trade trade1, Trade trade2) {
									return (trade1.getId().compareTo(trade2.getId()));
								}
							}).collect(Collectors.toList());
							for (int tradesCounter = 0; tradesCounter < trades.size(); tradesCounter++) {
								Trade trade = trades.get(tradesCounter);
								if (lastId == -1l) {
									lastId = trade.getId();
								} else {
									if (trade.getId() != (lastId + 1l)) {
										System.out.println(currency + " " + trade.getId() + ": "
												+ zonedDateTimeToStringConverter.convertTo(trade.getDate()));
									}
									lastId = trade.getId();
									while (exceptions.contains(lastId + 1)) {
										lastId++;
									}
								}
							}
						}
					}
				}
			}
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	@SuppressWarnings("unused")
	private static void consultant() {
		Consultant consultant = new Consultant();
		consultant.startConsulting();
		EntityManagerController.getInstance().close();
	}
}
