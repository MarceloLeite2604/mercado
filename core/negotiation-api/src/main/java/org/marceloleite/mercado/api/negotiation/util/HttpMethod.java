package org.marceloleite.mercado.api.negotiation.util;

public enum HttpMethod {
	GET,
	POST;
	
	@Override
	public String toString() {
		return this.name();
	}
}
