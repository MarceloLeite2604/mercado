package org.marceloleite.mercado.retriever;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.converter.datamodel.ListToMapTradeConverter;
import org.marceloleite.mercado.converter.entity.ListTemporalTickerPOToListTemporalTickerConverter;
import org.marceloleite.mercado.converter.entity.TemporalTickerPOToTemporalTickerConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TemporalTickerDAO;
import org.marceloleite.mercado.simulator.TemporalTicker;
import org.marceloleite.mercado.simulator.Trade;

public class TemporalTickerRetriever {

	private static final boolean IGNORE_DATABASE_VALUES = false;

	private static final int FIRST_CALL = 0;

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(TemporalTickerRetriever.class);

	private TemporalTickerDAO temporalTickerDAO;

	public TemporalTickerRetriever() {
		super();
		this.temporalTickerDAO = new TemporalTickerDAO();
	}

	public TemporalTicker retrieve(Currency currency, TimeInterval timeInterval,
			boolean ignoreValueOnDatabsase) {
		return retrieve(currency, timeInterval, ignoreValueOnDatabsase, FIRST_CALL);
	}

	private TemporalTicker retrieve(Currency currency, TimeInterval timeInterval,
			boolean ignoreValueOnDatabsase, int calls) {

		TemporalTickerPO temporalTickerPO = null;
		if (!ignoreValueOnDatabsase) {

			TemporalTickerIdPO temporalTickerIdPO = new TemporalTickerIdPO();
			temporalTickerIdPO.setCurrency(currency);
			temporalTickerIdPO.setStart(timeInterval.getStart());
			temporalTickerIdPO.setEnd(timeInterval.getEnd());
			TemporalTickerPO temporalTickerPOForEnquirement = new TemporalTickerPO();
			temporalTickerPOForEnquirement.setTemporalTickerIdPO(temporalTickerIdPO);
			temporalTickerPO = temporalTickerDAO.findById(temporalTickerPOForEnquirement);

		}

		if (temporalTickerPO == null) {
			TradesRetriever tradesRetriever = new TradesRetriever();

			List<Trade> trades = tradesRetriever.retrieve(currency, timeInterval.getStart(),
					timeInterval.getEnd(), IGNORE_DATABASE_VALUES);
			Map<Long, Trade> tradesMap = new ListToMapTradeConverter().convertTo(trades);
			TemporalTicker temporalTicker = new TemporalTickerCreator().create(currency, timeInterval,
					tradesMap);
			if (temporalTicker != null) {
				temporalTickerPO = new TemporalTickerPOToTemporalTickerConverter()
						.convertFrom(temporalTicker);
				temporalTickerDAO.merge(temporalTickerPO);
			}
		}

		return new TemporalTickerPOToTemporalTickerConverter().convertTo(temporalTickerPO);
	}

	public List<TemporalTicker> bulkRetrieve(Currency currency,
			TimeDivisionController timeDivisionController) {
		List<TemporalTickerPO> temporalTickerPOs = temporalTickerDAO.bulkRetrieve(currency, timeDivisionController);
		ListTemporalTickerPOToListTemporalTickerConverter listTemporalTickerPOToListTemporalTickerConverter = new ListTemporalTickerPOToListTemporalTickerConverter();
		List<TemporalTicker> temporalTickers = listTemporalTickerPOToListTemporalTickerConverter.convertTo(temporalTickerPOs);
		temporalTickers = retrieveTemporalTickersNotFoundOnBulk(temporalTickers, currency, timeDivisionController);
		
		return sortTemporalTickers(temporalTickers);
	}

	public TreeMap<TimeInterval, Map<Currency, TemporalTicker>> bulkRetrieve(
			TimeDivisionController timeDivisionController) {
		ListTemporalTickerPOToListTemporalTickerConverter listTemporalTickerPOToListTemporalTickerConverter = new ListTemporalTickerPOToListTemporalTickerConverter();
		Map<Currency, List<TemporalTicker>> temporalTickersByCurrency = new EnumMap<>(Currency.class);
		for (Currency currency : Currency.values()) {
			if (currency.isDigital()) {
				List<TemporalTickerPO> temporalTickerPOs = temporalTickerDAO.bulkRetrieve(currency,
						timeDivisionController);
				List<TemporalTicker> temporalTickers = new ListTemporalTickerPOToListTemporalTickerConverter()
						.convertTo(temporalTickerPOs);
				temporalTickers = retrieveTemporalTickersNotFoundOnBulk(temporalTickers, currency,
						timeDivisionController);

				temporalTickersByCurrency.put(currency,
						listTemporalTickerPOToListTemporalTickerConverter.convertTo(temporalTickerPOs));
			}
		}

		return elaborateTemporalTickerPOsByTimeInterval(temporalTickersByCurrency);
	}

	private TreeMap<TimeInterval, Map<Currency, TemporalTicker>> elaborateTemporalTickerPOsByTimeInterval(
			Map<Currency, List<TemporalTicker>> temporalTickerPOsByCurrency) {
		TreeMap<TimeInterval, Map<Currency, TemporalTicker>> result = new TreeMap<>();
		for (Currency currency : temporalTickerPOsByCurrency.keySet()) {
			List<TemporalTicker> temporalTickers = temporalTickerPOsByCurrency.get(currency);
			for (TemporalTicker temporalTicker : temporalTickers) {
				TimeInterval timeInterval = new TimeInterval(temporalTicker.getStart(),
						temporalTicker.getEnd());
				Map<Currency, TemporalTicker> temporalTickersByCurrency = result
						.getOrDefault(timeInterval, new EnumMap<>(Currency.class));
				temporalTickersByCurrency.put(temporalTicker.getCurrency(), temporalTicker);
				result.put(timeInterval, temporalTickersByCurrency);
			}
		}
		return result;
	}

	private List<TemporalTicker> retrieveTemporalTickersNotFoundOnBulk(List<TemporalTicker> temporalTickers,
			Currency currency, TimeDivisionController timeDivisionController) {
		/*List<TemporalTickerIdPO> temporalTickerIdPOsFromBulk = temporalTickerPOs.stream().map(TemporalTickerPO::getId)
				.collect(Collectors.toList());*/
		TemporalTicker temporalTickerToCheck;
		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			temporalTickerToCheck = new TemporalTicker();
			temporalTickerToCheck.setCurrency(currency);
			temporalTickerToCheck.setStart(timeInterval.getStart());
			temporalTickerToCheck.setEnd(timeInterval.getEnd());
			if (!temporalTickers.contains(temporalTickerToCheck)) {
				TemporalTicker temporalTickerRetrieved = retrieve(currency, timeInterval, IGNORE_DATABASE_VALUES);
				temporalTickers.add(temporalTickerRetrieved);
			}
		}
		return temporalTickers;
	}

	private List<TemporalTicker> sortTemporalTickers(List<TemporalTicker> temporalTickerPOs) {
		Collections.sort(temporalTickerPOs, new Comparator<TemporalTicker>() {

			public int compare(TemporalTicker firstObject, TemporalTicker secondObject) {
				if (firstObject.getStart().isBefore(secondObject.getStart())) {
					return -1;
				} else if (firstObject.getStart().isAfter(secondObject.getStart())) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		return temporalTickerPOs;
	}

}
