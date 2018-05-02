package org.marceloleite.mercado.databaseretriever.persistence.daos;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyPO;

public class StrategyDAO extends AbstractDAO<StrategyPO> {

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return StrategyPO.class;
	}

	@Override
	public StrategyPO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof StrategyPO)) {
			new ClassCastExceptionThrower(StrategyPO.class, persistenceObject);
		}

		return (StrategyPO) persistenceObject;
	}

}
