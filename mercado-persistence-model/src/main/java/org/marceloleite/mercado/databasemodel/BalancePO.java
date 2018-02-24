package org.marceloleite.mercado.databasemodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "BALANCES")
public class BalancePO implements PersistenceObject<BalanceIdPO> {

	@EmbeddedId
	private BalanceIdPO balanceIdPO;

	@Column(name = "AMOUNT", nullable = false)
	private Double amount;

	@Override
	public Class<?> getEntityClass() {
		return BalancePO.class;
	}

	@Override
	public BalanceIdPO getId() {
		return balanceIdPO;
	}

	public BalanceIdPO getBalanceIdPO() {
		return balanceIdPO;
	}

	public void setBalanceIdPO(BalanceIdPO balanceIdPO) {
		this.balanceIdPO = balanceIdPO;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
