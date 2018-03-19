package org.marceloleite.mercado.strategies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.data.TemporalTicker;

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
			CurrencyAmount realBalance = account.getBalance().get(Currency.REAL);
			if (realBalance.getAmount() > 0.000000001) {
				CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
				CurrencyAmount currencyAmountToBuy = calculateCurrencyAmountToBuy(realBalance, currencyAmountUnitPrice);
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
		Double quantity = realBalance.getAmount() / currencyAmountUnitPrice.getAmount();
		Double restoredValue = quantity * currencyAmountUnitPrice.getAmount();
		Double difference = restoredValue - realBalance.getAmount();
		if ( difference > 0 ) {
			quantity -= difference/currencyAmountUnitPrice.getAmount(); 
		}
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
		Double lastPrice = temporalTicker.getLastPrice();
		if (lastPrice == null || lastPrice == 0.0) {
			lastPrice = temporalTicker.getPreviousLastPrice();
		}
		CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(Currency.REAL, lastPrice);
		return currencyAmountUnitPrice;
	}
}
