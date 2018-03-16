package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

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
 * The persistent class for the OPERATIONS database table.
 * 
 */
@Entity
@Table(name = "OPERATIONS")
@NamedQuery(name = "OperationPO.findAll", query = "SELECT o FROM OperationPO o")
public class OperationPO {

	@EmbeddedId
	private OperationIdPO operationIdPO;

	// bi-directional many-to-one association to ClassPO
	@ManyToOne
	@JoinColumns(value = {
			@JoinColumn(name = "ORDE_ACCO_OWNER", referencedColumnName = "ACCO_OWNER", insertable = false, updatable = false),
			@JoinColumn(name = "ORDE_ID", referencedColumnName = "ID", insertable = false, updatable = false), }, foreignKey = @ForeignKey(name = "OPER_ORDE_FK"))
	private OrderPO order;

	@Column(name = "QUANTITY")
	private BigDecimal quantity;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "FEE_RATE")
	private BigDecimal feeRate;

	@Column(name = "EXECUTED")
	private ZonedDateTime executed;
}
