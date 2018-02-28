package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the BALANCES database table.
 * 
 */
@Entity
@Table(name = "BALANCES")
@NamedQuery(name = "BalancePO.findAll", query = "SELECT b FROM BalancePO b")
public class BalancePO implements Serializable, PersistenceObject<BalanceIdPO> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BalanceIdPO id;

	private BigDecimal amount;

	// bi-directional many-to-one association to AccountPO
	@ManyToOne
	@JoinColumn(name = "ACCO_OWNER")
	private AccountPO accountPO;

	public BalancePO() {
	}

	public BalanceIdPO getId() {
		return this.id;
	}

	public void setId(BalanceIdPO id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public AccountPO getAccountPO() {
		return this.accountPO;
	}

	public void setAccountPO(AccountPO accountPO) {
		this.accountPO = accountPO;
	}

	@Override
	public Class<?> getEntityClass() {
		return BalancePO.class;
	}

}