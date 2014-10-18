package fudan.mmdb.mds.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.model.solr.MdsSolrDocument;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverter;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverterImpl;
import fudan.mmdb.mds.web.model.ClusteredResponse;
import fudan.mmdb.mds.web.repository.mongo.MongoDocRepository;

@Service
public class RootService {

    @Autowired
    private SolrIndexService solrService;

    @Autowired
    private MongoDocRepository mongoRepository;
    
    @Autowired
    private SummaryService sumSerivce;
    
    public String getSummary(List<String> ids){
        
        List<MdsDocument> mdsDocs = Lists.newArrayList(mongoRepository
                .findAll(ids));
        
        return sumSerivce.genSummary(mdsDocs);
    }
    
   
    public MdsDocument addDoc(MdsDocument newDoc) {

        MdsDocument returnedDoc = mongoRepository.save(newDoc);

        MdsSolrDocument solrDoc = new MdsSolrDocument(returnedDoc.getId(),
                returnedDoc.getTitle(), returnedDoc.getContent());

        solrService.addToIndex(solrDoc);

        return returnedDoc;
    }
    
    public List<MdsDocument> addDocs(List<MdsDocument> newDocs){
        
        List<MdsDocument> returnedDocs=Lists.newArrayList();
        
        for(MdsDocument newDoc: newDocs){
            MdsDocument retDoc=addDoc(newDoc);
            returnedDocs.add(retDoc);
        }
        return returnedDocs; 
    }
    
    public ClusteredResponse searchAndCluster(String searchTerm){
    	return solrService.clusterOnSearch(searchTerm); 
    }

}
