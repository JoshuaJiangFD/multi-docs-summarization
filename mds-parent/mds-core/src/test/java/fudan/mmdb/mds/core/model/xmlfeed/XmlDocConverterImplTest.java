package fudan.mmdb.mds.core.model.xmlfeed;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import fudan.mmdb.mds.core.model.MdsDocument;

public class XmlDocConverterImplTest {

	XmlDocConverterImpl cnvtrImpl;
	
	@Before
	public void setUp() throws JAXBException{
		cnvtrImpl=new XmlDocConverterImpl();
	}
	
	@Test
	public void test_ConvertFromXml() {
		//arrange the data
		MdsDocXmlFeed feed=new MdsDocXmlFeed();
		ArrayList<MdsDocument> docs=Lists.newArrayList();
		docs.add(new MdsDocument("1","Doc1","Doc Content 1",null) );
		docs.add(new MdsDocument("2","Doc2","Doc Content 2",null) );
		feed.setDocuments(docs);
		feed.setSize(2);
		//action
		String xml=this.cnvtrImpl.convertToXMl(feed);
		//assertion
		Assert.isTrue(!Strings.isNullOrEmpty(xml));
		System.out.println(xml);
	}
	
	@Test
	public void test_convertFromXml(){
	   	String path="C:/Users/泳/Desktop/dataset_619961/target/20131102.xml";
	   	File file=new File(path);
	   	MdsDocXmlFeed feed=this.cnvtrImpl.convertFromFile(file);
	   	System.out.println(feed.getSize());
	   	
	   	
	}

}
