package org.marceloleite.mercado.simulator.conversor;

import java.time.LocalDateTime;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class BuyOrder {

	private LocalDateTime time;

	private CurrencyAmount currencyAmountToBuy;

	private CurrencyAmount currencyAmountToPay;

	public BuyOrder() {
		this(null, null, null, null, null);
	}

	private BuyOrder(LocalDateTime time, Currency currencyToBuy, Double amountToBuy, Currency currencyToPay,
			Double amountToPay) {
		super();
		this.time = time;
		this.currencyAmountToBuy = new CurrencyAmount(currencyToBuy, amountToBuy);
		this.currencyAmountToPay = new CurrencyAmount(currencyToPay, amountToPay);
	}

	public BuyOrder(LocalDateTime time, Currency currencyToBuy, Double amountToBuy, Currency currencyToPay) {
		this(time, currencyToBuy, amountToBuy, currencyToPay, null);
	}

	public BuyOrder(LocalDateTime time, Currency currencyToBuy, Currency currencyToPay, Double amountToPay) {
		this(time, currencyToBuy, null, currencyToPay, amountToPay);
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
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
}
