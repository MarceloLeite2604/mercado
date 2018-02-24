package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.Currency;

public class TemporalTicker {

	private Currency currency;

	private ZonedDateTime start;

	private ZonedDateTime end;

	private Long orders;

	private Long buyOrders;

	private Long sellOrders;

	private Double buy;

	private Double previousBuy;

	private Double sell;

	private Double previousSell;

	private Double lastPrice;

	private Double previousLastPrice;

	private Double firstPrice;

	private Double highestPrice;

	private Double lowestPrice;

	private Double averagePrice;

	private Duration timeDuration;

	private Double volumeTrades;

	public TemporalTicker() {
		super();
	}

	private TemporalTicker(Currency currency, ZonedDateTime start, ZonedDateTime end, Long orders,
			Long buyOrders, Long sellOrders, Double buy, Double previousBuy, Double sell, Double previousSell,
			Double lastPrice, Double previousLastPrice, Double firstPrice, Double highestPrice, Double lowestPrice,
			Double averagePrice, Duration timeDuration, Double volumeTrades) {
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
		this(temporalTicker.getCurrency(), temporalTicker.getStart(),
				temporalTicker.getEnd(), temporalTicker.getOrders(),
				temporalTicker.getBuyOrders(), temporalTicker.getSellOrders(),
				temporalTicker.getBuy(), temporalTicker.getPreviousBuy(),
				temporalTicker.getSell(), temporalTicker.getPreviousSell(),
				temporalTicker.getLastPrice(), temporalTicker.getPreviousLastPrice(),
				temporalTicker.getFirstPrice(), temporalTicker.getHighestPrice(),
				temporalTicker.getLowestPrice(), temporalTicker.getAveragePrice(),
				temporalTicker.getTimeDuration(), temporalTicker.getVolumeTrades());
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

	public Double getBuy() {
		return buy;
	}

	public void setBuy(Double buy) {
		this.buy = buy;
	}

	public Double getPreviousBuy() {
		return previousBuy;
	}

	public void setPreviousBuy(Double previousBuy) {
		this.previousBuy = previousBuy;
	}

	public Double getSell() {
		return sell;
	}

	public void setSell(Double sell) {
		this.sell = sell;
	}

	public Double getPreviousSell() {
		return previousSell;
	}

	public void setPreviousSell(Double previousSell) {
		this.previousSell = previousSell;
	}

	public Double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(Double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public Double getPreviousLastPrice() {
		return previousLastPrice;
	}

	public void setPreviousLastPrice(Double previousLastPrice) {
		this.previousLastPrice = previousLastPrice;
	}

	public Double getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(Double firstPrice) {
		this.firstPrice = firstPrice;
	}

	public Double getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(Double highestPrice) {
		this.highestPrice = highestPrice;
	}

	public Double getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(Double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public Double getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(Double averagePrice) {
		this.averagePrice = averagePrice;
	}

	public Duration getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(Duration timeDuration) {
		this.timeDuration = timeDuration;
	}

	public Double getVolumeTrades() {
		return volumeTrades;
	}

	public void setVolumeTrades(Double volumeTrades) {
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
}
