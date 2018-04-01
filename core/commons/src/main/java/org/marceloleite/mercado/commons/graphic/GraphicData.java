package org.marceloleite.mercado.commons.graphic;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class GraphicData {

	private int graphicType;

	private MercadoRangeAxis mercadoRangeAxis;
	
	private String title;

	Map<ZonedDateTime, Double> values;

	public GraphicData(String title, int graphicType, MercadoRangeAxis mercadoRangeAxis) {
		this.title = title;
		this.graphicType = graphicType;
		this.mercadoRangeAxis = mercadoRangeAxis;
		this.values = new HashMap<>();
	}

	public void addValue(ZonedDateTime zonedDateTime, Double value) {
		values.put(zonedDateTime, value);
	}
	
	public Map<ZonedDateTime, Double> getValues() {
		return values;
	}

	public int getGraphicType() {
		return graphicType;
	}

	public MercadoRangeAxis getMercadoRangeAxis() {
		return mercadoRangeAxis;
	}
	
	public String getTitle() {
		return title;
	}
}
