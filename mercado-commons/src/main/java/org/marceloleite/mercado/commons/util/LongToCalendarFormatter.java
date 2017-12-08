package org.marceloleite.mercado.commons.util;

import java.util.Calendar;
import java.util.Date;

import org.marceloleite.mercado.commons.interfaces.Formatter;

public class LongToCalendarFormatter implements Formatter<Long, Calendar>{

	@Override
	public Calendar format(Long value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(value*1000l));
		return calendar;
	}
}
