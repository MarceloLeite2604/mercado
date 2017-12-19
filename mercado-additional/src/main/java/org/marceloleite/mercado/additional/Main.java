package org.marceloleite.mercado.additional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.modeler.persistence.model.TemporalTicker;

public class Main {
	public static void main(String[] args) {
		
		LocalDateTime to = LocalDateTime.of(2017, 12, 11, 13, 58);
		LocalDateTime from = LocalDateTime.from(to)
			.minus(Duration.ofMinutes(60));
		Duration stepDuration = Duration.ofMinutes(1);
		TemporalTickerRetriever temporalTickerRetriever = new TemporalTickerRetriever();
		List<TemporalTicker> temporalTickers = temporalTickerRetriever.retrieve(Currency.BITCOIN, from, to,
				stepDuration);
		List<Comparison> changes = compare(temporalTickers);

		for (int counter = 0; counter < temporalTickers.size(); counter++) {
			TemporalTicker temporalTicker = temporalTickers.get(counter);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(new LocalDateTimeToStringConverter().convert(temporalTicker.getFrom()) + " - First: "
					+ temporalTicker.getFirst() + ", last: " + temporalTicker.getLast() + ", highest: "
					+ temporalTicker.getHigh() + ", lowest: " + temporalTicker.getLow());
			if (counter > 0) {
				Comparison change = changes.get(counter - 1);
				stringBuffer.append(" (" + change.calculateDiff() + ", " + change.calculatePercentage() + ").");
			} else {
				stringBuffer.append(".");
			}
			System.out.println(stringBuffer);
		}
	}

	private static List<Comparison> compare(List<TemporalTicker> temporalTickers) {
		TemporalTicker previousTemporalTicker = null;

		List<Comparison> comparisons = new ArrayList<>();

		for (TemporalTicker temporalTicker : temporalTickers) {
			if (previousTemporalTicker != null) {
				comparisons.add(createComparison(previousTemporalTicker, temporalTicker));
			}
			previousTemporalTicker = temporalTicker;
		}

		return comparisons;
	}

	private static Comparison createComparison(TemporalTicker previousTemporalTicker, TemporalTicker temporalTicker) {

		Comparison comparison = new Comparison();
		comparison.setPreviousValue(previousTemporalTicker.getLast());
		comparison.setCurrentValue(temporalTicker.getLast());

		return comparison;
	}
}
