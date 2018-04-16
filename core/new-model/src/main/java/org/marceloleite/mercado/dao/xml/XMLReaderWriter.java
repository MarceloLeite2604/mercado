package org.marceloleite.mercado.dao.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Property;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Ticker;
import org.marceloleite.mercado.model.Trade;

public class XMLReaderWriter {

	private static final Class<?>[] XML_CLASSES = { Account.class, Property.class, TemporalTicker.class, Ticker.class,
			Trade.class };

	private JAXBContext jaxbContext;

	public XMLReaderWriter() {
		try {
			this.jaxbContext = createJaxbContext();
		} catch (JAXBException exception) {
			throw new RuntimeException(exception);
		}
	}

	private JAXBContext createJaxbContext() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(XML_CLASSES);
		return jaxbContext;
	}

	private Unmarshaller createUnmarshaller() throws JAXBException {
		return jaxbContext.createUnmarshaller();
	}

	private Marshaller createMarshaller() throws JAXBException {
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		return jaxbMarshaller;
	}

	public <T> void write(T object, File outputFile) {
		try {
			Marshaller marshaller = createMarshaller();
			marshaller.marshal(object, outputFile);
		} catch (JAXBException exception) {
			String message = String.format("Error while writing \"%s\" on file \"%s\".", object.getClass(),
					outputFile.getAbsolutePath());
			throw new RuntimeException(message, exception);
		}
	}

	public <T> T read(File XMLFile, Class<T> classObject) {
		try {
			Unmarshaller unmarshaller = createUnmarshaller();
			Object object = unmarshaller.unmarshal(XMLFile);
			return classObject.cast(object);
		} catch (JAXBException exception) {
			String message = String.format("Error while reading file \"%s\" to create a \"%s\" object.",
					XMLFile.getAbsolutePath(), classObject.getName());
			throw new RuntimeException(message, exception);
		}
	}
}
