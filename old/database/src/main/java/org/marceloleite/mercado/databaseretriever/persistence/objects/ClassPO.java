package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * The persistent class for the CLASSES database table.
 * 
 */
@Entity
@Table(name = "CLASSES")
@NamedQuery(name = "ClassPO.findAll", query = "SELECT c FROM ClassPO c")
public class ClassPO implements Serializable, PersistenceObject<ClassIdPO> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ClassIdPO id;

	// bi-directional many-to-one association to StrategyPO
	@ManyToOne
	@JoinColumns(value = {
			@JoinColumn(name = "STRA_ACCO_OWNER", referencedColumnName = "ACCO_OWNER", insertable = false, updatable = false),
			@JoinColumn(name = "STRA_CURRENCY", referencedColumnName = "CURRENCY", insertable = false, updatable = false) }, foreignKey = @ForeignKey(name = "CLASS_STRA_FK"))
	private StrategyPO strategyPO;

	// bi-directional many-to-one association to ParameterPO
	@OneToMany(mappedBy = "classPO", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<ParameterPO> parameterPOs;

	// bi-directional many-to-one association to VariablePO
	@OneToMany(mappedBy = "classPO", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<VariablePO> variablePOs;

	public ClassPO() {
	}

	public ClassIdPO getId() {
		return this.id;
	}

	public void setId(ClassIdPO id) {
		this.id = id;
	}

	public StrategyPO getStrategyPO() {
		return this.strategyPO;
	}

	public void setStrategyPO(StrategyPO strategyPO) {
		this.strategyPO = strategyPO;
	}

	public List<ParameterPO> getParameterPOs() {
		return this.parameterPOs;
	}

	public void setParameterPOs(List<ParameterPO> parameterPOs) {
		this.parameterPOs = parameterPOs;
	}

	public ParameterPO addParameterPO(ParameterPO parameterPO) {
		getParameterPOs().add(parameterPO);
		parameterPO.setClassPO(this);

		return parameterPO;
	}

	public ParameterPO removeParameterPO(ParameterPO parameterPO) {
		getParameterPOs().remove(parameterPO);
		parameterPO.setClassPO(null);

		return parameterPO;
	}

	public List<VariablePO> getVariablePOs() {
		return this.variablePOs;
	}

	public void setVariablePOs(List<VariablePO> variablePOs) {
		this.variablePOs = variablePOs;
	}

	public VariablePO addVariable(VariablePO variablePO) {
		getVariablePOs().add(variablePO);
		variablePO.setClassPO(this);

		return variablePO;
	}

	public VariablePO removeVariable(VariablePO variablePO) {
		getVariablePOs().remove(variablePO);
		variablePO.setClassPO(null);

		return variablePO;
	}

	@Override
	public Class<?> getEntityClass() {
		return ClassPO.class;
	}

}