package org.marceloleite.mercado.strategies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.Balance;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.strategy.AbstractStrategy;

public class OriginalStrategy extends AbstractStrategy {

	private static final Logger LOGGER = LogManager.getLogger(OriginalStrategy.class);

	private Currency currency;

	public OriginalStrategy(Currency currency) {
		this.currency = currency;
	}

	public OriginalStrategy() {
	}

	@Override
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public void check(TimeInterval simulationTimeInterval, Account account, House house) {
		if (house.getTemporalTickers().get(currency) != null) {
			Balance balance = account.getBalance();
			if (balance.hasPositiveBalance(Currency.REAL)) {
				CurrencyAmount currencyAmountToPay = balance.get(Currency.REAL);
				CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
				CurrencyAmount currencyAmountToBuy = calculateCurrencyAmountToBuy(currencyAmountToPay, currencyAmountUnitPrice);
				Order order = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
						.buying(currencyAmountToBuy).payingUnitPriceOf(currencyAmountUnitPrice).build();
				LOGGER.debug("Order created is " + order);
				executeOrder(order, account, house);
			}
		}
	}

	private void executeOrder(Order order, Account account, House house) {
		house.getOrderExecutor().placeOrder(order, house, account);

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
		MercadoBigDecimal quantity = realBalance.getAmount().divide(currencyAmountUnitPrice.getAmount());
		CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, quantity);
		return currencyAmountToBuy;
	}

	@Override
	protected Property retrieveVariable(String name) {
		return null;
	}

	@Override
	protected void defineVariable(Property variable) {

	}

	@Override
	protected void defineParameter(Property parameter) {

	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		TemporalTicker temporalTicker = house.getTemporalTickers().get(currency);
		MercadoBigDecimal lastPrice = temporalTicker.getLastPrice();
		if (lastPrice.compareTo(MercadoBigDecimal.NOT_A_NUMBER) == 0 || lastPrice.compareTo(MercadoBigDecimal.ZERO) == 0) {
			lastPrice = temporalTicker.getPreviousLastPrice();
		}
		CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(Currency.REAL, lastPrice);
		return currencyAmountUnitPrice;
	}
}
