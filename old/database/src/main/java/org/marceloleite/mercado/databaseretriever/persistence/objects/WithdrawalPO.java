package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the BALANCES database table.
 * 
 */
@Entity
@Table(name = "WITHDRAWALS")
@NamedQuery(name = "WithdrawalPO.findAll", query = "SELECT w FROM WithdrawalPO w")
public class WithdrawalPO implements Serializable, PersistenceObject<WithdrawalIdPO> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private WithdrawalIdPO id;

	@Column(name="AMOUNT")
	private BigDecimal amount;

	// bi-directional many-to-one association to AccountPO
	@ManyToOne
	@JoinColumns(value = {
			@JoinColumn(name = "ACCO_OWNER", insertable = false, updatable = false) }, foreignKey = @ForeignKey(name = "WITH_ACCO_FK"))
	private AccountPO accountPO;

	public WithdrawalPO() {
	}

	public WithdrawalIdPO getId() {
		return this.id;
	}

	public void setId(WithdrawalIdPO id) {
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
		return WithdrawalPO.class;
	}

}