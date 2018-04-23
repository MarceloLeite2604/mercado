package org.marceloleite.mercado.commons.formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NonDigitalCurrencyFormatter {
	
	private static NonDigitalCurrencyFormatter instance;
	
	private static final String NUMBER_FORMAT = "0.00000";
	
	private NonDigitalCurrencyFormatter() {
		
	}

	public String format(BigDecimal amount) {
		DecimalFormat decimalFormat = new DecimalFormat(NUMBER_FORMAT);
		return decimalFormat.format(amount);
	}
	
	public static NonDigitalCurrencyFormatter getInstance() {
		if (instance == null ) {
			instance = new NonDigitalCurrencyFormatter();
		}
		return instance;
	}
}
