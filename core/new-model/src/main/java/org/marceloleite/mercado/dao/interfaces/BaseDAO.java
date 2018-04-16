package org.marceloleite.mercado.dao.interfaces;

public interface BaseDAO<T> {

	public <S extends T> S save(S entity);
}
