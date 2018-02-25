package org.marceloleite.mercado.databasemodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "BALANCES")
public class BalancePO implements PersistenceObject<BalanceIdPO> {

	@EmbeddedId
	private BalanceIdPO balanceIdPO;

	@ManyToOne
	@JoinColumn(name = "ACCO_OWNER")
	private AccountPO accountPO;

	@Column(name = "AMOUNT", nullable = false)
	private Double amount;

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

	public AccountPO getAccountPO() {
		return accountPO;
	}

	public void setAccountPO(AccountPO accountPO) {
		this.accountPO = accountPO;
	}

	@Override
	public Class<?> getEntityClass() {
		return BalancePO.class;
	}

	@Override
	public BalanceIdPO getId() {
		return balanceIdPO;
	}

}
