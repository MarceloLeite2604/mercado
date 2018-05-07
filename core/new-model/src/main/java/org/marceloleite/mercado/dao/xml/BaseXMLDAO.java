package org.marceloleite.mercado.dao.xml;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

@Component
public abstract class BaseXMLDAO<T> {

	private static final String XML_FILE_EXTENSION = "xml";

	private static final String DEFAULT_XML_DIRECTORY = "src/main/resources/xml";

	private static String XMLDirectory = DEFAULT_XML_DIRECTORY;

	@Inject
	private XMLReaderWriter XML_READER_WRITER;

	protected File findXMLFileByName(String fileName) {
		List<File> xmlFiles = getXMLFiles();
		for (File xmlFile : xmlFiles) {
			if (xmlFile.getName().equals(fileName)) {
				return xmlFile;
			}
		}
		return null;
	}

	protected List<File> findXMLFileByRegex(String regex) {
		List<File> XMLFiles = getXMLFiles();
		List<File> selectedXMLFiles = new ArrayList<>();
		for (File XMLFile : XMLFiles) {
			if (Pattern.matches(regex, XMLFile.getName())) {
				selectedXMLFiles.add(XMLFile);
			}
		}
		return selectedXMLFiles;
	}

	private List<File> getXMLFiles() {
		FilenameFilter xmlFileNameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toUpperCase().endsWith(XML_FILE_EXTENSION.toUpperCase());
			}
		};
		File xmlDirectoryFile = getXMLDirectoryFile();
		File[] xmlFiles = xmlDirectoryFile.listFiles(xmlFileNameFilter);
		return Arrays.asList(xmlFiles);
	}

	private File getXMLDirectoryFile() {
		File xmlDirectoryFile = new File(createEntityDirectoryPath());
		if (!xmlDirectoryFile.exists()) {
			throw new IllegalArgumentException(
					"The directory \"" + xmlDirectoryFile.getAbsolutePath() + "\" does not exist.");
		}

		if (!xmlDirectoryFile.isDirectory()) {
			throw new IllegalArgumentException(
					"The file \"" + xmlDirectoryFile.getAbsolutePath() + "\" is not a directory.");
		}
		return xmlDirectoryFile;
	}

	private String createEntityDirectoryPath() {
		return XMLDirectory + File.separator + getEntityDirectory();
	}

	protected T readXMLFile(File XMLFile) {
		Class<T> clazz = retrieveTemplateClass();
		return XML_READER_WRITER.read(XMLFile, clazz);
	}

	protected <S extends T> S writeXMLFile(S object) {
		XML_READER_WRITER.write(object, createEntityFile(object));
		return object;
	}

	private <S extends T> File createEntityFile(S object) {
		return new File(
				createEntityDirectoryPath() + File.separator + createFileName(object) + "." + XML_FILE_EXTENSION);
	}

	@SuppressWarnings("unchecked")
	private Class<T> retrieveTemplateClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected abstract String getEntityDirectory();

	protected abstract String createFileName(T object);

	public static void setXMLDirectory(String XMLDirectory) {
		BaseXMLDAO.XMLDirectory = XMLDirectory;
	}
}
