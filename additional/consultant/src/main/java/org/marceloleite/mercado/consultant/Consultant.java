package org.marceloleite.mercado.consultant;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.consultant.property.ConsultantProperty;
import org.marceloleite.mercado.consultant.thread.BackwardConsultantThread;
import org.marceloleite.mercado.consultant.thread.ForwardConsultantThread;
import org.marceloleite.mercado.retriever.PropertyRetriever;

public class Consultant {

	public static final boolean IGNORE_DATABASE_PROPERTIES = true;

	PropertyRetriever propertyRetriever;

	private List<Thread> consultantThreads;

	public Consultant() {
		super();
		this.consultantThreads = new ArrayList<>();
		this.propertyRetriever = new PropertyRetriever();
	}

	public void startConsulting() {
		if (executeForwardConsultant()) {
			consultantThreads.add(new ForwardConsultantThread());
		}

		if (executeBackwardConsultant()) {
			consultantThreads.add(new BackwardConsultantThread());
		}

		for (Thread consultantThread : consultantThreads) {
			consultantThread.start();
		}

		try {
			for (Thread consultantThread : consultantThreads) {
				consultantThread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean executeForwardConsultant() {
		return retrieveBooleanProperty(ConsultantProperty.FORWARD_EXECUTE);
	}

	private boolean executeBackwardConsultant() {
		return retrieveBooleanProperty(ConsultantProperty.BACKWARD_EXECUTE);
	}

	private boolean retrieveBooleanProperty(ConsultantProperty consultantProperty) {
		StandardProperty standardProperty = propertyRetriever.retrieve(consultantProperty, IGNORE_DATABASE_PROPERTIES);
		return Boolean.parseBoolean(standardProperty.getValue());
	}

}
