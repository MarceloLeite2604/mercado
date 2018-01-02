package org.marceloleite.mercado.simulator.property;

import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardProperty;

public enum SimulatorProperty {

	BALANCE_REAL_INITIAL_VALUE("simulator.house.tradePercentage", false);

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
