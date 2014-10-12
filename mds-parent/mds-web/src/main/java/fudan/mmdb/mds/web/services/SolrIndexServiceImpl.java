package fudan.mmdb.mds.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fudan.mmdb.mds.core.model.solr.MdsSolrDocument;
import fudan.mmdb.mds.web.model.ClusteredResponse;
import fudan.mmdb.mds.web.repository.solr.DocumentRepository;


@Service
public class SolrIndexServiceImpl implements SolrIndexService {

    @Autowired
    private DocumentRepository docRepo;
    
    @Override
    public void addToIndex(MdsSolrDocument todoEntry) {
        docRepo.save(todoEntry);
    }

    @Override
    public void deleteFromIndex(String id) {
        
        docRepo.delete(id);
    }

    @Override
    public List<MdsSolrDocument> search(String searchTerm) {
        return docRepo.findDocs(searchTerm);
    }

    @Override
    public void update(MdsSolrDocument todoEntry) {
        docRepo.save(todoEntry);
    }
    
    public ClusteredResponse clusterOnSearch(String searchTerm){
    	return docRepo.getClusters(searchTerm);
    }

}
