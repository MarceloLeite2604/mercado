package org.marceloleite.mercado.additional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {
	public static void main(String[] args) {
		
		LocalDateTime to = LocalDateTime.of(2016, 01, 01, 00, 00);
		LocalDateTime from = LocalDateTime.of(2015, 01, 01, 00, 00);
		Duration stepDuration = Duration.ofDays(1);
		TemporalTickerGenerator temporalTickerGenerator = new TemporalTickerGenerator();
		TimeDivisionController timeDivisionController = new TimeDivisionController(from, to, stepDuration);
		List<TemporalTickerPO> temporalTickers = temporalTickerGenerator.generate(Currency.BITCOIN, timeDivisionController);
		List<Comparison> changes = compare(temporalTickers);

		for (int counter = 0; counter < temporalTickers.size(); counter++) {
			TemporalTickerPO temporalTicker = temporalTickers.get(counter);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(new LocalDateTimeToStringConverter().convert(temporalTicker.getTemporalTickerId().getStart()) + " - First: "
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
		
		EntityManagerController.getInstance().close();
	}

	private static List<Comparison> compare(List<TemporalTickerPO> temporalTickers) {
		TemporalTickerPO previousTemporalTicker = null;

		List<Comparison> comparisons = new ArrayList<>();

		for (TemporalTickerPO temporalTicker : temporalTickers) {
			if (previousTemporalTicker != null) {
				comparisons.add(createComparison(previousTemporalTicker, temporalTicker));
			}
			previousTemporalTicker = temporalTicker;
		}

		return comparisons;
	}

	private static Comparison createComparison(TemporalTickerPO previousTemporalTicker, TemporalTickerPO temporalTicker) {

		Comparison comparison = new Comparison();
		comparison.setPreviousValue(previousTemporalTicker.getLast());
		comparison.setCurrentValue(temporalTicker.getLast());

		return comparison;
	}
}
