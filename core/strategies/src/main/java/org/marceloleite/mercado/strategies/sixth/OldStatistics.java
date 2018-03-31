package org.marceloleite.mercado.strategies.sixth;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.base.model.util.VariationCalculator;
import org.marceloleite.mercado.commons.CircularArray;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.retriever.TemporalTickerCreator;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class OldStatistics {

	private Currency currency;

	private TemporalTicker baseTemporalTicker;

	private CircularArray<TemporalTicker> temporalTickerCircularArray;
	
	private CircularArray<MercadoBigDecimal> bosoCircularArray;

	private MercadoBigDecimal summation;

	private MercadoBigDecimal derivativeLastsSummation;

	private MercadoBigDecimal variation;

	private MercadoBigDecimal average;

	private MercadoBigDecimal next;

	private MercadoBigDecimal ratio;
	
	private MercadoBigDecimal nextValueSteps;

	public OldStatistics(Currency currency, int circularArraySize, int nextValueSteps) {
		super();
		this.temporalTickerCircularArray = new CircularArray<>(circularArraySize);
		this.bosoCircularArray = new CircularArray<>(circularArraySize);
		this.currency = currency;
		this.nextValueSteps = new MercadoBigDecimal(nextValueSteps);
		this.summation = new MercadoBigDecimal("0");
		this.derivativeLastsSummation = new MercadoBigDecimal("0");
	}

	public void setBaseTemporalTicker(TemporalTicker baseTemporalTicker) {
		this.baseTemporalTicker = baseTemporalTicker;
	}

	public TemporalTicker getBaseTemporalTicker() {
		return baseTemporalTicker;
	}

	public MercadoBigDecimal getVariation() {
		return variation;
	}

	public MercadoBigDecimal getAverage() {
		return average;
	}

	public MercadoBigDecimal getNext() {
		return next;
	}
	
	public MercadoBigDecimal getRatio() {
		return ratio;
	}

	public void add(TemporalTicker temporalTicker, TimeInterval timeInterval) {
		if (!temporalTickerCircularArray.isFilled()) {
			fillTemporalTickerCircularArray(timeInterval);
			temporalTickerCircularArray.add(temporalTicker);
			calculateValues();
		} else {
			calculatePreAddValues();
			temporalTickerCircularArray.add(temporalTicker);
			calculatePosAddValues();
		}
	}

	private void fillTemporalTickerCircularArray(TimeInterval timeInterval) {
		Duration stepTime = timeInterval.getDuration();
		ZonedDateTime endTime = timeInterval.getStart();
		Duration duration = stepTime.multipliedBy(temporalTickerCircularArray.getVacantPositions());
		TimeInterval timeIntervalToRetrieve = new TimeInterval(duration, endTime);
		TradesRetriever tradesRetriever = new TradesRetriever();

		List<Trade> trades = tradesRetriever.retrieve(currency, timeIntervalToRetrieve, false);
		TimeDivisionController timeDivisionController = new TimeDivisionController(timeIntervalToRetrieve, stepTime);
		TemporalTickerCreator temporalTickerCreator = new TemporalTickerCreator();
		List<TimeInterval> retrievedTimeIntervals = timeDivisionController.geTimeIntervals();
		for (TimeInterval retrievedTimeInterval : retrievedTimeIntervals) {
			List<Trade> tradesOnTimeInterval = trades.stream()
					.filter(trade -> ZonedDateTimeUtils.isBetween(trade.getDate(), retrievedTimeInterval))
					.collect(Collectors.toList());

			TemporalTicker temporalTickerForTimeInterval = temporalTickerCreator.create(currency, retrievedTimeInterval,
					tradesOnTimeInterval);
			temporalTickerCircularArray.add(temporalTickerForTimeInterval);
		}
	}

	private MercadoBigDecimal calculateNext() {
		if (variation.equals(MercadoBigDecimal.NOT_A_NUMBER)) {
			return new MercadoBigDecimal(MercadoBigDecimal.NOT_A_NUMBER);
		} else {
			return average.add(variation.multiply(nextValueSteps));
		}
	}

	private void calculatePreAddValues() {
		if (temporalTickerCircularArray.isFilled()) {
			MercadoBigDecimal firstLastPrice = temporalTickerCircularArray.get(0).retrieveCurrentOrPreviousLastPrice();
			MercadoBigDecimal secondLastPrice = temporalTickerCircularArray.get(1).retrieveCurrentOrPreviousLastPrice();

			MercadoBigDecimal subtract = secondLastPrice.subtract(firstLastPrice);

			derivativeLastsSummation = derivativeLastsSummation.subtract(subtract);
			summation = summation.subtract(firstLastPrice);
		}
	}

	private void calculatePosAddValues() {
		MercadoBigDecimal currentLastPrice = temporalTickerCircularArray
				.get(temporalTickerCircularArray.getOccupiedPositions() - 1).retrieveCurrentOrPreviousLastPrice();

		int previousPosition = 0;
		if (temporalTickerCircularArray.getOccupiedPositions() <= 1) {
			previousPosition = 0;
		} else {
			previousPosition = temporalTickerCircularArray.getOccupiedPositions() - 2;
		}
		MercadoBigDecimal previousLastPrice = temporalTickerCircularArray.get(previousPosition)
				.retrieveCurrentOrPreviousLastPrice();

		MercadoBigDecimal subtract = currentLastPrice.subtract(previousLastPrice);

		derivativeLastsSummation = derivativeLastsSummation.add(subtract);
		summation = summation.add(currentLastPrice);
		average = calculateAverage();
		variation = calculateVariation();
		next = calculateNext();
		ratio = calculateRatio();
	}

	private void calculateValues() {
		List<TemporalTicker> temporalTickersList = temporalTickerCircularArray.asList();
		List<MercadoBigDecimal> lastPrices = temporalTickersList.stream()
				.map(temporalTicker -> temporalTicker.retrieveCurrentOrPreviousLastPrice())
				.collect(Collectors.toList());
		MercadoBigDecimal previousLast = null;
		summation = new MercadoBigDecimal("0");
		derivativeLastsSummation = new MercadoBigDecimal("0");
		for (int counter = 0; counter < lastPrices.size(); counter++) {
			if (counter == 0) {
				previousLast = lastPrices.get(counter);
			} else {
				previousLast = lastPrices.get(counter - 1);
			}
			MercadoBigDecimal currentLast = lastPrices.get(counter);

			MercadoBigDecimal subtract = currentLast.subtract(previousLast);

			derivativeLastsSummation = derivativeLastsSummation.add(subtract);
			summation = summation.add(currentLast);
		}
		average = calculateAverage();
		variation = calculateVariation();
		next = calculateNext();
		ratio = calculateRatio();
	}

	private MercadoBigDecimal calculateVariation() {
		if (temporalTickerCircularArray.getOccupiedPositions() == 0) {
			return new MercadoBigDecimal(MercadoBigDecimal.NOT_A_NUMBER);
		} else {
			return derivativeLastsSummation
					.divide(new MercadoBigDecimal(temporalTickerCircularArray.getOccupiedPositions()));
		}
	}

	private MercadoBigDecimal calculateAverage() {
		if (temporalTickerCircularArray.getOccupiedPositions() == 0) {
			return new MercadoBigDecimal(MercadoBigDecimal.NOT_A_NUMBER);
		} else {
			return summation.divide(new MercadoBigDecimal(temporalTickerCircularArray.getOccupiedPositions()));
		}
	}

	private MercadoBigDecimal calculateRatio() {
		if (next.equals(MercadoBigDecimal.NOT_A_NUMBER)) {
			return new MercadoBigDecimal(MercadoBigDecimal.NOT_A_NUMBER);
		} else {
			return new VariationCalculator().calculate(next, baseTemporalTicker.retrieveCurrentOrPreviousLastPrice());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		NonDigitalCurrencyFormatter nonDigitalCurrencyFormatter = new NonDigitalCurrencyFormatter();
		stringBuilder.append("[ratio: " + new PercentageFormatter().format(ratio));
		stringBuilder.append(", base: " + nonDigitalCurrencyFormatter.format(baseTemporalTicker.retrieveCurrentOrPreviousLastPrice()));
		stringBuilder.append(", average: " + nonDigitalCurrencyFormatter.format(average));
		stringBuilder.append(", variation: " + nonDigitalCurrencyFormatter.format(variation));
		stringBuilder.append(", next: " + nonDigitalCurrencyFormatter.format(next) + "]");
		return stringBuilder.toString();
	}
}
