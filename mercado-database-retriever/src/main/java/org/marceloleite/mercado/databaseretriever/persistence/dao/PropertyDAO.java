package org.marceloleite.mercado.databaseretriever.persistence.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.Entity;
import org.marceloleite.mercado.databasemodel.PersistenceObject;
import org.marceloleite.mercado.databasemodel.PropertyPO;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.converter.CurrencyAttributeConverter;

public class PropertyDAO extends AbstractDAO {

	private static final String NAME_PREFIX_PARAMETER = "namePrefix";

	private static final String RETRIEVE_BY_PREFIX_QUERY = "SELECT * FROM " + Entity.PROPERTY.getEntityClass()
			+ " WHERE name LIKE :" + NAME_PREFIX_PARAMETER;

	public List<PropertyPO> retrieveAll(String namePrefix) {
		createEntityManager();

		Map<String, String> parameters = new HashMap<>();
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
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
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return PropertyPO.class;
	}

}
