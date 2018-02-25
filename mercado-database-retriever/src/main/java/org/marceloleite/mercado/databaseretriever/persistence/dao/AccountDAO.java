package org.marceloleite.mercado.databaseretriever.persistence.dao;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.databaseretriever.persistence.objects.AccountPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

public class AccountDAO extends AbstractDAO<AccountPO> {

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return AccountPO.class;
	}

	@Override
	public AccountPO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof AccountPO)) {
			new ClassCastExceptionThrower(AccountPO.class, persistenceObject);
		}

		return (AccountPO) persistenceObject;
	}

}
