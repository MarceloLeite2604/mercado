package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the STRATEGIES database table.
 * 
 */
@Entity
@Table(name="STRATEGIES")
@NamedQuery(name="StrategyPO.findAll", query="SELECT s FROM StrategyPO s")
public class StrategyPO implements Serializable, PersistenceObject<StrategyIdPO> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private StrategyIdPO id;

	//bi-directional many-to-one association to ClassPO
	@OneToMany(mappedBy="strategyPO")
	private List<ClassPO> classPOs;

	//bi-directional many-to-one association to AccountPO
	@ManyToOne
	@JoinColumn(name="ACCO_OWNER")
	private AccountPO account;

	public StrategyPO() {
	}

	public StrategyIdPO getId() {
		return this.id;
	}

	public void setId(StrategyIdPO id) {
		this.id = id;
	}

	public List<ClassPO> getClassPOs() {
		return this.classPOs;
	}

	public void setClassPOs(List<ClassPO> classPOs) {
		this.classPOs = classPOs;
	}

	public ClassPO addClass(ClassPO classPO) {
		getClassPOs().add(classPO);
		classPO.setStrategyPO(this);

		return classPO;
	}

	public ClassPO removeClass(ClassPO classPO) {
		getClassPOs().remove(classPO);
		classPO.setStrategyPO(null);

		return classPO;
	}

	public AccountPO getAccount() {
		return this.account;
	}

	public void setAccount(AccountPO account) {
		this.account = account;
	}

	@Override
	public Class<?> getEntityClass() {
		return StrategyPO.class;
	}

}