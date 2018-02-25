package org.marceloleite.mercado.retriever.temporalticker;

import java.util.Comparator;

import org.marceloleite.mercado.databaseretriever.persistence.objects.TemporalTickerPO;

public class TemporalTickersComparator implements Comparator<TemporalTickerPO> {

	@Override
	public int compare(TemporalTickerPO first, TemporalTickerPO second) {
		return first.getTemporalTickerId().getStart().compareTo(second.getTemporalTickerId().getStart());
	}

}
