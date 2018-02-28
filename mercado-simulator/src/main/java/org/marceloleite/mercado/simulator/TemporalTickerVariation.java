package org.marceloleite.mercado.simulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.TemporalTicker;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.strategies.second.VariationCalculator;

public class TemporalTickerVariation {

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

	public TemporalTickerVariation(TemporalTicker previousTemporalTicker,
			TemporalTicker currentTemporalTicker) {
		this.orderVariation = Double.NaN;
		this.highVariation = Double.NaN;
		this.averageVariation = Double.NaN;
		this.lowVariation = Double.NaN;
		this.volVariation = Double.NaN;
		this.firstVariation = Double.NaN;
		this.lastVariation = Double.NaN;
		this.currency = null;

		if (previousTemporalTicker == null) {
			LOGGER.debug("Previous temporal ticker is null.");
		} else if (currentTemporalTicker == null) {
			LOGGER.debug("Current temporal ticker is null.");

		} else {
			LOGGER.debug("Previous temporal ticker: " + previousTemporalTicker);
			LOGGER.debug("Current temporal ticker: " + currentTemporalTicker);
			this.orderVariation = calculateVariation(previousTemporalTicker.getOrders(),
					currentTemporalTicker.getOrders());
			this.highVariation = calculateVariation(previousTemporalTicker.getHighestPrice(),
					currentTemporalTicker.getHighestPrice());
			this.averageVariation = calculateVariation(previousTemporalTicker.getAveragePrice(),
					currentTemporalTicker.getAveragePrice());
			this.lowVariation = calculateVariation(previousTemporalTicker.getLowestPrice(), currentTemporalTicker.getLowestPrice());
			this.volVariation = calculateVariation(previousTemporalTicker.getVolumeTrades(), currentTemporalTicker.getVolumeTrades());
			this.firstVariation = calculateVariation(previousTemporalTicker.getFirstPrice(),
					currentTemporalTicker.getFirstPrice());
			
			double previousLast = previousTemporalTicker.getLastPrice();
			if (previousTemporalTicker.getLastPrice() == 0.0) {
				previousLast = previousTemporalTicker.getPreviousLastPrice();
			}
			
			double currentLast = currentTemporalTicker.getLastPrice();
			if (currentTemporalTicker.getLastPrice() == 0.0) {
				currentLast = currentTemporalTicker.getPreviousLastPrice();
			}
			
			this.lastVariation = calculateVariation(previousLast, currentLast);
			this.currency = previousTemporalTicker.getCurrency();
		}
	}

	private TemporalTickerVariation(Currency currency, double orderVariation, double highVariation,
			double averageVariation, double lowVariation, double volVariation, double firstVariation,
			double lastVariation, double buyVariation, double sellVariation) {
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

	private double calculateVariation(long previous, long current) {
		return calculateVariation((double) previous, (double) current);
	}

	private double calculateVariation(double previous, double current) {
		return new VariationCalculator().calculate(current, previous);
	}

	public Currency getCurrency() {
		return currency;
	}

	public double getOrderVariation() {
		return orderVariation;
	}

	public double getHighVariation() {
		return highVariation;
	}

	public double getAverageVariation() {
		return averageVariation;
	}

	public double getLowVariation() {
		return lowVariation;
	}

	public double getVolVariation() {
		return volVariation;
	}

	public double getFirstVariation() {
		return firstVariation;
	}

	public double getLastVariation() {
		return lastVariation;
	}

	public double getBuyVariation() {
		return buyVariation;
	}

	public double getSellVariation() {
		return sellVariation;
	}
}
