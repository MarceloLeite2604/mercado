package org.marceloleite.mercado.modeler.persistence.dao;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.cfg.NotYetImplementedException;

public class MercadoAbstractDAO<E, I> implements MercadoDAOInterface<E, I> {

	@Override
	public void merge(E entity) {
		createEntityManagerForOperation(JpaOperation.MERGE, Arrays.asList(entity));
	}

	@Override
	public void persist(E entity) {
		createEntityManagerForOperation(JpaOperation.PERSIST, Arrays.asList(entity));
	}

	@Override
	public void remove(E entity) {
		createEntityManagerForOperation(JpaOperation.REMOVE, Arrays.asList(entity));

	}

	@Override
	public void findById(I id) {
		throw new NotYetImplementedException();
	}

	private void createEntityManagerForOperation(JpaOperation jpaOperation, List<E> entities) {
		EntityManager entityManager = EntityManagerController.getInstance().createEntityManager();
		openTransactionForOperation(entityManager, jpaOperation, entities);
		entityManager.close();
	}

	private void openTransactionForOperation(EntityManager entityManager, JpaOperation jpaOperation, List<E> entities) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		executeOperation(entityManager, jpaOperation, entities);
		transaction.commit();
	}

	private void executeOperation(EntityManager entityManager, JpaOperation jpaOperation, List<E> entities) {
		for (E entity : entities) {
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

	@Override
	public void persist(List<E> entity) {

	}
}
