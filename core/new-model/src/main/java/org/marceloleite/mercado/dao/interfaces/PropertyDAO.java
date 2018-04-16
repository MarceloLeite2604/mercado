package org.marceloleite.mercado.dao.interfaces;

import org.marceloleite.mercado.model.Property;

public interface PropertyDAO extends BaseDAO<Property> {

	public Property findByName(String accountOwner);
}
