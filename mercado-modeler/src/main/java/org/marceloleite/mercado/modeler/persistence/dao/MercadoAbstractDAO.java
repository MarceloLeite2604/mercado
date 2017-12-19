package org.marceloleite.mercado.modeler.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.cfg.NotYetImplementedException;

public class MercadoAbstractDAO<E, I> implements MercadoDAOInterface<E, I> {

	

	@Override
	public void merge(E entity) {
		createEntityManagerForOperation(JpaOperation.MERGE, entity);
	}

	@Override
	public void persist(E entity) {
		createEntityManagerForOperation(JpaOperation.PERSIST, entity);
	}

	@Override
	public void remove(E entity) {
		createEntityManagerForOperation(JpaOperation.REMOVE, entity);

	}

	@Override
	public void findById(I id) {
		throw new NotYetImplementedException();
	}

	private void createEntityManagerForOperation(JpaOperation jpaOperation, E entity) {
		EntityManager entityManager = EntityManagerController.getInstance().createEntityManager();
		openTransactionForOperation(entityManager, jpaOperation, entity);
		entityManager.close();
	}

	private void openTransactionForOperation(EntityManager entityManager, JpaOperation jpaOperation, E entity) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		executeOperation(entityManager, jpaOperation, entity);
		transaction.commit();
	}

	private void executeOperation(EntityManager entityManager, JpaOperation jpaOperation, E entity) {
		switch (jpaOperation) {
		case MERGE:
			entityManager.merge(entity);
			break;
		case PERSIST:
			entityManager.persist(entity);
			break;
		case REMOVE:
			entityManager.remove(entity);
			break;
		}
	}
}
