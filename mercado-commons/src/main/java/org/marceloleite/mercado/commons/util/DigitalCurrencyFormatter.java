package org.marceloleite.mercado.commons.util;

import java.text.DecimalFormat;

public class DigitalCurrencyFormatter {
	
	private static final String NUMBER_FORMAT = "0.0000";
	
	public String format(double amount) {
		DecimalFormat decimalFormat = new DecimalFormat(NUMBER_FORMAT);
		return decimalFormat.format(amount);
	}

}
