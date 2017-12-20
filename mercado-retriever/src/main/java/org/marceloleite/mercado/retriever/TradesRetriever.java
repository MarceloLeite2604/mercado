package org.marceloleite.mercado.retriever;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.modeler.persistence.dao.EntityManagerController;
import org.marceloleite.mercado.modeler.persistence.model.Tables;
import org.marceloleite.mercado.modeler.util.LocalDateTimeAttributeConverter;

public class TradesRetriever {

	private static final String QUERY_MAXIMUM_DATE_RETRIEVED = "SELECT max(date) FROM " + Tables.TRADE.getName();

	private static final String QUERY_MINIMUM_DATE_RETRIEVED = "SELECT min(date) FROM " + Tables.TRADE.getName();

	

	public void retrieve() {
		LocalDateTime maximumTimeRetrieved = obtainMaximumTimeRetrieved();
		LocalDateTime minimumTimeRetrieved = obtainMinimumTimeRetrieved();
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		if (minimumTimeRetrieved != null) {
			System.out.println(localDateTimeToStringConverter.convert(minimumTimeRetrieved));
		}

		if (maximumTimeRetrieved != null) {
			System.out.println(localDateTimeToStringConverter.convert(maximumTimeRetrieved));
		}
	}

	public TradesRetriever() {
		entityManager = EntityManagerController.getInstance().createEntityManager();
	}

	
}
