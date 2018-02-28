package org.marceloleite.mercado.databaseretriever.persistence.objects.old;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

// @Entity(name = "CLASSES_PARAMETERS")
public class OldClassParameterPO implements PersistenceObject<OldClassParameterIdPO> {

	@EmbeddedId
	private OldClassParameterIdPO strategyParameterIdPO;

	@Column(name = "VALUE")
	private String value;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "STCL_STRA_ACCO_OWNER", referencedColumnName = "STRA_ACCO_OWNER", insertable = false, updatable = false),
			@JoinColumn(name = "STCL_STRA_CURRENCY", referencedColumnName = "STRA_CURRENCY", insertable = false, updatable = false),
			@JoinColumn(name = "STCL_CLASS_NAME", referencedColumnName = "CLASS_NAME", insertable = false, updatable = false)})
	private OldStrategyClassPO strategyClassPO;

	@Override
	public Class<?> getEntityClass() {
		return OldClassParameterPO.class;
	}

	@Override
	public OldClassParameterIdPO getId() {
		return strategyParameterIdPO;
	}

	public OldClassParameterIdPO getStrategyParameterIdPO() {
		return strategyParameterIdPO;
	}

	public void setStrategyParameterIdPO(OldClassParameterIdPO strategyParameterIdPO) {
		this.strategyParameterIdPO = strategyParameterIdPO;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public OldStrategyClassPO getStrategyClassPO() {
		return strategyClassPO;
	}

	public void setStrategyClassPO(OldStrategyClassPO strategyClassPO) {
		this.strategyClassPO = strategyClassPO;
	}
}
