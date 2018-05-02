package org.marceloleite.mercado.model.xmladapter;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

public class ZonedDateTimeXmlAdapter extends XmlAdapter<String, ZonedDateTime> {

	@Override
	public ZonedDateTime unmarshal(String string) throws Exception {
		return ZonedDateTimeUtils.parse(string);
	}

	@Override
	public String marshal(ZonedDateTime zonedDateTime) throws Exception {
		return ZonedDateTimeUtils.format(zonedDateTime);
	}

}
