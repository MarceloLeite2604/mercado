package org.marceloleite.mercado.retriever;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.marceloleite.mercado.modeler.persistence.dao.EntityManagerController;
import org.marceloleite.mercado.modeler.persistence.model.Tables;
import org.marceloleite.mercado.modeler.persistence.model.Trade;
import org.marceloleite.mercado.modeler.util.LocalDateTimeAttributeConverter;

public class DatabaseTradesRetriever {

	private EntityManager entityManager;

	public DatabaseTradesRetriever() {
		entityManager = EntityManagerController.getInstance()
			.createEntityManager();
	}

	public List<Trade> retrieve(LocalDateTime start, LocalDateTime end) {
		String startParameter = ":start";
		String endParameter = ":end";
		String stringQuery = "SELECT * FROM " + Tables.TRADE.getName() + " WHERE date BETWEEN " + startParameter
				+ " AND " + endParameter;
		Query query = createNativeQuery(stringQuery);
		LocalDateTimeAttributeConverter localDateTimeAttributeConverter = new LocalDateTimeAttributeConverter();
		query.setParameter(startParameter, localDateTimeAttributeConverter.convertToDatabaseColumn(start));
		query.setParameter(endParameter, localDateTimeAttributeConverter.convertToDatabaseColumn(end));
		return castObjectListToTradeList(query.getResultList());
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
}
