package org.marceloleite.mercado.simulator.order;

import java.time.ZonedDateTime;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.structure.SellOrderData;
import org.marceloleite.mercado.simulator.temporalcontroller.AbstractTimedObject;

public class SellOrder extends AbstractTimedObject {

	private ZonedDateTime time;

	private CurrencyAmount currencyAmountToSell;

	private CurrencyAmount currencyAmountToReceive;

	public SellOrder() {
		this(null, null, null, null, null);
	}

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
	}

	public SellOrder(ZonedDateTime time, Currency currencyToSell, Double amountToSell, Currency currencyToReceive) {
		this(time, currencyToSell, amountToSell, currencyToReceive, null);
	}

	public SellOrder(ZonedDateTime time, Currency currencyToSell, Currency currencyToReceive, Double amountToReceive) {
		this(time, currencyToSell, null, currencyToReceive, amountToReceive);
	}

	public SellOrder(ZonedDateTime time, CurrencyAmount currencyAmountToSell, CurrencyAmount currencyAmountToReceive) {
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

	public void setTime(ZonedDateTime time) {
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

	public void updateOrder(Map<Currency, TemporalTickerPO> temporalTickers, TimeInterval timeInterval) {
		double sellingPrice = retrieveSellingPrice(temporalTickers, timeInterval);
		if (getCurrencyAmountToSell().getAmount() == null) {
			Double amountToBuy = getCurrencyAmountToReceive().getAmount() / sellingPrice;
			currencyAmountToSell.setAmount(amountToBuy);

		} else if (getCurrencyAmountToReceive().getAmount() == null) {
			Double amountToPay = getCurrencyAmountToSell().getAmount() * sellingPrice;
			currencyAmountToReceive.setAmount(amountToPay);
		}
	}

	private double retrieveSellingPrice(Map<Currency, TemporalTickerPO> temporalTickers,
			TimeInterval currentTimeInterval) {
		double buyingPrice;
		Currency currency = currencyAmountToSell.getCurrency();
		TemporalTickerPO temporalTickerPO = temporalTickers.get(currency);
		if (temporalTickerPO != null) {
			buyingPrice = temporalTickerPO.getBuy();
		} else {
			TradePO previousTrade = new TradeDAO().retrievePreviousTrade(currency, TradeType.SELL,
					currentTimeInterval.getStart());
			if (previousTrade != null) {
				buyingPrice = previousTrade.getPrice();
			} else {
				TradePO nextTrade = new TradeDAO().retrieveNextTrade(currency, TradeType.SELL,
						currentTimeInterval.getEnd());
				if (nextTrade != null) {
					buyingPrice = nextTrade.getPrice();
				} else {
					throw new RuntimeException(
							"Could not retrieve a selling price for time interval " + currentTimeInterval);
				}
			}
		}
		return buyingPrice;
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
