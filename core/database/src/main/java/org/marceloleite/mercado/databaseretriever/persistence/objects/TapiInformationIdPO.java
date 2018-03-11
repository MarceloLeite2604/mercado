package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TAPI_INFORMATIONS database table.
 * 
 */
@Embeddable
public class TapiInformationIdPO implements Serializable {
	
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ACCO_OWNER", length=64, insertable=false, updatable=false)
	private String accoOwner;

	@Column(name="ID", length=64)
	private String id;

	public TapiInformationIdPO() {
	}
	public String getAccoOwner() {
		return this.accoOwner;
	}
	public void setAccoOwner(String accoOwner) {
		this.accoOwner = accoOwner;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TapiInformationIdPO)) {
			return false;
		}
		TapiInformationIdPO castOther = (TapiInformationIdPO)other;
		return 
			this.accoOwner.equals(castOther.accoOwner)
			&& this.id.equals(castOther.id);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.accoOwner.hashCode();
		hash = hash * prime + this.id.hashCode();
		
		return hash;
	}
}