package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the ACCOUNTS database table.
 * 
 */
@Entity
@Table(name = "ACCOUNTS")
@NamedQuery(name = "AccountPO.findAll", query = "SELECT a FROM AccountPO a")
public class AccountPO implements Serializable, PersistenceObject<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String owner;

	@OneToOne(mappedBy = "accountPO", cascade = CascadeType.ALL)
	private TapiInformationPO tapiInformationPO;

	// bi-directional many-to-one association to BalancePO
	@OneToMany(mappedBy = "accountPO", cascade = CascadeType.ALL)
	private List<BalancePO> balancePOs;

	// bi-directional many-to-one association to StrategyPO
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<StrategyPO> strategyPOs;

	public AccountPO() {
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public TapiInformationPO getTapiInformationPO() {
		return tapiInformationPO;
	}

	public void setTapiInformationPO(TapiInformationPO tapiInformationPO) {
		this.tapiInformationPO = tapiInformationPO;
	}

	public List<BalancePO> getBalancePOs() {
		return this.balancePOs;
	}

	public void setBalancePOs(List<BalancePO> balances) {
		this.balancePOs = balances;
	}

	public BalancePO addBalance(BalancePO balance) {
		getBalancePOs().add(balance);
		balance.setAccountPO(this);

		return balance;
	}

	public BalancePO removeBalance(BalancePO balance) {
		getBalancePOs().remove(balance);
		balance.setAccountPO(null);

		return balance;
	}

	public List<StrategyPO> getStrategyPOs() {
		return this.strategyPOs;
	}

	public void setStrategyPOs(List<StrategyPO> strategies) {
		this.strategyPOs = strategies;
	}

	public StrategyPO addStrategy(StrategyPO strategy) {
		getStrategyPOs().add(strategy);
		strategy.setAccount(this);

		return strategy;
	}

	public StrategyPO removeStrategy(StrategyPO strategy) {
		getStrategyPOs().remove(strategy);
		strategy.setAccount(null);

		return strategy;
	}

	@Override
	public Class<?> getEntityClass() {
		return AccountPO.class;
	}

	@Override
	public String getId() {
		return owner;
	}

}