package org.marceloleite.mercado.databaseretriever.persistence.dao;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.databasemodel.PersistenceObject;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class TemporalTickerDAO extends AbstractDAO<TemporalTickerPO> {

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return TemporalTickerPO.class;
	}

	@Override
	public TemporalTickerPO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof TemporalTickerPO)) {
			new ClassCastExceptionThrower(TemporalTickerPO.class, persistenceObject);
		}

		return (TemporalTickerPO) persistenceObject;
	}
}
