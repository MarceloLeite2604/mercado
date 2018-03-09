package org.marceloleite.mercado.databaseretriever.persistence.daos;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ClassPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

public class ClassDAO extends AbstractDAO<ClassPO> {

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return ClassPO.class;
	}

	@Override
	public ClassPO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof ClassPO)) {
			new ClassCastExceptionThrower(ClassPO.class, persistenceObject);
		}

		return (ClassPO) persistenceObject;
	}

}
