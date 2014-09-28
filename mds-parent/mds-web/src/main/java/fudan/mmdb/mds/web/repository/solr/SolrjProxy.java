package fudan.mmdb.mds.web.repository.solr;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class SolrjProxy {

    SolrServer solrServer;
    
    String urlString="http://localhost:8983/solr";
    
    public SolrjProxy(){
        solrServer = new HttpSolrServer(urlString);
    }
    
    public void clustering(String queryStr) throws SolrServerException{
        SolrQuery query=new SolrQuery().setRequestHandler("/clustering").setParam("q", "以色列");
        QueryResponse response=this.solrServer.query(query);
        NamedList<Object> repObjs= response.getResponse();
        Object obj=repObjs.get("clusters");
        Class clazz=obj.getClass();
        }
}
