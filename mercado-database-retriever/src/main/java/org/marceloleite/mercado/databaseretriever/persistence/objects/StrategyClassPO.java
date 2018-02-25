package org.marceloleite.mercado.databaseretriever.persistence.objects;

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
	@JoinColumns({ @JoinColumn(name = "STRA_ACCO_OWNER", referencedColumnName = "ACCO_OWNER", insertable = false, updatable = false),
			@JoinColumn(name = "STRA_CURRENCY", referencedColumnName = "CURRENCY", insertable = false, updatable = false) })
	private StrategyPO strategyPO;

	public StrategyClassIdPO getStrategyClassIdPO() {
		return strategyClassIdPO;
	}

	public void setStrategyClassIdPO(StrategyClassIdPO strategyClassIdPO) {
		this.strategyClassIdPO = strategyClassIdPO;
	}

	public StrategyPO getStrategyPO() {
		return strategyPO;
	}

	public void setStrategyPO(StrategyPO strategyPO) {
		this.strategyPO = strategyPO;
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
