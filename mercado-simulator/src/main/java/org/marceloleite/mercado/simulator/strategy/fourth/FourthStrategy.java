package org.marceloleite.mercado.simulator.strategy.fourth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.PercentageFormatter;
import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.House;
import org.marceloleite.mercado.simulator.TemporalTicker;
import org.marceloleite.mercado.simulator.TemporalTickerVariation;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.simulator.order.MinimalAmounts;
import org.marceloleite.mercado.simulator.order.SellOrderBuilder;
import org.marceloleite.mercado.simulator.order.SellOrderBuilder.SellOrder;
import org.marceloleite.mercado.simulator.strategy.Strategy;
import org.marceloleite.mercado.simulator.strategy.second.CircularArray;

public class FourthStrategy implements Strategy {

	private static final Logger LOGGER = LogManager.getLogger(FourthStrategy.class);

	private static final double GROWTH_PERCENTAGE_THRESHOLD = 0.0496;

	private static final double SHRINK_PERCENTAGE_THRESHOLD = -0.05;
	
	private static final int CIRCULAR_ARRAY_SIZE = 5;

	private Currency currency;

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
		TemporalTickerVariation temporalTickerVariation = generateTemporalTickerVariation(simulationTimeInterval, house.getTemporalTickers().get(currency));
		
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
				} else if (lastVariation != Double.NaN && lastVariation >= GROWTH_PERCENTAGE_THRESHOLD) {
					updateBase(house);
					createBuyOrder(simulationTimeInterval, account);
				}
				break;
			case APPLIED:
				if (lastVariation != Double.NaN && lastVariation > 0) {
					updateBase(house);
				} else if (lastVariation != Double.NaN && lastVariation <= SHRINK_PERCENTAGE_THRESHOLD) {
					updateBase(house);
					createSellOrder(simulationTimeInterval, account, house);
				}
				break;
			}
		}
	}

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval, TemporalTicker temporalTicker) {
		if (temporalTicker == null) {
			throw new RuntimeException("No temporal ticker for time interval " + simulationTimeInterval);
		}
		circularArray.add(temporalTicker);
		if ( circularArray.getSize() == CIRCULAR_ARRAY_SIZE ) {
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

	private enum Status {
		UNDEFINED,
		APPLIED,
		SAVED;
	}
}
