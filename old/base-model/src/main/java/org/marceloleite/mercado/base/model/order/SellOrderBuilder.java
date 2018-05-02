package org.marceloleite.mercado.base.model.order;

import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.OrderType;

public class SellOrderBuilder {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(SellOrderBuilder.class);

	private ZonedDateTime time;

	private Currency secondCurrency;

	private Currency firstCurrency;

	private MercadoBigDecimal quantity;

	private MercadoBigDecimal limitPrice;

	public SellOrderBuilder() {
		this.firstCurrency = Currency.REAL;
	}

	public SellOrderBuilder selling(CurrencyAmount currencyAmountToSell) {
		this.secondCurrency = currencyAmountToSell.getCurrency();
		this.quantity = currencyAmountToSell.getAmount();
		return this;
	}

	public SellOrderBuilder receivingUnitPriceOf(CurrencyAmount currencyAmountUnitPrice) {
		this.firstCurrency = currencyAmountUnitPrice.getCurrency();
		this.limitPrice = currencyAmountUnitPrice.getAmount();
		return this;
	}

	public SellOrderBuilder toExecuteOn(ZonedDateTime time) {
		this.time = time;
		return this;
	}

	public Order build() {
		checkValues();
		checkRules();

		ZonedDateTime time = ZonedDateTime.from(this.time);
		this.time = null;

		Currency secondCurrency = this.secondCurrency;
		this.secondCurrency = null;

		MercadoBigDecimal quantity = new MercadoBigDecimal(this.quantity.toString());
		this.quantity = null;

		Currency firstCurrency = this.firstCurrency;

		MercadoBigDecimal limitPrice = new MercadoBigDecimal(this.limitPrice.toString());
		this.limitPrice = null;

		return new Order(firstCurrency, secondCurrency, OrderType.SELL, quantity, limitPrice, time);
	}

	private void checkRules() {
		if (quantity != null) {
			CurrencyAmount currencyAmountToSell = new CurrencyAmount(secondCurrency, quantity);
			if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmountToSell)) {
				CurrencyAmount minimalAmount = new CurrencyAmount(secondCurrency,
						MinimalAmounts.retrieveMinimalAmountFor(secondCurrency));
				throw new RuntimeException("The amount of " + currencyAmountToSell
						+ " to sell is inferior to the limit amount of " + minimalAmount + ".");
			}
		}

		if (limitPrice != null) {
			CurrencyAmount currencyAmountToReceive = new CurrencyAmount(firstCurrency, limitPrice);
			if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmountToReceive)) {
				CurrencyAmount minimalAmount = new CurrencyAmount(firstCurrency,
						MinimalAmounts.retrieveMinimalAmountFor(firstCurrency));
				throw new RuntimeException("The amount of " + currencyAmountToReceive
						+ " to receive is inferior to the limit amount of " + minimalAmount + ".");
			}
		}
	}

	private void checkValues() {
		if (time == null) {
			throw new RuntimeException("Execution time for operation was not informed.");
		}

		if (secondCurrency == null) {
			throw new RuntimeException("Currency to sell was not informed.");
		}

		if (firstCurrency == null) {
			throw new RuntimeException("Currency to receive was not informed.");
		}

		if (quantity == null && limitPrice == null) {
			throw new RuntimeException("Neither the amount of " + secondCurrency + " to sell nor the amount of "
					+ firstCurrency + " to receive were informed.");
		}
	}
}
