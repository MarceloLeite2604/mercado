package org.marceloleite.mercado.databasemodel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name="BALANCES")
public class BalancePO implements PersistenceObject<BalanceIdPO>{

	@EmbeddedId
	private BalanceIdPO balanceIdPO;

	@Override
	public Class<?> getEntityClass() {
		return BalancePO.class;
	}

	@Override
	public BalanceIdPO getId() {
		return balanceIdPO;
	}
}
