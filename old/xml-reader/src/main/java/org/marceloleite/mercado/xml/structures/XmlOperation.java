package org.marceloleite.mercado.xml.structures;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="operation")
public class XmlOperation {

	private Long id;

	private Double quantity;

	private Double price;

	private Double feeRate;

	private ZonedDateTime executed;

	public XmlOperation(Long id, Double quantity, Double price, Double feeRate,
			ZonedDateTime executed) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.price = price;
		this.feeRate = feeRate;
		this.executed = executed;
	}
	
	public XmlOperation() {
		this(null, null, null, null, null);
	}

	@XmlElement
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@XmlElement
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@XmlElement
	public Double getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}

	@XmlElement
	public ZonedDateTime getExecuted() {
		return executed;
	}

	public void setExecuted(ZonedDateTime executed) {
		this.executed = executed;
	}
}
