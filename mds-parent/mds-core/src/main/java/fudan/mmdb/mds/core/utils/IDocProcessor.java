package fudan.mmdb.mds.core.utils;

import java.util.List;

import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.model.Sentence;

public interface IDocProcessor {

    /**
     * generate a brand-new document object with all related data prepared.
     * 
     * @param content
     */
    MdsDocument genDocument(String content);

    /**
     * split content into list of sentence.
     * 
     * @param content
     * @return list of sentence
     */
    List<String> splitContent(String content);

    /**
     * calculate the similarity between two sentences.</br> this method uses the
     * Ngram of each sentence.</br>
     * 
     * @param s1
     * @param s2
     * @return
     */
    double simSentence(Sentence s1, Sentence s2);
    
    /**
     * analyze the semantic info inside a doc, mainly includes:
     * sentence detection, word parser.
     */
    void analyzeMdsDocument(MdsDocument doc);
}
