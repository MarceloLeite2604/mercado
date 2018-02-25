package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

@Embeddable
public class StrategyVariableIdPO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "STRA_ACCO_OWNER", insertable=false, updatable=false)
	private String accountOwner;

	@Column(name = "STRA_CURRENCY", insertable=false, updatable=false)
	private Currency strategyCurrency;

	@Column(name = "NAME", nullable = false)
	private String name;

	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public Currency getStrategyCurrency() {
		return strategyCurrency;
	}

	public void setStrategyCurrency(Currency strategyCurrency) {
		this.strategyCurrency = strategyCurrency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountOwner == null) ? 0 : accountOwner.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		StrategyVariableIdPO other = (StrategyVariableIdPO) obj;
		if (accountOwner == null) {
			if (other.accountOwner != null)
				return false;
		} else if (!accountOwner.equals(other.accountOwner))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (strategyCurrency != other.strategyCurrency)
			return false;
		return true;
	}
}
