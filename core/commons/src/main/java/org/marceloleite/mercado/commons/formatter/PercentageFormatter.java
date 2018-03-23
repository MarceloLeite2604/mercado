package org.marceloleite.mercado.commons.formatter;

import java.text.DecimalFormat;

import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class PercentageFormatter {

	private static final String NUMBER_FORMAT = "0.00";

	public String format(MercadoBigDecimal value) {
		DecimalFormat decimalFormatter = new DecimalFormat(NUMBER_FORMAT);
		return decimalFormatter.format(value.multiply(new MercadoBigDecimal("100"))) + " %";
	}
}
