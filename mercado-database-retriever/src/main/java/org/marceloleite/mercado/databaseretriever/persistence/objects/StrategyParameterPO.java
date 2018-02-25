package org.marceloleite.mercado.databaseretriever.persistence.objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity(name = "STRATEGIES_PARAMETERS")
public class StrategyParameterPO implements PersistenceObject<StrategyParameterIdPO> {

	@EmbeddedId
	private StrategyParameterIdPO strategyParameterIdPO;

	@Column(name = "VALUE")
	private String value;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "STRA_ACCO_OWNER", referencedColumnName = "ACCO_OWNER", insertable = false, updatable = false),
			@JoinColumn(name = "STRA_CURRENCY", referencedColumnName = "CURRENCY", insertable = false, updatable = false) })
	private StrategyPO strategyPO;

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

	public StrategyPO getStrategyPO() {
		return strategyPO;
	}

	public void setStrategyPO(StrategyPO strategyPO) {
		this.strategyPO = strategyPO;
	}
}
