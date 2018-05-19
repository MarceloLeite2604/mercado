package org.marceloleite.mercado.strategies.sixth.analyser;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.BuyOrderBuilder;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.SellOrderBuilder;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.orderanalyser.OrderAnalyser;
import org.marceloleite.mercado.orderanalyser.exception.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.exception.NoBalanceOrderAnalyserException;
import org.marceloleite.mercado.strategies.sixth.SixthStrategy;

public abstract class StatusAnalyserTemplate implements StatusAnalyser {

	private static final Logger LOGGER = LogManager.getLogger(SixthStrategy.class);

	private SixthStrategy strategy;

	protected StatusAnalyserTemplate() {
	}

	protected StatusAnalyserTemplate(Builder<?> builder) {
		this.strategy = builder.strategy;
	}

	public SixthStrategy getStrategy() {
		return strategy;
	}

	protected Order createBuyOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderAnalyser = new OrderAnalyser(account, OrderType.BUY, calculateCurrencyAmountUnitPrice(house),
				Currency.REAL, strategy.getCurrency());

		try {
			orderAnalyser.setFirst(calculateOrderAmount(orderAnalyser, account));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			return null;
		}

		Order order = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.buying(orderAnalyser.getSecond())
				.payingUnitPriceOf(orderAnalyser.getUnitPrice())
				.build();
		LOGGER.info(ZonedDateTimeUtils.format(simulationTimeInterval.getStart()) + ": Created " + order + ".");
		return order;
	}

	protected Order createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderAnalyser = new OrderAnalyser(account, OrderType.SELL,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, strategy.getCurrency());
		try {
			orderAnalyser.setSecond(calculateOrderAmount(orderAnalyser, account));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			return null;
		}

		Order order = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.selling(orderAnalyser.getSecond())
				.receivingUnitPriceOf(orderAnalyser.getUnitPrice())
				.build();
		LOGGER.info(ZonedDateTimeUtils.format(simulationTimeInterval.getStart()) + ": Created " + order + ".");
		return order;
	}

	private CurrencyAmount calculateOrderAmount(OrderAnalyser orderAnalyser, Account account) {
		Currency currency = null;
		BigDecimal amount = null;
		switch (orderAnalyser.getOrderType()) {
		case BUY:
			currency = Currency.REAL;
			if (strategy.getWorkingAmountCurrency()
					.compareTo(BigDecimal.ZERO) > 0) {
				if (account.getWallet()
						.getBalanceFor(currency)
						.getAmount()
						.compareTo(strategy.getWorkingAmountCurrency()) > 0) {
					amount = strategy.getWorkingAmountCurrency();
				} else {
					amount = new BigDecimal(account.getWallet()
							.getBalanceFor(currency)
							.getAmount()
							.toString());
				}
			} else {
				amount = new BigDecimal(account.getWallet()
						.getBalanceFor(currency)
						.getAmount()
						.toString());
			}
			break;
		case SELL:
			currency = strategy.getCurrency();
			amount = new BigDecimal(account.getWallet()
					.getBalanceFor(currency)
					.getAmount()
					.toString());
			break;
		}
		CurrencyAmount orderAmount = new CurrencyAmount(currency, amount);
		LOGGER.debug("Order amount is " + orderAmount + ".");
		return orderAmount;
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		BigDecimal lastPrice = house.getTemporalTickerFor(strategy.getCurrency())
				.getCurrentOrPreviousLast();
		return new CurrencyAmount(Currency.REAL, lastPrice);
	}

	@SuppressWarnings("unchecked")
	public abstract static class Builder<T extends Builder<?>> {

		private SixthStrategy strategy;

		protected Builder() {
		};

		public T strategy(SixthStrategy strategy) {
			this.strategy = strategy;
			return (T) this;
		}
	}

	protected void updateGraphicAndBase(TemporalTicker temporalTicker) {
		ZonedDateTime time = temporalTicker.getStart();
		addLimitPointsOnGraphicFor(time);
		strategy.updateBase(temporalTicker);
		addLimitPointsOnGraphicFor(time.plusMinutes(1));
	}

	private void addLimitPointsOnGraphicFor(ZonedDateTime time) {
		if (strategy.getGraphic() != null) {
			strategy.getGraphic()
					.addLimitPointsOnGraphicData(time);
		}
	}

}
