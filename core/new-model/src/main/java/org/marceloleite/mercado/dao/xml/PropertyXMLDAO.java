package org.marceloleite.mercado.dao.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.marceloleite.mercado.dao.interfaces.PropertyDAO;
import org.marceloleite.mercado.model.Property;
import org.springframework.util.StringUtils;

@Named("PropertyXMLDAO")
public class PropertyXMLDAO extends BaseXMLDAO<Property> implements PropertyDAO {

	private static final String ENTITY_DIRECTORY = "properties";

	private static final String FILE_NAME_PREFIX = "property";

	private static final String FILE_NAME_TEMPLATE = FILE_NAME_PREFIX + "_%s";

	private static final String FILE_NAME_REGEX = FILE_NAME_PREFIX + "_.+";

	@Override
	public <S extends Property> S save(S property) {
		return writeXMLFile(property);
	}
	
	@Override
	public <S extends Property> Iterable<S> saveAll(Iterable<S> properties) {
		for (S property : properties) {
			save(property);
		}
		return properties;
	}

	@Override
	public Property findByName(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new RuntimeException("Property name cannot be null or empty.");
		}
		List<File> XMLFiles = findXMLFileByRegex(FILE_NAME_REGEX);
		Property property = null;
		for (File XMLFile : XMLFiles) {
			Property propertyFromXML = readXMLFile(XMLFile);
			if (name.equals(propertyFromXML.getName())) {
				property = propertyFromXML;
			}
		}
		return property;
	}
	
	@Override
	public Iterable<Property> findAll() {
		List<Property> properties = new ArrayList<>();
		List<File> XMLFiles = findXMLFileByRegex(FILE_NAME_REGEX);
		for (File XMLFile : XMLFiles) {
			properties.add(readXMLFile(XMLFile));
		}
		return properties;
	}

	@Override
	protected String getEntityDirectory() {
		return ENTITY_DIRECTORY;
	}

	@Override
	protected String createFileName(Property property) {
		return String.format(FILE_NAME_TEMPLATE, property.getName());
	}	
}
