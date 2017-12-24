package org.marceloleite.mercado.consultant;

import org.marceloleite.mercado.properties.StandardPropertiesReader;

public class Consultant {
	
	StandardPropertiesReader standardConfiguration;

	public void startConsulting() {
		retrieveConfiguration();
	}

	private void retrieveConfiguration() {
		standardConfiguration.readConfiguration();
	}
	
	

	
}
