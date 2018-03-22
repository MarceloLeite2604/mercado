package org.marceloleite.mercado.data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;

public class TemporalTicker {

	private Currency currency;

	private ZonedDateTime start;

	private ZonedDateTime end;

	private Long orders;

	private Long buyOrders;

	private Long sellOrders;

	private BigDecimal buy;

	private BigDecimal previousBuy;

	private BigDecimal sell;

	private BigDecimal previousSell;

	private BigDecimal lastPrice;

	private BigDecimal previousLastPrice;

	private BigDecimal firstPrice;

	private BigDecimal highestPrice;

	private BigDecimal lowestPrice;

	private BigDecimal averagePrice;

	private Duration timeDuration;

	private BigDecimal volumeTrades;

	public TemporalTicker() {
		super();
	}

	private TemporalTicker(Currency currency, ZonedDateTime start, ZonedDateTime end, Long orders, Long buyOrders,
			Long sellOrders, BigDecimal buy, BigDecimal previousBuy, BigDecimal sell, BigDecimal previousSell,
			BigDecimal lastPrice, BigDecimal previousLastPrice, BigDecimal firstPrice, BigDecimal highestPrice,
			BigDecimal lowestPrice, BigDecimal averagePrice, Duration timeDuration, BigDecimal volumeTrades) {
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

	public BigDecimal getBuy() {
		return buy;
	}

	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}

	public BigDecimal getPreviousBuy() {
		return previousBuy;
	}

	public void setPreviousBuy(BigDecimal previousBuy) {
		this.previousBuy = previousBuy;
	}

	public BigDecimal getSell() {
		return sell;
	}

	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}

	public BigDecimal getPreviousSell() {
		return previousSell;
	}

	public void setPreviousSell(BigDecimal previousSell) {
		this.previousSell = previousSell;
	}

	public BigDecimal getLastPrice() {
		return lastPrice;
	}

	public BigDecimal getCurrentOrPreviousLastPrice() {
		if (lastPrice != null && lastPrice.compareTo(BigDecimal.ZERO) != 0) {
			return lastPrice;
		} else {
			return previousLastPrice;
		}
	}

	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	public BigDecimal getPreviousLastPrice() {
		return previousLastPrice;
	}

	public void setPreviousLastPrice(BigDecimal previousLastPrice) {
		this.previousLastPrice = previousLastPrice;
	}

	public BigDecimal getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	public BigDecimal getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(BigDecimal highestPrice) {
		this.highestPrice = highestPrice;
	}

	public BigDecimal getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(BigDecimal lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}

	public Duration getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(Duration timeDuration) {
		this.timeDuration = timeDuration;
	}

	public BigDecimal getVolumeTrades() {
		return volumeTrades;
	}

	public void setVolumeTrades(BigDecimal volumeTrades) {
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

		BigDecimal buy = (this.buy != null ? this.buy : this.previousBuy);
		stringBuilder.append(", buy: " + digitalCurrencyFormatter.format(buy));

		BigDecimal sell = (this.sell != null ? this.sell : this.previousSell);
		stringBuilder.append(", sell: " + digitalCurrencyFormatter.format(sell));

		BigDecimal lastPrice = (this.lastPrice != null ? this.lastPrice : this.previousLastPrice);
		stringBuilder.append(", last: " + digitalCurrencyFormatter.format(lastPrice));

		stringBuilder.append(", first: " + digitalCurrencyFormatter.format(firstPrice));
		stringBuilder.append(", high: " + digitalCurrencyFormatter.format(highestPrice));
		stringBuilder.append(", low: " + digitalCurrencyFormatter.format(lowestPrice));
		stringBuilder.append(", average: " + digitalCurrencyFormatter.format(averagePrice));
		stringBuilder.append(" ]");

		return stringBuilder.toString();
	}
}
