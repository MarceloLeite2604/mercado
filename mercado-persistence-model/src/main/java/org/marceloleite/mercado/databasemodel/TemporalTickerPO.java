package org.marceloleite.mercado.databasemodel;

import java.time.Duration;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.marceloleite.mercado.commons.util.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.util.converter.DurationToStringConverter;

@Entity(name = "TEMPORAL_TICKERS")
public class TemporalTickerPO implements PersistenceObject<TemporalTickerIdPO> {

	@EmbeddedId
	private TemporalTickerIdPO temporalTickerId;

	@Column(name="ORDERS", nullable = false)
	private long orders;

	@Column(name="HIGHEST_PRICE", nullable = false)
	private double high;

	@Column(name="AVERAGE_PRICE", nullable = false)
	private double average;

	@Column(name="LOWEST_PRICE", nullable = false)
	private double low;

	@Column(name="VOLUME_TRADED", nullable = false)
	private double vol;

	@Column(name="FIRST_PRICE", nullable = false)
	private double first;

	@Column(name="LAST_PRICE", nullable = false)
	private double last;

	@Column(name="PREVIOUS_LAST_PRICE", nullable = false)
	private double previousLast;

	@Column(name="BUY", nullable = false)
	private double buy;

	@Column(name="PREVIOUS_BUY", nullable = false)
	private double previousBuy;

	@Column(name="SELL", nullable = false)
	private double sell;

	@Column(name="PREVIOUS_SELL", nullable = false)
	private double previousSell;

	@Column(name="BUY_ORDERS", nullable = false)
	private long buyOrders;

	@Column(name="SELL_ORDERS", nullable = false)
	private long sellOrders;

	@Column(name="TIME_DURATION", nullable = false)
	private Duration duration;

	public TemporalTickerPO() {
		super();
	}

	private TemporalTickerPO(TemporalTickerIdPO temporalTickerId, long orders, double high, double average, double low,
			double vol, double first, double last, double previousLast, double buy, double previousBuy, double sell,
			double previousSell, long buyOrders, long sellOrders, Duration duration) {
		super();
		this.temporalTickerId = temporalTickerId;
		this.orders = orders;
		this.buyOrders = buyOrders;
		this.sellOrders = sellOrders;
		this.high = high;
		this.average = average;
		this.low = low;
		this.vol = vol;
		this.first = first;
		this.last = last;
		this.previousLast = previousLast;
		this.buy = buy;
		this.previousBuy = previousBuy;
		this.sell = sell;
		this.previousSell = previousSell;
		this.duration = duration;
	}

	public TemporalTickerPO(TemporalTickerPO temporalTickerPO) {
		this(new TemporalTickerIdPO(temporalTickerPO.getId()), temporalTickerPO.getOrders(), temporalTickerPO.getHigh(),
				temporalTickerPO.getAverage(), temporalTickerPO.getLow(), temporalTickerPO.getVol(),
				temporalTickerPO.getFirst(), temporalTickerPO.getLast(), temporalTickerPO.getPreviousLast(),
				temporalTickerPO.getBuy(), temporalTickerPO.getPreviousBuy(), temporalTickerPO.getSell(),
				temporalTickerPO.getPreviousSell(), temporalTickerPO.getBuyOrders(), temporalTickerPO.getSellOrders(),
				temporalTickerPO.getDuration());
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getVol() {
		return vol;
	}

	public void setVol(double vol) {
		this.vol = vol;
	}

	public double getLast() {
		return last;
	}

	public void setLast(double last) {
		this.last = last;
	}

	public double getPreviousLast() {
		return previousLast;
	}

	public void setPreviousLast(double previousLast) {
		this.previousLast = previousLast;
	}

	public double getBuy() {
		return buy;
	}

	public void setBuy(double buy) {
		this.buy = buy;
	}

	public double getPreviousBuy() {
		return previousBuy;
	}

	public void setPreviousBuy(double lastBuy) {
		this.previousBuy = lastBuy;
	}

	public double getSell() {
		return sell;
	}

	public void setSell(double sell) {
		this.sell = sell;
	}

	public double getPreviousSell() {
		return previousSell;
	}

	public void setPreviousSell(double lastSell) {
		this.previousSell = lastSell;
	}

	public long getOrders() {
		return orders;
	}

	public void setOrders(long orders) {
		this.orders = orders;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public double getFirst() {
		return first;
	}

	public void setFirst(double first) {
		this.first = first;
	}

	public TemporalTickerIdPO getTemporalTickerId() {
		return temporalTickerId;
	}

	public void setTemporalTickerIdPO(TemporalTickerIdPO temporalTickerId) {
		this.temporalTickerId = temporalTickerId;
	}

	public long getBuyOrders() {
		return buyOrders;
	}

	public void setBuyOrders(long buyOrders) {
		this.buyOrders = buyOrders;
	}

	public long getSellOrders() {
		return sellOrders;
	}

	public void setSellOrders(long sellOrders) {
		this.sellOrders = sellOrders;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	@Override
	public Class<?> getEntityClass() {
		return TemporalTickerPO.class;
	}

	@Override
	public TemporalTickerIdPO getId() {
		return this.temporalTickerId;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		NonDigitalCurrencyFormatter nonDigitalCurrencyFormatter = new NonDigitalCurrencyFormatter();
		stringBuffer.append("[");
		stringBuffer.append("ID: [" + temporalTickerId + "], ");
		stringBuffer.append("Orders: " + orders);
		stringBuffer.append(", buy orders: " + buyOrders);
		stringBuffer.append(", sell orders: " + sellOrders);
		stringBuffer.append(", high: " + nonDigitalCurrencyFormatter.format(high));
		stringBuffer.append(", average: " + nonDigitalCurrencyFormatter.format(average));
		stringBuffer.append(", low: " + nonDigitalCurrencyFormatter.format(low));
		stringBuffer.append(", vol: " + nonDigitalCurrencyFormatter.format(vol));
		stringBuffer.append(", first: " + nonDigitalCurrencyFormatter.format(first));
		stringBuffer.append(", last: " + nonDigitalCurrencyFormatter.format(last));
		stringBuffer.append(", previous last: " + nonDigitalCurrencyFormatter.format(previousLast));
		stringBuffer.append(", buy: " + nonDigitalCurrencyFormatter.format(buy));
		stringBuffer.append(", last buy: " + nonDigitalCurrencyFormatter.format(previousBuy));
		stringBuffer.append(", sell: " + nonDigitalCurrencyFormatter.format(sell));
		stringBuffer.append(", last sell: " + nonDigitalCurrencyFormatter.format(previousSell));
		stringBuffer.append(", duration: " + new DurationToStringConverter().convertTo(duration));
		stringBuffer.append("]");
		return stringBuffer.toString();
	}
}
