package org.marceloleite.mercado.modeler.util.formatter;

import java.util.List;

import org.marceloleite.mercado.commons.interfaces.Formatter;
import org.marceloleite.mercado.modeler.persistence.Offer;

public class OfferFormatter implements Formatter<List<Double>, Offer> {

	private static final int UNIT_PRICE_POSITION = 0;
	private static final int QUANTITY_POSITION = 1;

	@Override
	public Offer format(List<Double> doublesList) {
		Offer offer = new Offer();
		offer.setUnitPrice(doublesList.get(UNIT_PRICE_POSITION));
		offer.setQuantity(doublesList.get(QUANTITY_POSITION));
		return offer;
	}

}
