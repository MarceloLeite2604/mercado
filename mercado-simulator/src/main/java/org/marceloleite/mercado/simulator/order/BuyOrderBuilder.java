package org.marceloleite.mercado.simulator.order;

import java.time.ZonedDateTime;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.DigitalCurrencyFormatter;
import org.marceloleite.mercado.database.data.structure.TemporalTickerDataModel;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.data.BuyOrderData;
import org.marceloleite.mercado.simulator.temporalcontroller.AbstractTimedObject;

public class BuyOrderBuilder {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(BuyOrderBuilder.class);

	private ZonedDateTime time;

	private Currency currencyToBuy;

	private Currency currencyToPay;

	private Double amountToBuy;

	private Double amountToPay;

	public BuyOrderBuilder() {
		this.currencyToPay = Currency.REAL;
	}

	public BuyOrderBuilder buyingCurrency(Currency currencyToBuy) {
		this.currencyToBuy = currencyToBuy;
		return this;
	}

	public BuyOrderBuilder buying(CurrencyAmount currencyAmountToBuy) {
		this.currencyToBuy = currencyAmountToBuy.getCurrency();
		this.amountToBuy = currencyAmountToBuy.getAmount();
		return this;
	}

	public BuyOrderBuilder paying(CurrencyAmount currencyAmountToPay) {
		this.currencyToPay = currencyAmountToPay.getCurrency();
		this.amountToPay = currencyAmountToPay.getAmount();
		return this;
	}

	public BuyOrderBuilder toExecuteOn(ZonedDateTime time) {
		this.time = time;
		return this;
	}

	public BuyOrder build() {
		checkValues();
		checkRules();

		ZonedDateTime time = ZonedDateTime.from(this.time);
		this.time = null;

		Currency currencyToBuy = this.currencyToBuy;
		this.currencyToBuy = null;

		Double amountToBuy = this.amountToBuy;
		this.amountToBuy = null;

		Currency currencyToPay = this.currencyToPay;

		Double amountToPay = this.amountToPay;
		this.amountToPay = null;

		return new BuyOrder(time, currencyToBuy, amountToBuy, currencyToPay, amountToPay);
	}

