package org.marceloleite.mercado.dao.interfaces;

public interface BaseDAO<T> {

	public <S extends T> S save(S entity);
	
	public <S extends T> Iterable<S> saveAll(Iterable<S> entities);
	
	public Iterable<T> findAll();
}
