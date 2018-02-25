package org.marceloleite.mercado.databasemodel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity(name = "STRATEGIES_CLASSES")
public class StrategyClassPO implements PersistenceObject<StrategyClassIdPO> {

	@EmbeddedId
	private StrategyClassIdPO strategyClassIdPO;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "STRA_ACCO_OWNER", referencedColumnName = "ACCO_OWNER"),
			@JoinColumn(name = "STRA_CURRENCY", referencedColumnName = "CURRENCY") })
	private StrategyPO strategyPO;

	public StrategyClassIdPO getStrategyClassIdPO() {
		return strategyClassIdPO;
	}

	public void setStrategyClassIdPO(StrategyClassIdPO strategyClassIdPO) {
		this.strategyClassIdPO = strategyClassIdPO;
	}

	@Override
	public Class<?> getEntityClass() {
		return StrategyClassPO.class;
	}

	@Override
	public StrategyClassIdPO getId() {
		return strategyClassIdPO;
	}
}
