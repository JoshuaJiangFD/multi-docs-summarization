package fudan.mmdb.mds.web.repository.solr;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

public class SolrjProxyTest {

    SolrjProxy proxy=new SolrjProxy();
    
    @Test
    public void testClustering() throws SolrServerException {
        proxy.clustering("以色列");
    }

}
