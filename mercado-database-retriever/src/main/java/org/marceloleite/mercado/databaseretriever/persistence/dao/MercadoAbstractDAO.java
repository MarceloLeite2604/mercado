package org.marceloleite.mercado.databaseretriever.persistence.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.marceloleite.mercado.databasemodel.DatabaseEntity;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.databaseretriever.util.JpaOperation;

public class MercadoAbstractDAO implements MercadoDAOInterface {

	private EntityManager entityManager;
	
	private EntityTransaction transaction;

	@Override
	public void merge(DatabaseEntity<?> databaseEntity) {
		executeOperation(JpaOperation.MERGE, Arrays.asList(databaseEntity));
	}

	@Override
	public void merge(List<? extends DatabaseEntity<?>> databaseEntities) {
		executeOperation(JpaOperation.MERGE, databaseEntities);
	}

	@Override
	public void persist(DatabaseEntity<?> databaseEntity) {
		executeOperation(JpaOperation.PERSIST, Arrays.asList(databaseEntity));
	}

	@Override
	public void persist(List<? extends DatabaseEntity<?>> databaseEntities) {
		executeOperation(JpaOperation.PERSIST, databaseEntities);
	}

	@Override
	public void remove(DatabaseEntity<?> entity) {
		executeOperation(JpaOperation.REMOVE, Arrays.asList(entity));
	}

	@Override
	public void remove(List<? extends DatabaseEntity<?>> entities) {
		executeOperation(JpaOperation.REMOVE, entities);
	}

	@Override
	public DatabaseEntity<?> findById(DatabaseEntity<?> entity) {
		return null;
	}

	@Override
	public List<DatabaseEntity<?>> findById(List<DatabaseEntity<?>> entities) {
		return null;
	}

	public void executeOperation(JpaOperation jpaOperation, List<? extends DatabaseEntity<?>> databaseEntities) {
		createEntityManager();
		createTransaction();

		for (DatabaseEntity<?> databaseEntity : databaseEntities) {
			switch (jpaOperation) {
			case MERGE:
				entityManager.merge(databaseEntity);
				break;
			case PERSIST:
				entityManager.persist(databaseEntity);
				break;
			case REMOVE:
				entityManager.remove(databaseEntity);
				break;
			case FIND_BY_ID:
				entityManager.find(databaseEntity.getClass(), databaseEntity.getId());
				break;
			default:
				throw new IllegalStateException("Invalid void operation " + jpaOperation + ".");
			}
		}

		commitTransaction();
		closeEntityManager();
	}

	public List<DatabaseEntity<?>> executeOperationWithResult(JpaOperation jpaOperation,
			List<DatabaseEntity<?>> databaseEntities) {
		createEntityManager();
		createTransaction();

		List<DatabaseEntity<?>> retrievedDatabaseEntities = new ArrayList<>();
		for (DatabaseEntity<?> databaseEntity : databaseEntities) {
			switch (jpaOperation) {
			case FIND_BY_ID:
				DatabaseEntity<?> retrievedDatabaseEntity = entityManager.find(databaseEntity.getClass(),
						databaseEntity.getId());
				retrievedDatabaseEntities.add(retrievedDatabaseEntity);
				break;
			default:
				throw new IllegalStateException("Invalid returning operation " + jpaOperation + ".");
			}
		}

		commitTransaction();
		closeEntityManager();

		return retrievedDatabaseEntities;
	}

	private void createEntityManager() {
		entityManager = EntityManagerController.getInstance().createEntityManager();
	}

	private void closeEntityManager() {
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

}
