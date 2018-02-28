package org.marceloleite.mercado.databaseretriever.persistence.objects.old;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

@Embeddable
public class OldStrategyClassIdPO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "STRA_ACCO_OWNER", insertable=false, updatable=false)
	private String accountOwner;

	@Column(name = "STRA_CURRENCY", insertable=false, updatable=false)
	private Currency strategyCurrency;

	@Column(name = "CLASS_NAME", nullable = false)
	private String className;

	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public Currency getStrategyCurrency() {
		return strategyCurrency;
	}

	public void setStrategyCurrency(Currency currency) {
		this.strategyCurrency = currency;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountOwner == null) ? 0 : accountOwner.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((strategyCurrency == null) ? 0 : strategyCurrency.hashCode());
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
		StrategyClassIdPO other = (StrategyClassIdPO) obj;
		if (accountOwner == null) {
			if (other.accountOwner != null)
				return false;
		} else if (!accountOwner.equals(other.accountOwner))
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (strategyCurrency == null) {
			if (other.strategyCurrency != null)
				return false;
		} else if (!strategyCurrency.equals(other.strategyCurrency))
			return false;
		return true;
	}

}
