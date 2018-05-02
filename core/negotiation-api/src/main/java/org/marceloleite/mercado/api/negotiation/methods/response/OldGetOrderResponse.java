package org.marceloleite.mercado.api.negotiation.methods.response;

import java.util.HashMap;
import java.util.Map;

import org.marceloleite.mercado.jsonmodel.api.data.JsonOrder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "order" })
public class OldGetOrderResponse {

	@JsonProperty("order")
	private JsonOrder order = null;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("order")
	public JsonOrder getOrder() {
		return order;
	}

	@JsonProperty("order")
	public void setOrder(JsonOrder order) {
		this.order = order;
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
