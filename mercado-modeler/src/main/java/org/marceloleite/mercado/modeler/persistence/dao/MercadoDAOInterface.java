package org.marceloleite.mercado.modeler.persistence.dao;

import java.util.List;

public interface MercadoDAOInterface<E, I> {

	void merge(E entity);
	
	void persist(E entity);
	
	void persist(List<E> entity);
	
	void remove(E entity);
	
	void findById(I id);
	
}
