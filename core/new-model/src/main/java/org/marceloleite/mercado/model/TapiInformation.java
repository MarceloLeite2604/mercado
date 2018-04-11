package org.marceloleite.mercado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "TAPI_INFORMATIONS")
@JsonIgnoreProperties({ "id" })
@JsonPropertyOrder({ "identification", "secret" })
@XmlRootElement(name = "tapiInformation")
@XmlType(propOrder = { "identification", "secret" })
public class TapiInformation {
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="IDENTIFICATION", length=128)
	private String identification;

	@Column(name="SECRET", length=128)
	private String secret;

	@OneToOne
	@JoinColumn(name = "ACCO_ID", foreignKey = @ForeignKey(name = "TAPI_ACCO_FK"))
	private Account account;

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name="identification")
	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	@XmlElement(name="secret")
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@XmlTransient
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
}