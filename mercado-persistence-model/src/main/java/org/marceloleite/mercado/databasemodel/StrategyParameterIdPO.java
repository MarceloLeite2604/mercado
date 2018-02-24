package org.marceloleite.mercado.databasemodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StrategyParameterIdPO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ACCOUNT_NAME", nullable = false)
	private String accountOwner;

	@Column(name = "STRATEGY_NAME", nullable = false)
	private String strategyName;

	@Column(name = "PARAMETER_NAME", nullable = false)
	private String parameterName;

	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountOwner == null) ? 0 : accountOwner.hashCode());
		result = prime * result + ((parameterName == null) ? 0 : parameterName.hashCode());
		result = prime * result + ((strategyName == null) ? 0 : strategyName.hashCode());
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
		StrategyParameterIdPO other = (StrategyParameterIdPO) obj;
		if (accountOwner == null) {
			if (other.accountOwner != null)
				return false;
		} else if (!accountOwner.equals(other.accountOwner))
			return false;
		if (parameterName == null) {
			if (other.parameterName != null)
				return false;
		} else if (!parameterName.equals(other.parameterName))
			return false;
		if (strategyName == null) {
			if (other.strategyName != null)
				return false;
		} else if (!strategyName.equals(other.strategyName))
			return false;
		return true;
	}
}
