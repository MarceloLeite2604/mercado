package org.marceloleite.mercado.jsonmodel.api.negotiation.getwithdrawal;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "coin", "fee", "status", "description", "created_timestamp", "updated_timestamp",
	"quantity", "net_quantity", "account", "address", "tx"})
public class JsonWithdrawal {

	@JsonProperty("id")
	private long id;

	@JsonProperty("coin")
	private String coin;

	@JsonProperty("quantity")
	private double quantity;
	
	@JsonProperty("net_quantity")
	private double net_quantity;
	
	@JsonProperty("account")
	private String account;

	@JsonProperty("fee")
	private double fee;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("status")
	private long status;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("created_timestamp")
	private long created_timestamp;
	
	@JsonProperty("updated_timestamp")
	private long updated_timestamp;
	
	@JsonProperty("tx")
	private String tx;
	
	@JsonProperty("id")
	public long getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(long id) {
		this.id = id;
	}

	@JsonProperty("coin")
	public String getCoin() {
		return coin;
	}

	@JsonProperty("coin")
	public void setCoin(String coin) {
		this.coin = coin;
	}

	@JsonProperty("quantity")
	public double getQuantity() {
		return quantity;
	}

	@JsonProperty("quantity")
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	@JsonProperty("net_quantity")
	public double getNet_quantity() {
		return net_quantity;
	}

	@JsonProperty("net_quantity")
	public void setNet_quantity(double net_quantity) {
		this.net_quantity = net_quantity;
	}

	@JsonProperty("account")
	public String getAccount() {
		return account;
	}

	@JsonProperty("account")
	public void setAccount(String account) {
		this.account = account;
	}

	@JsonProperty("fee")
	public double getFee() {
		return fee;
	}

	@JsonProperty("fee")
	public void setFee(double fee) {
		this.fee = fee;
	}

	@JsonProperty("address")
	public String getAddress() {
		return address;
	}

	@JsonProperty("address")
	public void setAddress(String address) {
		this.address = address;
	}

	@JsonProperty("status")
	public long getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(long status) {
		this.status = status;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("created_timestamp")
	public long getCreated_timestamp() {
		return created_timestamp;
	}

	@JsonProperty("created_timestamp")
	public void setCreated_timestamp(long created_timestamp) {
		this.created_timestamp = created_timestamp;
	}

	@JsonProperty("updated_timestamp")
	public long getUpdated_timestamp() {
		return updated_timestamp;
	}

	@JsonProperty("updated_timestamp")
	public void setUpdated_timestamp(long updated_timestamp) {
		this.updated_timestamp = updated_timestamp;
	}

	@JsonProperty("tx")
	public String getTx() {
		return tx;
	}

	@JsonProperty("tx")
	public void setTx(String tx) {
		this.tx = tx;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}
