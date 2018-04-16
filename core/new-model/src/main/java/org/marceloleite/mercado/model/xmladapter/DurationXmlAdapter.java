package org.marceloleite.mercado.model.xmladapter;

import java.time.Duration;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DurationXmlAdapter extends XmlAdapter<Long, Duration> {

	@Override
	public Duration unmarshal(Long seconds) throws Exception {
		if (seconds == null) {
			return null;
		}
		return Duration.ofSeconds(seconds);
	}

	@Override
	public Long marshal(Duration duration) throws Exception {
		if (duration == null) {
			return null;
		}
		return duration.getSeconds();
	}

}
