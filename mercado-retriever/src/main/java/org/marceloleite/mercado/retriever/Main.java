package org.marceloleite.mercado.retriever;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.ZonedDateTimeUtils;
import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.database.data.structure.TemporalTickerDataModel;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {

	public static void main(String[] args) {
		// tradesRetriever();
		bulkRetrieve();
	}

	private static void bulkRetrieve() {
		try {
			/*ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
			ZonedDateTime zdt1 = zonedDateTimeToStringConverter.convertFrom("01/01/2017 00:00:00");
			ZonedDateTime zdt2 = zonedDateTimeToStringConverter.convertFrom("01/01/2017 00:00:00");
			
			System.out.println(zdt1.equals(zdt2));*/
			ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
			ZonedDateTime start = zonedDateTimeToStringConverter.convertFrom("01/01/2017 00:00:00");
			ZonedDateTime end = zonedDateTimeToStringConverter.convertFrom("02/01/2017 00:00:00");
			Duration duration = Duration.ofSeconds(60);
			TimeDivisionController timeDivisionController = new TimeDivisionController(start, end, duration);
			TemporalTickerRetriever temporalTickerRetriever = new TemporalTickerRetriever();
			Map<TimeInterval, Map<Currency, TemporalTickerDataModel>> temporalTickerPOsByTimeInterval = temporalTickerRetriever.bulkRetrieve(timeDivisionController);
			for (Entry<TimeInterval, Map<Currency, TemporalTickerDataModel>> temporalTickerDataModelsForTimeInterval : temporalTickerPOsByTimeInterval.entrySet()) {
				TimeInterval timeInterval = temporalTickerDataModelsForTimeInterval.getKey();
				Map<Currency, TemporalTickerDataModel> temporalTickerPOByCurrency = temporalTickerDataModelsForTimeInterval.getValue();
				
				System.out.println(timeInterval);
				for (Entry<Currency, TemporalTickerDataModel> temporalTickerDataModelsForCurrency : temporalTickerPOByCurrency.entrySet()) {
					Currency currency = temporalTickerDataModelsForCurrency.getKey();
					TemporalTickerDataModel temporalTickerDataModel = temporalTickerDataModelsForCurrency.getValue();
					System.out.println("\t" + currency + ": " + temporalTickerDataModel.getLastPrice());
				}
			}
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	@SuppressWarnings("unused")
	private static void tradesRetriever() {
		try {
			ZonedDateTime end = ZonedDateTimeUtils.now();
			ZonedDateTime start = end.minus(Duration.ofSeconds(30));
			TradesRetriever tradesRetriever = new TradesRetriever();
			List<TradeDataModel> trades = tradesRetriever.retrieve(Currency.BITCOIN, start, end, false);
			if (trades.size() > 0) {
				System.out.println(trades.get(trades.size() - 1).getPrice());
			}
			EntityManagerController.getInstance().close();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
}
