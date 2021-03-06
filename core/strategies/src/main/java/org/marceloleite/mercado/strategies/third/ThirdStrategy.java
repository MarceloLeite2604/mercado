package org.marceloleite.mercado.strategies.third;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.TemporalTickerVariation;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.base.model.order.MinimalAmounts;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder.SellOrder;
import org.marceloleite.mercado.commons.Currency;
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

	// private static final double GROWTH_PERCENTAGE_THRESHOLD = 0.0496;

	// private static final double SHRINK_PERCENTAGE_THRESHOLD = -0.05;
	
	private Double growthPercentageThreshold;
	
	private Double shrinkPercentageThreshold;

	private Currency currency;

	private TemporalTicker baseTemporalTicker;

	private Status status;

	public ThirdStrategy(Currency currency) {
		super(ThirdStrategyParameter.class, ThirdStrategyVariable.class);
		this.currency = currency;
		this.status = Status.UNDEFINED;
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
			double lastVariation = temporalTickerVariation.getLastVariation();
			switch (status) {
			case UNDEFINED:
				if (lastVariation != Double.NaN && lastVariation > 0) {
					LOGGER.debug(simulationTimeInterval + ": Last variation is "
							+ new PercentageFormatter().format(lastVariation));
					updateBase(house);
					createBuyOrder(simulationTimeInterval, account);
				}
				break;
			case SAVED:
				if (lastVariation != Double.NaN && lastVariation < 0) {
					updateBase(house);
				} else if (lastVariation != Double.NaN && lastVariation >= growthPercentageThreshold) {
					updateBase(house);
					createBuyOrder(simulationTimeInterval, account);
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

	private void createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountToSell = calculateOrderAmount(OrderType.SELL, account);
		if (currencyAmountToSell != null) {
			CurrencyAmount currencyAmountToReceive = new CurrencyAmount(Currency.REAL, null);
			SellOrder sellOrder = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.selling(currencyAmountToSell).receiving(currencyAmountToReceive).build();
			LOGGER.info(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
					+ sellOrder + ".");
			account.getSellOrdersTemporalController().add(sellOrder);
			status = Status.SAVED;
		}
	}

	private void createBuyOrder(TimeInterval simulationTimeInterval, Account account) {
		CurrencyAmount currencyAmountToPay = calculateOrderAmount(OrderType.BUY, account);
		if (currencyAmountToPay != null) {
			CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, null);
			BuyOrder buyOrder = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.buying(currencyAmountToBuy).paying(currencyAmountToPay).build();
			LOGGER.info(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
					+ buyOrder + ".");
			account.getBuyOrdersTemporalController().add(buyOrder);
			status = Status.APPLIED;
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

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval, House house) {
		TemporalTicker currentTemporalTicker = house.getTemporalTickers().get(currency);
		if (currentTemporalTicker == null) {
			throw new RuntimeException("No temporal ticker for time interval " + simulationTimeInterval);
		}

		TemporalTickerVariation temporalTickerVariation = new TemporalTickerVariation(baseTemporalTicker, currentTemporalTicker);
		LOGGER.debug("Variation : " + temporalTickerVariation + ".");
		return temporalTickerVariation;
	}

	private enum Status {
		UNDEFINED,
		APPLIED,
		SAVED;
	}

	@Override
	protected Property retrieveVariable(String name) {
		ThirdStrategyVariable thirdStrategyVariable = ThirdStrategyVariable.findByName(name);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		String json = null;
		switch(thirdStrategyVariable) {
		case BASE_TEMPORAL_TICKER:
			json = objectToJsonConverter.convertTo(baseTemporalTicker);
			break;
		case STATUS:
			json = objectToJsonConverter.convertTo(status);
			break;
		}
		
		thirdStrategyVariable.setValue(json);
		return thirdStrategyVariable;
	}

	@Override
	protected void defineVariable(Property variable) {
		ThirdStrategyVariable thirdStrategyVariable = ThirdStrategyVariable.findByName(variable.getName());
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		switch(thirdStrategyVariable) {
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
		ThirdStrategyParameter thirdStrategyParameter = ThirdStrategyParameter.findByName(parameter.getName());
		switch(thirdStrategyParameter) {
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = Double.parseDouble(parameter.getValue());
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = Double.parseDouble(parameter.getValue());
			break;
		}
	}	
}
