package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

/**
 * The primary key class for the TICKERS database table.
 * 
 */
@Embeddable
public class TickerIdPO implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CURRENCY", length=4)
	private Currency currency;

	@Column(name="TICKER_TIME")
	private ZonedDateTime tickerTime;

	public TickerIdPO() {
	}
	public Currency getCurrency() {
		return this.currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public ZonedDateTime getTickerTime() {
		return this.tickerTime;
	}
	public void setTickerTime(ZonedDateTime tickerTime) {
		this.tickerTime = tickerTime;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TickerIdPO)) {
			return false;
		}
		TickerIdPO castOther = (TickerIdPO)other;
		return 
			this.currency.equals(castOther.currency)
			&& this.tickerTime.equals(castOther.tickerTime);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.currency.hashCode();
		hash = hash * prime + this.tickerTime.hashCode();
		
		return hash;
	}
}