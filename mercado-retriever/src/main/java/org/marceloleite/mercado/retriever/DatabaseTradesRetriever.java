package org.marceloleite.mercado.retriever;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.modeler.persistence.dao.EntityManagerController;
import org.marceloleite.mercado.modeler.persistence.model.Tables;
import org.marceloleite.mercado.modeler.persistence.model.Trade;
import org.marceloleite.mercado.modeler.util.LocalDateTimeAttributeConverter;

public class DatabaseTradesRetriever {
	
	private static final String QUERY_MAXIMUM_DATE_RETRIEVED = "SELECT max(date) FROM " + Tables.TRADE.getName();

	private static final String QUERY_MINIMUM_DATE_RETRIEVED = "SELECT min(date) FROM " + Tables.TRADE.getName();
	
	private EntityManager entityManager;

	private void createEntityManager() {
		entityManager = EntityManagerController.getInstance()
			.createEntityManager();
	}

	public List<Trade> retrieve(LocalDateTime start, LocalDateTime end) {
		createEntityManager();
		String startParameter = "start";
		String endParameter = "end";
		String stringQuery = "SELECT * FROM " + Tables.TRADE.getName() + " WHERE date BETWEEN :" + startParameter
				+ " AND :" + endParameter;
		Query query = createNativeQuery(stringQuery);
		LocalDateTimeAttributeConverter localDateTimeAttributeConverter = new LocalDateTimeAttributeConverter();
		query.setParameter(startParameter, localDateTimeAttributeConverter.convertToDatabaseColumn(start));
		query.setParameter(endParameter, localDateTimeAttributeConverter.convertToDatabaseColumn(end));
		List<Trade> trades = castObjectListToTradeList(query.getResultList());
		finishEntityManager();
		return trades;
	}
	
	private void finishEntityManager() {
		entityManager.close();
	}

	public List<Trade> castObjectListToTradeList(List<?> objects) {
		List<Trade> trades = new ArrayList<>();

		for (Object object : objects) {
			if (object instanceof Trade) {
				trades.add((Trade) object);
			} else {
				throw new ClassCastException(
						"Expecting object of type \"" + Trade.class.getName() + "\", but it is \"" + object.getClass()
							.getName() + "\" instead.");
			}
		}

		return trades;
	}

	private Query createNativeQuery(String query) {
		return entityManager.createNativeQuery(query, Trade.class);
	}

	public TimeInterval retrieveTimeIntervalAvailable() {
		LocalDateTime minimumTimeRetrieved = obtainMinimumTimeRetrieved();
		LocalDateTime maximumTimeRetrieved = obtainMaximumTimeRetrieved();
		
		if ( minimumTimeRetrieved != null && maximumTimeRetrieved != null ) {
			return new TimeInterval(minimumTimeRetrieved, maximumTimeRetrieved);
		} else {
			return null;	
		}
		
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
		if (queryResult != null) {
			if (queryResult instanceof Timestamp) {
				result = (Timestamp) queryResult;
			} else {
				throw new RuntimeException("Cannot convert query result to \"" + Timestamp.class.getName()
						+ "\". Object is of type \"" + queryResult.getClass().getName() + "\".");
			}
			return result;
		} else {
			return null;
		}
	}
	
	private Query createQuery(String query) {
		return entityManager.createQuery(query);
	}
}
