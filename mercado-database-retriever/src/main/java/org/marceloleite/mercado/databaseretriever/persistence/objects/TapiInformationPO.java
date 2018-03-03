package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the TAPI_INFORMATIONS database table.
 * 
 */
@Entity
@Table(name="TAPI_INFORMATIONS")
@NamedQuery(name="TapiInformation.findAll", query="SELECT t FROM TapiInformationPO t")
public class TapiInformationPO implements Serializable, PersistenceObject<TapiInformationIdPO>  {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TapiInformationIdPO id;
	
	private String secret;

	//bi-directional many-to-one association to AccountPOLixo
	@OneToOne
	@JoinColumn(name="ACCO_OWNER", insertable=false, updatable=false)
	private AccountPO accountPO;

	public TapiInformationPO() {
	}

	public TapiInformationIdPO getId() {
		return this.id;
	}

	public void setId(TapiInformationIdPO id) {
		this.id = id;
	}

	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public AccountPO getAccountPO() {
		return this.accountPO;
	}

	public void setAccountPO(AccountPO account) {
		this.accountPO = account;
	}

	@Override
	public Class<?> getEntityClass() {
		return TapiInformationPO.class;
	}
}