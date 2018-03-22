package org.marceloleite.mercado.strategies.second;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.TemporalTickerVariation;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.strategies.AbstractStrategy;

public class SecondStrategy extends AbstractStrategy {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(SecondStrategy.class);

	private static final int TOTAL_TIME_INTERVAL_TO_ANALYZE = 10;

	private Currency currency;

	private CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray;

	private CircularArray<TemporalTicker> temporalTickerCircularArray;

	/* B.O.S.O (Buy order sell order) - Ratio between buy and sell orders */ 
	private CircularArray<BigDecimal> bosoCircularArray;
	// private CircularArray<Double> bosoVariationCircularArray;
	
	private CircularArray<BigDecimal> lastFirstRatioCircularArray;

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

	private BigDecimal calculateLastFirstRatio(TemporalTicker currentTemporalTicker) {
		BigDecimal first = currentTemporalTicker.getFirstPrice();
		BigDecimal last = currentTemporalTicker.getLastPrice();
		return new RatioCalculator().calculate(last, first);
	}

	private BigDecimal calulateBuySellRatio(TemporalTicker temporalTicker) {
		BigDecimal buyOrders = new BigDecimal(temporalTicker.getBuyOrders());
		BigDecimal sellOrders = new BigDecimal(temporalTicker.getSellOrders());
		return new RatioCalculator().calculate(buyOrders, sellOrders);
	}

	DigitalCurrencyFormatter digitalCurrencyFormatter = new DigitalCurrencyFormatter();
	PercentageFormatter percentageFormatter = new PercentageFormatter();

	private void analyze() {
		ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
		System.out.println("Current status: ");
		System.out.println("               Time    Order variation     Buy/sell ratio     Last/first ratio\n");
		for (int counter = 0; counter < temporalTickerCircularArray.getSize(); counter++) {
			BigDecimal orderVariation = new BigDecimal("0.0");
			if ( counter > 0 ) {
				orderVariation = temporalTickerVariationCircularArray.get(counter-1).getOrderVariation();
			}
			BigDecimal buySellRatio = bosoCircularArray.get(counter);
			BigDecimal lastFirstRatio = lastFirstRatioCircularArray.get(counter);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(zonedDateTimeToStringConverter
					.convertTo(temporalTickerCircularArray.get(counter).getStart()) + "  | ");
			stringBuffer.append(String.format("%15s", percentageFormatter.format(orderVariation)) + "  | ");
			stringBuffer.append(String.format("%15s", digitalCurrencyFormatter.format(buySellRatio.subtract(BigDecimal.ONE))) + "  | ");
			stringBuffer.append(String.format("%7s", digitalCurrencyFormatter.format(lastFirstRatio.subtract(BigDecimal.ONE))) + "  ");
			System.out.println(stringBuffer.toString());
		}

		VariationCircularArrayPivot variationCircularArrayPivot = new VariationCircularArrayPivot(
				temporalTickerVariationCircularArray);
		PositiveNegativeCounter positiveNegativeCounter = new PositiveNegativeCounter();
		List<BigDecimal> orderVariations = variationCircularArrayPivot.getOrderVariations();
		System.out.println("\tPositive order variations: " + positiveNegativeCounter.countPositives(orderVariations));
		System.out.println("\tNegative order variations: " + positiveNegativeCounter.countNegatives(orderVariations));
	}

	@Override
	protected Property retrieveVariable(String name) {
		SecondStrategyVariable secondStrategyVariable = SecondStrategyVariable.findByName(name);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		String json = null;
		switch (secondStrategyVariable) {
		case BOSO_CIRCULAR_ARRAY:
			json = objectToJsonConverter.convertTo(bosoCircularArray);
			break;
		case LAST_FIRST_RATIO_CIRCULAR_ARRAY:
			json = objectToJsonConverter.convertTo(lastFirstRatioCircularArray);
			break;
		case TEMPORAL_TICKER_CIRCULAR_ARRAY:
			json = objectToJsonConverter.convertTo(temporalTickerCircularArray);
			break;
		case TEMPORAL_TICKER_VARIATION_CIRCULAR_ARRAY:
			json = objectToJsonConverter.convertTo(temporalTickerVariationCircularArray);
			break;
		}
		secondStrategyVariable.setValue(json);
		return secondStrategyVariable;
	}

	@Override
	protected void defineVariable(Property variable) {
		SecondStrategyVariable secondStrategyVariable = SecondStrategyVariable.findByName(variable.getName());
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		switch(secondStrategyVariable) {
		case BOSO_CIRCULAR_ARRAY:
			bosoCircularArray = objectToJsonConverter.convertFromToObject(variable.getValue(), bosoCircularArray);
			break;
		case LAST_FIRST_RATIO_CIRCULAR_ARRAY:
			lastFirstRatioCircularArray = objectToJsonConverter.convertFromToObject(variable.getValue(), lastFirstRatioCircularArray);
			break;
		case TEMPORAL_TICKER_CIRCULAR_ARRAY:
			temporalTickerCircularArray = objectToJsonConverter.convertFromToObject(variable.getValue(), temporalTickerCircularArray);
			break;
		case TEMPORAL_TICKER_VARIATION_CIRCULAR_ARRAY:
			temporalTickerVariationCircularArray = objectToJsonConverter.convertFromToObject(variable.getValue(), temporalTickerVariationCircularArray);
			break;
		}
		
	}

	@Override
	protected void defineParameter(Property parameter) {
		// TODO Auto-generated method stub
		
	}
}
