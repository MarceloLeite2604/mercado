package org.marceloleite.mercado.additional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class PriceRetriever {

	public double retrieve(Currency currency, LocalDateTime time) {
		TemporalTickerGenerator temporalTickerGenerator = new TemporalTickerGenerator();
		Duration duration = Duration.ofSeconds(10);
		LocalDateTime startTime = LocalDateTime.from(time).minus(duration);

		boolean priceRetrieved = false;
		double price = 0;

		while (!priceRetrieved) {
			TimeDivisionController timeDivisionController = new TimeDivisionController(startTime, time, duration);
			List<TemporalTickerPO> temporalTickers = temporalTickerGenerator.generate(currency, timeDivisionController);
			TemporalTickerPO temporalTicker = temporalTickers.get(0);
			price = temporalTicker.getLast();
			if (price == 0) {
				duration = duration.multipliedBy(2);
				startTime = LocalDateTime.from(time).minus(duration);
			} else {
				priceRetrieved = true;
			}
		}
		return price;
	}
}
