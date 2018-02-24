package org.marceloleite.mercado.databasemodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name="STRATEGIES_VARIABLES")
public class StrategyVariablePO implements PersistenceObject<StrategyVariableIdPO>{

	@EmbeddedId
	private StrategyVariableIdPO strategyVariableIdPO;
	
	@Column(name="VARIABLE_	VALUE")
	private String value;
	
	public StrategyVariableIdPO getStrategyVariableIdPO() {
		return strategyVariableIdPO;
	}
	
	public void setStrategyVariableIdPO(StrategyVariableIdPO strategyVariableIdPO) {
		this.strategyVariableIdPO = strategyVariableIdPO;
	}

	@Override
	public Class<?> getEntityClass() {
		return StrategyVariablePO.class;
	}

	@Override
	public StrategyVariableIdPO getId() {
		return strategyVariableIdPO;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
