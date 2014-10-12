package fudan.mmdb.mds.web.model.xmlfeed;

import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fudan.mmdb.mds.web.model.MdsDocXmlFeed;

public class XmlDocConverterImpl implements XmlDocConverter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlDocConverterImpl.class);
	
	// create JAXB context and instantiate marshaller
	private static JAXBContext context;

	public XmlDocConverterImpl() throws JAXBException {
		context = JAXBContext.newInstance(MdsDocXmlFeed.class);
	}

	@Override
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

	@Override
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

}
