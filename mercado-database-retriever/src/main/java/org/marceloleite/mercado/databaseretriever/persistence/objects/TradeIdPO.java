package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

/**
 * The primary key class for the TRADES database table.
 * 
 */
@Embeddable
public class TradeIdPO implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private Currency currency;

	@Column(name="TRADE_ID")
	private long tradeId;

	public TradeIdPO() {
	}
	public Currency getCurrency() {
		return this.currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public long getTradeId() {
		return this.tradeId;
	}
	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TradeIdPO)) {
			return false;
		}
		TradeIdPO castOther = (TradeIdPO)other;
		return 
			this.currency.equals(castOther.currency)
			&& (this.tradeId == castOther.tradeId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.currency.hashCode();
		hash = hash * prime + ((int) (this.tradeId ^ (this.tradeId >>> 32)));
		
		return hash;
	}
}