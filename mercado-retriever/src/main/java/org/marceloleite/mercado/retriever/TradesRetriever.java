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

	private EntityManager entityManager;

	public static void main(String[] args) {
		TradesRetriever tradesRetriever = new TradesRetriever();
		LocalDateTime maximumTimeRetrieved = tradesRetriever.obtainMaximumTimeRetrieved();
		LocalDateTime minimumTimeRetrieved = tradesRetriever.obtainMinimumTimeRetrieved();
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		
		System.out.println(localDateTimeToStringConverter.convert(minimumTimeRetrieved));
		System.out.println(localDateTimeToStringConverter.convert(maximumTimeRetrieved));
		
	}

	public TradesRetriever() {
		entityManager = EntityManagerController.getInstance()
			.createEntityManager();
	}

	private LocalDateTime obtainMaximumTimeRetrieved() {
		Timestamp result = getQueryResultAsTimestamp(QUERY_MAXIMUM_DATE_RETRIEVED);
		return new LocalDateTimeAttributeConverter().convertToEntityAttribute(result);
	}

	private LocalDateTime obtainMinimumTimeRetrieved() {
		Timestamp result = getQueryResultAsTimestamp(QUERY_MINIMUM_DATE_RETRIEVED);
		return new LocalDateTimeAttributeConverter().convertToEntityAttribute(result);
	}

	private Timestamp getQueryResultAsTimestamp(String stringQuery) {
		Query query = createQuery(stringQuery);
		Object queryResult = query.getSingleResult();
		Timestamp result;
		if (queryResult instanceof Timestamp) {
			result = (Timestamp) queryResult;
		} else {
			throw new RuntimeException("Cannot convert query result to \"" + Timestamp.class.getName()
					+ "\". Object is of type \"" + queryResult.getClass()
						.getName()
					+ "\".");
		}
		return result;
	}

	private Query createQuery(String query) {
		return entityManager.createQuery(query);
	}
}
