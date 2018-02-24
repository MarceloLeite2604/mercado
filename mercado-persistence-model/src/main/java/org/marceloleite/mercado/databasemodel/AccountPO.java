package org.marceloleite.mercado.databasemodel;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="ACCOUNTS")
public class AccountPO implements PersistenceObject<String> {

	@Id
	@Column(name="OWNER", nullable = false)
	private String owner;
	
	@OneToMany
	private List<BalancePO> balancePOs;
	
	@OneToMany
	private List<StrategyPO> strategyPOs;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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
