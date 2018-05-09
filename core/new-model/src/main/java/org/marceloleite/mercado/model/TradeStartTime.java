package org.marceloleite.mercado.model;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.attributeconverter.ZonedDateTimeAttributeConverter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "TRADES_START_TIME")
@JsonIgnoreProperties({ "id" })
@JsonPropertyOrder({ "currency" ,"startTime"})
@XmlRootElement(name = "tradeStartTime")
@XmlType(propOrder = { "currency" ,"startTime" })
public class TradeStartTime {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CURRENCY", nullable = false, length = 4)
	private Currency currency;

	@Column(name = "START_TIME", nullable = false)
	@Convert(converter = ZonedDateTimeAttributeConverter.class)
	private ZonedDateTime startTime;

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@XmlElement
	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}
	
	
}
