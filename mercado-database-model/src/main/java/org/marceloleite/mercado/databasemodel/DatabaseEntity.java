package org.marceloleite.mercado.databasemodel;

public interface DatabaseEntity<I> {

	Class<?> getEntityClass();
	
	I getId();
}
