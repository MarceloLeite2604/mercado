package org.marceloleite.mercado.xml.adapter;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;

public class ZonedDateTimeXmlAdapter extends XmlAdapter<String, ZonedDateTime> {

	@Override
	public ZonedDateTime unmarshal(String string) throws Exception {
		return new ZonedDateTimeToStringConverter().convertFrom(string);
	}

	@Override
	public String marshal(ZonedDateTime zonedDateTime) throws Exception {
		return new ZonedDateTimeToStringConverter().convertTo(zonedDateTime);
	}

}
