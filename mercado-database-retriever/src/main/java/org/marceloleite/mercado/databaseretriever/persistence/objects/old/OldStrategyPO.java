package org.marceloleite.mercado.databaseretriever.persistence.objects.old;

import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

@Entity(name = "STRATEGIES")
public class OldStrategyPO implements PersistenceObject<StrategyIdPO> {

	@EmbeddedId
	private StrategyIdPO strategyIdPO;

	@ManyToOne
	@JoinColumn(name = "ACCO_OWNER", insertable = false, updatable = false)
	private AccountPO accountPO;

	@OneToMany(mappedBy="strategyClassPO")
	private List<ClassParameterPO> strategyParameterPOs;

	@OneToMany(mappedBy="strategyPO")
	private List<StrategyVariablePO> strategyVariablePOs;

	@OneToMany(mappedBy="strategyPO")
	private List<StrategyClassPO> stratrategyClassPOs;

	public StrategyIdPO getStrategyIdPO() {
		return strategyIdPO;
	}

	public void setStrategyIdPO(StrategyIdPO strategyIdPO) {
		this.strategyIdPO = strategyIdPO;
	}

	public List<ClassParameterPO> getStrategyParameterPOs() {
		return strategyParameterPOs;
	}

	public void setStrategyParameterPOs(List<ClassParameterPO> strategyPropertyPOs) {
		this.strategyParameterPOs = strategyPropertyPOs;
	}

	public List<StrategyVariablePO> getStrategyVariablePOs() {
		return strategyVariablePOs;
	}

	public void setStrategyVariablePOs(List<StrategyVariablePO> strategyVariablePOs) {
		this.strategyVariablePOs = strategyVariablePOs;
	}

	public List<StrategyClassPO> getStratrategyClassPOs() {
		return stratrategyClassPOs;
	}

	public void setStratrategyClassPOs(List<StrategyClassPO> stratrategyClassPOs) {
		this.stratrategyClassPOs = stratrategyClassPOs;
	}

	public AccountPO getAccountPO() {
		return accountPO;
	}

	public void setAccountPO(AccountPO account) {
		this.accountPO = account;
	}

	@Override
	public Class<?> getEntityClass() {
		return StrategyPO.class;
	}

	@Override
	public StrategyIdPO getId() {
		return strategyIdPO;
	}
}