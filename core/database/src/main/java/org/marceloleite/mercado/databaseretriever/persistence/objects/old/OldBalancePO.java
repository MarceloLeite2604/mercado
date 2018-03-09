package org.marceloleite.mercado.databaseretriever.persistence.objects.old;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

//@Entity(name = "BALANCES")
public class OldBalancePO implements PersistenceObject<OldBalanceIdPO> {

	@EmbeddedId
	private OldBalanceIdPO balanceIdPO;

	@ManyToOne
	@JoinColumn(name = "ACCO_OWNER", insertable = false, updatable = false)
	private OldAccountPO accountPO;

	@Column(name = "AMOUNT", nullable = false)
	private Double amount;

	public OldBalanceIdPO getBalanceIdPO() {
		return balanceIdPO;
	}

	public void setBalanceIdPO(OldBalanceIdPO balanceIdPO) {
		this.balanceIdPO = balanceIdPO;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public OldAccountPO getAccountPO() {
		return accountPO;
	}

	public void setAccountPO(OldAccountPO accountPO) {
		this.accountPO = accountPO;
	}

	@Override
	public Class<?> getEntityClass() {
		return OldBalancePO.class;
	}

	@Override
	public OldBalanceIdPO getId() {
		return balanceIdPO;
	}

}
