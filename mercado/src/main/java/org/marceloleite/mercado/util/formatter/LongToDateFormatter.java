package org.marceloleite.mercado.util.formatter;

import java.util.Date;

public class LongToDateFormatter implements Formatter<Long, Date>{

	@Override
	public Date format(Long value) {
		return new Date(value*1000L);
	}
}
