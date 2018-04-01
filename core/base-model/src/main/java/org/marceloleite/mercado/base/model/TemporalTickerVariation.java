package org.marceloleite.mercado.base.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.VariationCalculator;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.data.TemporalTicker;

public class TemporalTickerVariation {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(TemporalTickerVariation.class);

	private Currency currency;

	private MercadoBigDecimal orderVariation;

	private MercadoBigDecimal highVariation;

	private MercadoBigDecimal averageVariation;

	private MercadoBigDecimal lowVariation;

	private MercadoBigDecimal volVariation;

	private MercadoBigDecimal firstVariation;

	private MercadoBigDecimal lastVariation;

	private MercadoBigDecimal buyVariation;

	private MercadoBigDecimal sellVariation;

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

			MercadoBigDecimal previousLast = previousTemporalTicker.getLastPrice();
			if (previousTemporalTicker.getLastPrice().compareTo(MercadoBigDecimal.ZERO) == 0) {
				previousLast = previousTemporalTicker.getPreviousLastPrice();
			}

			MercadoBigDecimal currentLast = currentTemporalTicker.getLastPrice();
			if (currentTemporalTicker.getLastPrice().compareTo(MercadoBigDecimal.ZERO) == 0) {
				currentLast = currentTemporalTicker.getPreviousLastPrice();
			}

			this.lastVariation = calculateVariation(previousLast, currentLast);
			this.currency = previousTemporalTicker.getCurrency();
		}
	}

	private TemporalTickerVariation(Currency currency, MercadoBigDecimal orderVariation, MercadoBigDecimal highVariation,
			MercadoBigDecimal averageVariation, MercadoBigDecimal lowVariation, MercadoBigDecimal volVariation, MercadoBigDecimal firstVariation,
			MercadoBigDecimal lastVariation, MercadoBigDecimal buyVariation, MercadoBigDecimal sellVariation) {
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

	private MercadoBigDecimal calculateVariation(long previous, long current) {
		return calculateVariation(new MercadoBigDecimal(previous), new MercadoBigDecimal(current));
	}

	private MercadoBigDecimal calculateVariation(MercadoBigDecimal previous, MercadoBigDecimal current) {
		return new VariationCalculator().calculate(current, previous);
	}

	public Currency getCurrency() {
		return currency;
	}

	public MercadoBigDecimal getOrderVariation() {
		return orderVariation;
	}

	public MercadoBigDecimal getHighVariation() {
		return highVariation;
	}

	public MercadoBigDecimal getAverageVariation() {
		return averageVariation;
	}

	public MercadoBigDecimal getLowVariation() {
		return lowVariation;
	}

	public MercadoBigDecimal getVolVariation() {
		return volVariation;
	}

	public MercadoBigDecimal getFirstVariation() {
		return firstVariation;
	}

	public MercadoBigDecimal getLastVariation() {
		return lastVariation;
	}

	public MercadoBigDecimal getBuyVariation() {
		return buyVariation;
	}

	public MercadoBigDecimal getSellVariation() {
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

	private String printPercentage(MercadoBigDecimal percentage) {
		if (percentage != null) {
			return new PercentageFormatter().format(percentage);
		} else {
			return "null";
		}
	}
}
