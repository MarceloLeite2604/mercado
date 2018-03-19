package org.marceloleite.mercado.base.model.order;

import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;

public class BuyOrderBuilder {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(BuyOrderBuilder.class);

	private ZonedDateTime time;

	private Currency secondCurrency;

	private Currency firstCurrency;

	private Double quantity;

	private Double limitPrice;

	public BuyOrderBuilder() {
		this.firstCurrency = Currency.REAL;
	}

	public BuyOrderBuilder buyingCurrency(Currency currencyToBuy) {
		this.secondCurrency = currencyToBuy;
		return this;
	}

	public BuyOrderBuilder buying(CurrencyAmount currencyAmountToBuy) {
		this.secondCurrency = currencyAmountToBuy.getCurrency();
		this.quantity = currencyAmountToBuy.getAmount();
		return this;
	}

	public BuyOrderBuilder payingUnitPriceOf(CurrencyAmount currencyAmountPayingUnitPrice) {
		this.firstCurrency = currencyAmountPayingUnitPrice.getCurrency();
		this.limitPrice = currencyAmountPayingUnitPrice.getAmount();
		return this;
	}

	public BuyOrderBuilder toExecuteOn(ZonedDateTime time) {
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

		Double amountToBuy = this.quantity;
		this.quantity = null;

		Currency firstCurrency = this.firstCurrency;

		Double limitPrice = this.limitPrice;
		this.limitPrice = null;

		return new Order(firstCurrency, secondCurrency, OrderType.BUY, amountToBuy, limitPrice, time);
	}

	private void checkRules() {
		if (quantity != null) {
			CurrencyAmount currencyAmountToBuy = new CurrencyAmount(secondCurrency, quantity);
			if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmountToBuy)) {
				CurrencyAmount minimalAmount = new CurrencyAmount(secondCurrency,
						MinimalAmounts.retrieveMinimalAmountFor(secondCurrency));
				throw new RuntimeException("The amount of " + currencyAmountToBuy
						+ " to buy is inferior to the limit amount of " + minimalAmount + ".");
			}
		}

		if (limitPrice != null) {
			CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(firstCurrency, limitPrice);
			if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmountUnitPrice)) {
				CurrencyAmount minimalAmount = new CurrencyAmount(firstCurrency,
						MinimalAmounts.retrieveMinimalAmountFor(firstCurrency));
				throw new RuntimeException("The amount of " + currencyAmountUnitPrice
						+ " to pay is inferior to the limit amount of " + minimalAmount + ".");
			}
		}
	}

	private void checkValues() {
		if (time == null) {
			throw new RuntimeException("Execution time for operation was not informed.");
		}

		if (secondCurrency == null) {
			throw new RuntimeException("Currency to buy was not informed.");
		}

		if (firstCurrency == null) {
			throw new RuntimeException("Currency to pay was not informed.");
		}

		if (quantity == null) {
			throw new RuntimeException("Quantity of \"" + secondCurrency + "\" to buy was not informed.");
		}

		if (limitPrice == null) {
			throw new RuntimeException(
					"Max \"" + firstCurrency + "\" unit price to buy  \"" + secondCurrency + "\" was not informed.");
		}
	}
}
