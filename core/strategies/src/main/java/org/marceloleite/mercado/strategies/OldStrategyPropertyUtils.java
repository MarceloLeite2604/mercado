package org.marceloleite.mercado.strategies;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.properties.StandardProperty;

public abstract class OldStrategyPropertyUtils {

	public static Enum<? extends Property> findByName(Class<? extends Enum<? extends Property>> enumClass, String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Parameter name is empty.");
		}
		Enum<? extends Property>[] enumConstants = enumClass.getEnumConstants();
		for (Enum<? extends Property> enumConstant : enumConstants) {
			if (((Property) enumConstant).getName().equals(name)) {
				return enumConstant;
			}
		}
		throw new RuntimeException("Could not find a parameter with name \"" + name + "\".");
	}

	public static List<StandardProperty> toStandardPropertyList(List<Enum<? extends Property>> enumProperties) {
		List<StandardProperty> properties = new ArrayList<>();
		for (Enum<? extends Property> enumProperty : enumProperties) {
			Property property = (Property) enumProperty;
			properties.add(new StandardProperty(property.getName(), property.getValue(), property.isRequired()));
		}
		return properties;
	}
}
