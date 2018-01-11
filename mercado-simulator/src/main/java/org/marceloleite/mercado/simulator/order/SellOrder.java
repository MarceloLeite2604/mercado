package org.marceloleite.mercado.simulator.order;

import java.time.LocalDateTime;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.structure.SellOrderData;
import org.marceloleite.mercado.simulator.temporalcontroller.AbstractTimedObject;

public class SellOrder extends AbstractTimedObject {

	private LocalDateTime time;

	private CurrencyAmount currencyAmountToSell;

	private CurrencyAmount currencyAmountToReceive;

	public SellOrder() {
		this(null, null, null, null, null);
	}

	private SellOrder(LocalDateTime time, Currency currencyToSell, Double amountToBuy, Currency currencyToReceive,
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
	}

	public SellOrder(LocalDateTime time, Currency currencyToSell, Double amountToSell, Currency currencyToReceive) {
		this(time, currencyToSell, amountToSell, currencyToReceive, null);
	}

	public SellOrder(LocalDateTime time, Currency currencyToSell, Currency currencyToReceive, Double amountToReceive) {
		this(time, currencyToSell, null, currencyToReceive, amountToReceive);
	}

	public SellOrder(LocalDateTime time, CurrencyAmount currencyAmountToSell, CurrencyAmount currencyAmountToReceive) {
		this(time, currencyAmountToSell.getCurrency(), currencyAmountToSell.getAmount(),
				currencyAmountToReceive.getCurrency(), currencyAmountToReceive.getAmount());
	}

	public SellOrder(SellOrderData sellOrderData) {
		this(sellOrderData.getTime(), new CurrencyAmount(sellOrderData.getCurrencyAmountToSell()),
				new CurrencyAmount(sellOrderData.getCurrencyAmountToReceive()));
	}

	@Override
	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public CurrencyAmount getCurrencyAmountToSell() {
		return currencyAmountToSell;
	}

	public void setCurrencyAmountToSell(CurrencyAmount currencyAmountToSell) {
		this.currencyAmountToSell = currencyAmountToSell;
	}

	public CurrencyAmount getCurrencyAmountToReceive() {
		return currencyAmountToReceive;
	}

	public void setCurrencyAmountToReceive(CurrencyAmount currencyAmountToReceive) {
		this.currencyAmountToReceive = currencyAmountToReceive;
	}

	public void updateOrder(Map<Currency, TemporalTickerPO> temporalTickers) {
		TemporalTickerPO temporalTickerPO = temporalTickers.get(currencyAmountToSell.getCurrency());
		double sellingPrice = temporalTickerPO.getSell();
		if (getCurrencyAmountToSell().getAmount() == null) {
			Double amountToBuy = getCurrencyAmountToReceive().getAmount() / sellingPrice;
			currencyAmountToSell.setAmount(amountToBuy);

		} else if (getCurrencyAmountToReceive().getAmount() == null) {
			Double amountToPay = getCurrencyAmountToSell().getAmount() * sellingPrice;
			currencyAmountToReceive.setAmount(amountToPay);
		}
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
