package org.marceloleite.mercado.modeler.persistence.dao;

public interface MercadoDAOInterface<E, I> {

	void merge(E entity);
	
	void persist(E entity);
	
	void remove(E entity);
	
	void findById(I id);
	
}
