package org.marceloleite.mercado.nnew;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.modeler.persistence.model.TemporalTicker;

public class PriceRetriever {

	public double retrieve(Currency currency, LocalDateTime time) {
		TemporalTickerRetriever temporalTickerRetriever = new TemporalTickerRetriever();
		Duration duration = Duration.ofSeconds(10);
		LocalDateTime startTime = LocalDateTime.from(time)
			.minus(duration);

		boolean priceRetrieved = false;
		double price = 0;

		while (!priceRetrieved) {
			List<TemporalTicker> temporalTickers = temporalTickerRetriever.retrieve(currency, startTime, time,
					duration);
			TemporalTicker temporalTicker = temporalTickers.get(0);
			price = temporalTicker.getLast();
			if (price == 0) {
				duration = duration.multipliedBy(2);
				startTime = LocalDateTime.from(time)
					.minus(duration);
			} else {
				priceRetrieved = true;
			}
		}
		return price;
	}
}
