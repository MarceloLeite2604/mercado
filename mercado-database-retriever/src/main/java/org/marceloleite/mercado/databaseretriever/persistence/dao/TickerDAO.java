package org.marceloleite.mercado.databaseretriever.persistence.dao;

import org.marceloleite.mercado.databasemodel.PersistenceObject;
import org.marceloleite.mercado.databasemodel.TickerPO;

public class TickerDAO extends AbstractDAO<TickerPO> {

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return TickerPO.class;
	}
}
