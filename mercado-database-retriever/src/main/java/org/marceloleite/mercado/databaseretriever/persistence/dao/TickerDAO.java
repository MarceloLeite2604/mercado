package org.marceloleite.mercado.databaseretriever.persistence.dao;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.databasemodel.PersistenceObject;
import org.marceloleite.mercado.databasemodel.TickerPO;

public class TickerDAO extends AbstractDAO<TickerPO> {

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return TickerPO.class;
	}

	@Override
	public TickerPO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof TickerPO)) {
			new ClassCastExceptionThrower(TickerPO.class, persistenceObject);
		}

		return (TickerPO) persistenceObject;
	}
}
