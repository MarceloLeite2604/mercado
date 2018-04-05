package org.marceloleite.mercado.strategies.sixth.graphic;

import java.awt.Dimension;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.marceloleite.mercado.commons.Alarm;
import org.marceloleite.mercado.commons.graphic.Graphic;
import org.marceloleite.mercado.commons.graphic.GraphicData;
import org.marceloleite.mercado.commons.graphic.GraphicStrokeType;
import org.marceloleite.mercado.commons.graphic.MercadoRangeAxis;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.retriever.email.EmailMessage;

public class SixStrategyGraphic {

	private Graphic graphic;

	private static final String GRAPHIC_LOCATION = "output/graphic.png";

	private Map<String, GraphicData> graphicDatas;

	private static final LocalTime DAILY_GRAPHIC_TIME = LocalTime.of(8, 0, 0);

	private static final int DAILY_GRAPHIC_TIME_INTERVAL_MINUTES = 30;

	private Alarm dailyGraphicAlarm;

	public SixStrategyGraphic() {
		this.graphic = new Graphic();
		this.graphic.setDimension(new Dimension(1024, 768));
		this.graphic.setTitle("Mercado Controller");
		this.graphicDatas = createGraphicDatas();
	}

	public Map<String, GraphicData> getGraphicDatas() {
		return graphicDatas;
	}

	public void setGraphicDatas(Map<String, GraphicData> graphicDatas) {
		this.graphicDatas = graphicDatas;
	}

	public Graphic getGraphic() {
		return graphic;
	}

	public void setGraphic(Graphic graphic) {
		this.graphic = graphic;
	}

	private Map<String, GraphicData> createGraphicDatas() {
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

	public void createGraphicFile() {
		for (SixthStrategyGraphicData sixthStrategyGraphicData : SixthStrategyGraphicData.values()) {
			GraphicData graphicData = graphicDatas.get(sixthStrategyGraphicData.getTitle());
			if (sixthStrategyGraphicData.isPrintable()) {
				graphic.put(graphicData);
			}
		}
		graphic.writeGraphicToFile(GRAPHIC_LOCATION);
	}

	private Alarm createDailyGraphicAlarm(ZonedDateTime time) {
		ZonedDateTime alarmTime = ZonedDateTime.of(time.toLocalDate(), DAILY_GRAPHIC_TIME, ZonedDateTimeUtils.DEFAULT_ZONE_ID);
		if ( time.isAfter(alarmTime)) {
			alarmTime = alarmTime.plusDays(1);
		}
		return new Alarm(alarmTime, DAILY_GRAPHIC_TIME_INTERVAL_MINUTES);
	}

	private void sendGraphic(String emailAddressToSend) {
		EmailMessage emailMessage = new EmailMessage();
		/*emailMessage.getToAddresses().add(emailAddressToSend);
		emailMessage.setSubject("Daily Graphic");
		emailMessage.setMimeMultipart(createGraphicMimeMultipart(GRAPHIC_LOCATION));
		emailMessage.send();*/
	}

	private MimeMultipart createGraphicMimeMultipart(String graphicLocation) {

		MimeMultipart multipart = new MimeMultipart("related");

		BodyPart messageBodyPart = new MimeBodyPart();
		String htmlText = "<img src=\"cid:image\">";
		try {
			messageBodyPart.setContent(htmlText, "text/html");
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			DataSource dataSource = new FileDataSource(graphicLocation);

			messageBodyPart.setDataHandler(new DataHandler(dataSource));
			messageBodyPart.setHeader("Content-ID", "<image>");

			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException exception) {
			throw new RuntimeException("Error while creating graphic mime multipart.", exception);
		}

		return multipart;
	}

	public void clearData() {
		this.graphicDatas = createGraphicDatas();
	}

	public void sendDailyGraphic(ZonedDateTime time, String emailAddress) {
		if (dailyGraphicAlarm.isRinging(time)) {
			dailyGraphicAlarm.disarm();
			setAlarmForNextDay(time);
		}
		sendGraphic(emailAddress);
	}

	private void setAlarmForNextDay(ZonedDateTime time) {
		LocalDate nextDay = time.toLocalDate().plusDays(1);
		ZonedDateTime nextAlarmTime = ZonedDateTime.of(nextDay, DAILY_GRAPHIC_TIME, ZonedDateTimeUtils.DEFAULT_ZONE_ID);
		dailyGraphicAlarm.setStartTime(nextAlarmTime);
	}

	public boolean isTimeToSendGraphic(ZonedDateTime time) {
		if (dailyGraphicAlarm == null ) {
			dailyGraphicAlarm = createDailyGraphicAlarm(time);
		}
		return dailyGraphicAlarm.isRinging(time);
	}
}
