package org.marceloleite.mercado.strategies.sixth.graphic;

import org.marceloleite.mercado.commons.graphic.GraphicStrokeType;

public enum SixthStrategyGraphicData {
	LAST_PRICE("Last price", false, GraphicStrokeType.SOLID),
	AVERAGE_LAST_PRICE("Average last price", true, GraphicStrokeType.SOLID),
	UPPER_LIMIT("Upper limit", true, GraphicStrokeType.DASHED),
	LOWER_LIMIT("Lower limit", true, GraphicStrokeType.DASHED),
	NEXT_LAST_PRICE("Next last price", false, GraphicStrokeType.SOLID),
	BASE_PRICE("Base price", true, GraphicStrokeType.SOLID);

	private String title;
	
	private boolean printable;
	
	private GraphicStrokeType graphicStrokeType;

	private SixthStrategyGraphicData(String title, boolean printable, GraphicStrokeType graphicStrokeType) {
		this.title = title;
		this.printable = printable;
		this.graphicStrokeType = graphicStrokeType;
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean isPrintable() {
		return printable;
	}
	
	public GraphicStrokeType getGraphicStrokeType() {
		return graphicStrokeType;
	}
}
