package org.marceloleite.mercado.dao.database;

import org.marceloleite.mercado.dao.database.repository.PropertyRepository;
import org.marceloleite.mercado.dao.interfaces.PropertyDAO;
import org.marceloleite.mercado.model.Property;
import org.springframework.beans.factory.annotation.Autowired;

public class PropertyDatabaseDAO implements PropertyDAO {

	@Autowired
	private PropertyRepository propertyRepository;

	@Override
	public Property findByName(String name) {
		return propertyRepository.findByName(name);
	}

	@Override
	public <S extends Property> S save(S entity) {
		return propertyRepository.save(entity);
	}
}
