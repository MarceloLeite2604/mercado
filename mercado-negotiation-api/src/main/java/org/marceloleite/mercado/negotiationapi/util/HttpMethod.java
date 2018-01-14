package org.marceloleite.mercado.negotiationapi.util;

public enum HttpMethod {
	GET,
	POST;
	
	@Override
	public String toString() {
		return this.name();
	}
}
