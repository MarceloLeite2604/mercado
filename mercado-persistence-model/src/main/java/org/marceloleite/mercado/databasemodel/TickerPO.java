package org.marceloleite.mercado.databasemodel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "Tickers")
public class TickerPO implements PersistenceObject<TickerIdPO> {

	@EmbeddedId
	private TickerIdPO tickerId;

	private double high;

	private double low;

	private double vol;

	private double last;

	private double buy;

	private double sell;

	public TickerPO() {
		super();
	}

	public TickerPO(double high, double low, double vol, double last, double buy, double sell, TickerIdPO tickerId) {
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

	public TickerIdPO getTickerId() {
		return tickerId;
	}

	public void setTickerId(TickerIdPO tickerId) {
		this.tickerId = tickerId;
	}

	@Override
	public Class<?> getEntityClass() {
		return TickerPO.class;
	}

	@Override
	public TickerIdPO getId() {
		return tickerId;
	}
}
