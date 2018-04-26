package org.marceloleite.mercado.strategies.fourth;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.BuyOrderBuilder;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.MinimalAmounts;
import org.marceloleite.mercado.SellOrderBuilder;
import org.marceloleite.mercado.TemporalTickerVariation;
import org.marceloleite.mercado.commons.CircularArray;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategy.AbstractStrategyExecutor;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public class FourthStrategy extends AbstractStrategyExecutor {

	private static final Logger LOGGER = LogManager.getLogger(FourthStrategy.class);

	// private static final double GROWTH_PERCENTAGE_THRESHOLD = 0.0496;

	// private static final double SHRINK_PERCENTAGE_THRESHOLD = -0.05;

	private static final int CIRCULAR_ARRAY_SIZE = 5;

	private Currency currency;

	private double growthPercentageThreshold;

	private double shrinkPercentageThreshold;

	private TemporalTicker baseTemporalTicker;

	private FourthStrategyStatus status;

	private CircularArray<TemporalTicker> circularArray;

	public FourthStrategy(Strategy strategy) {
		super(strategy);
		this.status = FourthStrategyStatus.UNDEFINED;
		this.circularArray = new CircularArray<>(CIRCULAR_ARRAY_SIZE);
	}

	@Override
	public void execute(TimeInterval timeInterval, Account account, House house) {
		TemporalTicker currentTemporalTicker = house.getTemporalTickerFor(getCurrency());
		setBaseIfNull(currentTemporalTicker);
		TemporalTickerVariation temporalTickerVariation = generateTemporalTickerVariation(timeInterval,
				currentTemporalTicker);

		if (temporalTickerVariation != null) {
			double lastVariation = temporalTickerVariation.getLastVariation();
			if (Double.isNaN(lastVariation)) {
				switch (status) {
				case UNDEFINED:
					if (lastVariation > 0) {
						LOGGER.debug(timeInterval + ": Last variation is " + PercentageFormatter.getInstance()
								.format(lastVariation));
						updateBase(house);
						createBuyOrder(timeInterval, account, house);
					}
					break;
				case SAVED:
					if (lastVariation < 0) {
						updateBase(house);
					} else if (lastVariation >= growthPercentageThreshold) {
						updateBase(house);
						createBuyOrder(timeInterval, account, house);
					}
					break;
				case APPLIED:
					if (lastVariation > 0) {
						updateBase(house);
					} else if (lastVariation <= shrinkPercentageThreshold) {
						updateBase(house);
						createSellOrder(timeInterval, account, house);
					}
					break;
				}
			}
		}
	}

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval,
			TemporalTicker temporalTicker) {
		if (temporalTicker == null) {
			throw new RuntimeException("No temporal ticker for time interval " + simulationTimeInterval);
		}
		circularArray.add(temporalTicker);
		if (circularArray.getOccupiedPositions() == CIRCULAR_ARRAY_SIZE) {
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
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		if (temporalTicker != null) {
			baseTemporalTicker = temporalTicker;
		}
	}

	private void createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountToSell = calculateOrderAmount(OrderType.SELL, account);
		if (currencyAmountToSell != null) {
			CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
			Order sellOrder = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.selling(currencyAmountToSell)
					.receivingUnitPriceOf(currencyAmountUnitPrice)
					.build();
			LOGGER.info(ZonedDateTimeToStringConverter.getInstance()
					.convertTo(simulationTimeInterval.getStart()) + ": Created " + sellOrder + ".");
			executeOrder(sellOrder, account, house);
		}
	}

	private void createBuyOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountToPay = calculateOrderAmount(OrderType.BUY, account);
		if (currencyAmountToPay != null) {
			CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, null);
			CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
			Order buyOrder = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.buying(currencyAmountToBuy)
					.payingUnitPriceOf(currencyAmountUnitPrice)
					.build();
			LOGGER.info(ZonedDateTimeToStringConverter.getInstance()
					.convertTo(simulationTimeInterval.getStart()) + ": Created " + buyOrder + ".");
			executeOrder(buyOrder, account, house);
		}
	}

	private CurrencyAmount calculateOrderAmount(OrderType orderType, Account account) {
		Currency currency = (orderType == OrderType.SELL ? this.currency : Currency.REAL);
		CurrencyAmount balance = new CurrencyAmount(getCurrency(), account.getBalanceFor(getCurrency()));
		LOGGER.debug("Balance amount: " + balance + ".");

		if (MinimalAmounts.isAmountLowerThanMinimal(balance)) {
			LOGGER.debug("Current balance is lower thant the minimal order value. Aborting order.");
			return null;
		}

		CurrencyAmount orderAmount = new CurrencyAmount(currency, balance.getAmount());
		LOGGER.debug("Order amount is " + orderAmount + ".");
		return orderAmount;
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		BigDecimal lastPrice = house.getTemporalTickerFor(getCurrency())
				.getLast();
		CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(currency, lastPrice);
		return currencyAmountUnitPrice;
	}

	private void executeOrder(Order order, Account account, House house) {
		house.getOrderExecutor()
				.placeOrder(order, house, account);

		switch (order.getStatus()) {
		case OPEN:
		case UNDEFINED:
			throw new RuntimeException(
					"Requested " + order + " execution, but its status returned as \"" + order.getStatus() + "\".");
		case FILLED:
			switch (order.getType()) {
			case BUY:
				status = FourthStrategyStatus.APPLIED;
				break;
			case SELL:
				status = FourthStrategyStatus.SAVED;
				break;
			}
			break;
		case CANCELLED:
			LOGGER.info(order + " cancelled.");
			break;
		}
	}

	@Override
	protected void setParameter(String name, Object object) {
		FourthStrategyParameter fourthStrategyparameter = FourthStrategyParameter.findByName(name);

		switch (fourthStrategyparameter) {
		case CIRCULAR_ARRAY_SIZE:
			circularArray = new CircularArray<TemporalTicker>((Integer) object);
			break;
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = (Double) object;
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = (Double) object;
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setVariable(String name, Object object) {
		FourthStrategyVariable fourthStrategyVariable = FourthStrategyVariable.findByName(name);
		switch (fourthStrategyVariable) {
		case BASE_TEMPORAL_TICKER:
			baseTemporalTicker = (TemporalTicker) object;
			break;
		case CIRCULAR_ARRAY:
			circularArray = (CircularArray<TemporalTicker>) object;
			break;
		case STATUS:
			status = (FourthStrategyStatus) object;
			break;
		}
	}

	@Override
	protected Object getVariable(String name) {
		Object result;
		FourthStrategyVariable fourthStrategyVariable = FourthStrategyVariable.findByName(name);
		switch (fourthStrategyVariable) {
		case BASE_TEMPORAL_TICKER:
			result = (Object) baseTemporalTicker;
		case CIRCULAR_ARRAY:
			result = (Object) circularArray;
			break;
		case STATUS:
			result = (Object) status;
			break;
		default:
			throw new RuntimeException("Unknown variable \"" + name + "\".");
		}
		return result;
	}

	@Override
	protected Map<String, ObjectDefinition> getParameterDefinitions() {
		return FourthStrategyParameter.getObjectDefinitions();
	}

	@Override
	protected Map<String, ObjectDefinition> getVariableDefinitions() {
		return FourthStrategyVariable.getObjectDefinitions();
	}
}
