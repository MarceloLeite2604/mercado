package org.marceloleite.mercado.databaseretriever.persistence.objects.old;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

@Entity(name="ACCOUNTS")
public class OldAccountPO implements PersistenceObject<String> {

	@Id
	@Column(name="OWNER", nullable = false)
	private String owner;
	
	@OneToMany(mappedBy="accountPO")
	private List<OldBalancePO> balancePOs;
	
	@OneToMany(mappedBy="accountPO")
	private List<OldStrategyPO> strategyPOs;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public Class<?> getEntityClass() {
		return OldAccountPO.class;
	}

	@Override
	public String getId() {
		return owner;
	}

	public List<OldBalancePO> getBalancePOs() {
		return balancePOs;
	}

	public void setBalancePOs(List<OldBalancePO> balancePOs) {
		this.balancePOs = balancePOs;
	}

	public List<OldStrategyPO> getStrategyPOs() {
		return strategyPOs;
	}

	public void setStrategyPOs(List<OldStrategyPO> strategyPOs) {
		this.strategyPOs = strategyPOs;
	}
}
