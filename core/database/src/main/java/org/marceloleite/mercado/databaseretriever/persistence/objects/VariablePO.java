package org.marceloleite.mercado.databaseretriever.persistence.objects;

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

/**
 * The persistent class for the "VARIABLES" database table.
 * 
 */
@Entity
@Table(name="\"VARIABLES\"")
@NamedQuery(name="VariablePO.findAll", query="SELECT v FROM VariablePO v")
public class VariablePO implements Serializable, PersistenceObject<VariableIdPO> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VariableIdPO id;

	@Column(name="\"VALUE\"")
	private String value;

	//bi-directional many-to-one association to ClassPO
	@ManyToOne
	@JoinColumns(value = {
		@JoinColumn(name="CLASS_NAME", referencedColumnName="NAME", insertable=false, updatable=false),
		@JoinColumn(name="CLASS_STRA_ACCO_OWNER", referencedColumnName="STRA_ACCO_OWNER", insertable=false, updatable=false),
		@JoinColumn(name="CLASS_STRA_CURRENCY", referencedColumnName="STRA_CURRENCY", insertable=false, updatable=false)
		}, foreignKey=@ForeignKey(name="VARI_CLASS_FK"))
	private ClassPO classPO;

	public VariablePO() {
	}

	public VariableIdPO getId() {
		return this.id;
	}

	public void setId(VariableIdPO id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ClassPO getClassPO() {
		return this.classPO;
	}

	public void setClassPO(ClassPO classPO) {
		this.classPO = classPO;
	}

	@Override
	public Class<?> getEntityClass() {
		return VariablePO.class;
	}

}