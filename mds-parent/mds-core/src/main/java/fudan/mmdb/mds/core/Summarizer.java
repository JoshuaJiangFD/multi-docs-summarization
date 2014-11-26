package fudan.mmdb.mds.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.model.Sentence;
import fudan.mmdb.mds.core.utils.IDocProcessor;
import fudan.mmdb.mds.wordsimilarity.model.Word;

/**
 * Task class for handling concrete summarization of given document set. For
 * each pending summarization task, an new DocSummarizer will be created. This
 * object will be disposed by GC after completion.
 * 
 * @author Joshua Jiang
 * 
 */
public final class Summarizer{

    private IDocProcessor docProcessor;

    private final Logger logger = Logger.getLogger(this.getClass());

    private List<MdsDocument> documents;

    private final Map<String, Map<String, Double>> wordWeights = new HashMap<String, Map<String, Double>>();

    private Map<String, Double> sentenceWeights = new HashMap<String, Double>();

    private static double compressionRatio = 1.0;
    
    private int size;

	private static double alpha = 0.4;

    /**
     * the over all count of words in documents
     */
    private int wordCount;

    /**
     * the over all sentences
     */
    private List<Sentence> unSelectedSentences = new ArrayList<Sentence>();

    /**
     * the selected sentences.
     */
    private List<Sentence> selectedSentences = new ArrayList<Sentence>();

    /**
     * keep the maximum similarity between every unselected sentence and the
     * selected set. updated after every new selection. key: sentenceId value:
     * the maximum similarity between the key sentence and the selected set
     */
    private Map<String, Double> waitingSentsSim = new HashMap<String, Double>();

    /**
     * similarity between words
     */
    public ConcurrentMap<String, Double> WordSims = Maps.newConcurrentMap();

    /**
     * All the words in {@link Summarizer#documents}, and the corresponding
     * count. The value is derived from every document's TF.</br> This is
     * Ultimately used to calculate word's TF*IDF.(Inverted Document Frequency)
     * 
     */
    private final Map<String, Integer> docFrequencies = new HashMap<String, Integer>();

    public Summarizer(List<MdsDocument> docs,int size) {
        this.documents = docs;
        this.size=size;
    }
    
    public Summarizer(List<MdsDocument> docs) {
    	this(docs,0);
    }


    public String summarizeDocs() {
        // calculate document frequencies of each word.
        calculateDocFreqs();

        // calculate the all the words' weight(TF*IDF).
        calculateWordTFIDF();

        // calculate every sentence's weight, which is based on words' weights
        // in each document.
        CalcuateSentenceWeights();

        // initiate data
        double selectedLen = 0;
        double averageLen = (double) this.wordCount / documents.size();

        for (Sentence unselected : this.unSelectedSentences) {
            this.waitingSentsSim.put(unselected.getId(), 0.0);
        }

        // main loop: iterating sentence set to select sentence.
        while (selectedLen / averageLen < compressionRatio) {
            Sentence selected = this.selectSentence();
            if(selected==null){
            	logger.info(String.format("Select NULL sentence. selected size :%f.",selectedLen));
            	break;
            }
            logger.info(String.format("Select the sentence : %s", selected.getId()));
            selectedLen += selected.getWordCount();
            unSelectedSentences.remove(selected);
            waitingSentsSim.remove(selected.getId());
            selectedSentences.add(selected);
            // update waitingSentsSim
            for (Sentence unselected : unSelectedSentences) {
                double sim = docProcessor.simSentence(unselected, selected);
                if (sim > waitingSentsSim.get(unselected.getId())) {
                    waitingSentsSim.put(unselected.getId(), sim);
                }
            }
            if(this.size>0&selectedSentences.size()>=size)
            	break;
        }
        logger.info(String.format(
                "Selected set size: %d, unselected set size: %d.",
                this.selectedSentences.size(), this.unSelectedSentences.size()));

        // sort sentences
        return sortSentence();
    }

    protected void calculateDocFreqs() {
        for (MdsDocument doc : this.documents) {
            wordCount += doc.getWordCount();
            unSelectedSentences.addAll(doc.getSentences());
            // calculate df for each word.
            for (String word : doc.getTermFreqs().keySet()) {
                if (this.docFrequencies.containsKey(word))
                    this.docFrequencies.put(word, docFrequencies.get(word) + 1);
                else
                    this.docFrequencies.put(word, 1);
            }
        }
    }

    protected Sentence selectSentence() {
        double max = Double.MIN_VALUE;
        Sentence toBeSelected = null;
        for (Sentence sentence : unSelectedSentences) {
            // sim1
            double sim1 = this.sentenceWeights.get(sentence.getId());
            // sim2
            double sim2 = waitingSentsSim.get(sentence.getId());

            double mmr = sim1 * alpha + (1 - alpha) * sim2;
            if (mmr > max) {
                toBeSelected = sentence;
                max = mmr;
            }
        }
        return toBeSelected;
    }

    /**
     * Calculate every word's <code>TF*IDF</code> for each document.</br> IDF
     * information is kept inside corresponding DocSummarizer.
     * 
     * @see {@link MdsDocument#tfIdfs}
     * @author Joshua Jiang
     */
    protected void calculateWordTFIDF() {
        int N = documents.size();
        for (MdsDocument document : documents) {
            HashMap<String, Double> weights = new HashMap<String, Double>();
            int count = document.getWordCount();
            Map<String, Integer> words = document.getTermFreqs();
            for (String word : words.keySet()) {
                int dfValue = docFrequencies.get(word);
                double doubleIDF = Math.log(N * 1.0 / dfValue);
                double tfidf = (words.get(word) * 1.0 / count) * doubleIDF;
                weights.put(word, tfidf);
            }
            this.wordWeights.put(document.getId(), weights);
        }
    }

    /**
     * calculate each sentence'e weight inside the document.</br> The
     * calculation is based on {@link MdsDocument#tfIdfs}
     */
    public void CalcuateSentenceWeights() {
        for (MdsDocument doc : documents) {
            Map<String, Double> tfIdfs = this.wordWeights.get(doc.getId());
            for (Sentence sentence : doc.getSentences()) {
                List<Word> words = sentence.getWords();
                double weight = 0.0;
                for (Word word : words) {
                    weight += tfIdfs.get(word.getWord());
                }
                weight /= words.size();
                this.sentenceWeights.put(sentence.getId(), weight);
            }
        }
    }

    public String sortSentence() {
        StringBuffer buff = new StringBuffer();
        while (selectedSentences.size() > 0) {
            Sentence s = null;
            double p = 1.1;
            for (Sentence sentence : selectedSentences) {
                if (sentence.getDocPos() < p) {
                    s = sentence;
                    p = sentence.getDocPos();
                }
            }
            buff.append(s.getContent() + "\n");
            selectedSentences.remove(s);
        }
        return buff.toString();
    }

    public IDocProcessor getDocProcessor() {
        return docProcessor;
    }

    public void setDocProcessor(IDocProcessor docProcessor) {
        this.docProcessor = docProcessor;
    }

    public Map<String, Double> getSentenceWeights() {
        return sentenceWeights;
    }

    public Map<String, Integer> getDocFrequencies() {
        return docFrequencies;
    }
    public Map<String, Map<String, Double>> getWordWeights() {
        return wordWeights;
    }
    
    public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
