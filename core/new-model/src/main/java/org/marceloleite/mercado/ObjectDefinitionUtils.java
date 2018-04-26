package org.marceloleite.mercado;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public class ObjectDefinitionUtils {

	private static ObjectDefinitionUtils instance;

	private ObjectDefinitionUtils() {
	}
	
	public Map<String, ObjectDefinition> elaborateObjectDefinitionsMap(ObjectDefinition[] objectDefinitions) {
		return elaborateObjectDefinitionsMap(Arrays.asList(objectDefinitions));
	}

	public Map<String, ObjectDefinition> elaborateObjectDefinitionsMap(List<ObjectDefinition> objectDefinitions) {
		return objectDefinitions.stream()
				.collect(Collectors.toMap(ObjectDefinition::getName, objectDefinition -> objectDefinition));
	}

	public ObjectDefinition findByName(List<ObjectDefinition> objectDefinitions, String name) {
		
		if ( objectDefinitions == null) {
			throw new IllegalArgumentException("Object definitions list is null.");
		}
		
		if (name == null ) {
			throw new IllegalArgumentException("Object name is null.");
		}
		
		return objectDefinitions.stream()
				.filter(objectDefinition -> name.equals(name))
				.findFirst()
				.orElse(null);
	}
	
	public static Enum<? extends ObjectDefinition> findByName(Class<? extends Enum<? extends ObjectDefinition>> enumClass, String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Parameter name is empty.");
		}
		Enum<? extends ObjectDefinition>[] enumConstants = enumClass.getEnumConstants();
		for (Enum<? extends ObjectDefinition> enumConstant : enumConstants) {
			if (((Property) enumConstant).getName().equals(name)) {
				return enumConstant;
			}
		}
		throw new RuntimeException("Could not find a parameter with name \"" + name + "\".");
	}

	public static ObjectDefinitionUtils getInstance() {
		if (instance == null) {
			instance = new ObjectDefinitionUtils();
		}
		return instance;
	}
}
