package org.marceloleite.mercado.strategies.third;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.BuyOrderBuilder;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.SellOrderBuilder;
import org.marceloleite.mercado.TemporalTickerVariation;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.orderanalyser.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.NoBalanceOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.OrderAnalyser;
import org.marceloleite.mercado.strategy.AbstractStrategyExecutor;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public class ThirdStrategy extends AbstractStrategyExecutor {

	private static final Logger LOGGER = LogManager.getLogger(ThirdStrategy.class);

	private double growthPercentageThreshold;

	private double shrinkPercentageThreshold;

	private TemporalTicker baseTemporalTicker;

	private ThirdStrategyStatus status;

	public ThirdStrategy(Strategy strategy) {
		super(strategy);
		this.status = ThirdStrategyStatus.UNDEFINED;
	}

	@Override
	public void execute(TimeInterval timeInterval, Account account, House house) {
		setBaseIfNull(house.getTemporalTickerFor(getCurrency()));
		TemporalTickerVariation temporalTickerVariation = generateTemporalTickerVariation(timeInterval, house);
		if (temporalTickerVariation != null) {
			double lastVariation = temporalTickerVariation.getLastVariation();
			if (Double.isFinite(lastVariation)) {
				Order order = null;
				switch (status) {
				case UNDEFINED:
					if (lastVariation > 0) {
						LOGGER.debug(timeInterval + ": Last variation is " + PercentageFormatter.getInstance()
								.format(lastVariation));
						updateBase(house);
						order = createBuyOrder(timeInterval, account, house);
					}
					break;
				case SAVED:
					if (lastVariation < 0) {
						updateBase(house);
					} else if (lastVariation >= growthPercentageThreshold) {
						updateBase(house);
						order = createBuyOrder(timeInterval, account, house);
					}
					break;
				case APPLIED:
					if (lastVariation > 0) {
						updateBase(house);
					} else if (lastVariation <= shrinkPercentageThreshold) {
						updateBase(house);
						order = createSellOrder(timeInterval, account, house);
					}
					break;
				}
				if (order != null) {
					executeOrder(order, account, house);
				}
			}
		}
	}

	private void setBaseIfNull(TemporalTicker temporalTicker) {
		if (baseTemporalTicker == null) {
			LOGGER.debug("Setting base temporal ticker as: " + temporalTicker + ".");
			baseTemporalTicker = temporalTicker;
		}
	}

	private void updateBase(House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		if (temporalTicker != null) {
			baseTemporalTicker = temporalTicker;
		}
	}

	private Order createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderAnalyser = new OrderAnalyser(account, OrderType.SELL,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, getCurrency());
		try {
			orderAnalyser.setSecond(calculateOrderAmount(OrderType.SELL, account));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			return null;
		}

		Order order = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.selling(orderAnalyser.getSecond())
				.receivingUnitPriceOf(orderAnalyser.getUnitPrice())
				.build();
		LOGGER.info(ZonedDateTimeToStringConverter.getInstance()
				.convertTo(simulationTimeInterval.getStart()) + ": Created " + order + ".");
		return order;
	}

	private Order createBuyOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderAnalyser = new OrderAnalyser(account, OrderType.BUY, calculateCurrencyAmountUnitPrice(house),
				Currency.REAL, getCurrency());

		try {
			orderAnalyser.setFirst(calculateOrderAmount(OrderType.BUY, account));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			return null;
		}

		Order order = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.buying(orderAnalyser.getSecond())
				.payingUnitPriceOf(orderAnalyser.getUnitPrice())
				.build();
		LOGGER.info(ZonedDateTimeToStringConverter.getInstance()
				.convertTo(simulationTimeInterval.getStart()) + ": Created " + order + ".");
		return order;
	}

	private CurrencyAmount calculateOrderAmount(OrderType orderType, Account account) {
		Currency currency = (orderType == OrderType.SELL ? getCurrency() : Currency.REAL);
		CurrencyAmount orderAmount = account.getWallet()
				.getBalanceFor(currency)
				.asCurrencyAmount();
		LOGGER.debug("Order amount is " + orderAmount + ".");
		return orderAmount;
	}

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval, House house) {
		TemporalTicker currentTemporalTicker = house.getTemporalTickerFor(getCurrency());
		if (currentTemporalTicker == null) {
			throw new RuntimeException("No temporal ticker for time interval " + simulationTimeInterval);
		}

		TemporalTickerVariation temporalTickerVariation = new TemporalTickerVariation(baseTemporalTicker,
				currentTemporalTicker);
		/* LOGGER.debug("Variation : " + temporalTickerVariation + "."); */
		return temporalTickerVariation;
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		BigDecimal lastPrice = house.getTemporalTickerFor(getCurrency())
				.getCurrentOrPreviousLast();
		return new CurrencyAmount(Currency.REAL, lastPrice);
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
				status = ThirdStrategyStatus.APPLIED;
				break;
			case SELL:
				status = ThirdStrategyStatus.SAVED;
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
		switch (ThirdStrategyParameter.findByName(name)) {
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = (Double) object;
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = (Double) object;
			break;
		default:
			throw new IllegalArgumentException("Unknown parameter \"" + name + "\".");
		}
	}

	@Override
	protected void setVariable(String name, Object object) {
		switch (ThirdStrategyVariable.findByName(name)) {
		case BASE_TEMPORAL_TICKER:
			baseTemporalTicker = (TemporalTicker) object;
			break;
		case STATUS:
			status = (ThirdStrategyStatus) object;
			break;
		default:
			throw new IllegalArgumentException("Unknown variable \"" + name + "\".");
		}
	}

	@Override
	protected Object getVariable(String name) {
		Object result = null;
		switch (ThirdStrategyVariable.findByName(name)) {
		case BASE_TEMPORAL_TICKER:
			result = baseTemporalTicker;
			break;
		case STATUS:
			result = status;
			break;
		default:
			throw new IllegalArgumentException("Unknown variable \"" + name + "\".");
		}

		return result;
	}

	@Override
	protected Map<String, ObjectDefinition> getParameterDefinitions() {
		return ThirdStrategyParameter.getObjectDefinitions();
	}

	@Override
	protected Map<String, ObjectDefinition> getVariableDefinitions() {
		return ThirdStrategyVariable.getObjectDefinitions();
	}
}
