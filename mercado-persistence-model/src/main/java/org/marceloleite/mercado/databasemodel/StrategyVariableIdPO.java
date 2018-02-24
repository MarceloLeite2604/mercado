package org.marceloleite.mercado.databasemodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StrategyVariableIdPO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ACCOUNT_OWNER", nullable = false)
	private String accountOwner;

	@Column(name = "STRATEGY_NAME", nullable = false)
	private String strategyName;

	@Column(name = "VARIABLE_NAME", nullable = false)
	private String variableName;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountOwner == null) ? 0 : accountOwner.hashCode());
		result = prime * result + ((variableName == null) ? 0 : variableName.hashCode());
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
		StrategyVariableIdPO other = (StrategyVariableIdPO) obj;
		if (accountOwner == null) {
			if (other.accountOwner != null)
				return false;
		} else if (!accountOwner.equals(other.accountOwner))
			return false;
		if (variableName == null) {
			if (other.variableName != null)
				return false;
		} else if (!variableName.equals(other.variableName))
			return false;
		if (strategyName == null) {
			if (other.strategyName != null)
				return false;
		} else if (!strategyName.equals(other.strategyName))
			return false;
		return true;
	}

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

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
}
