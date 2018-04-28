package org.marceloleite.mercado.dao.database;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.dao.database.repository.PropertyRepository;
import org.marceloleite.mercado.dao.interfaces.PropertyDAO;
import org.marceloleite.mercado.model.Property;
import org.springframework.stereotype.Repository;

@Repository
@Named("PropertyDatabaseDAO")
public class PropertyDatabaseDAO implements PropertyDAO {

	@Inject
	private PropertyRepository propertyRepository;

	@Override
	public Property findByName(String name) {
		return propertyRepository.findByName(name);
	}
	
	@Override
	public Iterable<Property> findAll() {
		return propertyRepository.findAll();
	}

	@Override
	public <S extends Property> S save(S entity) {
		return propertyRepository.save(entity);
	}

	@Override
	public <S extends Property> Iterable<S> saveAll(Iterable<S> entities) {
		return propertyRepository.saveAll(entities);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Property> Optional<S> findById(Long id) {
		return (Optional<S>) propertyRepository.findById(id);
	}
}
