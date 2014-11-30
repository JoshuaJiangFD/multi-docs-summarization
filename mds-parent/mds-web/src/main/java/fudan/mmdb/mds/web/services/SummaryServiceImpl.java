package fudan.mmdb.mds.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fudan.mmdb.mds.core.Summarizer;
import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.utils.IDocProcessor;
import fudan.mmdb.mds.web.model.SumRequest;

@Service
public class SummaryServiceImpl implements SummaryService {

	@Autowired
	IDocProcessor processor;
	
    @Override
    public String genSummary(List<MdsDocument> docs,SumRequest request) {
        
        Summarizer worker=new Summarizer(docs,request.getSize());
        
        worker.setDocProcessor(processor);
        
        return worker.summarizeDocs();
    }

}
