package org.marceloleite.mercado.strategies.second;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.TemporalTickerVariation;
import org.marceloleite.mercado.commons.CircularArray;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategy.AbstractStrategyExecutor;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public class SecondStrategy extends AbstractStrategyExecutor {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(SecondStrategy.class);

	private static final int TOTAL_TIME_INTERVAL_TO_ANALYZE = 10;

	private CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray;

	private CircularArray<TemporalTicker> temporalTickerCircularArray;

	/* B.O.S.O (Buy order sell order) - Ratio between buy and sell orders */
	private CircularArray<Double> bosoCircularArray;
	// private CircularArray<Double> bosoVariationCircularArray;

	private CircularArray<Double> lastFirstRatioCircularArray;

	public SecondStrategy(Strategy strategy) {
		super(strategy);
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

	private Double calculateLastFirstRatio(TemporalTicker currentTemporalTicker) {
		double first = currentTemporalTicker.getFirst()
				.doubleValue();
		double last = currentTemporalTicker.getLast()
				.doubleValue();
		return RatioCalculator.getInstance()
				.calculate(last, first);
	}

	private Double calulateBuySellRatio(TemporalTicker temporalTicker) {
		double buyOrders = temporalTicker.getBuyOrders()
				.doubleValue();
		double sellOrders = temporalTicker.getSellOrders()
				.doubleValue();
		return RatioCalculator.getInstance()
				.calculate(buyOrders, sellOrders);
	}

	private void analyze() {
		System.out.println("Current status: ");
		System.out.println("               Time    Order variation     Buy/sell ratio     Last/first ratio\n");
		for (int counter = 0; counter < temporalTickerCircularArray.getOccupiedPositions(); counter++) {
			double orderVariation = 0;
			if (counter > 0) {
				orderVariation = temporalTickerVariationCircularArray.get(counter - 1)
						.getOrderVariation();
			}
			double buySellRatio = bosoCircularArray.get(counter);
			double lastFirstRatio = lastFirstRatioCircularArray.get(counter);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(ZonedDateTimeToStringConverter.getInstance()
					.convertTo(temporalTickerCircularArray.get(counter)
							.getStart())
					+ "  | ");
			stringBuffer.append(String.format("%15s", PercentageFormatter.getInstance()
					.format(orderVariation)) + "  | ");
			stringBuffer.append(String.format("%15s", DigitalCurrencyFormatter.getInstance()
					.format(buySellRatio - 1.0)) + "  | ");
			stringBuffer.append(String.format("%7s", DigitalCurrencyFormatter.getInstance()
					.format(lastFirstRatio - 1.0)) + "  ");
			System.out.println(stringBuffer.toString());
		}

		VariationCircularArrayPivot variationCircularArrayPivot = new VariationCircularArrayPivot(
				temporalTickerVariationCircularArray);
		PositiveNegativeCounter positiveNegativeCounter = new PositiveNegativeCounter();
		List<Double> orderVariations = variationCircularArrayPivot.getOrderVariations();
		System.out.println("\tPositive order variations: " + positiveNegativeCounter.countPositives(orderVariations));
		System.out.println("\tNegative order variations: " + positiveNegativeCounter.countNegatives(orderVariations));
	}

	@Override
	protected void setParameter(String name, Object object) {
		switch (SecondStrategyParameter.findByName(name)) {
		case TOTAL_TIME_INTERVAL_TO_ANALYZE:
			this.temporalTickerVariationCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE);
			this.temporalTickerCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
			this.bosoCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
			// this.bosoVariationCircularArray = new
			// CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE);
			this.lastFirstRatioCircularArray = new CircularArray<>(TOTAL_TIME_INTERVAL_TO_ANALYZE + 1);
			break;
		default:
			throw new IllegalArgumentException("Unknown parameter \"" + name + "\".");

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setVariable(String name, Object object) {
		switch (SecondStrategyVariable.findByName(name)) {
		case BOSO_CIRCULAR_ARRAY:
			bosoCircularArray = (CircularArray<Double>) object;
			break;
		case LAST_FIRST_RATIO_CIRCULAR_ARRAY:
			lastFirstRatioCircularArray = (CircularArray<Double>) object;
			break;
		case TEMPORAL_TICKER_CIRCULAR_ARRAY:
			temporalTickerCircularArray = (CircularArray<TemporalTicker>) object;
			break;
		case TEMPORAL_TICKER_VARIATION_CIRCULAR_ARRAY:
			temporalTickerVariationCircularArray = (CircularArray<TemporalTickerVariation>) object;
			break;
		}

	}

	@Override
	protected Object getVariable(String name) {
		Object result = null;
		switch (SecondStrategyVariable.findByName(name)) {
		case BOSO_CIRCULAR_ARRAY:
			result = bosoCircularArray;
			break;
		case LAST_FIRST_RATIO_CIRCULAR_ARRAY:
			result = lastFirstRatioCircularArray;
			break;
		case TEMPORAL_TICKER_CIRCULAR_ARRAY:
			result = temporalTickerCircularArray;
			break;
		case TEMPORAL_TICKER_VARIATION_CIRCULAR_ARRAY:
			result = temporalTickerVariationCircularArray;
			break;
		default:
			throw new IllegalArgumentException("Unknown variable \"" + name + "\".");
		}
		return result;
	}

	@Override
	protected Map<String, ObjectDefinition> getParameterDefinitions() {
		return SecondStrategyParameter.getObjectDefinitions();
	}

	@Override
	protected Map<String, ObjectDefinition> getVariableDefinitions() {
		return SecondStrategyVariable.getObjectDefinitions();
	}
}
