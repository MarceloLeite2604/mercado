package org.marceloleite.mercado.strategies.sixth;

enum SixthStrategyStatus {
	APPLIED("applied"),
	SAVED("saved");

	private String name;

	private SixthStrategyStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static SixthStrategyStatus findByName(String name) {

		if (name == null) {
			throw new IllegalArgumentException("Status name cannot be null.");
		}

		for (SixthStrategyStatus sixthStrategyStatus : SixthStrategyStatus.values()) {
			if (sixthStrategyStatus.getName().equalsIgnoreCase(name)) {
				return sixthStrategyStatus;
			}
		}

		throw new IllegalArgumentException("Could not find a status with name \"" + name + "\".");
	}
}