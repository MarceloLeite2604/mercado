package org.marceloleite.mercado.modeler.util.converter;

import java.util.List;

import org.marceloleite.mercado.commons.interfaces.Converter;
import org.marceloleite.mercado.modeler.persistence.model.Offer;

public class OfferConverter implements Converter<List<Double>, Offer> {

	private static final int UNIT_PRICE_POSITION = 0;
	private static final int QUANTITY_POSITION = 1;

	@Override
	public Offer convert(List<Double> doublesList) {
		Offer offer = new Offer();
		offer.setUnitPrice(doublesList.get(UNIT_PRICE_POSITION));
		offer.setQuantity(doublesList.get(QUANTITY_POSITION));
		return offer;
	}

}
