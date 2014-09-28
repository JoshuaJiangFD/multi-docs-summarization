package fudan.mmdb.mds.web.repository.solr;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import fudan.mmdb.mds.web.model.MdsSolrDocument;

public interface DocumentRepository extends SolrCrudRepository<MdsSolrDocument,String> {
    
    @Query("title:*?0* OR content:*?0*")
    public List<MdsSolrDocument> findByQueryAnnotation(String searchTerm, Sort sort);
    
    @Query("title:*?0* OR content:*?0*")
    public List<MdsSolrDocument> findByQueryAnnotation(String searchTerm);
    
    @Query("text:*?0*")
    public List<MdsSolrDocument> findDocs(String searchTerm);
    
    
    public List<MdsSolrDocument> findByContent(String searchTerm);
    
    @Query(requestHandler="/clustering",value="text:*?0*")
    public List<MdsSolrDocument> clusteringAllDocs(String searchTerm);
}
