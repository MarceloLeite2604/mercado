package org.marceloleite.mercado.databasemodel;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity(name = "STRATEGIES")
public class StrategyPO implements PersistenceObject<StrategyIdPO> {

	@EmbeddedId
	private StrategyIdPO strategyIdPO;

	@Column(name = "STRATEGY_CLASS")
	private String className;

	@OneToMany
	private List<StrategyParameterPO> strategyParameterPOs;

	@OneToMany
	private List<StrategyVariablePO> strategyVariablePOs;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public StrategyIdPO getStrategyIdPO() {
		return strategyIdPO;
	}

	public void setStrategyIdPO(StrategyIdPO strategyIdPO) {
		this.strategyIdPO = strategyIdPO;
	}

	public List<StrategyParameterPO> getStrategyParameterPOs() {
		return strategyParameterPOs;
	}

	public void setStrategyParameterPOs(List<StrategyParameterPO> strategyPropertyPOs) {
		this.strategyParameterPOs = strategyPropertyPOs;
	}

	public List<StrategyVariablePO> getStrategyVariablePOs() {
		return strategyVariablePOs;
	}

	public void setStrategyVariablePOs(List<StrategyVariablePO> strategyVariablePOs) {
		this.strategyVariablePOs = strategyVariablePOs;
	}

	@Override
	public Class<?> getEntityClass() {
		return StrategyPO.class;
	}

	@Override
	public StrategyIdPO getId() {
		return strategyIdPO;
	}
}
