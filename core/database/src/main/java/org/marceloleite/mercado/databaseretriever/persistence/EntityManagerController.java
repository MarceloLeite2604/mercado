package org.marceloleite.mercado.databaseretriever.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.marceloleite.mercado.databaseretriever.configuration.PersistenceConfiguration;

public final class EntityManagerController {

	private static EntityManagerController INSTANCE;
	
	private static final String PERSISTENCE_UNIT = "mercadoPU";

	private EntityManagerFactory entityManagerFactory;
	
	private EntityManagerController() {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, new PersistenceConfiguration().getProperties());
	}
	
	public static EntityManagerController getInstance() {
		if ( INSTANCE == null ) {
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
}
