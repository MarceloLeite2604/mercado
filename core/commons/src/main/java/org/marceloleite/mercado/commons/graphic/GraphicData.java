package org.marceloleite.mercado.commons.graphic;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class GraphicData {

	private int graphicType;

	private MercadoRangeAxis mercadoRangeAxis;
	
	private String title;
	
	private GraphicStrokeType graphicStrokeType;

	Map<ZonedDateTime, Double> values;

	public GraphicData(String title, int graphicType, MercadoRangeAxis mercadoRangeAxis, GraphicStrokeType graphicStrokeType) {
		this.title = title;
		this.graphicType = graphicType;
		this.mercadoRangeAxis = mercadoRangeAxis;
		this.values = new HashMap<>();
		this.graphicStrokeType = graphicStrokeType;
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
	
	public void setGraphicStrokeType(GraphicStrokeType graphicStrokeType) {
		this.graphicStrokeType = graphicStrokeType;
	}
	
	public GraphicStrokeType getGraphicStrokeType() {
		return graphicStrokeType;
	}
}
