package org.marceloleite.mercado.nnew;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.util.LocalDateTimeToString;
import org.marceloleite.mercado.consumer.model.Cryptocoin;
import org.marceloleite.mercado.modeler.persistence.TemporalTicker;

public class Main {
	public static void main(String[] args) {
		LocalDateTime to = LocalDateTime.of(2017, 12, 10, 17, 00);
		LocalDateTime from = LocalDateTime.from(to).minus(Duration.ofMinutes(60));
		Duration stepDuration = Duration.ofMinutes(60);
		TemporalTickerRetriever temporalTickerRetriever = new TemporalTickerRetriever();
		List<TemporalTicker> temporalTickers = temporalTickerRetriever.retrieve(Cryptocoin.BITCOIN, from, to,
				stepDuration);
		for (TemporalTicker temporalTicker : temporalTickers) {
			System.out.println(new LocalDateTimeToString().format(temporalTicker.getFrom()) + " - First: "
					+ temporalTicker.getFirst() + ", last: " + temporalTicker.getLast() + ", highest: "
					+ temporalTicker.getHigh() + ", lowest: " + temporalTicker.getLow());
		}
	}
}
