package org.marceloleite.mercado.simulator.strategy.third;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.PercentageFormatter;
import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.House;
import org.marceloleite.mercado.simulator.TemporalTickerVariation;
import org.marceloleite.mercado.simulator.order.BuyOrder;
import org.marceloleite.mercado.simulator.order.SellOrder;
import org.marceloleite.mercado.simulator.strategy.Strategy;

public class ThirdStrategy implements Strategy {

	private static final Logger LOGGER = LogManager.getLogger(ThirdStrategy.class);

	private static final double GROWTH_PERCENTAGE_THRESHOLD = 0.0496;

	private static final double SHRINK_PERCENTAGE_THRESHOLD = -0.05;

	private Currency currency;

	private TemporalTickerPO baseTemporalTickerPO;

	private Status status;

	public ThirdStrategy(Currency currency) {
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
		TemporalTickerVariation temporalTickerVariation;
		switch (status) {
		case UNDEFINED:
			temporalTickerVariation = house.getTemporalTickerVariations().get(currency);
			if (temporalTickerVariation != null) {
				if (temporalTickerVariation.getLastVariation() != Double.NaN
						&& temporalTickerVariation.getLastVariation() > 0) {
					LOGGER.debug(simulationTimeInterval + ": Last variation is "
							+ new PercentageFormatter().format(temporalTickerVariation.getLastVariation()));
					updateBase(house);
					createBuyOrder(simulationTimeInterval, account);
				}
			}
			break;
		case SAVED:
			temporalTickerVariation = generateTemporalTickerVariation(simulationTimeInterval, house);
			if (temporalTickerVariation != null) {
				if (temporalTickerVariation.getLastVariation() != Double.NaN
						&& temporalTickerVariation.getLastVariation() < 0) {
					updateBase(house);
				} else if (temporalTickerVariation.getLastVariation() != Double.NaN
						&& temporalTickerVariation.getLastVariation() >= GROWTH_PERCENTAGE_THRESHOLD) {
					updateBase(house);
					createBuyOrder(simulationTimeInterval, account);
				}
			}
			break;
		case APPLIED:
			temporalTickerVariation = generateTemporalTickerVariation(simulationTimeInterval, house);
			if (temporalTickerVariation != null) {
				if (temporalTickerVariation.getLastVariation() != Double.NaN
						&& temporalTickerVariation.getLastVariation() > 0) {
					updateBase(house);
				} else if (temporalTickerVariation.getLastVariation() != Double.NaN
						&& temporalTickerVariation.getLastVariation() <= SHRINK_PERCENTAGE_THRESHOLD) {
					updateBase(house);
					createSellOrder(simulationTimeInterval, account, house);
				}
			}
			break;
		}

	}

	private void updateBase(House house) {
		TemporalTickerPO temporalTickerPO = house.getTemporalTickers().get(currency);
		if (temporalTickerPO != null) {
			baseTemporalTickerPO = temporalTickerPO;
		}
	}

	private void createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountToSell = new CurrencyAmount(account.getBalance().get(currency));
		CurrencyAmount currencyAmountToRetrieve = new CurrencyAmount(Currency.REAL, null);
		SellOrder sellOrder = new SellOrder(simulationTimeInterval.getStart(), currencyAmountToSell,
				currencyAmountToRetrieve);
		LOGGER.debug(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
				+ sellOrder + ".");
		account.getSellOrdersTemporalController().add(sellOrder);
		status = Status.SAVED;
	}

	private void createBuyOrder(TimeInterval simulationTimeInterval, Account account) {
		CurrencyAmount currencyAmountToInvest = new CurrencyAmount(account.getBalance().get(Currency.REAL));
		CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, null);
		BuyOrder buyOrder = new BuyOrder(simulationTimeInterval.getStart(), currencyAmountToBuy,
				currencyAmountToInvest);
		LOGGER.debug(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
				+ buyOrder + ".");
		account.getBuyOrdersTemporalController().add(buyOrder);
		status = Status.APPLIED;
	}

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval, House house) {
		TemporalTickerVariation temporalTickerVariation = null;
		TemporalTickerPO currentTemporalTickerPO = house.getTemporalTickers().get(currency);
		if (currentTemporalTickerPO != null) {
			temporalTickerVariation = new TemporalTickerVariation(baseTemporalTickerPO, currentTemporalTickerPO);
			/*
			 * TimeIntervalToStringConverter timeIntervalToStringConverter = new
			 * TimeIntervalToStringConverter();
			 * LOGGER.debug(timeIntervalToStringConverter.convertTo(simulationTimeInterval)
			 * + ": Last variation is " + new
			 * PercentageFormatter().format(temporalTickerVariation.getLastVariation()));
			 */
		} else {
			LOGGER.debug("No ticker variation for period " + simulationTimeInterval + ".");
		}
		return temporalTickerVariation;
	}

	private enum Status {
		UNDEFINED,
		APPLIED,
		SAVED;
	}
}
