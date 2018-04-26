package org.marceloleite.mercado.strategies.second;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.TemporalTickerVariation;
import org.marceloleite.mercado.commons.CircularArray;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategy.AbstractStrategyExecutor;

public class SecondStrategy extends AbstractStrategyExecutor {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(SecondStrategy.class);

	private static final int TOTAL_TIME_INTERVAL_TO_ANALYZE = 10;

	private Currency currency;

	private CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray;

	private CircularArray<TemporalTicker> temporalTickerCircularArray;

	/* B.O.S.O (Buy order sell order) - Ratio between buy and sell orders */ 
	private CircularArray<MercadoBigDecimal> bosoCircularArray;
	// private CircularArray<Double> bosoVariationCircularArray;
	
	private CircularArray<MercadoBigDecimal> lastFirstRatioCircularArray;

	public SecondStrategy(Strategy strategy) {
		super(strategy);
		this.temporalTickerVariationCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE);
		this.temporalTickerCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
		this.bosoCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
		// this.bosoVariationCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE);
		this.lastFirstRatioCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
	}

	@Override
	public void execute(TimeInterval timeInterval, Account account, House house) {
		updateCircularArrays(house);
		analyze();
	}

	private void updateCircularArrays(House house) {
		TemporalTicker lastTemporalTicker = temporalTickerCircularArray.last();
		TemporalTicker currentTemporalTicker = house.getTemporalTickerFor(getCurrency());
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

	private MercadoBigDecimal calculateLastFirstRatio(TemporalTicker currentTemporalTicker) {
		double first = currentTemporalTicker.getFirst().doubleValue();
		double last = currentTemporalTicker.getLast().doubleValue();
		return RatioCalculator.getInstance().calculate(last, first);
	}

	private MercadoBigDecimal calulateBuySellRatio(TemporalTicker temporalTicker) {
		double buyOrders = temporalTicker.getBuyOrders().doubleValue();
		double sellOrders = temporalTicker.getSellOrders().doubleValue();
		return RatioCalculator.getInstance().calculate(buyOrders, sellOrders);
	}

	private void analyze() {
		System.out.println("Current status: ");
		System.out.println("               Time    Order variation     Buy/sell ratio     Last/first ratio\n");
		for (int counter = 0; counter < temporalTickerCircularArray.getOccupiedPositions(); counter++) {
			MercadoBigDecimal orderVariation = new MercadoBigDecimal("0.0");
			if ( counter > 0 ) {
				orderVariation = temporalTickerVariationCircularArray.get(counter-1).getOrderVariation();
			}
			MercadoBigDecimal buySellRatio = bosoCircularArray.get(counter);
			MercadoBigDecimal lastFirstRatio = lastFirstRatioCircularArray.get(counter);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(ZonedDateTimeToStringConverter.getInstance()
					.convertTo(temporalTickerCircularArray.get(counter).getStart()) + "  | ");
			stringBuffer.append(String.format("%15s", percentageFormatter.format(orderVariation)) + "  | ");
			stringBuffer.append(String.format("%15s", digitalCurrencyFormatter.format(buySellRatio.subtract(MercadoBigDecimal.ONE))) + "  | ");
			stringBuffer.append(String.format("%7s", digitalCurrencyFormatter.format(lastFirstRatio.subtract(MercadoBigDecimal.ONE))) + "  ");
			System.out.println(stringBuffer.toString());
		}

		VariationCircularArrayPivot variationCircularArrayPivot = new VariationCircularArrayPivot(
				temporalTickerVariationCircularArray);
		PositiveNegativeCounter positiveNegativeCounter = new PositiveNegativeCounter();
		List<MercadoBigDecimal> orderVariations = variationCircularArrayPivot.getOrderVariations();
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
