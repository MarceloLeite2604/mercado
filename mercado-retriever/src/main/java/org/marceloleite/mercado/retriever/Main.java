package org.marceloleite.mercado.retriever;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.ZonedDateTimeUtils;
import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.databasemodel.TradePO;
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
			ZonedDateTime end = zonedDateTimeToStringConverter.convertFrom("01/01/2017 01:00:00");
			Duration duration = Duration.ofSeconds(60);
			TimeDivisionController timeDivisionController = new TimeDivisionController(start, end, duration);
			TemporalTickerRetriever temporalTickerRetriever = new TemporalTickerRetriever();
			List<TemporalTickerPO> temporalTickerPOs = temporalTickerRetriever.bulkRetrieve(Currency.BITCOIN,
					timeDivisionController);
			for (TemporalTickerPO temporalTickerPO : temporalTickerPOs) {
				TemporalTickerIdPO temporalTickerIdPO = temporalTickerPO.getId();
				TimeInterval timeInterval = new TimeInterval(temporalTickerIdPO.getStart(),
						temporalTickerIdPO.getEnd());
				System.out.println(temporalTickerIdPO.getCurrency() + ": " + timeInterval);
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
			List<TradePO> trades = tradesRetriever.retrieve(Currency.BITCOIN, start, end, false);
			if (trades.size() > 0) {
				System.out.println(trades.get(trades.size() - 1).getPrice());
			}
			EntityManagerController.getInstance().close();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
}
