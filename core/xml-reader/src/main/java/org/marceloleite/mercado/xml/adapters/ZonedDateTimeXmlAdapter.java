package org.marceloleite.mercado.xml.adapters;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;

public class ZonedDateTimeXmlAdapter extends XmlAdapter<String, ZonedDateTime> {

	@Override
	public ZonedDateTime unmarshal(String string) throws Exception {
		return ZonedDateTimeToStringConverter.getInstance().convertFrom(string);
	}

	@Override
	public String marshal(ZonedDateTime zonedDateTime) throws Exception {
		return ZonedDateTimeToStringConverter.getInstance().convertTo(zonedDateTime);
	}

}
