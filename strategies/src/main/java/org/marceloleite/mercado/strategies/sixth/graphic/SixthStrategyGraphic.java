package org.marceloleite.mercado.strategies.sixth.graphic;

import java.awt.Dimension;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.marceloleite.mercado.commons.graphic.Graphic;
import org.marceloleite.mercado.commons.graphic.GraphicData;
import org.marceloleite.mercado.commons.graphic.GraphicStrokeType;
import org.marceloleite.mercado.commons.graphic.MercadoRangeAxis;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatistics;
import org.marceloleite.mercado.strategies.sixth.SixthStrategyThresholds;

public class SixthStrategyGraphic {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(SixthStrategyGraphic.class);

	private Graphic graphic;

	private static final String GRAPHIC_LOCATION = "output/graphic.png";

	private Map<String, GraphicData> graphicDataMap;

	private SixthStrategyStatistics sixthStrategyStatistics;

	private SixthStrategyThresholds sixthStrategyThresholds;

	public SixthStrategyGraphic(SixthStrategyStatistics sixthStrategyStatistics,
			SixthStrategyThresholds sixthStrategyThresholds) {
		this.graphic = new Graphic();
		this.graphic.setDimension(new Dimension(1024, 768));
		this.graphic.setTitle("Mercado Controller");
		this.graphicDataMap = createGraphicDataMap();
		this.sixthStrategyStatistics = sixthStrategyStatistics;
		this.sixthStrategyThresholds = sixthStrategyThresholds;
	}

	public Graphic getGraphic() {
		return graphic;
	}

	private Map<String, GraphicData> createGraphicDataMap() {
		Map<String, GraphicData> graphicDatas = new HashMap<>();
		for (SixthStrategyGraphicData sixthStrategyGraphicData : SixthStrategyGraphicData.values()) {
			GraphicData graphicData = createGraphicData(sixthStrategyGraphicData.getTitle(),
					sixthStrategyGraphicData.getGraphicStrokeType());
			graphicDatas.put(graphicData.getTitle(), graphicData);
		}
		return graphicDatas;
	}

	private GraphicData createGraphicData(String title, GraphicStrokeType graphicStrokeType) {
		return createGraphicData(title, StandardXYItemRenderer.LINES, MercadoRangeAxis.CURRENCY_REAL,
				graphicStrokeType);
	}

	private GraphicData createGraphicData(String title, int graphicType, MercadoRangeAxis mercadoRangeAxis,
			GraphicStrokeType graphicStrokeType) {
		return new GraphicData(title, graphicType, mercadoRangeAxis, graphicStrokeType);
	}

	public File createFile() {
		for (SixthStrategyGraphicData sixthStrategyGraphicData : SixthStrategyGraphicData.values()) {
			GraphicData graphicData = graphicDataMap.get(sixthStrategyGraphicData.getTitle());
			if (sixthStrategyGraphicData.isPrintable()) {
				graphic.put(graphicData);
			}
		}
		return graphic.writeGraphicToFile(GRAPHIC_LOCATION);
	}

	public void clearData() {
		this.graphicDataMap = createGraphicDataMap();
	}

	public void addInformation(TemporalTicker temporalTicker) {
		addLastPriceToGraphicData(temporalTicker);
		addAveragePriceToGraphicData(temporalTicker);
		addBasePriceToGraphicData(temporalTicker);
	}

	private void addBasePriceToGraphicData(TemporalTicker temporalTicker) {
		addPointOnGraphicdata(SixthStrategyGraphicData.BASE_PRICE, temporalTicker,
				sixthStrategyStatistics.getLastPriceStatistics()
						.getBase());
	}

	private void addAveragePriceToGraphicData(TemporalTicker temporalTicker) {
		addPointOnGraphicdata(SixthStrategyGraphicData.AVERAGE_LAST_PRICE, temporalTicker,
				sixthStrategyStatistics.getLastPriceStatistics()
						.getAverage());
	}

	private void addLastPriceToGraphicData(TemporalTicker temporalTicker) {
		addPointOnGraphicdata(SixthStrategyGraphicData.LAST_PRICE, temporalTicker,
				temporalTicker.getCurrentOrPreviousLast()
						.doubleValue());
	}

	private void addPointOnGraphicdata(SixthStrategyGraphicData sixthStrategyGraphicData, TemporalTicker temporalTicker,
			double value) {
		getGraphicDataFor(sixthStrategyGraphicData).addValue(temporalTicker.getStart(), value);
	}

	private GraphicData getGraphicDataFor(SixthStrategyGraphicData sixthStrategyGraphicData) {
		return graphicDataMap.get(sixthStrategyGraphicData.getTitle());
	}
	
	public void addLimitPointsOnGraphicData(ZonedDateTime time) {
		addUpperLimitPoinOnGraphic(time);
		addLowerLimitPointOnGraphic(time);
	}

	private void addUpperLimitPoinOnGraphic(ZonedDateTime time) {
		addPointOnGraphicData(SixthStrategyGraphicData.UPPER_LIMIT, time,
				calculateLimitValue(sixthStrategyThresholds.getGrowthPercentage()));
	}

	private void addLowerLimitPointOnGraphic(ZonedDateTime time) {
		addPointOnGraphicData(SixthStrategyGraphicData.LOWER_LIMIT, time,
				calculateLimitValue(sixthStrategyThresholds.getShrinkPercentage()));
	}

	private void addPointOnGraphicData(SixthStrategyGraphicData sixthStrategyGraphicData, ZonedDateTime zonedDateTime,
			double value) {
		graphicDataMap.get(sixthStrategyGraphicData.getTitle())
				.addValue(zonedDateTime, value);
	}

	private Double calculateLimitValue(double percentageThreshold) {
		return sixthStrategyStatistics.getLastPriceStatistics()
				.getBase() * (1.0 + percentageThreshold);
	}

}
