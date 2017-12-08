package org.marceloleite.mercado.commons.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class UnixTimeSeconds {
	
	private LocalDateTime time;

	public UnixTimeSeconds(LocalDateTime time) {
		super();
		this.time = time;
	}

	public long get() {
		ZoneId zoneId = ZoneId.systemDefault();
		long epoch = time.atZone(zoneId).toEpochSecond();
		return epoch;
	}
}
