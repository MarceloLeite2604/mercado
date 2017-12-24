package org.marceloleite.mercado.databaseretriever.persistence.dao;

import java.util.List;

import org.marceloleite.mercado.databasemodel.PersistenceObject;

public interface DataAccessObject {

	void merge(PersistenceObject<?> entity);
	
	void merge(List<? extends PersistenceObject<?>> entities);
	
	void persist(PersistenceObject<?> entity);
	
	void persist(List<? extends PersistenceObject<?>> entities);
	
	void remove(PersistenceObject<?> entity);
	
	void remove(List<? extends PersistenceObject<?>> entities);
	
	PersistenceObject<?> findById(PersistenceObject<?> entity);
	
	List<PersistenceObject<?>> findById(List<PersistenceObject<?>> entities);
	
	Class<? extends PersistenceObject<?>> getPOClass();
	
}
