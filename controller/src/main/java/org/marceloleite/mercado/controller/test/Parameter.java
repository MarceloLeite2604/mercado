package org.marceloleite.mercado.controller.test;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.marceloleite.mercado.databaseretriever.persistence.objects.ClassPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;


/**
 * The persistent class for the "PARAMETERS" database table.
 * 
 */
@Entity
@Table(name="\"PARAMETERS\"")
@NamedQuery(name="ParameterPO.findAll", query="SELECT p FROM ParameterPO p")
public class Parameter implements Serializable, PersistenceObject<ParameterId> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ParameterId id;

	@Column(name="\"VALUE\"", length=128)
	private String value;

	//bi-directional many-to-one association to ClassPO
//	@ManyToOne
//	@JoinColumns(value={
//		@JoinColumn(name="CLASS_NAME", referencedColumnName="NAME", insertable=false, updatable=false),
//		@JoinColumn(name="CLASS_STRA_ACCO_OWNER", referencedColumnName="STRA_ACCO_OWNER", insertable=false, updatable=false),
//		@JoinColumn(name="CLASS_STRA_CURRENCY", referencedColumnName="STRA_CURRENCY", insertable=false, updatable=false)
//		}, foreignKey = @ForeignKey(name = "PARA_CLASS_FK"))
//	private StrategyClass strategyClass;

	public Parameter() {
	}

	public ParameterId getId() {
		return this.id;
	}

	public void setId(ParameterId id) {
		this.id = id;
	}

	@XmlElement(name="value")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

//	public StrategyClass getClassPO() {
//		return this.strategyClass;
//	}

//	public void setClassPO(StrategyClass strategyClass) {
//		this.strategyClass = strategyClass;
//	}



	@Override
	public Class<?> getEntityClass() {
		return Parameter.class;
	}

	}