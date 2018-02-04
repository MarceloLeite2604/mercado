package org.marceloleite.mercado.databasemodel;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;

@Embeddable
public class TemporalTickerIdPO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Currency currency;

	@Column(name="START_TIME", nullable = false)
	private ZonedDateTime start;

	@Column(name="END_TIME", nullable = false)
	private ZonedDateTime end;

	public TemporalTickerIdPO() {
		super();
	}

	public TemporalTickerIdPO(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		super();
		this.currency = currency;
		this.start = start;
		this.end = end;
	}

	public TemporalTickerIdPO(TemporalTickerIdPO id) {
		this(id.getCurrency(), ZonedDateTime.from(id.getStart()), ZonedDateTime.from(id.getEnd()));
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public ZonedDateTime getStart() {
		return start;
	}

	public void setStart(ZonedDateTime start) {
		this.start = start;
	}

	public ZonedDateTime getEnd() {
		return end;
	}

	public void setEnd(ZonedDateTime end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		return new TimeInterval(start, end).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemporalTickerIdPO other = (TemporalTickerIdPO) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}
}
