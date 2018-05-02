package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

/**
 * The primary key class for the BALANCES database table.
 * 
 */
@Embeddable
public class BalanceIdPO implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ACCO_OWNER", length = 64, insertable = false, updatable = false)
	private String accoOwner;

	@Column(name = "CURRENCY", length = 4)
	private Currency currency;

	public BalanceIdPO() {
	}

	public String getAccoOwner() {
		return this.accoOwner;
	}

	public void setAccoOwner(String accoOwner) {
		this.accoOwner = accoOwner;
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BalanceIdPO)) {
			return false;
		}
		BalanceIdPO castOther = (BalanceIdPO) other;
		return this.accoOwner.equals(castOther.accoOwner) && this.currency.equals(castOther.currency);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.accoOwner.hashCode();
		hash = hash * prime + this.currency.hashCode();

		return hash;
	}
}