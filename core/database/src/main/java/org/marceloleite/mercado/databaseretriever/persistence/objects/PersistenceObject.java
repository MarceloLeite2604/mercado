package org.marceloleite.mercado.databaseretriever.persistence.objects;

public interface PersistenceObject<I> {

	Class<?> getEntityClass();
	
	I getId();
}
