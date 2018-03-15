package org.marceloleite.mercado.configurator;

import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {

	public static void main(String[] args) {
		try { 
		new Configurator().configureSystem();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
}
