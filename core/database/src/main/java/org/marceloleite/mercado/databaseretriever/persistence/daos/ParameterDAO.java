package org.marceloleite.mercado.databaseretriever.persistence.daos;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ParameterPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

public class ParameterDAO extends AbstractDAO<ParameterPO> {

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return ParameterPO.class;
	}

	@Override
	public ParameterPO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof ParameterPO)) {
			new ClassCastExceptionThrower(ParameterPO.class, persistenceObject);
		}

		return (ParameterPO) persistenceObject;
	}

}
