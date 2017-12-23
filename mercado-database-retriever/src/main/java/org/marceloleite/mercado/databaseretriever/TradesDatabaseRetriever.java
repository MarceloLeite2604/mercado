package org.marceloleite.mercado.databaseretriever;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.databasemodel.Entity;
import org.marceloleite.mercado.databasemodel.Trade;
import org.marceloleite.mercado.databasemodel.converter.CurrencyAttributeConverter;

public class TradesDatabaseRetriever extends AbstractDatabaseRetriever {

	private static final String START_PARAMETER = "start";

	private static final String END_PARAMETER = "end";

	private static final String CURRENCY_PARAMETER = "currency";

	private static final String RETRIEVE_QUERY = "SELECT * FROM " + Entity.TRADE.getName() + " WHERE currency = :"
			+ CURRENCY_PARAMETER + " AND date BETWEEN :" + START_PARAMETER + " AND :" + END_PARAMETER;

	public List<Trade> retrieve(Currency currency, LocalDateTime start, LocalDateTime end) {
		createEntityManager();
		Query query = createNativeQuery(RETRIEVE_QUERY);
		query.setParameter(START_PARAMETER, start);
		query.setParameter(END_PARAMETER, end);
		query.setParameter(CURRENCY_PARAMETER, new CurrencyAttributeConverter().convertToDatabaseColumn(currency));
		List<Trade> trades = castObjectListToTradeList(query.getResultList());
		finishEntityManager();
		return trades;
	}

	public List<Trade> castObjectListToTradeList(List<?> objects) {
		List<Trade> trades = new ArrayList<>();

		for (Object object : objects) {
			if (object instanceof Trade) {
				trades.add((Trade) object);
			} else {
				new ClassCastExceptionThrower(getEntityClass(), object).throwException();
			}
		}

		return trades;
	}

	@Override
	public Class<?> getEntityClass() {
		return Trade.class;
	}
}
