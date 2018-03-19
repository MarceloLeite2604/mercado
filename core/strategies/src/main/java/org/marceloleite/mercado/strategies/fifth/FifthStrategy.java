package org.marceloleite.mercado.strategies.fifth;

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
import org.marceloleite.mercado.strategies.third.ThirdStrategyParameter;
import org.marceloleite.mercado.strategies.third.ThirdStrategyVariable;

public class FifthStrategy extends AbstractStrategy {

	private static final Logger LOGGER = LogManager.getLogger(FifthStrategy.class);

	// private static final double GROWTH_PERCENTAGE_THRESHOLD = 0.0496;

	// private static final double SHRINK_PERCENTAGE_THRESHOLD = -0.05;
	
	private Double growthPercentageThreshold;
	
	private Double shrinkPercentageThreshold;

	private Currency currency;

	private TemporalTicker baseTemporalTicker;
	
	private Double workingAmountCurrency;

	private Status status;

	public FifthStrategy(Currency currency) {
		super(ThirdStrategyParameter.class, ThirdStrategyVariable.class);
		this.currency = currency;
		this.status = Status.UNDEFINED;
	}

	public FifthStrategy() {
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
			CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
			Order order = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.selling(currencyAmountToSell).receivingUnitPriceOf(currencyAmountUnitPrice).build();
			LOGGER.info(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
					+ order + ".");
			executeOrder(order, account, house);
		}
	}

	private void createBuyOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountToPay = calculateOrderAmount(OrderType.BUY, account);
		if (currencyAmountToPay != null) {
			CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
			CurrencyAmount calculateCurrencyAmountToBuy = calculateCurrencyAmountToBuy(currencyAmountToPay, currencyAmountUnitPrice);
			Order order = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.buying(calculateCurrencyAmountToBuy).payingUnitPriceOf(currencyAmountUnitPrice).build();
			LOGGER.info(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
					+ order + ".");
			executeOrder(order, account, house);
		}
	}
	
	private CurrencyAmount calculateCurrencyAmountToBuy(CurrencyAmount currencyAmountToPay,
			CurrencyAmount currencyAmountUnitPrice) {
		Double quantity = currencyAmountToPay.getAmount()/currencyAmountUnitPrice.getAmount();
				CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, quantity);
		return currencyAmountToBuy;
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

	@Override
	protected Property retrieveVariable(String name) {
		FifthStrategyVariable fifthStrategyVariable = FifthStrategyVariable.findByName(name);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		String json = null;
		switch(fifthStrategyVariable) {
		case BASE_TEMPORAL_TICKER:
			json = objectToJsonConverter.convertTo(baseTemporalTicker);
			break;
		case STATUS:
			json = objectToJsonConverter.convertTo(status);
		case WORKING_AMOUNT_CURRENCY:
			json = objectToJsonConverter.convertTo(workingAmountCurrency);
			break;
		}
		
		fifthStrategyVariable.setValue(json);
		return fifthStrategyVariable;
	}

	@Override
	protected void defineVariable(Property variable) {
		FifthStrategyVariable fifthStrategyVariable = FifthStrategyVariable.findByName(variable.getName());
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		switch(fifthStrategyVariable) {
		case BASE_TEMPORAL_TICKER:
			baseTemporalTicker = new TemporalTicker();
			baseTemporalTicker = objectToJsonConverter.convertFromToObject(variable.getValue(), baseTemporalTicker);
			break;
		case STATUS:
			status = objectToJsonConverter.convertFromToObject(variable.getValue(), status);
			break;
		case WORKING_AMOUNT_CURRENCY:
			workingAmountCurrency = new Double(0.0);
			workingAmountCurrency = objectToJsonConverter.convertFromToObject(variable.getValue(),
					workingAmountCurrency);
			break;
		}
	}

	@Override
	protected void defineParameter(Property parameter) {
		FifthStrategyParameter fifthStrategyParameter = FifthStrategyParameter.findByName(parameter.getName());
		switch(fifthStrategyParameter) {
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = Double.parseDouble(parameter.getValue());
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = Double.parseDouble(parameter.getValue());
			break;
		case WORKING_AMOUNT_CURRENCY:
			workingAmountCurrency = Double.parseDouble(parameter.getValue());
			break;
		}
	}
	
	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		Double lastPrice = house.getTemporalTickers().get(currency).getLastPrice();
		CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(currency, lastPrice);
		return currencyAmountUnitPrice;
	}
	
	private enum Status {
		UNDEFINED,
		APPLIED,
		SAVED;
	}
}
