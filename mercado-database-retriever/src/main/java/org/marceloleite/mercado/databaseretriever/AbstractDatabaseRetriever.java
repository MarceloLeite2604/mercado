package org.marceloleite.mercado.databaseretriever;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public abstract class AbstractDatabaseRetriever implements DataBaseRetriever {
	
	private EntityManager entityManager;

	protected void createEntityManager() {
		if (entityManager == null) {
			entityManager = EntityManagerController.getInstance().createEntityManager();
		}
	}
	
	protected void finishEntityManager() {
		createEntityManager();
		entityManager.close();
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	


	protected Query createNativeQuery(String query) {
		createEntityManager();
		return getEntityManager().createNativeQuery(query, getEntityClass());
	}
}
