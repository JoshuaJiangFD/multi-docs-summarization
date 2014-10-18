package fudan.mmdb.mds.web.services;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fudan.mmdb.mds.core.model.solr.MdsSolrDocument;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverter;
import fudan.mmdb.mds.web.config.HttpSolrConfig;
import fudan.mmdb.mds.web.config.MongoDbConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceTestConfig.class, MongoDbConfig.class,
        HttpSolrConfig.class })
public class SolrIndexServiceImplTest {

	@Autowired
	SolrIndexService indexService;
	
	@Autowired
	XmlDocConverter converter;
	
	@Test
	public void testAddToIndex() {
		String path="C:/Users/泳/Desktop/dataset_619961/target/20131102.xml";
		List<MdsSolrDocument> docs=converter.convertFromFile(new File(path)).getDocuments();
		indexService.addToIndex(docs);
		
	}

}
