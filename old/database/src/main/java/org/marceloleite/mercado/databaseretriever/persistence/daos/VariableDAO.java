package org.marceloleite.mercado.databaseretriever.persistence.daos;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;
import org.marceloleite.mercado.databaseretriever.persistence.objects.VariablePO;

public class VariableDAO extends AbstractDAO<VariablePO> {

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return VariablePO.class;
	}

	@Override
	public VariablePO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof VariablePO)) {
			new ClassCastExceptionThrower(VariablePO.class, persistenceObject);
		}

		return (VariablePO) persistenceObject;
	}

}
