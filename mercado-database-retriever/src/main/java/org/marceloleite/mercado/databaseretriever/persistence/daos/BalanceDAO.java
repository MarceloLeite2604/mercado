package org.marceloleite.mercado.databaseretriever.persistence.daos;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.databaseretriever.persistence.objects.BalancePO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

public class BalanceDAO extends AbstractDAO<BalancePO> {

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return BalancePO.class;
	}

	@Override
	public BalancePO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof BalancePO)) {
			new ClassCastExceptionThrower(BalancePO.class, persistenceObject);
		}

		return (BalancePO) persistenceObject;
	}

}
