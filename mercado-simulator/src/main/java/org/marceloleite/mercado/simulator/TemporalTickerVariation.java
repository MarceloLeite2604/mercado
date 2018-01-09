package org.marceloleite.mercado.simulator;

import java.util.OptionalDouble;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class TemporalTickerVariation {

	private Currency currency;

	private double orderVariation;

	private double highVariation;

	private double averageVariation;

	private double lowVariation;

	private double volVariation;

	private double firstVariation;

	private double lastVariation;

	private double buyVariation;

	private double sellVariation;

	public TemporalTickerVariation(TemporalTickerPO previousTemporalTickerPO,
			TemporalTickerPO currentTemporalTickerPO) {
		if (previousTemporalTickerPO == null) {
			this.orderVariation = 0;
			this.highVariation = 0;
			this.averageVariation = 0;
			this.lowVariation = 0;
			this.volVariation = 0;
			this.firstVariation = 0;
			this.lastVariation = 0;
		} else {
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
			this.lastVariation = calculateVariation(previousTemporalTickerPO.getLast(),
					currentTemporalTickerPO.getLast());
		}
		this.currency = currentTemporalTickerPO.getId().getCurrency();
	}

	private double calculateVariation(long previous, long current) {
		return calculateVariation((double) previous, (double) current);
	}

	private double calculateVariation(double previous, double current) {
		return (current / OptionalDouble.of(previous).orElse(current)) - 1;
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
