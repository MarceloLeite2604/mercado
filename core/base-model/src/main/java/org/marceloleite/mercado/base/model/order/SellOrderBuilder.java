package org.marceloleite.mercado.base.model.order;

import java.time.ZonedDateTime;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.temporalcontroller.AbstractTimedObject;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;
import org.marceloleite.mercado.data.SellOrderData;
import org.marceloleite.mercado.data.TemporalTicker;

public class SellOrderBuilder {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(SellOrderBuilder.class);

	private ZonedDateTime time;

	private Currency currencyToSell;

	private Currency currencyToReceive;

	private Double amountToSell;

	private Double amountToReceive;

	public SellOrderBuilder() {
		this.currencyToReceive = Currency.REAL;
	}

	public SellOrderBuilder selling(CurrencyAmount currencyAmountToSell) {
		this.currencyToSell = currencyAmountToSell.getCurrency();
		this.amountToSell = currencyAmountToSell.getAmount();
		return this;
	}

	public SellOrderBuilder receiving(CurrencyAmount currencyAmountToReceive) {
		this.currencyToReceive = currencyAmountToReceive.getCurrency();
		this.amountToReceive = currencyAmountToReceive.getAmount();
		return this;
	}

	public SellOrderBuilder toExecuteOn(ZonedDateTime time) {
		this.time = time;
		return this;
	}

	public SellOrder build() {
		checkValues();
		checkRules();

		ZonedDateTime time = ZonedDateTime.from(this.time);
		this.time = null;

		Currency currencyToSell = this.currencyToSell;
		this.currencyToSell = null;

		Double amountToSell = this.amountToSell;
		this.amountToSell = null;

		Currency currencyToReceive = this.currencyToReceive;

		Double amountToReceive = this.amountToReceive;
		this.amountToReceive = null;

		return new SellOrder(time, currencyToSell, amountToSell, currencyToReceive, amountToReceive);
	}

	private void checkRules() {
		if (amountToSell != null) {
			CurrencyAmount currencyAmountToSell = new CurrencyAmount(currencyToSell, amountToSell);
			if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmountToSell)) {
				CurrencyAmount minimalAmount = new CurrencyAmount(currencyToSell,
						MinimalAmounts.retrieveMinimalAmountFor(currencyToSell));
				throw new RuntimeException("The amount of " + currencyAmountToSell
						+ " to sell is inferior to the limit amount of " + minimalAmount + ".");
			}
		}

		if (amountToReceive != null) {
			CurrencyAmount currencyAmountToReceive = new CurrencyAmount(currencyToReceive, amountToReceive);
			if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmountToReceive)) {
				CurrencyAmount minimalAmount = new CurrencyAmount(currencyToReceive,
						MinimalAmounts.retrieveMinimalAmountFor(currencyToReceive));
				throw new RuntimeException("The amount of " + currencyAmountToReceive
						+ " to receive is inferior to the limit amount of " + minimalAmount + ".");
			}
		}
	}

	private void checkValues() {
		if (time == null) {
			throw new RuntimeException("Execution time for operation was not informed.");
		}

		if (currencyToSell == null) {
			throw new RuntimeException("Currency to sell was not informed.");
		}

		if (currencyToReceive == null) {
			throw new RuntimeException("Currency to receive was not informed.");
		}

		if (amountToSell == null && amountToReceive == null) {
			throw new RuntimeException("Neither the amount of " + currencyToSell + " to sell nor the amount of "
					+ currencyToReceive + " to receive were informed.");
		}
	}

	public static class SellOrder extends AbstractTimedObject {

		private static final Logger LOGGER = LogManager.getLogger(SellOrder.class);

		private ZonedDateTime time;

		private CurrencyAmount currencyAmountToSell;

		private CurrencyAmount currencyAmountToReceive;

		private OrderStatus orderStatus;

		private SellOrder(ZonedDateTime time, Currency currencyToSell, Double amountToBuy, Currency currencyToReceive,
				Double amountToReceive) {
			super();
			this.time = time;
			if (!currencyToSell.isDigital()) {
				throw new IllegalArgumentException(
						"Currency to sell must be digital. " + currencyToSell.getAcronym() + " is not digital.");
			}

			if (currencyToReceive.isDigital()) {
				throw new IllegalArgumentException(
						"Currency to receive cannot be digital. " + currencyToReceive.getAcronym() + " is digital.");
			}
			this.currencyAmountToSell = new CurrencyAmount(currencyToSell, amountToBuy);
			this.currencyAmountToReceive = new CurrencyAmount(currencyToReceive, amountToReceive);
			this.orderStatus = OrderStatus.CREATED;
		}

		private SellOrder(ZonedDateTime time, CurrencyAmount currencyAmountToSell,
				CurrencyAmount currencyAmountToReceive) {
			this(time, currencyAmountToSell.getCurrency(), currencyAmountToSell.getAmount(),
					currencyAmountToReceive.getCurrency(), currencyAmountToReceive.getAmount());
		}

		public SellOrder(SellOrderData sellOrderData) {
			this(sellOrderData.getTime(), new CurrencyAmount(sellOrderData.getCurrencyAmountToSell()),
					new CurrencyAmount(sellOrderData.getCurrencyAmountToReceive()));
		}

		@Override
		public ZonedDateTime getTime() {
			return time;
		}

		public CurrencyAmount getCurrencyAmountToSell() {
			return currencyAmountToSell;
		}

		public CurrencyAmount getCurrencyAmountToReceive() {
			return currencyAmountToReceive;
		}

		public OrderStatus getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(OrderStatus orderStatus) {
			this.orderStatus = orderStatus;
		}

		public void updateOrder(Map<Currency, TemporalTicker> temporalTickers) {
			double sellingPrice = retrieveSellingPrice(temporalTickers);
			if (currencyAmountToSell.getAmount() == null) {
				Double amountToSell = currencyAmountToReceive.getAmount() / sellingPrice;
				currencyAmountToSell.setAmount(amountToSell);

			} else if (currencyAmountToReceive.getAmount() == null) {
				Double amountToPay = currencyAmountToSell.getAmount() * sellingPrice;
				currencyAmountToReceive.setAmount(amountToPay);
			}
		}

		private double retrieveSellingPrice(Map<Currency, TemporalTicker> temporalTickers) {
			Currency currency = currencyAmountToSell.getCurrency();
			TemporalTicker temporalTicker = temporalTickers.get(currency);
			if (temporalTicker == null) {
				throw new RuntimeException(
						"No temporal ticker while retrieving selling price for " + currency + " currency.");
			}

			double sellingPrice = temporalTicker.getSell();
			if (sellingPrice == 0.0) {
				sellingPrice = temporalTicker.getPreviousSell();
				if (sellingPrice == 0.0) {
					TimeInterval timeInterval = new TimeInterval(temporalTicker.getStart(),
							temporalTicker.getEnd());
					throw new RuntimeException("Selling price informed on period " + timeInterval + " is zero.");
				}
			}
			LOGGER.debug("Selling price is " + new DigitalCurrencyFormatter().format(sellingPrice));
			return sellingPrice;
		}

		@Override
		public String toString() {
			String result = "sell order ";
			if (currencyAmountToSell.getAmount() != null) {
				result += "of " + currencyAmountToSell + " receiving ";
				if (currencyAmountToReceive.getAmount() != null) {
					result += currencyAmountToReceive;
				} else {
					result += currencyAmountToReceive.getCurrency() + " currency";
				}

			} else {
				result += "receiving " + currencyAmountToReceive + " by selling " + currencyAmountToSell.getCurrency()
						+ " currency";
			}
			return result;
		}
	}
}
