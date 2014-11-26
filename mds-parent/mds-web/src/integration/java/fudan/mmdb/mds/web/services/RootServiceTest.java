package fudan.mmdb.mds.web.services;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.model.xmlfeed.MdsDocXmlFeed;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverter;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverterImpl;
import fudan.mmdb.mds.core.utils.IDocProcessor;
import fudan.mmdb.mds.web.config.HttpSolrConfig;
import fudan.mmdb.mds.web.config.MongoDbConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceTestConfig.class, MongoDbConfig.class,HttpSolrConfig.class })
public class RootServiceTest {

	@Autowired
	RootService rootService;

	@Autowired
	IDocProcessor docProcessor;

	@Autowired
	XmlDocConverter converter;

	private List<MdsDocument> docs = Lists.newArrayList();

	@Before
	public void setUp() {
		genAllDocs();
	}

	private void genAllDocs() {
		String path = "C:/Users/泳/Desktop/dataset_619961/target/20131103.xml";
		File file = new File(path);
		MdsDocXmlFeed feed = this.converter.convertFromFile(file);
		docs = feed.getDocuments();
	}

	@Test
	public void testAddDocs() {

		List<MdsDocument> resDocs = rootService.addDocs(docs);
		int i = 0;
		for (MdsDocument doc : resDocs) {
			i++;
			System.out.printf("[id: %s]", doc.getId());
			if (i == 5) {
				i = 0;
				System.out.println();
			}
		}
	}
}
