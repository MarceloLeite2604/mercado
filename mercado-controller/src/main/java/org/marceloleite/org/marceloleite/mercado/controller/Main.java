package org.marceloleite.org.marceloleite.mercado.controller;

import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {

	public static void main(String[] args) {
		try {
		new Controller().start();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
}
