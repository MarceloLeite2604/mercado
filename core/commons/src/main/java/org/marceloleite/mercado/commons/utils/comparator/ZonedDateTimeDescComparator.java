package org.marceloleite.mercado.commons.utils.comparator;

import java.time.ZonedDateTime;
import java.util.Comparator;

public class ZonedDateTimeDescComparator implements Comparator<ZonedDateTime> {

	@Override
	public int compare(ZonedDateTime o1, ZonedDateTime o2) {
		return -1 * o1.compareTo(o2);
	}

}
