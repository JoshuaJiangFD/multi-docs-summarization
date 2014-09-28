package fudan.mmdb.mds.web.services;

import java.util.List;

import fudan.mmdb.mds.core.model.MdsDocument;


public interface SummaryService {

     String genSummary(List<MdsDocument> docs);
    
}
