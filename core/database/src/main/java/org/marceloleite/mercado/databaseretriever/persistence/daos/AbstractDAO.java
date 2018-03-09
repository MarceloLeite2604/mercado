package org.marceloleite.mercado.databaseretriever.persistence.daos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;
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
		List<E> retrievedObjects = executeOperationWithResult(JpaOperation.FIND_BY_ID, Arrays.asList(persistenceObject));
		if ( !retrievedObjects.isEmpty()) {
			return retrievedObjects.get(0);
		} else {
			return null;
		}
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
			default:
				throw new IllegalStateException("Invalid void operation " + jpaOperation + ".");
			}
		}

		commitTransaction();
		closeEntityManager();
	}

	public List<E> executeOperationWithResult(JpaOperation jpaOperation,
			List<E> persistenceObjects) {
		createEntityManager();
		createTransaction();

		List<E> retrievedPersistenceObjects = new ArrayList<>();
		for (E persistenceObject : persistenceObjects) {
			switch (jpaOperation) {
			case FIND_BY_ID:
				
				PersistenceObject<?> persistenceObjectRetrieved = entityManager.find(persistenceObject.getClass(),
						persistenceObject.getId());
				retrievedPersistenceObjects.add(castPersistenceObject(persistenceObjectRetrieved));
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
	protected List<? extends PersistenceObject<?>> executeQuery(String stringQuery, Map<String, Object> parameters) {
		createEntityManager();
		// createTransaction();
		Query query = createNativeQuery(stringQuery);
		if (null != parameters && !parameters.isEmpty()) {
			for (String parameterName : parameters.keySet()) {
				Object parameterValue = parameters.get(parameterName);
				query.setParameter(parameterName, parameterValue);
			}
		}
		List<? extends PersistenceObject<?>> result = query.getResultList();
		closeEntityManager();
		return result;
	}

	protected PersistenceObject<?> executeQueryForSingleResult(String queryString, Map<String, Object> parameters) {
		createEntityManager();
		List<? extends PersistenceObject<?>> queryResult = executeQuery(queryString, parameters);
		if (queryResult.size() > 0) {
			return queryResult.get(0);
		} else {
			return null;
		}
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
