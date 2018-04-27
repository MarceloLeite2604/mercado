package org.marceloleite.mercado.commons.formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DigitalCurrencyFormatter {
	
	private static DigitalCurrencyFormatter instance;
	
	private static final String NUMBER_FORMAT = "0.00000000";
	
	private DigitalCurrencyFormatter() {
		
	}
	
	public String format(BigDecimal amount) {
		DecimalFormat decimalFormat = new DecimalFormat(NUMBER_FORMAT);
		return decimalFormat.format(amount);
	}
	
	public String format(Double amount) {
		return format(new BigDecimal(amount));
	}
	
	public static DigitalCurrencyFormatter getInstance() {
		if (instance == null) {
			instance = new DigitalCurrencyFormatter(); 
		}
		return instance;
	}

}
