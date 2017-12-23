package org.marceloleite.mercado.databaseretriever;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.databasemodel.DatabaseEntity;
import org.marceloleite.mercado.databasemodel.Entity;
import org.marceloleite.mercado.databasemodel.Property;
import org.marceloleite.mercado.databaseretriever.persistence.dao.PropertyDAO;

public class PropertyDatabaseRetriever extends AbstractDatabaseRetriever {

	private PropertyDAO propertyDAO;

	private static final String NAME_PREFIX_PARAMETER = "namePrefix";

	private static final String RETRIEVE_BY_PREFIX_QUERY = "SELECT * FROM " + Entity.PROPERTY.getEntityClass()
			+ " WHERE name LIKE :" + NAME_PREFIX_PARAMETER;

	public PropertyDatabaseRetriever() {
		propertyDAO = new PropertyDAO();
	}

	public Property retrieve(String name) {
		Property propertyForQuery = new Property();
		propertyForQuery.setName(name);
		DatabaseEntity<?> databaseEntity = propertyDAO.findById(propertyForQuery);
		Property property = null;
		if (databaseEntity instanceof Property) {
			property = (Property) databaseEntity;
		} else {
			new ClassCastExceptionThrower(Property.class, databaseEntity).throwException();
		}
		return property;
	}

	public List<Property> retrieveAll(String prefix) {
		createEntityManager();
		Query query = createNativeQuery(RETRIEVE_BY_PREFIX_QUERY);
		query.setParameter(NAME_PREFIX_PARAMETER, prefix);
		List<Property> properties = castObjectListToPropertiesList(query.getResultList());
		return properties;

	}

	public List<Property> castObjectListToPropertiesList(List<?> objects) {
		List<Property> properties = new ArrayList<>();

		for (Object object : objects) {
			if (object instanceof Property) {
				properties.add((Property) object);
			} else {
				new ClassCastExceptionThrower(getEntityClass(), object).throwException();
			}
		}

		return properties;
	}

	@Override
	public Class<?> getEntityClass() {
		return Property.class;
	}
}
