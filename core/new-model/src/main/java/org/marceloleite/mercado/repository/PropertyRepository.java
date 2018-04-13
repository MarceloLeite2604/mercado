package org.marceloleite.mercado.repository;

import org.marceloleite.mercado.model.Property;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepository extends CrudRepository<Property, Long> {

	public Property findByName(String accountOwner);
}
