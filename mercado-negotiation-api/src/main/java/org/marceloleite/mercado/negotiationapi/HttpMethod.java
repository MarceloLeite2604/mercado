package org.marceloleite.mercado.negotiationapi;

public enum HttpMethod {
	GET,
	POST;
	
	@Override
	public String toString() {
		return this.name();
	}
}
