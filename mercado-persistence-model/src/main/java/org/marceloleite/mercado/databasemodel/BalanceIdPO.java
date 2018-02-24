package org.marceloleite.mercado.databasemodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

@Embeddable
public class BalanceIdPO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ACCOUNT_OWNER", nullable = false)
	private String accountOwner;

	@Column(name = "CURRENCY", nullable = false)
	private Currency currency;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountOwner == null) ? 0 : accountOwner.hashCode());
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
		BalanceIdPO other = (BalanceIdPO) obj;
		if (accountOwner == null) {
			if (other.accountOwner != null)
				return false;
		} else if (!accountOwner.equals(other.accountOwner))
			return false;
		if (currency != other.currency)
			return false;
		return true;
	}
}
