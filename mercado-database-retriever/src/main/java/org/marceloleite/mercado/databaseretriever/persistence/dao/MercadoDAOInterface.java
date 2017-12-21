package org.marceloleite.mercado.databaseretriever.persistence.dao;

import java.util.List;

public interface MercadoDAOInterface<E, I> {

	void merge(E entity);
	
	void merge(List<E> entities);
	
	void persist(E entity);
	
	void persist(List<E> entities);
	
	void remove(E entity);
	
	void remove(List<E> entities);
	
	void findById(I id);
	
}
