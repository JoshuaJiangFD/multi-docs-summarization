package fudan.mmdb.mds.web.services;

import java.util.List;

import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.web.model.SumRequest;


public interface SummaryService {

     String genSummary(List<MdsDocument> docs,SumRequest request);
    
}
