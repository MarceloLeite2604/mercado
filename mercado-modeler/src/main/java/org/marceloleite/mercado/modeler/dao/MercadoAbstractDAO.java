package org.marceloleite.mercado.modeler.dao;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.cfg.NotYetImplementedException;

public class MercadoAbstractDAO<E, I> implements MercadoDAOInterface<E, I> {
	
	private static final String PERSISTENCE_UNIT = "mercadoPU";

	private EntityManager entityManager;

	public MercadoAbstractDAO() {
		this.entityManager = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT).createEntityManager();
	}

	@Override
	public void insert(E entity) {
		entityManager.merge(entity);
	}

	@Override
	public void update(E entity) {
		entityManager.merge(entity);	
	}

	@Override
	public void delete(E entity) {
		entityManager.remove(entity);
		
	}

	@Override
	public void findById(I id) {
		throw new NotYetImplementedException();
	}
}
