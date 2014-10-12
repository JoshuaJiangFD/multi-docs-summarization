package fudan.mmdb.mds.web.model.xmlfeed;

import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import fudan.mmdb.mds.web.model.MdsDocXmlFeed;
import fudan.mmdb.mds.web.model.MdsSolrDocument;

public class XmlDocConverterImplTest {

	XmlDocConverterImpl cnvtrImpl;
	
	@Before
	public void setUp() throws JAXBException{
		cnvtrImpl=new XmlDocConverterImpl();
	}
	
	@Test
	public void testConvertFromXml() {
		//arrange the data
		MdsDocXmlFeed feed=new MdsDocXmlFeed();
		ArrayList<MdsSolrDocument> docs=Lists.newArrayList();
		docs.add(new MdsSolrDocument("1","Doc1","Doc Content 1") );
		docs.add(new MdsSolrDocument("2","Doc2","Doc Content 2") );
		feed.setDocuments(docs);
		feed.setSize(2);
		//action
		String xml=this.cnvtrImpl.convertToXMl(feed);
		//assertion
		Assert.isTrue(!Strings.isNullOrEmpty(xml));
		System.out.println(xml);
	}

}
