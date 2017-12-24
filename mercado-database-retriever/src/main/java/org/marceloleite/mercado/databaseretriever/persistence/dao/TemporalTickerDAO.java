package org.marceloleite.mercado.databaseretriever.persistence.dao;

import org.marceloleite.mercado.databasemodel.PersistenceObject;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class TemporalTickerDAO extends AbstractDAO {

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return TemporalTickerPO.class;
	}
}
