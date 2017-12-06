package org.marceloleite.mercado.model.persistence;

import java.util.Calendar;

public class Ticker {

	private double high;

	private double low;

	private double vol;

	private double last;

	private double buy;

	private double sell;

	private Calendar date;

	public Ticker() {
		super();
	}

	public Ticker(double high, double low, double vol, double last, double buy, double sell, Calendar date) {
		super();
		this.high = high;
		this.low = low;
		this.vol = vol;
		this.last = last;
		this.buy = buy;
		this.sell = sell;
		this.date = date;
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

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

}
