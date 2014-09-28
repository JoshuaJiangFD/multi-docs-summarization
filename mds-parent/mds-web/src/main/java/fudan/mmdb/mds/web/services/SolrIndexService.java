package fudan.mmdb.mds.web.services;

import java.util.List;

import fudan.mmdb.mds.web.model.MdsSolrDocument;

public interface SolrIndexService {

    public void addToIndex(MdsSolrDocument todoEntry);

    public void deleteFromIndex(String id);

    public List<MdsSolrDocument> search(String searchTerm);

    public void update(MdsSolrDocument todoEntry);
    
}
