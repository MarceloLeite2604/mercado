package org.marceloleite.mercado.dao.interfaces;

import java.util.Optional;

public interface BaseDAO<T> {

	public <S extends T> S save(S entity);
	
	public <S extends T> Iterable<S> saveAll(Iterable<S> entities);
	
	public Iterable<T> findAll();
	
	public <S extends T> Optional<S> findById(Long id);
}
