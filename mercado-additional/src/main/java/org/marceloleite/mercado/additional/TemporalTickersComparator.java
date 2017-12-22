package org.marceloleite.mercado.additional;

import java.util.Comparator;

import org.marceloleite.mercado.databasemodel.TemporalTicker;

public class TemporalTickersComparator implements Comparator<TemporalTicker> {

	@Override
	public int compare(TemporalTicker first, TemporalTicker second) {
		return first.getTemporalTickerId().getStart().compareTo(second.getTemporalTickerId().getStart());
	}

}
