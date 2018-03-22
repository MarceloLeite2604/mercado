package org.marceloleite.mercado.base.model;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.util.VariationCalculator;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.data.TemporalTicker;

public class TemporalTickerVariation {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(TemporalTickerVariation.class);

	private Currency currency;

	private BigDecimal orderVariation;

	private BigDecimal highVariation;

	private BigDecimal averageVariation;

	private BigDecimal lowVariation;

	private BigDecimal volVariation;

	private BigDecimal firstVariation;

	private BigDecimal lastVariation;

	private BigDecimal buyVariation;

	private BigDecimal sellVariation;

	public TemporalTickerVariation(TemporalTicker previousTemporalTicker, TemporalTicker currentTemporalTicker) {
		/* TODO: Replaced NaN values. */
		this.orderVariation = null;
		this.highVariation = null;
		this.averageVariation = null;
		this.lowVariation = null;
		this.volVariation = null;
		this.firstVariation = null;
		this.lastVariation = null;
		this.currency = null;

		if (previousTemporalTicker == null) {
			/* LOGGER.debug("Previous temporal ticker is null."); */
		} else if (currentTemporalTicker == null) {
			/* LOGGER.debug("Current temporal ticker is null."); */

		} else {
			/*
			 * LOGGER.debug("Previous temporal ticker: " + previousTemporalTicker);
			 * LOGGER.debug("Current temporal ticker: " + currentTemporalTicker);
			 */
			this.orderVariation = calculateVariation(previousTemporalTicker.getOrders(),
					currentTemporalTicker.getOrders());
			this.highVariation = calculateVariation(previousTemporalTicker.getHighestPrice(),
					currentTemporalTicker.getHighestPrice());
			this.averageVariation = calculateVariation(previousTemporalTicker.getAveragePrice(),
					currentTemporalTicker.getAveragePrice());
			this.lowVariation = calculateVariation(previousTemporalTicker.getLowestPrice(),
					currentTemporalTicker.getLowestPrice());
			this.volVariation = calculateVariation(previousTemporalTicker.getVolumeTrades(),
					currentTemporalTicker.getVolumeTrades());
			this.firstVariation = calculateVariation(previousTemporalTicker.getFirstPrice(),
					currentTemporalTicker.getFirstPrice());

			BigDecimal previousLast = previousTemporalTicker.getLastPrice();
			if (previousTemporalTicker.getLastPrice().compareTo(BigDecimal.ZERO) == 0) {
				previousLast = previousTemporalTicker.getPreviousLastPrice();
			}

			BigDecimal currentLast = currentTemporalTicker.getLastPrice();
			if (currentTemporalTicker.getLastPrice().compareTo(BigDecimal.ZERO) == 0) {
				currentLast = currentTemporalTicker.getPreviousLastPrice();
			}

			this.lastVariation = calculateVariation(previousLast, currentLast);
			this.currency = previousTemporalTicker.getCurrency();
		}
	}

	private TemporalTickerVariation(Currency currency, BigDecimal orderVariation, BigDecimal highVariation,
			BigDecimal averageVariation, BigDecimal lowVariation, BigDecimal volVariation, BigDecimal firstVariation,
			BigDecimal lastVariation, BigDecimal buyVariation, BigDecimal sellVariation) {
		super();
		this.currency = currency;
		this.orderVariation = orderVariation;
		this.highVariation = highVariation;
		this.averageVariation = averageVariation;
		this.lowVariation = lowVariation;
		this.volVariation = volVariation;
		this.firstVariation = firstVariation;
		this.lastVariation = lastVariation;
		this.buyVariation = buyVariation;
		this.sellVariation = sellVariation;
	}

	public TemporalTickerVariation(TemporalTickerVariation temporalTickerVariation) {
		this(temporalTickerVariation.getCurrency(), temporalTickerVariation.getOrderVariation(),
				temporalTickerVariation.getHighVariation(), temporalTickerVariation.getAverageVariation(),
				temporalTickerVariation.getLowVariation(), temporalTickerVariation.getVolVariation(),
				temporalTickerVariation.getFirstVariation(), temporalTickerVariation.getLastVariation(),
				temporalTickerVariation.getBuyVariation(), temporalTickerVariation.getSellVariation());
	}

	private BigDecimal calculateVariation(long previous, long current) {
		return calculateVariation(new BigDecimal(previous), new BigDecimal(current));
	}

	private BigDecimal calculateVariation(BigDecimal previous, BigDecimal current) {
		return new VariationCalculator().calculate(current, previous);
	}

	public Currency getCurrency() {
		return currency;
	}

	public BigDecimal getOrderVariation() {
		return orderVariation;
	}

	public BigDecimal getHighVariation() {
		return highVariation;
	}

	public BigDecimal getAverageVariation() {
		return averageVariation;
	}

	public BigDecimal getLowVariation() {
		return lowVariation;
	}

	public BigDecimal getVolVariation() {
		return volVariation;
	}

	public BigDecimal getFirstVariation() {
		return firstVariation;
	}

	public BigDecimal getLastVariation() {
		return lastVariation;
	}

	public BigDecimal getBuyVariation() {
		return buyVariation;
	}

	public BigDecimal getSellVariation() {
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

	private String printPercentage(BigDecimal percentage) {
		if (percentage != null) {
			return new PercentageFormatter().format(percentage);
		} else {
			return "null";
		}
	}
}
