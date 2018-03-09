package org.marceloleite.mercado.converter.json.api.data;

import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.Offer;

public class OfferConverter implements Converter<List<Double>, Offer> {

	private static final int UNIT_PRICE_POSITION = 0;
	private static final int QUANTITY_POSITION = 1;

	@Override
	public Offer convertTo(List<Double> doublesList) {
		Offer offer = new Offer();
		offer.setUnitPrice(doublesList.get(UNIT_PRICE_POSITION));
		offer.setQuantity(doublesList.get(QUANTITY_POSITION));
		return offer;
	}

	@Override
	public List<Double> convertFrom(Offer object) {
		throw new UnsupportedOperationException();
	}

}
