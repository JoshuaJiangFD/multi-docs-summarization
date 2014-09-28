package fudan.mmdb.mds.web.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fudan.mmdb.mds.core.Summarizer;
import fudan.mmdb.mds.core.model.MdsDocument;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Override
    public String genSummary(List<MdsDocument> docs) {
        
        Summarizer worker=new Summarizer(docs);
        
        return worker.summarizeDocs();
    }

}
