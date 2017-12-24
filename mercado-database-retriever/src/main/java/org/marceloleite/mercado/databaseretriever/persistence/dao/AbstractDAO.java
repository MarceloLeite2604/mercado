package org.marceloleite.mercado.databaseretriever.persistence.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.marceloleite.mercado.databasemodel.PersistenceObject;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.databaseretriever.util.JpaOperation;

public abstract class AbstractDAO<E extends PersistenceObject<?>> implements DataAccessObject<E> {

	private EntityManager entityManager;
	
	private EntityTransaction transaction;

	@Override
	public void merge(E persistenceObject) {
		executeOperation(JpaOperation.MERGE, Arrays.asList(persistenceObject));
	}

	@Override
	public void merge(List<E> persistenceObjects) {
		executeOperation(JpaOperation.MERGE, persistenceObjects);
	}

	@Override
	public void persist(E persistenceObject) {
		executeOperation(JpaOperation.PERSIST, Arrays.asList(persistenceObject));
	}

	@Override
	public void persist(List<E> persistenceObjects) {
		executeOperation(JpaOperation.PERSIST, persistenceObjects);
	}

	@Override
	public void remove(E persistenceObject) {
		executeOperation(JpaOperation.REMOVE, Arrays.asList(persistenceObject));
	}

	@Override
	public void remove(List<E> persistenceObjects) {
		executeOperation(JpaOperation.REMOVE, persistenceObjects);
	}

	@Override
	public E findById(E persistenceObject) {
		return null;
	}

	@Override
	public List<E> findById(List<E> persistenceObjects) {
		return null;
	}

	public void executeOperation(JpaOperation jpaOperation, List<? extends PersistenceObject<?>> persistenceObjects) {
		createEntityManager();
		createTransaction();

		for (PersistenceObject<?> persistenceObject : persistenceObjects) {
			switch (jpaOperation) {
			case MERGE:
				entityManager.merge(persistenceObject);
				break;
			case PERSIST:
				entityManager.persist(persistenceObject);
				break;
			case REMOVE:
				entityManager.remove(persistenceObject);
				break;
			case FIND_BY_ID:
				entityManager.find(persistenceObject.getClass(), persistenceObject.getId());
				break;
			default:
				throw new IllegalStateException("Invalid void operation " + jpaOperation + ".");
			}
		}

		commitTransaction();
		closeEntityManager();
	}

	public List<PersistenceObject<?>> executeOperationWithResult(JpaOperation jpaOperation,
			List<PersistenceObject<?>> persistenceObjects) {
		createEntityManager();
		createTransaction();

		List<PersistenceObject<?>> retrievedPersistenceObjects = new ArrayList<>();
		for (PersistenceObject<?> persistenceObject : persistenceObjects) {
			switch (jpaOperation) {
			case FIND_BY_ID:
				PersistenceObject<?> retrievedDatabaseEntity = entityManager.find(persistenceObject.getClass(),
						persistenceObject.getId());
				retrievedPersistenceObjects.add(retrievedDatabaseEntity);
				break;
			default:
				throw new IllegalStateException("Invalid returning operation " + jpaOperation + ".");
			}
		}

		commitTransaction();
		closeEntityManager();

		return retrievedPersistenceObjects;
	}
	
	@SuppressWarnings("unchecked")
	protected List<? extends PersistenceObject<?>> executeQuery(String stringQuery, Map<String, String> parameters) {
		createEntityManager();
		// createTransaction();
		Query query = createNativeQuery(stringQuery);
		for (String parameterName : parameters.keySet()) {
			String parameterValue = parameters.get(parameterName);
			query.setParameter(parameterName, parameterValue);
		}
		List<? extends PersistenceObject<?>> result = query.getResultList();
		// commitTransaction();
		closeEntityManager();
		return result;
	}

	private void closeEntityManager() {
		createEntityManager();
		entityManager.close();
		entityManager = null;
	}

	private void createTransaction() {
		transaction = entityManager.getTransaction();
		transaction.begin();
	}

	private void commitTransaction() {
		transaction.commit();
		transaction = null;
	}
	
	protected void createEntityManager() {
		if (entityManager == null) {
			entityManager = EntityManagerController.getInstance().createEntityManager();
		}
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	protected Query createNativeQuery(String query) {
		createEntityManager();
		return getEntityManager().createNativeQuery(query, getPOClass());
	}

}
