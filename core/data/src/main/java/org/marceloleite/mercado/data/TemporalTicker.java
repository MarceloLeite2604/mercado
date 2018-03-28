package org.marceloleite.mercado.data;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;

public class TemporalTicker {

	private Currency currency;

	private ZonedDateTime start;

	private ZonedDateTime end;

	private Long orders;

	private Long buyOrders;

	private Long sellOrders;

	private MercadoBigDecimal buy;

	private MercadoBigDecimal previousBuy;

	private MercadoBigDecimal sell;

	private MercadoBigDecimal previousSell;

	private MercadoBigDecimal lastPrice;

	private MercadoBigDecimal previousLastPrice;

	private MercadoBigDecimal firstPrice;

	private MercadoBigDecimal highestPrice;

	private MercadoBigDecimal lowestPrice;

	private MercadoBigDecimal averagePrice;

	private Duration timeDuration;

	private MercadoBigDecimal volumeTrades;

	public TemporalTicker() {
		super();
	}

	private TemporalTicker(Currency currency, ZonedDateTime start, ZonedDateTime end, Long orders, Long buyOrders,
			Long sellOrders, MercadoBigDecimal buy, MercadoBigDecimal previousBuy, MercadoBigDecimal sell,
			MercadoBigDecimal previousSell, MercadoBigDecimal lastPrice, MercadoBigDecimal previousLastPrice,
			MercadoBigDecimal firstPrice, MercadoBigDecimal highestPrice, MercadoBigDecimal lowestPrice,
			MercadoBigDecimal averagePrice, Duration timeDuration, MercadoBigDecimal volumeTrades) {
		super();
		this.currency = currency;
		this.start = start;
		this.end = end;
		this.orders = orders;
		this.buyOrders = buyOrders;
		this.sellOrders = sellOrders;
		this.buy = buy;
		this.previousBuy = previousBuy;
		this.sell = sell;
		this.previousSell = previousSell;
		this.lastPrice = lastPrice;
		this.previousLastPrice = previousLastPrice;
		this.firstPrice = firstPrice;
		this.highestPrice = highestPrice;
		this.lowestPrice = lowestPrice;
		this.averagePrice = averagePrice;
		this.timeDuration = timeDuration;
		this.volumeTrades = volumeTrades;
	}

	public TemporalTicker(TemporalTicker temporalTicker) {
		this(temporalTicker.getCurrency(), temporalTicker.getStart(), temporalTicker.getEnd(),
				temporalTicker.getOrders(), temporalTicker.getBuyOrders(), temporalTicker.getSellOrders(),
				temporalTicker.getBuy(), temporalTicker.getPreviousBuy(), temporalTicker.getSell(),
				temporalTicker.getPreviousSell(), temporalTicker.getLastPrice(), temporalTicker.getPreviousLastPrice(),
				temporalTicker.getFirstPrice(), temporalTicker.getHighestPrice(), temporalTicker.getLowestPrice(),
				temporalTicker.getAveragePrice(), temporalTicker.getTimeDuration(), temporalTicker.getVolumeTrades());
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public ZonedDateTime getStart() {
		return start;
	}

	public void setStart(ZonedDateTime start) {
		this.start = start;
	}

	public ZonedDateTime getEnd() {
		return end;
	}

	public void setEnd(ZonedDateTime end) {
		this.end = end;
	}

	public Long getOrders() {
		return orders;
	}

	public void setOrders(Long orders) {
		this.orders = orders;
	}

	public Long getBuyOrders() {
		return buyOrders;
	}

	public void setBuyOrders(Long buyOrders) {
		this.buyOrders = buyOrders;
	}

	public Long getSellOrders() {
		return sellOrders;
	}

	public void setSellOrders(Long sellOrders) {
		this.sellOrders = sellOrders;
	}

	public MercadoBigDecimal getBuy() {
		return buy;
	}

	public void setBuy(MercadoBigDecimal buy) {
		this.buy = buy;
	}

	public MercadoBigDecimal getPreviousBuy() {
		return previousBuy;
	}

	public void setPreviousBuy(MercadoBigDecimal previousBuy) {
		this.previousBuy = previousBuy;
	}

	public MercadoBigDecimal getSell() {
		return sell;
	}

	public void setSell(MercadoBigDecimal sell) {
		this.sell = sell;
	}

	public MercadoBigDecimal getPreviousSell() {
		return previousSell;
	}

	public void setPreviousSell(MercadoBigDecimal previousSell) {
		this.previousSell = previousSell;
	}

	public MercadoBigDecimal getLastPrice() {
		return lastPrice;
	}

	public MercadoBigDecimal retrieveCurrentOrPreviousLastPrice() {
		if (lastPrice != null && lastPrice.compareTo(MercadoBigDecimal.ZERO) != 0) {
			return lastPrice;
		} else {
			return previousLastPrice;
		}
	}

	public void setLastPrice(MercadoBigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	public MercadoBigDecimal getPreviousLastPrice() {
		return previousLastPrice;
	}

	public void setPreviousLastPrice(MercadoBigDecimal previousLastPrice) {
		this.previousLastPrice = previousLastPrice;
	}

	public MercadoBigDecimal getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(MercadoBigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	public MercadoBigDecimal getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(MercadoBigDecimal highestPrice) {
		this.highestPrice = highestPrice;
	}

	public MercadoBigDecimal getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(MercadoBigDecimal lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public MercadoBigDecimal getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(MercadoBigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}

	public Duration getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(Duration timeDuration) {
		this.timeDuration = timeDuration;
	}

	public MercadoBigDecimal getVolumeTrades() {
		return volumeTrades;
	}

	public void setVolumeTrades(MercadoBigDecimal volumeTrades) {
		this.volumeTrades = volumeTrades;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemporalTicker other = (TemporalTicker) obj;
		if (currency != other.currency)
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		DigitalCurrencyFormatter digitalCurrencyFormatter = new DigitalCurrencyFormatter();

		stringBuilder.append("[ ");
		stringBuilder.append(currency);
		stringBuilder.append(", " + new TimeInterval(start, end));
		stringBuilder.append(", orders: " + orders);
		stringBuilder.append(", buy orders: " + buyOrders);
		stringBuilder.append(", sell orders: " + sellOrders);
		stringBuilder.append(", volume: " + volumeTrades);

		MercadoBigDecimal buy = (this.buy != null ? this.buy : this.previousBuy);
		stringBuilder.append(", buy: " + digitalCurrencyFormatter.format(buy));

		MercadoBigDecimal sell = (this.sell != null ? this.sell : this.previousSell);
		stringBuilder.append(", sell: " + digitalCurrencyFormatter.format(sell));

		MercadoBigDecimal lastPrice = (this.lastPrice != null ? this.lastPrice : this.previousLastPrice);
		stringBuilder.append(", last: " + digitalCurrencyFormatter.format(lastPrice));

		stringBuilder.append(", first: " + digitalCurrencyFormatter.format(firstPrice));
		stringBuilder.append(", high: " + digitalCurrencyFormatter.format(highestPrice));
		stringBuilder.append(", low: " + digitalCurrencyFormatter.format(lowestPrice));
		stringBuilder.append(", average: " + digitalCurrencyFormatter.format(averagePrice));
		stringBuilder.append(" ]");

		return stringBuilder.toString();
	}
}
