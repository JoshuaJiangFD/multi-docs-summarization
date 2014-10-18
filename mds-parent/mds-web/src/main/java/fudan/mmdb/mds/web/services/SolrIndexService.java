package fudan.mmdb.mds.web.services;

import java.util.List;

import fudan.mmdb.mds.core.model.solr.MdsSolrDocument;
import fudan.mmdb.mds.web.model.ClusteredResponse;

public interface SolrIndexService {

    public void addToIndex(List<MdsSolrDocument> todoEntry);

    public void deleteFromIndex(String id);

    public List<MdsSolrDocument> search(String searchTerm);

    public void update(MdsSolrDocument todoEntry);
    
    public ClusteredResponse clusterOnSearch(String searchTerm);
    
}
