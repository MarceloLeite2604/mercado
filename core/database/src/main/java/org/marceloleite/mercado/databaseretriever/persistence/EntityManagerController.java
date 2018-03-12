package org.marceloleite.mercado.databaseretriever.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.marceloleite.mercado.databaseretriever.configuration.PersistenceConfiguration;

public final class EntityManagerController {

	private static EntityManagerController INSTANCE;

	private static final String PERSISTENCE_UNIT = "mercadoPU";

	private static String persistenceFileName;

	private EntityManagerFactory entityManagerFactory;

	private EntityManagerController() {
		PersistenceConfiguration persistenceConfiguration;
		if (persistenceFileName == null) {
			persistenceConfiguration = new PersistenceConfiguration();
		} else {
			persistenceConfiguration = new PersistenceConfiguration(persistenceFileName);
		}
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT,
				persistenceConfiguration.getProperties());
	}

	public static EntityManagerController getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EntityManagerController();
		}
		return INSTANCE;
	}

	public EntityManager createEntityManager() {
		return INSTANCE.entityManagerFactory.createEntityManager();
	}

	public void close() {
		INSTANCE.entityManagerFactory.close();
	}

	public static void setPersistenceFileName(String persistenceFileName) {
		EntityManagerController.persistenceFileName = persistenceFileName;
	}
}
