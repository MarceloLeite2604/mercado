package org.marceloleite.mercado.util.formatter;

import java.util.Calendar;
import java.util.Date;

public class LongToCalendarFormatter implements Formatter<Long, Calendar>{

	@Override
	public Calendar format(Long value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(value*1000l));
		return calendar;
	}
}
