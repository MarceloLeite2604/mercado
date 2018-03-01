package org.marceloleite.mercado.databaseretriever.persistence.objects.old;

import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

//@Entity(name = "STRATEGIES")
public class OldStrategyPO implements PersistenceObject<OldStrategyIdPO> {

	@EmbeddedId
	private OldStrategyIdPO strategyIdPO;

	@ManyToOne
	@JoinColumn(name = "ACCO_OWNER", insertable = false, updatable = false)
	private OldAccountPO accountPO;

	// @OneToMany(mappedBy="strategyClassPO")
	private List<OldClassParameterPO> strategyParameterPOs;

	// @OneToMany(mappedBy="strategyPO")
	private List<OldStrategyVariablePO> strategyVariablePOs;

	// @OneToMany(mappedBy="strategyPO")
	private List<OldStrategyClassPO> stratrategyClassPOs;

	public OldStrategyIdPO getStrategyIdPO() {
		return strategyIdPO;
	}

	public void setStrategyIdPO(OldStrategyIdPO strategyIdPO) {
		this.strategyIdPO = strategyIdPO;
	}

	public List<OldClassParameterPO> getStrategyParameterPOs() {
		return strategyParameterPOs;
	}

	public void setStrategyParameterPOs(List<OldClassParameterPO> strategyPropertyPOs) {
		this.strategyParameterPOs = strategyPropertyPOs;
	}

	public List<OldStrategyVariablePO> getStrategyVariablePOs() {
		return strategyVariablePOs;
	}

	public void setStrategyVariablePOs(List<OldStrategyVariablePO> strategyVariablePOs) {
		this.strategyVariablePOs = strategyVariablePOs;
	}

	public List<OldStrategyClassPO> getStratrategyClassPOs() {
		return stratrategyClassPOs;
	}

	public void setStratrategyClassPOs(List<OldStrategyClassPO> stratrategyClassPOs) {
		this.stratrategyClassPOs = stratrategyClassPOs;
	}

	public OldAccountPO getAccountPO() {
		return accountPO;
	}

	public void setAccountPO(OldAccountPO account) {
		this.accountPO = account;
	}

	@Override
	public Class<?> getEntityClass() {
		return OldStrategyPO.class;
	}

	@Override
	public OldStrategyIdPO getId() {
		return strategyIdPO;
	}
}
