package org.marceloleite.mercado.consultant;

import org.marceloleite.mercado.configuration.StandardConfiguration;

public class Consultant {
	
	StandardConfiguration standardConfiguration;

	public void startConsulting() {
		retrieveConfiguration();
	}

	private void retrieveConfiguration() {
		standardConfiguration.readConfiguration();
	}
	
	

	
}
