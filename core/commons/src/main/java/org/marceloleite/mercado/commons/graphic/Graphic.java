package org.marceloleite.mercado.commons.graphic;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Graphic {

	private static final String FORMAT_NAME = "png";

	private static final String DEFAULT_GRAPHIC_TITLE = "Graphic";

	private static final Dimension DEFAULT_DIMENSION = new Dimension(1024, 768);

	private Map<String, GraphicData> graphicDatas;

	private Dimension dimension;

	private String title;

	public Graphic() {
		this.dimension = DEFAULT_DIMENSION;
		this.title = DEFAULT_GRAPHIC_TITLE;
		this.graphicDatas = new HashMap<>();
	}

	public Map<String, GraphicData> getGraphicDatas() {
		return graphicDatas;
	}
	
	public void put(GraphicData graphicData) {
		graphicDatas.put(graphicData.getTitle(), graphicData);
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void writeGraphicToFile(String filePath) {
		final JFreeChart jFreeChart = createOverlaidChart();
		try (OutputStream outputStreamGraphic = new FileOutputStream(filePath)) {
			BufferedImage bufferedImage = jFreeChart.createBufferedImage((int) dimension.getWidth(),
					(int) dimension.getHeight(), null);
			ImageIO.write(bufferedImage, FORMAT_NAME, outputStreamGraphic);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private JFreeChart createOverlaidChart() {

		XYPlot xyPlot = new XYPlot();

		xyPlot = createDomainAxis(xyPlot);
		xyPlot = createRangeAxes(xyPlot);

		int counter = 0;
		for (String graphicTitle : graphicDatas.keySet()) {
			GraphicData graphicData = graphicDatas.get(graphicTitle);
			TimeSeries timeSeries = createTimeSeries(graphicData);
			xyPlot.setRenderer(counter, new StandardXYItemRenderer(graphicData.getGraphicType()));
			xyPlot.setDataset(counter, new TimeSeriesCollection(timeSeries));
			if (graphicData.getMercadoRangeAxis() == MercadoRangeAxis.ORDERS) {
				xyPlot.mapDatasetToRangeAxis(counter, 1);
			}
			counter++;
		}

		// xyPlot = addLastPriceToXyPlot(xyPlot);
		// xyPlot = addBuyOrdersToXyPlot(xyPlot);
		// xyPlot = addSellOrdersToXyPlot(xyPlot);
		// xyPlot = addAverageLastPriceToXyPlot(xyPlot);
		// xyPlot = addNextLastPriceToXyPlot(xyPlot);

		xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		return new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true);

	}

	private TimeSeries createTimeSeries(GraphicData graphicData) {
		TimeSeries timeSeries = new TimeSeries(graphicData.getTitle());
		Set<Entry<ZonedDateTime, Double>> valuePairs = graphicData.getValues().entrySet();
		for (Entry<ZonedDateTime, Double> valuePair : valuePairs) {
			ZonedDateTime zonedDateTime = valuePair.getKey();
			Date date = Date.from(zonedDateTime.toInstant());
			Minute minute = new Minute(date);
			timeSeries.add(minute, valuePair.getValue());
		}
		return timeSeries;
	}

	private XYPlot createRangeAxes(XYPlot xyPlot) {
		int counter = 0;
		for (MercadoRangeAxis mercadoRangeAxis : MercadoRangeAxis.values()) {
			xyPlot.setRangeAxis(counter++, new NumberAxis(mercadoRangeAxis.getTitle()));
		}
		return xyPlot;
	}

	private XYPlot createDomainAxis(XYPlot xyPlot) {
		xyPlot.setDomainAxis(new DateAxis("Time"));
		return xyPlot;
	}
}