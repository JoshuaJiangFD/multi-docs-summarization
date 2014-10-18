package fudan.mmdb.mds.core.model.xmlfeed;

import java.io.File;

public interface XmlDocConverter {
	
	public MdsDocXmlFeed convertFromFile(File xml);
	
	public MdsDocXmlFeed convertFromXml(String xml);
	
	public String convertToXMl(MdsDocXmlFeed doc);
	
	public Boolean persistToXml(MdsDocXmlFeed doc,File file);
}