	private void checkRules() {
		if (amountToBuy != null) {
			CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currencyToBuy, amountToBuy);
			if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmountToBuy)) {
				CurrencyAmount minimalAmount = new CurrencyAmount(currencyToBuy,
						MinimalAmounts.retrieveMinimalAmountFor(currencyToBuy));
				throw new RuntimeException("The amount of " + currencyAmountToBuy
						+ " to buy is inferior to the limit amount of " + minimalAmount + ".");
			}
		}

		if (amountToPay != null) {
			CurrencyAmount currencyAmountToPay = new CurrencyAmount(currencyToPay, amountToPay);
			if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmountToPay)) {
				CurrencyAmount minimalAmount = new CurrencyAmount(currencyToPay,
						MinimalAmounts.retrieveMinimalAmountFor(currencyToPay));
				throw new RuntimeException("The amount of " + currencyAmountToPay
						+ " to pay is inferior to the limit amount of " + minimalAmount + ".");
			}
		}
	}

	private void checkValues() {
		if (time == null) {
			throw new RuntimeException("Execution time for operation was not informed.");
		}

		if (currencyToBuy == null) {
			throw new RuntimeException("Currency to buy was not informed.");
		}

		if (currencyToPay == null) {
			throw new RuntimeException("Currency to pay was not informed.");
		}

		if (amountToBuy == null && amountToPay == null) {
			throw new RuntimeException("Neither the amount of " + currencyToBuy + " to buy nor the amount of "
					+ currencyToPay + " to pay were informed.");
		}
	}

	public static class BuyOrder extends AbstractTimedObject {

		private static final Logger LOGGER = LogManager.getLogger(BuyOrder.class);

		private ZonedDateTime time;

		private CurrencyAmount currencyAmountToBuy;

		private CurrencyAmount currencyAmountToPay;

		private OrderStatus orderStatus;

		private BuyOrder(ZonedDateTime time, Currency currencyToBuy, Double amountToBuy, Currency currencyToPay,
				Double amountToPay) {
			super();
			this.time = time;
			if (!currencyToBuy.isDigital()) {
				throw new IllegalArgumentException(
						"Currency to buy must be digital. " + currencyToBuy.getAcronym() + " is not digital.");
			}

			if (currencyToPay.isDigital()) {
				throw new IllegalArgumentException(
						"Currency to pay cannot be digital. " + currencyToPay.getAcronym() + " is digital.");
			}
			this.currencyAmountToBuy = new CurrencyAmount(currencyToBuy, amountToBuy);
			this.currencyAmountToPay = new CurrencyAmount(currencyToPay, amountToPay);
			this.orderStatus = OrderStatus.CREATED;
		}

		private BuyOrder(ZonedDateTime time, CurrencyAmount currencyAmountToBuy, CurrencyAmount currencyAmountToPay) {
			this(time, currencyAmountToBuy.getCurrency(), currencyAmountToBuy.getAmount(),
					currencyAmountToPay.getCurrency(), currencyAmountToPay.getAmount());
		}

		private BuyOrder(BuyOrderData buyOrderData) {
			this(buyOrderData.getTime(), new CurrencyAmount(buyOrderData.getCurrencyAmountToBuy()),
					new CurrencyAmount(buyOrderData.getCurrencyAmountToPay()));
		}

		@Override
		public ZonedDateTime getTime() {
			return time;
		}

		public CurrencyAmount getCurrencyAmountToBuy() {
			return currencyAmountToBuy;
		}

		public CurrencyAmount getCurrencyAmountToPay() {
			return currencyAmountToPay;
		}

		public OrderStatus getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(OrderStatus orderStatus) {
			this.orderStatus = orderStatus;
		}

		public void updateOrder(Map<Currency, TemporalTickerDataModel> temporalTickers) {
			double buyingPrice;
			buyingPrice = retrieveBuyingPrice(temporalTickers);
			if (currencyAmountToBuy.getAmount() == null) {
				Double amountToBuy = currencyAmountToPay.getAmount() / buyingPrice;
				currencyAmountToBuy.setAmount(amountToBuy);

			} else if (currencyAmountToPay.getAmount() == null) {
				Double amountToPay = currencyAmountToBuy.getAmount() * buyingPrice;
				currencyAmountToPay.setAmount(amountToPay);
			}
		}

		private double retrieveBuyingPrice(Map<Currency, TemporalTickerDataModel> temporalTickers) {
			Currency currency = currencyAmountToBuy.getCurrency();
			TemporalTickerDataModel temporalTickerDataModel = temporalTickers.get(currency);
			if (temporalTickerDataModel == null) {
				throw new RuntimeException(
						"No temporal ticker while retrieving buying price for " + currency + " currency.");
			}

			double buyingPrice = temporalTickerDataModel.getBuy();
			if (buyingPrice == 0.0) {
				buyingPrice = temporalTickerDataModel.getPreviousBuy();
				if (buyingPrice == 0.0) {
					TimeInterval timeInterval = new TimeInterval(temporalTickerDataModel.getStart(),
							temporalTickerDataModel.getEnd());
					throw new RuntimeException("Buying price informed on period " + timeInterval + " is zero.");
				}
			}
			LOGGER.debug("Buying price is " + new DigitalCurrencyFormatter().format(buyingPrice));
			return buyingPrice;
		}

		@Override
		public String toString() {
			String result = "buy order ";
			if (currencyAmountToBuy.getAmount() != null) {
				result += "of " + currencyAmountToBuy + " using ";
				if (currencyAmountToPay.getAmount() != null) {
					result += currencyAmountToPay;
				} else {
					result += currencyAmountToPay.getCurrency();
				}

			} else {
				result += "paying " + currencyAmountToPay + " to buy " + currencyAmountToBuy.getCurrency();
			}
			return result;
		}
	}
}
