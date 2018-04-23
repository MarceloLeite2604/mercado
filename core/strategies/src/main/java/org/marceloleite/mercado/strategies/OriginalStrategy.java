package org.marceloleite.mercado.strategies;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.BuyOrderBuilder;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategy.AbstractStrategyExecutor;

public class OriginalStrategy extends AbstractStrategyExecutor {

	private static final Logger LOGGER = LogManager.getLogger(OriginalStrategy.class);

	public OriginalStrategy(Strategy strategy) {
		super(strategy);
	}

	@Override
	public void execute(TimeInterval timeInterval, Account account, House house) {
		if (house.getTemporalTickers()
				.get(getCurrency()) != null) {
			if (account.hasPositiveBalanceFor(getCurrency())) {
				CurrencyAmount currencyAmountToPay = new CurrencyAmount(Currency.REAL,
						account.getBalanceFor(Currency.REAL));
				CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
				CurrencyAmount currencyAmountToBuy = calculateCurrencyAmountToBuy(currencyAmountToPay,
						currencyAmountUnitPrice);
				Order order = new BuyOrderBuilder().toExecuteOn(timeInterval.getStart())
						.buying(currencyAmountToBuy)
						.payingUnitPriceOf(currencyAmountUnitPrice)
						.build();
				LOGGER.debug("Order created is " + order);
				executeOrder(order, account, house);
			}
		}
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
			LOGGER.info("Order " + order + " executed.");
			break;
		case CANCELLED:
			LOGGER.info("Order " + order + " cancelled.");
			break;
		}
	}

	private CurrencyAmount calculateCurrencyAmountToBuy(CurrencyAmount realBalance,
			CurrencyAmount currencyAmountUnitPrice) {
		BigDecimal quantity = realBalance.getAmount()
				.divide(currencyAmountUnitPrice.getAmount());
		CurrencyAmount currencyAmountToBuy = new CurrencyAmount(getCurrency(), quantity);
		return currencyAmountToBuy;
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		TemporalTicker temporalTicker = house.getTemporalTickers()
				.get(getCurrency());
		BigDecimal lastPrice = temporalTicker.getCurrentOrPreviousLast();
		CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(Currency.REAL, lastPrice);
		return currencyAmountUnitPrice;
	}

	@Override
	protected void setParameter(Object parameterObject) {
	}

	@Override
	protected void setVariable(Object variableObject) {
	}

	@Override
	protected Object getVariable(String variableName) {
		return null;
	}
}
