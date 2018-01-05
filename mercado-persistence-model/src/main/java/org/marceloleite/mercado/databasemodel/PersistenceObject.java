package org.marceloleite.mercado.databasemodel;

public interface PersistenceObject<I> {

	Class<?> getEntityClass();
	
	I getId();
}
