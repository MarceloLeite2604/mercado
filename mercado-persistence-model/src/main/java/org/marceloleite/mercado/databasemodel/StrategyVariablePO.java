package org.marceloleite.mercado.databasemodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity(name="STRATEGIES_VARIABLES")
public class StrategyVariablePO implements PersistenceObject<StrategyVariableIdPO>{

	@EmbeddedId
	private StrategyVariableIdPO strategyVariableIdPO;
	
	@Column(name="VALUE")
	private String value;
	
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "STRA_ACCO_OWNER", referencedColumnName = "ACCO_OWNER"),
			@JoinColumn(name = "STRA_CURRENCY", referencedColumnName = "CURRENCY") })
	private StrategyPO strategyPO;

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

	public StrategyPO getStrategyPO() {
		return strategyPO;
	}

	public void setStrategyPO(StrategyPO strategyPO) {
		this.strategyPO = strategyPO;
	}
}
