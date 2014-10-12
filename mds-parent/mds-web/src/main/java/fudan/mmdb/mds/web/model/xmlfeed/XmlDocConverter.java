package fudan.mmdb.mds.web.model.xmlfeed;

import fudan.mmdb.mds.web.model.MdsDocXmlFeed;

public interface XmlDocConverter {

	public MdsDocXmlFeed convertFromXml(String xml);
	
	public String convertToXMl(MdsDocXmlFeed doc);
}
