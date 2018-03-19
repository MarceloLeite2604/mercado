package org.marceloleite.mercado.strategies.fourth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.TemporalTickerVariation;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder;
import org.marceloleite.mercado.base.model.order.MinimalAmounts;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.strategies.AbstractStrategy;
import org.marceloleite.mercado.strategies.second.CircularArray;

public class FourthStrategy extends AbstractStrategy {

	private static final Logger LOGGER = LogManager.getLogger(FourthStrategy.class);

	// private static final double GROWTH_PERCENTAGE_THRESHOLD = 0.0496;

	// private static final double SHRINK_PERCENTAGE_THRESHOLD = -0.05;

	private static final int CIRCULAR_ARRAY_SIZE = 5;

	private Currency currency;

	private double growthPercentageThreshold;

	private double shrinkPercentageThreshold;

	private TemporalTicker baseTemporalTicker;

	private Status status;

	private CircularArray<TemporalTicker> circularArray;

	public FourthStrategy(Currency currency) {
		this.currency = currency;
		this.status = Status.UNDEFINED;
		this.circularArray = new CircularArray<>(CIRCULAR_ARRAY_SIZE);
	}

	public FourthStrategy() {
		this(null);
	}

	@Override
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public void check(TimeInterval simulationTimeInterval, Account account, House house) {
		setBaseIfNull(house.getTemporalTickers().get(currency));
		TemporalTickerVariation temporalTickerVariation = generateTemporalTickerVariation(simulationTimeInterval,
				house.getTemporalTickers().get(currency));

		if (temporalTickerVariation != null) {
			double lastVariation = temporalTickerVariation.getLastVariation();
			switch (status) {
			case UNDEFINED:
				if (lastVariation != Double.NaN && lastVariation > 0) {
					LOGGER.debug(simulationTimeInterval + ": Last variation is "
							+ new PercentageFormatter().format(lastVariation));
					updateBase(house);
					createBuyOrder(simulationTimeInterval, account, house);
				}
				break;
			case SAVED:
				if (lastVariation != Double.NaN && lastVariation < 0) {
					updateBase(house);
				} else if (lastVariation != Double.NaN && lastVariation >= growthPercentageThreshold) {
					updateBase(house);
					createBuyOrder(simulationTimeInterval, account, house);
				}
				break;
			case APPLIED:
				if (lastVariation != Double.NaN && lastVariation > 0) {
					updateBase(house);
				} else if (lastVariation != Double.NaN && lastVariation <= shrinkPercentageThreshold) {
					updateBase(house);
					createSellOrder(simulationTimeInterval, account, house);
				}
				break;
			}
		}
	}

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval,
			TemporalTicker temporalTicker) {
		if (temporalTicker == null) {
			throw new RuntimeException("No temporal ticker for time interval " + simulationTimeInterval);
		}
		circularArray.add(temporalTicker);
		if (circularArray.getSize() == CIRCULAR_ARRAY_SIZE) {
			return new TemporalTickerVariation(circularArray.first(), circularArray.last());
		} else {
			return null;
		}
	}

	private void setBaseIfNull(TemporalTicker temporalTicker) {
		if (baseTemporalTicker == null) {
			baseTemporalTicker = temporalTicker;
		}
	}

	private void updateBase(House house) {
		TemporalTicker temporalTicker = house.getTemporalTickers().get(currency);
		if (temporalTicker != null) {
			baseTemporalTicker = temporalTicker;
		}
	}

	private void createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountToSell = calculateOrderAmount(OrderType.SELL, account);
		if (currencyAmountToSell != null) {
			CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
			Order sellOrder = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.selling(currencyAmountToSell).receivingUnitPriceOf(currencyAmountUnitPrice).build();
			LOGGER.info(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
					+ sellOrder + ".");
			executeOrder(sellOrder, account, house);
		}
	}

	private void createBuyOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountToPay = calculateOrderAmount(OrderType.BUY, account);
		if (currencyAmountToPay != null) {
			CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, null);
			CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
			Order buyOrder = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.buying(currencyAmountToBuy).payingUnitPriceOf(currencyAmountUnitPrice).build();
			LOGGER.info(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
					+ buyOrder + ".");
			executeOrder(buyOrder, account, house);
		}
	}

	private CurrencyAmount calculateOrderAmount(OrderType orderType, Account account) {
		Currency currency = (orderType == OrderType.SELL ? this.currency : Currency.REAL);
		CurrencyAmount balance = account.getBalance().get(currency);
		LOGGER.debug("Balance amount: " + balance + ".");

		if (MinimalAmounts.isAmountLowerThanMinimal(balance)) {
			LOGGER.debug("Current balance is lower thant the minimal order value. Aborting order.");
			return null;
		}

		CurrencyAmount orderAmount = new CurrencyAmount(currency, balance.getAmount());
		LOGGER.debug("Order amount is " + orderAmount + ".");
		return orderAmount;
	}

	private enum Status {
		UNDEFINED,
		APPLIED,
		SAVED;
	}

	@Override
	protected Property retrieveVariable(String name) {
		FourthStrategyVariable fourthStrategyVariable = FourthStrategyVariable.findByName(name);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		switch (fourthStrategyVariable) {
		case BASE_TEMPORAL_TICKER:
			fourthStrategyVariable.setValue(objectToJsonConverter.convertTo(baseTemporalTicker));
		case CIRCULAR_ARRAY:
			fourthStrategyVariable.setValue(objectToJsonConverter.convertTo(circularArray));
			break;
		case STATUS:
			fourthStrategyVariable.setValue(objectToJsonConverter.convertTo(status));
			break;

		}
		return fourthStrategyVariable;
	}

	@Override
	protected void defineVariable(Property variable) {
		FourthStrategyVariable fourthStrategyVariable = FourthStrategyVariable.findByName(variable.getName());
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		switch (fourthStrategyVariable) {
		case BASE_TEMPORAL_TICKER:
			baseTemporalTicker = objectToJsonConverter.convertFromToObject(variable.getName(), baseTemporalTicker);
			break;
		case CIRCULAR_ARRAY:
			circularArray = objectToJsonConverter.convertFromToObject(variable.getName(), circularArray);
			break;
		case STATUS:
			status = objectToJsonConverter.convertFromToObject(variable.getName(), status);
			break;
		}
	}

	@Override
	protected void defineParameter(Property parameter) {
		FourthStrategyParameter fourthStrategyparameter = FourthStrategyParameter.findByName(parameter.getName());

		switch (fourthStrategyparameter) {
		case CIRCULAR_ARRAY_SIZE:
			circularArray = new CircularArray<>(Integer.parseInt(parameter.getValue()));
			break;
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = Double.parseDouble(parameter.getValue());
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = Double.parseDouble(parameter.getValue());
			break;

		}
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		Double lastPrice = house.getTemporalTickers().get(currency).getLastPrice();
		CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(currency, lastPrice);
		return currencyAmountUnitPrice;
	}

	private void executeOrder(Order order, Account account, House house) {
		house.getOrderExecutor().placeOrder(order, house, account);

		switch (order.getStatus()) {
		case OPEN:
		case UNDEFINED:
			throw new RuntimeException(
					"Requested " + order + " execution, but its status returned as \"" + order.getStatus() + "\".");
		case FILLED:
			switch (order.getType()) {
			case BUY:
				status = Status.APPLIED;
				break;
			case SELL:
				status = Status.SAVED;
				break;
			}
			break;
		case CANCELLED:
			LOGGER.info(order + " cancelled.");
			break;
		}
	}
}
