package org.marceloleite.mercado.commons.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class UnixTimeSeconds {
	
	private LocalDateTime time;
	
	private static final ZoneId ZONE_ID = ZoneId.of("UTC");
	
	public UnixTimeSeconds(LocalDateTime time) {
		super();
		this.time = time;
	}

	public long get() {
		long epoch = time.atZone(ZONE_ID).toEpochSecond();
		return epoch;
	}
}
