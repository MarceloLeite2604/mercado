package org.marceloleite.mercado.databasemodel;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.marceloleite.mercado.commons.Currency;

@Entity(name = "STRATEGIES")
public class StrategyPO implements PersistenceObject<StrategyIdPO> {

	@EmbeddedId
	private StrategyIdPO strategyIdPO;

	@Column(name = "CURRENCY")
	private Currency currency;

	@ManyToOne
	@JoinColumn(name = "ACCO_OWNER")
	private AccountPO account;

	@OneToMany
	@JoinColumns({ @JoinColumn(name = "STRA_ACCO_OWNER", referencedColumnName = "ACCO_OWNER"),
			@JoinColumn(name = "STRA_CURRENCY", referencedColumnName = "CURRENCY") })
	private List<StrategyParameterPO> strategyParameterPOs;

	@OneToMany
	@JoinColumns({ @JoinColumn(name = "STRA_ACCO_OWNER", referencedColumnName = "ACCO_OWNER"),
		@JoinColumn(name = "STRA_CURRENCY", referencedColumnName = "CURRENCY") })
	private List<StrategyVariablePO> strategyVariablePOs;

	@OneToMany
	@JoinColumns({ @JoinColumn(name = "STRA_ACCO_OWNER", referencedColumnName = "ACCO_OWNER"),
		@JoinColumn(name = "STRA_CURRENCY", referencedColumnName = "CURRENCY") })
	private List<StrategyClassPO> stratrategyClassPOs;

	public StrategyIdPO getStrategyIdPO() {
		return strategyIdPO;
	}

	public void setStrategyIdPO(StrategyIdPO strategyIdPO) {
		this.strategyIdPO = strategyIdPO;
	}

	public List<StrategyParameterPO> getStrategyParameterPOs() {
		return strategyParameterPOs;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setStrategyParameterPOs(List<StrategyParameterPO> strategyPropertyPOs) {
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

	public AccountPO getAccount() {
		return account;
	}

	public void setAccount(AccountPO account) {
		this.account = account;
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
