package org.marceloleite.mercado.databaseretriever.persistence.objects.old;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

@Entity(name = "TICKERS")
public class OldTickerPO implements PersistenceObject<OldTickerIdPO> {

	@EmbeddedId
	private OldTickerIdPO tickerIdPO;

	@Column(name="HIGH", nullable=false)
	private double high;

	@Column(name="LOW", nullable=false)
	private double low;

	@Column(name="VOLUME", nullable=false)
	private double vol;

	@Column(name="LAST", nullable=false)
	private double last;

	@Column(name="BUY", nullable=false)
	private double buy;

	@Column(name="SELL", nullable=false)
	private double sell;

	public OldTickerPO() {
		super();
	}

	public OldTickerPO(double high, double low, double vol, double last, double buy, double sell, OldTickerIdPO tickerIdPO) {
		super();
		this.high = high;
		this.low = low;
		this.vol = vol;
		this.last = last;
		this.buy = buy;
		this.sell = sell;
		this.tickerIdPO = tickerIdPO;
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

	public OldTickerIdPO getTickerIdPO() {
		return tickerIdPO;
	}

	public void setTickerIdPO(OldTickerIdPO tickerIdPO) {
		this.tickerIdPO = tickerIdPO;
	}

	@Override
	public Class<?> getEntityClass() {
		return OldTickerPO.class;
	}

	@Override
	public OldTickerIdPO getId() {
		return tickerIdPO;
	}
}
