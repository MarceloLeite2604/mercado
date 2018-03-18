package org.marceloleite.mercado.jsonmodel.api.negotiation;

import java.util.HashMap;
import java.util.Map;

import org.marceloleite.mercado.jsonmodel.api.negotiation.getwithdrawal.JsonWithdrawal;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "withdrawal" })
public class JsonWithdrawCoinResponse {

	@JsonProperty("withdrawal")
	private JsonWithdrawal jsonWithdrawal;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("withdrawal")
	public JsonWithdrawal getJsonWithdrawal() {
		return jsonWithdrawal;
	}

	@JsonProperty("withdrawal")
	public void setJsonWithdrawal(JsonWithdrawal jsonWithdrawal) {
		this.jsonWithdrawal = jsonWithdrawal;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}
