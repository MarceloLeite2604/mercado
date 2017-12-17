package org.marceloleite.mercado.modeler.persistence.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "Tickers")
public class Ticker {

	@EmbeddedId
	private TickerId tickerId;

	private double high;

	private double low;

	private double vol;

	private double last;

	private double buy;

	private double sell;

	public Ticker() {
		super();
	}

	public Ticker(double high, double low, double vol, double last, double buy, double sell, TickerId tickerId) {
		super();
		this.high = high;
		this.low = low;
		this.vol = vol;
		this.last = last;
		this.buy = buy;
		this.sell = sell;
		this.tickerId = tickerId;
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

	public TickerId getTickerId() {
		return tickerId;
	}

	public void setTickerId(TickerId tickerId) {
		this.tickerId = tickerId;
	}
}
