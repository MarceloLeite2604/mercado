package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the TICKERS database table.
 * 
 */
@Entity
@Table(name="TICKERS")
@NamedQuery(name="TickerPO.findAll", query="SELECT t FROM TickerPO t")
public class TickerPO implements Serializable, PersistenceObject<TickerIdPO> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TickerIdPO id;

	private double buy;

	private double high;

	@Column(name="\"LAST\"")
	private double last;

	private double low;

	private double sell;

	private double volume;

	public TickerPO() {
	}

	public TickerIdPO getId() {
		return this.id;
	}

	public void setId(TickerIdPO id) {
		this.id = id;
	}

	public double getBuy() {
		return this.buy;
	}

	public void setBuy(double buy) {
		this.buy = buy;
	}

	public double getHigh() {
		return this.high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLast() {
		return this.last;
	}

	public void setLast(double last) {
		this.last = last;
	}

	public double getLow() {
		return this.low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getSell() {
		return this.sell;
	}

	public void setSell(double sell) {
		this.sell = sell;
	}

	public double getVolume() {
		return this.volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	@Override
	public Class<?> getEntityClass() {
		return TickerPO.class;
	}

}