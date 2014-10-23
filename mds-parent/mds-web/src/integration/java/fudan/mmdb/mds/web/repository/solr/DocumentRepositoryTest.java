package fudan.mmdb.mds.web.repository.solr;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import fudan.mmdb.mds.core.model.solr.MdsSolrDocument;
import fudan.mmdb.mds.web.config.HttpSolrConfig;
import fudan.mmdb.mds.web.model.ClusteredResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HttpSolrConfig.class)
public class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository docRepository;

    @Before
    public void setUp() throws Exception {
    	
    }

    @After
    public void tearDown() throws Exception {
    	
    }

    @Test
    @Ignore
    public void testFindByQueryAnnotation() {
        List<MdsSolrDocument> docs = docRepository.findDocs("巴勒斯坦");
        for (MdsSolrDocument doc : docs) {
            System.out.println(doc.getContent());
        }
    }

    @Test
    public void testDeleteAllDocs() {
        docRepository.deleteAll();
    }

    @Test
    public void testClustering(){
    	ClusteredResponse response=this.docRepository.getClusters("中纪委");
    	System.out.println(response.getClusters().size());
    	System.out.println(response.getDocs().size());
    }
    
    @Test
    @Ignore
    public void testQuery() {
        Assert.notNull(docRepository);
        List<MdsSolrDocument> allDocs = Lists.newArrayList(docRepository
                .findAll());
        System.out.printf("docs size in Solr :%d", allDocs.size());
    }

}
