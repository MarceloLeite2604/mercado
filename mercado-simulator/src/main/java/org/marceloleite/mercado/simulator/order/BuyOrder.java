package org.marceloleite.mercado.simulator.order;

import java.time.ZonedDateTime;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.structure.BuyOrderData;
import org.marceloleite.mercado.simulator.temporalcontroller.AbstractTimedObject;

public class BuyOrder extends AbstractTimedObject {

	private ZonedDateTime time;

	private CurrencyAmount currencyAmountToBuy;

	private CurrencyAmount currencyAmountToPay;

	public BuyOrder() {
		this(null, null, null, null, null);
	}

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
	}

	public BuyOrder(ZonedDateTime time, Currency currencyToBuy, Double amountToBuy, Currency currencyToPay) {
		this(time, currencyToBuy, amountToBuy, currencyToPay, null);
	}

	public BuyOrder(ZonedDateTime time, Currency currencyToBuy, Currency currencyToPay, Double amountToPay) {
		this(time, currencyToBuy, null, currencyToPay, amountToPay);
	}

	public BuyOrder(ZonedDateTime time, CurrencyAmount currencyAmountToBuy, CurrencyAmount currencyAmountToPay) {
		this(time, currencyAmountToBuy.getCurrency(), currencyAmountToBuy.getAmount(),
				currencyAmountToPay.getCurrency(), currencyAmountToPay.getAmount());
	}

	public BuyOrder(BuyOrderData buyOrderData) {
		this(buyOrderData.getTime(), new CurrencyAmount(buyOrderData.getCurrencyAmountToBuy()),
				new CurrencyAmount(buyOrderData.getCurrencyAmountToPay()));
	}

	@Override
	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	public CurrencyAmount getCurrencyAmountToBuy() {
		return currencyAmountToBuy;
	}

	public void setCurrencyAmountToBuy(CurrencyAmount currencyAmountToBuy) {
		this.currencyAmountToBuy = currencyAmountToBuy;
	}

	public CurrencyAmount getCurrencyAmountToPay() {
		return currencyAmountToPay;
	}

	public void setCurrencyAmountToPay(CurrencyAmount currencyAmountToPay) {
		this.currencyAmountToPay = currencyAmountToPay;
	}

	public void updateOrder(Map<Currency, TemporalTickerPO> temporalTickers) {
		TemporalTickerPO temporalTickerPO = temporalTickers.get(currencyAmountToBuy.getCurrency());
		double buyingPrice = temporalTickerPO.getBuy();
		if (getCurrencyAmountToBuy().getAmount() == null) {
			Double amountToBuy = getCurrencyAmountToPay().getAmount() / buyingPrice;
			currencyAmountToBuy.setAmount(amountToBuy);

		} else if (getCurrencyAmountToPay().getAmount() == null) {
			Double amountToPay = getCurrencyAmountToBuy().getAmount() * buyingPrice;
			currencyAmountToPay.setAmount(amountToPay);
		}
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
