package org.marceloleite.mercado.commons.alarm;

import java.time.ZonedDateTime;

public interface Alarm {

	public boolean isRinging(ZonedDateTime time);
}
