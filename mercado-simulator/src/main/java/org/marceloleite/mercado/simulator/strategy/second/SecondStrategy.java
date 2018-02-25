package org.marceloleite.mercado.simulator.strategy.second;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.TemporalTicker;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.util.PercentageFormatter;
import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.House;
import org.marceloleite.mercado.simulator.TemporalTickerVariation;
import org.marceloleite.mercado.simulator.strategy.Strategy;

public class SecondStrategy implements Strategy {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(SecondStrategy.class);

	private static final int TOTAL_TIME_INTERVAL_TO_ANALYZE = 10;

	private Currency currency;

	private CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray;

	private CircularArray<TemporalTicker> temporalTickerCircularArray;

	/* B.O.S.O (Buy order sell order) - Ratio between buy and sell orders */ 
	private CircularArray<Double> bosoCircularArray;
	// private CircularArray<Double> bosoVariationCircularArray;
	
	private CircularArray<Double> lastFirstRatioCircularArray;

	public SecondStrategy(Currency currency) {
		this.currency = currency;
		this.temporalTickerVariationCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE);
		this.temporalTickerCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
		this.bosoCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
		// this.bosoVariationCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE);
		this.lastFirstRatioCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
	}

	@Override
	public void check(TimeInterval simulationTimeInterval, Account account, House house) {
		updateCircularArrays(house);
		analyze();
	}

	@Override
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	private void updateCircularArrays(House house) {
		TemporalTicker lastTemporalTicker = temporalTickerCircularArray.last();
		TemporalTicker currentTemporalTicker = house.getTemporalTickers().get(currency);
		bosoCircularArray.add(calulateBuySellRatio(currentTemporalTicker));
		if (lastTemporalTicker != null) {
			TemporalTickerVariation temporalTickerVariation = new TemporalTickerVariation(lastTemporalTicker,
					currentTemporalTicker);
			// new VariationCalculator().calculate(firstValue, bosoCircularArray.ge)
			temporalTickerVariationCircularArray.add(temporalTickerVariation);
			
			// bosoVariationCircularArray.add();
		}
		temporalTickerCircularArray.add(currentTemporalTicker);
		lastFirstRatioCircularArray.add(calculateLastFirstRatio(currentTemporalTicker));
	}

	private Double calculateLastFirstRatio(TemporalTicker currentTemporalTicker) {
		double first = currentTemporalTicker.getFirstPrice();
		double last = currentTemporalTicker.getLastPrice();
		return new RatioCalculator().calculate(last, first);
	}

	private double calulateBuySellRatio(TemporalTicker temporalTicker) {
		long buyOrders = temporalTicker.getBuyOrders();
		long sellOrders = temporalTicker.getSellOrders();
		return new RatioCalculator().calculate(buyOrders, sellOrders);
	}

	DigitalCurrencyFormatter digitalCurrencyFormatter = new DigitalCurrencyFormatter();
	PercentageFormatter percentageFormatter = new PercentageFormatter();

	private void analyze() {
		ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
		System.out.println("Current status: ");
		System.out.println("               Time    Order variation     Buy/sell ratio     Last/first ratio\n");
		for (int counter = 0; counter < temporalTickerCircularArray.getSize(); counter++) {
			double orderVariation = 0.0;
			if ( counter > 0 ) {
				orderVariation = temporalTickerVariationCircularArray.get(counter-1).getOrderVariation();
			}
			Double buySellRatio = bosoCircularArray.get(counter);
			Double lastFirstRatio = lastFirstRatioCircularArray.get(counter);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(zonedDateTimeToStringConverter
					.convertTo(temporalTickerCircularArray.get(counter).getStart()) + "  | ");
			stringBuffer.append(String.format("%15s", percentageFormatter.format(orderVariation)) + "  | ");
			stringBuffer.append(String.format("%15s", digitalCurrencyFormatter.format(buySellRatio - 1)) + "  | ");
			stringBuffer.append(String.format("%7s", digitalCurrencyFormatter.format(lastFirstRatio - 1)) + "  ");
			System.out.println(stringBuffer.toString());
		}

		VariationCircularArrayPivot variationCircularArrayPivot = new VariationCircularArrayPivot(
				temporalTickerVariationCircularArray);
		PositiveNegativeCounter positiveNegativeCounter = new PositiveNegativeCounter();
		List<Double> orderVariations = variationCircularArrayPivot.getOrderVariations();
		System.out.println("\tPositive order variations: " + positiveNegativeCounter.countPositives(orderVariations));
		System.out.println("\tNegative order variations: " + positiveNegativeCounter.countNegatives(orderVariations));
	}
}
