package org.marceloleite.mercado.strategies;

import java.math.BigDecimal;
import java.util.Map;

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
import org.marceloleite.mercado.strategy.ObjectDefinition;

public class OriginalStrategy extends AbstractStrategyExecutor {

	private static final Logger LOGGER = LogManager.getLogger(OriginalStrategy.class);

	public OriginalStrategy(Strategy strategy) {
		super(strategy);
	}

	@Override
	public void execute(TimeInterval timeInterval, Account account, House house) {
		if (house.getTemporalTickerFor(getCurrency()) != null) {
			if (account.hasPositiveBalanceOf(getCurrency())) {
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
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		BigDecimal lastPrice = temporalTicker.getCurrentOrPreviousLast();
		CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(Currency.REAL, lastPrice);
		return currencyAmountUnitPrice;
	}

	@Override
	protected void setParameter(String name, Object object) {
	}

	@Override
	protected void setVariable(String name, Object object) {
	}

	@Override
	protected Object getVariable(String name) {
		return null;
	}

	@Override
	protected Map<String, ObjectDefinition> getParameterDefinitions() {
		return null;
	}

	@Override
	protected Map<String, ObjectDefinition> getVariableDefinitions() {
		return null;
	}
}
