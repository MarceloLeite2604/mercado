package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

/**
 * The primary key class for the TEMPORAL_TICKERS database table.
 * 
 */
@Embeddable
public class TemporalTickerIdPO implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CURRENCY", length=4)
	private Currency currency;

	@Column(name="START_TIME")
	private ZonedDateTime startTime;

	@Column(name="END_TIME")
	private ZonedDateTime endTime;

	public TemporalTickerIdPO() {
	}
	public Currency getCurrency() {
		return this.currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public ZonedDateTime getStartTime() {
		return this.startTime;
	}
	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}
	public ZonedDateTime getEndTime() {
		return this.endTime;
	}
	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TemporalTickerIdPO)) {
			return false;
		}
		TemporalTickerIdPO castOther = (TemporalTickerIdPO)other;
		return 
			this.currency.equals(castOther.currency)
			&& this.startTime.equals(castOther.startTime)
			&& this.endTime.equals(castOther.endTime);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.currency.hashCode();
		hash = hash * prime + this.startTime.hashCode();
		hash = hash * prime + this.endTime.hashCode();
		
		return hash;
	}
}