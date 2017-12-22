package org.marceloleite.mercado.databasemodel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name="TemporalTickers")
public class TemporalTicker {

	@EmbeddedId
	private TemporalTickerId temporalTickerId;
	
	private long orders;

	private double high;
	
	private double average;

	private double low;

	private double vol;
	
	private double first;

	private double last;

	private double buy;

	private double sell;

	public TemporalTicker() {
		super();
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

	public TemporalTickerId getTemporalTickerId() {
		return temporalTickerId;
	}

	public void setTemporalTickerId(TemporalTickerId temporalTickerId) {
		this.temporalTickerId = temporalTickerId;
	}
	
	

}
