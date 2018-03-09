package org.marceloleite.mercado.databaseretriever.persistence.objects.old;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

// @Entity(name = "STRATEGIES_CLASSES")
public class OldStrategyClassPO implements PersistenceObject<OldStrategyClassIdPO> {

	@EmbeddedId
	private OldStrategyClassIdPO strategyClassIdPO;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "STRA_ACCO_OWNER", referencedColumnName = "ACCO_OWNER", insertable = false, updatable = false),
			@JoinColumn(name = "STRA_CURRENCY", referencedColumnName = "CURRENCY", insertable = false, updatable = false) })
	private OldStrategyPO strategyPO;

	public OldStrategyClassIdPO getStrategyClassIdPO() {
		return strategyClassIdPO;
	}

	public void setStrategyClassIdPO(OldStrategyClassIdPO strategyClassIdPO) {
		this.strategyClassIdPO = strategyClassIdPO;
	}

	public OldStrategyPO getStrategyPO() {
		return strategyPO;
	}

	public void setStrategyPO(OldStrategyPO strategyPO) {
		this.strategyPO = strategyPO;
	}

	@Override
	public Class<?> getEntityClass() {
		return OldStrategyClassPO.class;
	}

	@Override
	public OldStrategyClassIdPO getId() {
		return strategyClassIdPO;
	}
}
