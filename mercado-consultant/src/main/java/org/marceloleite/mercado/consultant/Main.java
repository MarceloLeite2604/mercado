package org.marceloleite.mercado.consultant;

import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {

	public static void main(String[] args) {
		
		Consultant consultant = new Consultant();
		consultant.startConsulting();
		EntityManagerController.getInstance().close();
	}
}
