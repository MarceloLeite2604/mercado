package org.marceloleite.mercado.xml.adapter;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;

public class LocalDateTimeXmlAdapter extends XmlAdapter<String, LocalDateTime> {

	@Override
	public LocalDateTime unmarshal(String string) throws Exception {
		return new LocalDateTimeToStringConverter().convertFrom(string);
	}

	@Override
	public String marshal(LocalDateTime localDateTime) throws Exception {
		return new LocalDateTimeToStringConverter().convertTo(localDateTime);
	}

}
