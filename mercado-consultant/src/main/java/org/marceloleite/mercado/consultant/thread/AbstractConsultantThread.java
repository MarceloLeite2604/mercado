package org.marceloleite.mercado.consultant.thread;

import org.marceloleite.mercado.consultant.thread.properties.ConsultantThreadProperties;
import org.marceloleite.mercado.consultant.thread.properties.ConsultantThreadPropertiesRetriever;

public abstract class AbstractConsultantThread extends Thread {

	private ConsultantThreadProperties consultantProperties;

	public AbstractConsultantThread(ConsultantThreadPropertiesRetriever consultantPropertiesRetriever) {
		this.consultantProperties = consultantPropertiesRetriever.retrieveProperties();
	}

	protected ConsultantThreadProperties getConsultantProperties() {
		return consultantProperties;
	}

}
