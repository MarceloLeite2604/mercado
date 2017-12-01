package org.marceloleite.mercado.util;

import java.util.Calendar;

public class UnixTimeSeconds {
	
	private Calendar calendar;

	public UnixTimeSeconds(Calendar calendar) {
		super();
		this.calendar = calendar;
	}

	public long get() {
		return (calendar.getTime().getTime()/1000);
	}
}
