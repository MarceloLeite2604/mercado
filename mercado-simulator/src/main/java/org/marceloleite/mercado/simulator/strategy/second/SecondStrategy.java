package org.marceloleite.mercado.simulator.strategy.second;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.util.PercentageFormatter;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.House;
import org.marceloleite.mercado.simulator.TemporalTickerVariation;
import org.marceloleite.mercado.simulator.strategy.Strategy;

public class SecondStrategy implements Strategy {

	private static final Logger LOGGER = LogManager.getLogger(SecondStrategy.class);

	private static final int TOTAL_TIME_INTERVAL_TO_ANALYZE = 10;

	private Currency currency;

	private CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray;

	private CircularArray<TemporalTickerPO> temporalTickerPOCircularArray;

	/* B.O.S.O (Buy order sell order) - Ratio between buy and sell orders */ 
	private CircularArray<Double> bosoCircularArray;
	private CircularArray<Double> bosoVariationCircularArray;
	
	private CircularArray<Double> lastFirstRatioCircularArray;

	public SecondStrategy(Currency currency) {
		this.currency = currency;
		this.temporalTickerVariationCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE);
		this.temporalTickerPOCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
		this.bosoCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
		this.bosoVariationCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE);
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
		TemporalTickerPO lastTemporalTickerPO = temporalTickerPOCircularArray.last();
		TemporalTickerPO currentTemporalTickerPO = house.getTemporalTickers().get(currency);
		bosoCircularArray.add(calulateBuySellRatio(currentTemporalTickerPO));
		if (lastTemporalTickerPO != null) {
			TemporalTickerVariation temporalTickerVariation = new TemporalTickerVariation(lastTemporalTickerPO,
					currentTemporalTickerPO);
			// new VariationCalculator().calculate(firstValue, bosoCircularArray.ge)
			temporalTickerVariationCircularArray.add(temporalTickerVariation);
			
			// bosoVariationCircularArray.add();
		}
		temporalTickerPOCircularArray.add(currentTemporalTickerPO);
		lastFirstRatioCircularArray.add(calculateLastFirstRatio(currentTemporalTickerPO));
	}

	private Double calculateLastFirstRatio(TemporalTickerPO currentTemporalTickerPO) {
		double first = currentTemporalTickerPO.getFirst();
		double last = currentTemporalTickerPO.getLast();
		return new RatioCalculator().calculate(last, first);
	}

	private double calulateBuySellRatio(TemporalTickerPO temporalTickerPO) {
		long buyOrders = temporalTickerPO.getBuyOrders();
		long sellOrders = temporalTickerPO.getSellOrders();
		return new RatioCalculator().calculate(buyOrders, sellOrders);
	}

	DigitalCurrencyFormatter digitalCurrencyFormatter = new DigitalCurrencyFormatter();
	PercentageFormatter percentageFormatter = new PercentageFormatter();

	private void analyze() {
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		System.out.println("Current status: ");
		System.out.println("               Time    Order variation     Buy/sell ratio     Last/first ratio\n");
		for (int counter = 0; counter < temporalTickerPOCircularArray.getSize(); counter++) {
			double orderVariation = 0.0;
			if ( counter > 0 ) {
				orderVariation = temporalTickerVariationCircularArray.get(counter-1).getOrderVariation();
			}
			Double buySellRatio = bosoCircularArray.get(counter);
			Double lastFirstRatio = lastFirstRatioCircularArray.get(counter);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(localDateTimeToStringConverter
					.convertTo(temporalTickerPOCircularArray.get(counter).getTemporalTickerId().getStart()) + "  | ");
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
