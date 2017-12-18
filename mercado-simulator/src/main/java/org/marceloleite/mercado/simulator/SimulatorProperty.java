package org.marceloleite.mercado.simulator;

import org.marceloleite.mercado.configuration.Property;
import org.marceloleite.mercado.configuration.StandardProperty;

public enum SimulatorProperty {

	BALANCE_REAL_INITIAL_VALUE("balance.real.initialValue", false);

	private String name;

	private boolean required;

	private SimulatorProperty(String name, boolean required) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	public Property toProperty() {
		return new StandardProperty(name, required);
	}
}
