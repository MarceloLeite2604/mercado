package org.marceloleite.mercado.consultant.thread;

import java.time.Duration;

import org.marceloleite.mercado.consultant.thread.property.ConsultantThreadProperties;
import org.marceloleite.mercado.consultant.thread.property.ConsultantThreadPropertiesRetriever;

public abstract class AbstractConsultantThread extends Thread {

	private ConsultantThreadProperties consultantProperties;

	public AbstractConsultantThread(ConsultantThreadPropertiesRetriever consultantPropertiesRetriever) {
		this.consultantProperties = consultantPropertiesRetriever.retrieveProperties();
	}

	protected ConsultantThreadProperties getConsultantProperties() {
		return consultantProperties;
	}
	
	protected void threadSleep(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract boolean finished();

}
