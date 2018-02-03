package org.marceloleite.mercado.simulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.simulator.strategy.second.VariationCalculator;

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

	public TemporalTickerVariation(TemporalTickerPO previousTemporalTickerPO,
			TemporalTickerPO currentTemporalTickerPO) {
		this.orderVariation = Double.NaN;
		this.highVariation = Double.NaN;
		this.averageVariation = Double.NaN;
		this.lowVariation = Double.NaN;
		this.volVariation = Double.NaN;
		this.firstVariation = Double.NaN;
		this.lastVariation = Double.NaN;
		this.currency = null;

		if (previousTemporalTickerPO == null) {
			LOGGER.debug("Previous temporal ticker is null.");
		} else if (currentTemporalTickerPO == null) {
			LOGGER.debug("Current temporal ticker is null.");

		} else {
			LOGGER.debug("Previous temporal ticker: " + previousTemporalTickerPO);
			LOGGER.debug("Current temporal ticker: " + currentTemporalTickerPO);
			this.orderVariation = calculateVariation(previousTemporalTickerPO.getOrders(),
					currentTemporalTickerPO.getOrders());
			this.highVariation = calculateVariation(previousTemporalTickerPO.getHigh(),
					currentTemporalTickerPO.getHigh());
			this.averageVariation = calculateVariation(previousTemporalTickerPO.getAverage(),
					currentTemporalTickerPO.getAverage());
			this.lowVariation = calculateVariation(previousTemporalTickerPO.getLow(), currentTemporalTickerPO.getLow());
			this.volVariation = calculateVariation(previousTemporalTickerPO.getVol(), currentTemporalTickerPO.getVol());
			this.firstVariation = calculateVariation(previousTemporalTickerPO.getFirst(),
					currentTemporalTickerPO.getFirst());
			
			double previousLast = previousTemporalTickerPO.getLast();
			if (previousTemporalTickerPO.getLast() == 0.0) {
				previousLast = previousTemporalTickerPO.getPreviousLast();
			}
			
			double currentLast = currentTemporalTickerPO.getLast();
			if (currentTemporalTickerPO.getLast() == 0.0) {
				currentLast = currentTemporalTickerPO.getPreviousLast();
			}
			
			this.lastVariation = calculateVariation(previousLast, currentLast);
			this.currency = previousTemporalTickerPO.getId().getCurrency();
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
