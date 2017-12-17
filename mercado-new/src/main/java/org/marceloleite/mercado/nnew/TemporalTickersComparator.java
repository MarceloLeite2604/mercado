package org.marceloleite.mercado.nnew;

import java.util.Comparator;

import org.marceloleite.mercado.modeler.persistence.model.TemporalTicker;

public class TemporalTickersComparator implements Comparator<TemporalTicker> {

	@Override
	public int compare(TemporalTicker first, TemporalTicker second) {
		return first.getFrom().compareTo(second.getFrom());
	}

}
