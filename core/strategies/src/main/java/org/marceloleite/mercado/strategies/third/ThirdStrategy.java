package org.marceloleite.mercado.strategies.third;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.TemporalTickerVariation;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder;
import org.marceloleite.mercado.base.model.order.analyser.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.base.model.order.analyser.NoBalanceOrderAnalyserException;
import org.marceloleite.mercado.base.model.order.analyser.OrderAnalyser;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.strategies.AbstractStrategy;

public class ThirdStrategy extends AbstractStrategy {

	private static final Logger LOGGER = LogManager.getLogger(ThirdStrategy.class);

	private MercadoBigDecimal growthPercentageThreshold;

	private MercadoBigDecimal shrinkPercentageThreshold;

	private Currency currency;

	private TemporalTicker baseTemporalTicker;

	private ThirdStrategyStatus status;

	public ThirdStrategy(Currency currency) {
		super(ThirdStrategyParameter.class, ThirdStrategyVariable.class);
		this.currency = currency;
		this.status = ThirdStrategyStatus.UNDEFINED;
	}

	public ThirdStrategy() {
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
				house);
		if (temporalTickerVariation != null) {
			MercadoBigDecimal lastVariation = temporalTickerVariation.getLastVariation();
			LOGGER.debug(simulationTimeInterval + ": Last variation is "
					+ new PercentageFormatter().format(lastVariation));
			Order order = null;
			switch (status) {
			case UNDEFINED:
				if (lastVariation.compareTo(MercadoBigDecimal.NOT_A_NUMBER) != 0  && lastVariation.compareTo(MercadoBigDecimal.ZERO) > 0) {
					LOGGER.debug(simulationTimeInterval + ": Last variation is "
							+ new PercentageFormatter().format(lastVariation));
					updateBase(house);
					order = createBuyOrder(simulationTimeInterval, account, house);
				}
				break;
			case SAVED:
				if (!lastVariation.equals(MercadoBigDecimal.NOT_A_NUMBER) && lastVariation.compareTo(MercadoBigDecimal.ZERO) < 0) {
					updateBase(house);
				} else if (!lastVariation.equals(MercadoBigDecimal.NOT_A_NUMBER) && lastVariation.compareTo(growthPercentageThreshold) >= 0) {
					updateBase(house);
					order = createBuyOrder(simulationTimeInterval, account, house);
				}
				break;
			case APPLIED:
				if (!lastVariation.equals(MercadoBigDecimal.NOT_A_NUMBER) && lastVariation.compareTo(MercadoBigDecimal.ZERO) > 0) {
					updateBase(house);
				} else if (!lastVariation.equals(MercadoBigDecimal.NOT_A_NUMBER) && lastVariation.compareTo(shrinkPercentageThreshold) <= 0) {
					updateBase(house);
					order = createSellOrder(simulationTimeInterval, account, house);
				}
				break;
			}
			if (order != null) {
				executeOrder(order, account, house);
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
		TemporalTicker temporalTicker = house.getTemporalTickers().get(currency);
		if (temporalTicker != null) {
			baseTemporalTicker = temporalTicker;
		}
	}

	private Order createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderAnalyser = new OrderAnalyser(account.getBalance(), OrderType.SELL,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, currency);
		try {
			orderAnalyser.setSecond(calculateOrderAmount(OrderType.SELL, account));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			return null;
		}
		
		Order order = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.selling(orderAnalyser.getSecond()).receivingUnitPriceOf(orderAnalyser.getUnitPrice()).build();
		LOGGER.info(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
				+ order + ".");
		return order;
	}

	private Order createBuyOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderAnalyser = new OrderAnalyser(account.getBalance(), OrderType.BUY,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, currency);

		try {
			orderAnalyser.setFirst(calculateOrderAmount(OrderType.BUY, account));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			return null;
		}

		Order order = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.buying(orderAnalyser.getSecond()).payingUnitPriceOf(orderAnalyser.getUnitPrice()).build();
		LOGGER.info(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
				+ order + ".");
		return order;
	}

	private CurrencyAmount calculateOrderAmount(OrderType orderType, Account account) {
		Currency currency = (orderType == OrderType.SELL ? this.currency : Currency.REAL);
		CurrencyAmount balance = account.getBalance().get(currency);
		CurrencyAmount orderAmount = new CurrencyAmount(balance);
		LOGGER.debug("Order amount is " + orderAmount + ".");
		return orderAmount;
	}

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval, House house) {
		TemporalTicker currentTemporalTicker = house.getTemporalTickers().get(currency);
		if (currentTemporalTicker == null) {
			throw new RuntimeException("No temporal ticker for time interval " + simulationTimeInterval);
		}

		TemporalTickerVariation temporalTickerVariation = new TemporalTickerVariation(baseTemporalTicker,
				currentTemporalTicker);
		/*LOGGER.debug("Variation : " + temporalTickerVariation + ".");*/
		return temporalTickerVariation;
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
	protected Property retrieveVariable(String name) {
		ThirdStrategyVariable fifthStrategyVariable = ThirdStrategyVariable.findByName(name);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		String json = null;
		switch (fifthStrategyVariable) {
		case BASE_TEMPORAL_TICKER:
			json = objectToJsonConverter.convertTo(baseTemporalTicker);
			break;
		case STATUS:
			json = objectToJsonConverter.convertTo(status);
			break;
		}

		fifthStrategyVariable.setValue(json);
		return fifthStrategyVariable;
	}

	@Override
	protected void defineVariable(Property variable) {
		ThirdStrategyVariable fifthStrategyVariable = ThirdStrategyVariable.findByName(variable.getName());
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		switch (fifthStrategyVariable) {
		case BASE_TEMPORAL_TICKER:
			baseTemporalTicker = new TemporalTicker();
			baseTemporalTicker = objectToJsonConverter.convertFromToObject(variable.getValue(), baseTemporalTicker);
			break;
		case STATUS:
			status = objectToJsonConverter.convertFromToObject(variable.getValue(), status);
			break;
		}
	}

	@Override
	protected void defineParameter(Property parameter) {
		ThirdStrategyParameter fifthStrategyParameter = ThirdStrategyParameter.findByName(parameter.getName());
		switch (fifthStrategyParameter) {
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = new MercadoBigDecimal(parameter.getValue());
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = new MercadoBigDecimal(parameter.getValue());
			break;
		}
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		MercadoBigDecimal lastPrice = house.getTemporalTickers().get(currency).getCurrentOrPreviousLastPrice();
		return new CurrencyAmount(Currency.REAL, lastPrice);
	}
}
