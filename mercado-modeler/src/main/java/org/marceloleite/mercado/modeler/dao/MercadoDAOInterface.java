package org.marceloleite.mercado.modeler.dao;

public interface MercadoDAOInterface<E, I> {

	void insert(E entity);
	
	void update(E entity);
	
	void delete(E entity);
	
	void findById(I id);
	
}
