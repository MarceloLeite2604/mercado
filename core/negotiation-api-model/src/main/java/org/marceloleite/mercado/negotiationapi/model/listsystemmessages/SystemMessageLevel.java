package org.marceloleite.mercado.negotiationapi.model.listsystemmessages;

public enum SystemMessageLevel {

	INFO,
	WARNING,
	ERROR;

	@Override
	public String toString() {
		return name().toUpperCase();
	}

	public static SystemMessageLevel getByName(String level) {
		if ( level == null || level.isEmpty() ) {
			throw new IllegalArgumentException("System level cannot be null.");
		}
		for (SystemMessageLevel systemMessageLevel : values()) {
			if ( level.equals(systemMessageLevel.name().toUpperCase())) {
				return systemMessageLevel;
			}
		}
		throw new IllegalArgumentException("Invalid system level: " + level);
	}
}
