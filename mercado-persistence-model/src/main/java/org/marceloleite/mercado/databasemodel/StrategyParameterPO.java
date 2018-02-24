package org.marceloleite.mercado.databasemodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name="STRATEGIES_PARAMETERS")
public class StrategyParameterPO implements PersistenceObject<StrategyParameterIdPO>{

	@EmbeddedId
	private StrategyParameterIdPO strategyParameterIdPO;
	
	@Column(name="PARAMETER_VALUE")
	private String value;

	@Override
	public Class<?> getEntityClass() {
		return StrategyParameterPO.class;
	}

	@Override
	public StrategyParameterIdPO getId() {
		return strategyParameterIdPO;
	}
	
	public StrategyParameterIdPO getStrategyParameterIdPO() {
		return strategyParameterIdPO;
	}
	
	public void setStrategyParameterIdPO(StrategyParameterIdPO strategyParameterIdPO) {
		this.strategyParameterIdPO = strategyParameterIdPO;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
