package fudan.mmdb.mds.web.services;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.model.Sentence;
import fudan.mmdb.mds.core.model.solr.MdsSolrDocument;
import fudan.mmdb.mds.core.utils.IDocProcessor;
import fudan.mmdb.mds.web.model.ClusteredResponse;
import fudan.mmdb.mds.web.model.SumRequest;
import fudan.mmdb.mds.web.repository.mongo.MongoDocRepository;

@Service
public class RootService {

	private static Log logger=LogFactory.getLog(RootService.class);
	
    @Autowired
    private SolrIndexService solrService;

    @Autowired
    private MongoDocRepository mongoRepository;
    
    @Autowired
    private SummaryService sumSerivce;
    
    @Autowired
    private IDocProcessor docProcessor;
    
    public String getSummary(SumRequest request){
        
        List<MdsDocument> mdsDocs = Lists.newArrayList(mongoRepository
                .findAll(request.getDocIds()));
        
        return sumSerivce.genSummary(mdsDocs,request);
    }
    
   
    public MdsDocument addDoc(MdsDocument newDoc) {

    	//analyze new doc's semantic info
    	docProcessor.analyzeMdsDocument(newDoc);
    	
    	//save to MongoDB to generate the docId
        MdsDocument returnedDoc = mongoRepository.save(newDoc);
        
        //assign sentenceId and docId to each sentence inside the newDoc object.
        int i=0;
        for(Sentence sent:newDoc.getSentences()){
        	sent.setDocId(newDoc.getId());
        	sent.setId(String.format("%s-%d", newDoc.getId(),i));
        	i++;
        }
        
        //save it again
        mongoRepository.save(newDoc);
        
        //save to Solr
        MdsSolrDocument solrDoc = new MdsSolrDocument(returnedDoc.getId(),
                returnedDoc.getTitle(), returnedDoc.getContent(),returnedDoc.getDate(),returnedDoc.getUrl());
        solrService.addToIndex(Lists.newArrayList(solrDoc));

        logger.info(String.format("Successfully save doc: %s",newDoc.getId()));
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
