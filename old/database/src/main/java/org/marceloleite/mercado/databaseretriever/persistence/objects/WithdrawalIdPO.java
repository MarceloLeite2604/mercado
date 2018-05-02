package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

/**
 * The primary key class for the WITHDRAWALS database table.
 * 
 */
@Embeddable
public class WithdrawalIdPO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ACCO_OWNER", length = 64, insertable = false, updatable = false)
	private String accoOwner;

	@Column(name = "CURRENCY", length = 4)
	private Currency currency;

	public WithdrawalIdPO() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accoOwner == null) ? 0 : accoOwner.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
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
		WithdrawalIdPO other = (WithdrawalIdPO) obj;
		if (accoOwner == null) {
			if (other.accoOwner != null)
				return false;
		} else if (!accoOwner.equals(other.accoOwner))
			return false;
		if (currency != other.currency)
			return false;
		return true;
	}

}