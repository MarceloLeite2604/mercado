package org.marceloleite.mercado.consultant;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.commons.util.converter.StringToLocalDateTimeConverter;
import org.marceloleite.mercado.databasemodel.TradeIdPO;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class Main {

	public static void main(String[] args) {

		consultant();
		// checkTrades();
	}

	private static void checkTrades() {
		try {
			long lastId = -1l;
			StringToLocalDateTimeConverter stringToLocalDateTimeConverter = new StringToLocalDateTimeConverter();
			LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
			LocalDateTime start = stringToLocalDateTimeConverter.convert("01/07/2013 00:00:00");
			LocalDateTime end = stringToLocalDateTimeConverter.convert("31/12/2017 00:00:00");
			Duration divisionDuration = Duration.ofDays(30);
			TimeDivisionController timeDivisionController = new TimeDivisionController(start, end, divisionDuration);
			for (long counter = 0; counter < timeDivisionController.getDivisions(); counter++) {
				TimeInterval nextTimeInterval = timeDivisionController.getNextTimeInterval();
				System.out.println("From " + localDateTimeToStringConverter.convert(nextTimeInterval.getStart())
						+ " to " + localDateTimeToStringConverter.convert(nextTimeInterval.getEnd()));
				TradesRetriever tradesRetriever = new TradesRetriever();
				List<TradePO> trades = tradesRetriever.retrieve(Currency.LITECOIN, nextTimeInterval.getStart(),
						nextTimeInterval.getEnd(), false);
				if (trades != null && trades.size() > 0) {
					for (int tradesCounter = 0; tradesCounter < trades.size(); tradesCounter++) {
						TradePO tradePO = trades.get(tradesCounter);
						TradeIdPO tradeIdPO = tradePO.getId();
						if (lastId == -1l) {
							lastId = tradeIdPO.getId();
						} else {
							if (tradeIdPO.getId() != (lastId + 1l)) {
								System.err.println(tradeIdPO.getId() + ": "
										+ localDateTimeToStringConverter.convert(tradePO.getDate()));
							}
							lastId = tradeIdPO.getId();
						}
					}
				}
			}
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	private static void consultant() {
		Consultant consultant = new Consultant();
		consultant.startConsulting();
		EntityManagerController.getInstance().close();
	}
}
