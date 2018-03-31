package org.marceloleite.mercado.strategies.sixth.graphic;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.marceloleite.mercado.commons.Currency;

public class Graphic {

	private List<Double> lastPrices;
	
	private List<Double> averageLastPrices;
	
	private List<Double> nextLastPrices;

	private List<Double> buyOrders;

	private List<Double> sellOrders;

	public Graphic() {
		this.lastPrices = new ArrayList<>();
		this.averageLastPrices = new ArrayList<>();
		this.nextLastPrices = new ArrayList<>();
		this.buyOrders = new ArrayList<>();
		this.sellOrders = new ArrayList<>();
	}

	private void addLastPrice(Double lastPrice) {
		this.lastPrices.add(lastPrice);
	}

	private void addBuyOrder(Double buyOrder) {
		this.buyOrders.add(buyOrder);
	}

	private void addSellOrder(Double sellOrder) {
		this.sellOrders.add(sellOrder);
	}
	
	private void addAverageLastPrice(Double averageLastPrice) {
		this.averageLastPrices.add(averageLastPrice);
	}
	
	private void addNextLastPrice(Double nextLastPrice) {
		this.nextLastPrices.add(nextLastPrice);
	}
	
	public void add(Double lastPrice, Double averageLastPrice, Double nextLastPrice, Double buyOrders, Double sellOrders) {
		addLastPrice(lastPrice);
		addBuyOrder(buyOrders);
		addSellOrder(sellOrders);
		addNextLastPrice(nextLastPrice);
		addAverageLastPrice(averageLastPrice);
	}
	

	public void plotGraphic() {
		final JFreeChart chart = createOverlaidChart();
		try (OutputStream outputStreamGraphic = new FileOutputStream("graphic.png")) {
			writeAsPNG(chart, outputStreamGraphic, 8192, 768);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void writeAsPNG(JFreeChart chart, OutputStream out, int width, int height) {
		try {
			BufferedImage chartImage = chart.createBufferedImage(width, height, null);
			ImageIO.write(chartImage, "png", out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private JFreeChart createOverlaidChart() {

		XYPlot xyPlot = new XYPlot();

		createRangeAxes(xyPlot);
		createDomainAxis(xyPlot);

		xyPlot = addLastPriceToXyPlot(xyPlot);
		//xyPlot = addBuyOrdersToXyPlot(xyPlot);
		//xyPlot = addSellOrdersToXyPlot(xyPlot);
		xyPlot = addAverageLastPriceToXyPlot(xyPlot);
		xyPlot = addNextLastPriceToXyPlot(xyPlot);

		xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		return new JFreeChart("Graphic", JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true);

	}

	private XYPlot addAverageLastPriceToXyPlot(XYPlot xyPlot) {
		XYSeries xySeriesAverageLastPrice = new XYSeries("Average price");
		for (int counter = 0; counter < averageLastPrices.size(); counter++) {
			xySeriesAverageLastPrice.add(counter, averageLastPrices.get(counter));
		}

		xyPlot.setRenderer(3, new StandardXYItemRenderer(StandardXYItemRenderer.LINES));
		xyPlot.setDataset(3, new XYSeriesCollection(xySeriesAverageLastPrice));

		return xyPlot;
	}
	
	private XYPlot addNextLastPriceToXyPlot(XYPlot xyPlot) {
		XYSeries xySeriesNextLastPrice = new XYSeries("Next price");
		for (int counter = 0; counter < nextLastPrices.size(); counter++) {
			xySeriesNextLastPrice.add(counter, nextLastPrices.get(counter));
		}

		xyPlot.setRenderer(4, new StandardXYItemRenderer(StandardXYItemRenderer.LINES));
		xyPlot.setDataset(4, new XYSeriesCollection(xySeriesNextLastPrice));

		return xyPlot;
	}

	private void createRangeAxes(XYPlot xyPlot) {
		xyPlot.setRangeAxis(0, new NumberAxis(Currency.REAL.getAcronym()));

		NumberAxis orderRangeAxis = new NumberAxis("Orders");
		xyPlot.setRangeAxis(1, orderRangeAxis);
	}

	private XYPlot addBuyOrdersToXyPlot(XYPlot xyPlot) {
		XYSeries xySeriesBuyOrders = new XYSeries("Buy Orders");
		for (int counter = 0; counter < buyOrders.size(); counter++) {
			xySeriesBuyOrders.add(counter, buyOrders.get(counter));
		}

		xyPlot.setDataset(1, new XYSeriesCollection(xySeriesBuyOrders));
		xyPlot.setRenderer(1, new StandardXYItemRenderer(StandardXYItemRenderer.LINES));
		xyPlot.mapDatasetToRangeAxis(1, 1);
		return xyPlot;
	}

	private XYPlot addSellOrdersToXyPlot(XYPlot xyPlot) {

		XYSeries xySeriesSellOrders = new XYSeries("Sell Orders");
		for (int counter = 0; counter < sellOrders.size(); counter++) {
			xySeriesSellOrders.add(counter, sellOrders.get(counter));
		}

		xyPlot.setDataset(2, new XYSeriesCollection(xySeriesSellOrders));
		xyPlot.setRenderer(2, new StandardXYItemRenderer(StandardXYItemRenderer.LINES));
		xyPlot.mapDatasetToRangeAxis(2, 1);
		return xyPlot;
	}

	private XYPlot addLastPriceToXyPlot(XYPlot xyPlot) {

		XYSeries xySeriesLastPrice = new XYSeries("Last price");
		for (int counter = 0; counter < lastPrices.size(); counter++) {
			xySeriesLastPrice.add(counter, lastPrices.get(counter));
		}

		xyPlot.setRenderer(0, new StandardXYItemRenderer(StandardXYItemRenderer.LINES));
		xyPlot.setDataset(0, new XYSeriesCollection(xySeriesLastPrice));

		return xyPlot;
	}

	private void createDomainAxis(XYPlot xyPlot) {
		xyPlot.setDomainAxis(new NumberAxis("Time"));
	}
}
