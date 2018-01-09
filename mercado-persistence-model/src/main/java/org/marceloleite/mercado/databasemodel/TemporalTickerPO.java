package org.marceloleite.mercado.databasemodel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "TemporalTickers")
public class TemporalTickerPO implements PersistenceObject<TemporalTickerIdPO> {

	@EmbeddedId
	private TemporalTickerIdPO temporalTickerId;

	private long orders;

	private double high;

	private double average;

	private double low;

	private double vol;

	private double first;

	private double last;

	private double buy;

	private double sell;

	public TemporalTickerPO() {
		super();
	}

	private TemporalTickerPO(TemporalTickerIdPO temporalTickerId, long orders, double high, double average, double low,
			double vol, double first, double last, double buy, double sell) {
		super();
		this.temporalTickerId = temporalTickerId;
		this.orders = orders;
		this.high = high;
		this.average = average;
		this.low = low;
		this.vol = vol;
		this.first = first;
		this.last = last;
		this.buy = buy;
		this.sell = sell;
	}

	public TemporalTickerPO(TemporalTickerPO temporalTickerPO) {
		this(new TemporalTickerIdPO(temporalTickerPO.getId()), temporalTickerPO.getOrders(), temporalTickerPO.getHigh(),
				temporalTickerPO.getAverage(), temporalTickerPO.getLow(), temporalTickerPO.getVol(),
				temporalTickerPO.getFirst(), temporalTickerPO.getLast(), temporalTickerPO.getBuy(),
				temporalTickerPO.getSell());
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

	public double getBuy() {
		return buy;
	}

	public void setBuy(double buy) {
		this.buy = buy;
	}

	public double getSell() {
		return sell;
	}

	public void setSell(double sell) {
		this.sell = sell;
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

	@Override
	public Class<?> getEntityClass() {
		return TemporalTickerPO.class;
	}

	@Override
	public TemporalTickerIdPO getId() {
		return this.temporalTickerId;
	}
}
