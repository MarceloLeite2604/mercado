package org.marceloleite.mercado.databaseretriever.persistence.dao;

import java.util.List;

import org.marceloleite.mercado.databasemodel.PersistenceObject;

public interface DataAccessObject<E extends PersistenceObject<?>> {

	void merge(E entity);

	void merge(List<E> entities);

	void persist(E entity);

	void persist(List<E> entities);

	void remove(E entity);

	void remove(List<E> entities);

	E findById(E entity);

	List<E> findById(List<E> entities);

	Class<? extends PersistenceObject<?>> getPOClass();
	
	E castPersistenceObject(PersistenceObject<?> persistenceObject);

}
