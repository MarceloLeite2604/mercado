package org.marceloleite.mercado.databaseretriever.persistence.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.Entity;
import org.marceloleite.mercado.databasemodel.PersistenceObject;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.converter.CurrencyAttributeConverter;

public class TradeDAO extends AbstractDAO<TradePO> {

	private static final String START_PARAMETER = "start";

	private static final String END_PARAMETER = "end";

	private static final String CURRENCY_PARAMETER = "currency";

	private static final String BETWEEN_TIME_INTERVAL_QUERY = "SELECT * FROM " + Entity.TRADE.getName()
			+ " WHERE currency = :" + CURRENCY_PARAMETER + " AND date BETWEEN :" + START_PARAMETER + " AND :"
			+ END_PARAMETER;

	private static final String NEWEST_TRADE_RETRIEVED = "SELECT * FROM " + Entity.TRADE.getName()
			+ " WHERE date = ( SELECT max(date) FROM " + Entity.TRADE.getName() + ")";

	private static final String OLDEST_TRADE_RETRIEVED = "SELECT * FROM " + Entity.TRADE.getName()
			+ " WHERE date = ( SELECT min(date) FROM " + Entity.TRADE.getName() + ")";

	public List<TradePO> retrieve(Currency currency, LocalDateTime start, LocalDateTime end) {
		createEntityManager();

		Map<String, String> parameters = new HashMap<>();
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		parameters.put(START_PARAMETER, localDateTimeToStringConverter.convert(start));
		parameters.put(END_PARAMETER, localDateTimeToStringConverter.convert(end));
		CurrencyAttributeConverter currencyAttributeConverter = new CurrencyAttributeConverter();
		parameters.put(CURRENCY_PARAMETER, currencyAttributeConverter.convertToDatabaseColumn(currency));
		List<? extends PersistenceObject<?>> queryResult = executeQuery(BETWEEN_TIME_INTERVAL_QUERY, parameters);
		return castToTradeList(queryResult);
	}

	public List<TradePO> castToTradeList(List<? extends PersistenceObject<?>> objects) {
		List<TradePO> trades = new ArrayList<>();

		for (Object object : objects) {
			trades.add(castToTradePO(object));
		}

		return trades;
	}

	public TradePO castToTradePO(Object object) {

		if (!(object instanceof TradePO)) {
			new ClassCastExceptionThrower(getPOClass(), object).throwException();
		}
		return (TradePO) object;
	}

	public TradePO retrieveNewestTrade() {
		PersistenceObject<?> queryResult = executeQueryForSingleResult(NEWEST_TRADE_RETRIEVED, null);
		return castToTradePO(queryResult);
	}

	public TradePO retrieveOldestTrade() {
		PersistenceObject<?> queryResult = executeQueryForSingleResult(OLDEST_TRADE_RETRIEVED, null);
		return castToTradePO(queryResult);
	}

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return TradePO.class;
	}

}
