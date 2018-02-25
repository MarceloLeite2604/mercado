package org.marceloleite.mercado.databaseretriever.persistence.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.databaseretriever.persistence.objects.Entity;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class PropertyDAO extends AbstractDAO<PropertyPO> {

	private static final String NAME_PREFIX_PARAMETER = "namePrefix";

	private static final String RETRIEVE_BY_PREFIX_QUERY = "SELECT * FROM " + Entity.PROPERTY.getEntityClass()
			+ " WHERE name LIKE :" + NAME_PREFIX_PARAMETER;

	public List<PropertyPO> retrieveAll(String namePrefix) {
		createEntityManager();

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(NAME_PREFIX_PARAMETER, namePrefix);
		List<? extends PersistenceObject<?>> queryResult = executeQuery(RETRIEVE_BY_PREFIX_QUERY, parameters);
		return castToPropertiesList(queryResult);
	}

	public List<PropertyPO> castToPropertiesList(List<?> objects) {
		List<PropertyPO> properties = new ArrayList<>();

		for (Object object : objects) {
			if (object instanceof PropertyPO) {
				properties.add((PropertyPO) object);
			} else {
				new ClassCastExceptionThrower(getPOClass(), object).throwException();
			}
		}

		return properties;
	}

	@Override
	public Class<PropertyPO> getPOClass() {
		return PropertyPO.class;
	}

	@Override
	public PropertyPO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof PropertyPO)) {
			new ClassCastExceptionThrower(PropertyPO.class, persistenceObject);
		}

		return (PropertyPO) persistenceObject;
	}
}
