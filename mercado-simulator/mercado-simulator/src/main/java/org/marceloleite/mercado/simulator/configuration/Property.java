package org.marceloleite.mercado.simulator.configuration;

public enum Property {

	BALANCE_REAL_INITIAL_VALUE("balance.real.initialValue");

	private String name;

	private Property(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
