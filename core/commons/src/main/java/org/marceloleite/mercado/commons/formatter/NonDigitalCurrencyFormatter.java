package org.marceloleite.mercado.commons.formatter;

import java.text.DecimalFormat;

import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class NonDigitalCurrencyFormatter {
	
	private static final String NUMBER_FORMAT = "0.00000";

	public String format(MercadoBigDecimal amount) {
		DecimalFormat decimalFormat = new DecimalFormat(NUMBER_FORMAT);
		return decimalFormat.format(amount);
	}
}
