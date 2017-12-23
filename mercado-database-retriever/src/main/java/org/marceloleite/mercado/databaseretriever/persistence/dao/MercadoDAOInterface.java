package org.marceloleite.mercado.databaseretriever.persistence.dao;

import java.util.List;

import org.marceloleite.mercado.databasemodel.DatabaseEntity;

public interface MercadoDAOInterface {

	void merge(DatabaseEntity<?> entity);
	
	void merge(List<? extends DatabaseEntity<?>> entities);
	
	void persist(DatabaseEntity<?> entity);
	
	void persist(List<? extends DatabaseEntity<?>> entities);
	
	void remove(DatabaseEntity<?> entity);
	
	void remove(List<? extends DatabaseEntity<?>> entities);
	
	DatabaseEntity<?> findById(DatabaseEntity<?> entity);
	
	List<DatabaseEntity<?>> findById(List<DatabaseEntity<?>> entities);
	
}
