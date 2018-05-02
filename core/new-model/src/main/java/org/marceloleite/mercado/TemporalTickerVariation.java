package org.marceloleite.mercado;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.VariationCalculator;
import org.marceloleite.mercado.commons.utils.formatter.PercentageFormatter;
import org.marceloleite.mercado.model.TemporalTicker;

public class TemporalTickerVariation {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(TemporalTickerVariation.class);

	private Currency currency;

	private Double orderVariation;

	private Double highVariation;

	private Double averageVariation;

	private Double lowVariation;

	private Double volVariation;

	private Double firstVariation;

	private Double lastVariation;

	private Double buyVariation;

	private Double sellVariation;

	public TemporalTickerVariation(TemporalTicker previousTemporalTicker, TemporalTicker currentTemporalTicker) {

		this.orderVariation = null;
		this.highVariation = null;
		this.averageVariation = null;
		this.lowVariation = null;
		this.volVariation = null;
		this.firstVariation = null;
		this.lastVariation = null;
		this.currency = null;

		if (previousTemporalTicker == null) {
			throw new IllegalArgumentException("Previous temporal ticker is null.");
		}

		if (currentTemporalTicker == null) {
			throw new IllegalArgumentException("Current temporal ticker is null.");
		}

		this.orderVariation = calculateVariation(previousTemporalTicker.getOrders(), currentTemporalTicker.getOrders());
		this.highVariation = calculateVariation(previousTemporalTicker.getHighest(),
				currentTemporalTicker.getHighest());
		this.averageVariation = calculateVariation(previousTemporalTicker.getAverage(),
				currentTemporalTicker.getAverage());
		this.lowVariation = calculateVariation(previousTemporalTicker.getLowest(), currentTemporalTicker.getLowest());
		this.volVariation = calculateVariation(previousTemporalTicker.getVolumeTraded(),
				currentTemporalTicker.getVolumeTraded());
		this.firstVariation = calculateVariation(previousTemporalTicker.getFirst(), currentTemporalTicker.getFirst());
		this.lastVariation = calculateVariation(previousTemporalTicker.getCurrentOrPreviousLast(),
				currentTemporalTicker.getCurrentOrPreviousLast());
		this.buyVariation = calculateVariation(previousTemporalTicker.getCurrentOrPreviousBuy(),
				currentTemporalTicker.getCurrentOrPreviousBuy());
		this.sellVariation = calculateVariation(previousTemporalTicker.getCurrentOrPreviousSell(),
				currentTemporalTicker.getCurrentOrPreviousSell());

		this.currency = previousTemporalTicker.getCurrency();
	}

	private double calculateVariation(BigDecimal previous, BigDecimal current) {
		double result;
		if (previous == null || current == null) {
			result = Double.NaN;
		} else {
			result = calculateVariation(previous.doubleValue(), current.doubleValue());
		}
		return result;
	}

	private double calculateVariation(Long previous, Long current) {
		double result;
		if (previous == null || current == null) {
			result = Double.NaN;
		} else {
			result = calculateVariation(previous.doubleValue(), current.doubleValue());
		}
		return result;
	}

	private double calculateVariation(Double previous, Double current) {
		return VariationCalculator.getInstance()
				.calculate(current, previous);
	}

	public Currency getCurrency() {
		return currency;
	}

	public Double getOrderVariation() {
		return orderVariation;
	}

	public Double getHighVariation() {
		return highVariation;
	}

	public Double getAverageVariation() {
		return averageVariation;
	}

	public Double getLowVariation() {
		return lowVariation;
	}

	public Double getVolVariation() {
		return volVariation;
	}

	public Double getFirstVariation() {
		return firstVariation;
	}

	public Double getLastVariation() {
		return lastVariation;
	}

	public Double getBuyVariation() {
		return buyVariation;
	}

	public Double getSellVariation() {
		return sellVariation;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("[ ");
		stringBuilder.append(currency);

		stringBuilder.append(", orders: " + printPercentage(orderVariation));
		stringBuilder.append(", volume: " + printPercentage(volVariation));
		stringBuilder.append(", buy: " + printPercentage(buyVariation));
		stringBuilder.append(", sell: " + printPercentage(sellVariation));
		stringBuilder.append(", last: " + printPercentage(lastVariation));
		stringBuilder.append(", first: " + printPercentage(firstVariation));
		stringBuilder.append(", high: " + printPercentage(highVariation));
		stringBuilder.append(", low: " + printPercentage(lowVariation));
		stringBuilder.append(", average: " + printPercentage(averageVariation));
		stringBuilder.append(" ]");

		return stringBuilder.toString();
	}

	private String printPercentage(double percentage) {
		return PercentageFormatter.getInstance()
				.format(percentage);
	}
}
