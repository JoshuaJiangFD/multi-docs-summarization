package fudan.mmdb.mds.core.model.xmlfeed;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDocConverterImpl implements XmlDocConverter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlDocConverterImpl.class);
	
	// create JAXB context and instantiate marshaller
	private static JAXBContext context;

	public XmlDocConverterImpl() throws JAXBException {
		context = JAXBContext.newInstance(MdsDocXmlFeed.class);
	}

	public MdsDocXmlFeed convertFromXml(String xml) {

		try {
			Unmarshaller um = context.createUnmarshaller();
			MdsDocXmlFeed docFeed = (MdsDocXmlFeed) um
					.unmarshal(new StringReader(xml));
			return docFeed;
		} catch (Exception ex) {
			String error = "Error in unserialize doc feed from xml.";
			LOGGER.error(error, ex);
			return null;
		}
	}

	public String convertToXMl(MdsDocXmlFeed docFeed) {
		try {
			StringWriter writer = new StringWriter();

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(docFeed, writer);
			return writer.toString();

		} catch (Exception ex) {
			String error = "Error in serialize doc feed into xml.";
			LOGGER.error(error, ex);
			return null;
		}
	}

	public Boolean persistToXml(MdsDocXmlFeed docFeed, File file) {
		try {
			StringWriter writer = new StringWriter();

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(docFeed, file);

			return true;
		} catch (Exception ex) {
			String error = "Error in serialize doc feed into xml.";
			LOGGER.error(error, ex);
			return false;
		}
		
	}

	public MdsDocXmlFeed convertFromFile(File xml) {
		try {
			Unmarshaller um = context.createUnmarshaller();
			MdsDocXmlFeed docFeed = (MdsDocXmlFeed) um
					.unmarshal(xml);
			return docFeed;
		} catch (Exception ex) {
			String error = "Error in unserialize doc feed from xml.";
			LOGGER.error(error, ex);
			return null;
		}
	}

}
