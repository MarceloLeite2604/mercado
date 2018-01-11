package org.marceloleite.mercado.commons.util;

import java.text.DecimalFormat;

public class NonDigitalCurrencyFormatter {
	
	private static final String NUMBER_FORMAT = "0.00";

	public String format(double amount) {
		DecimalFormat decimalFormat = new DecimalFormat(NUMBER_FORMAT);
		return decimalFormat.format(amount);
	}
}
