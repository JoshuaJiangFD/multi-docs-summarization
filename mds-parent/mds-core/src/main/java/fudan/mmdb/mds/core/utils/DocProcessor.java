package fudan.mmdb.mds.core.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fudan.mmdb.mds.analyzer.ICTCLASAnalyzer;
import fudan.mmdb.mds.analyzer.IZHAnalyzer;
import fudan.mmdb.mds.analyzer.MMSegAnalyzer;
import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.model.Ngram;
import fudan.mmdb.mds.core.model.Sentence;
import fudan.mmdb.mds.wordsimilarity.ISimilarityCalculator;
import fudan.mmdb.mds.wordsimilarity.model.Word;

@Component
public class DocProcessor implements IDocProcessor {

	private static Logger logger = Logger.getLogger(DocProcessor.class);

	private static String PERIOD = "。";

	private static String QUOTE_START = "：“";

	private static String QUOTE_END = "”";

	private final List<String> ADOPTEDWORDATTRs = Lists.newArrayList("n", "t",
			"v", "a", "f");

	/**
	 * constant. to avoid assigning zero value to sentence similarity
	 */
	private static double epsilon = Math.exp(-10);

	/**
	 * map to store the "word1#word2-->similarity" key-value pair
	 */
	public ConcurrentMap<String, Double> WordSims = Maps.newConcurrentMap();

	@Autowired
	private ISimilarityCalculator simCalculator;
	
	private StanfordNLPHelper nlpHelper=new StanfordNLPHelper();

	@Autowired
	private IZHAnalyzer zhAnalyzer;

	public List<String> STOPWORDS = new ArrayList<String>();

	public DocProcessor(){
		
	}
	
	
	@PostConstruct
	private void LoadStopWords() throws IOException {
		ResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader
				.getResource("classpath:chinese-stopwords.txt");
		List<String> allLines = Files.readAllLines(resource.getFile().toPath(),
				Charsets.UTF_8);
		for (String str : allLines) {
			if (Strings.isNullOrEmpty(str))
				continue;
			STOPWORDS.add(str.trim());
		}
	}

	/**
	 * generate a brand-new document object with all related data prepared. All
	 * the Id field in document and sentence are empty.
	 * 
	 * @param content
	 */
	public MdsDocument genDocument(String content) {
		MdsDocument doc = new MdsDocument();
		doc.setContent(content);
		List<String> sentenceStrs =this.nlpHelper.splitSentences(content);
		List<Sentence> sentences = new ArrayList<Sentence>();
		int wordCount = 0;
		for (int i = 0; i < sentenceStrs.size(); i++) {
			String sentenceStr = sentenceStrs.get(i);
			Sentence sent = new Sentence();
			sent.setContent(sentenceStr);
			List<Word> words = this.genWords(sentenceStr);
			sent.setWords(words);
			sentences.add(sent);
			wordCount += words.size();
			for (Word w : words) {
				Integer cur = doc.getTermFreqs().get(w.getWord());
				cur = (cur == null) ? 1 : cur + 1;
				doc.getTermFreqs().put(w.getWord(), cur);
			}
		}
		doc.setSentences(sentences);
		doc.setWordCount(wordCount);
		return doc;
	}

	/**
	 * split content into list of sentences.
	 * 
	 * @param content
	 * @return list of sentences
	 * @see {@link fudan.mmdb.mds.core.model.Sentence}
	 */
	public List<String> splitContent(String content) {
		List<String> result = new ArrayList<String>();
		int len = content.length();
		int index_start = 0;
		int sentence_start = index_start;
		String sentence = null;
		while (sentence_start < len) {
			int sentence_end = content.indexOf(PERIOD, sentence_start);
			// the last sentence
			if (sentence_end < 0) {
				// if the sentence is ended with period.
				if (content.indexOf(QUOTE_START, sentence_start) >= 0) {
					sentence_end = content.indexOf(QUOTE_END, sentence_start);
					sentence = content.substring(sentence_start, sentence_end);
					sentence += QUOTE_END;
					result.add(sentence.trim());
					sentence_start = sentence_end + 1;
					continue;
				}
				result.add((content.substring(sentence_start)).trim());
				break;
			}
			sentence = content.substring(sentence_start, sentence_end);
			// sentence is a quotation.
			if (sentence.indexOf(QUOTE_START) >= 0) {
				sentence_end = content.indexOf(QUOTE_END, sentence_start);
				sentence = content.substring(sentence_start, sentence_end);
				sentence += QUOTE_END;
				result.add(sentence.trim());
			} else {
				sentence += PERIOD;
				result.add(sentence.trim());
			}
			sentence_start = sentence_end + 1;
		}
		return result;
	}

	/**
	 * calculate the similarity between two sentences.
	 * 
	 * @param s1
	 * @param s2
	 * @return similarity between s1 and s2.
	 */
	public double simSentence(Sentence s1, Sentence s2) {
		double sim = 0.0;
		double result = 0;
		int minLen = Math.min(s1.getWordCount(), s2.getWordCount());
		int maxN = minLen > 3 ? 3 : minLen;
		for (int i = 1; i <= maxN; i++) {
			sim = simSentence(s1, s2, i);
			result += (Math.log(sim) / minLen);
		}
		return Math.exp(result);
	}

	/**
	 * calculate the similarity between two sentences based on N-Gram.
	 * 
	 * @param s1
	 * @param s2
	 * @param nParam
	 *            value of NGram.
	 * @return similarity between s1 and s2.
	 */
	protected double simSentence(Sentence s1, Sentence s2, int nParam) {
		List<Ngram> list1 = s1.getNgrams(nParam);
		List<Ngram> list2 = s2.getNgrams(nParam);
		double averageLen = (list1.size() + list2.size()) / 2.0;
		int min = Math.min(list1.size(), list2.size());
		double result = 0.0;
		double max = 0.0;
		int count = 0;
		int i, j;

		Map<String, Double> map = new HashMap<String, Double>();
		for (i = 0; i < list1.size(); i++) {
			for (j = 0; j < list2.size(); j++) {
				double sim = simNgram(list1.get(i), list2.get(j));
				map.put(i + "#" + j, sim);
			}
		}
		while (count < min) {
			max = 0.0;
			String index = "";
			for (String key : map.keySet()) {
				double sim = map.get(key);
				if (sim >= max) {
					max = sim;
					index = key;
				}
			}
			result += max;
			map.remove(index);
			int sharp_index = index.indexOf('#');
			String str1 = index.substring(0, sharp_index);
			String str2 = index.substring(sharp_index + 1);
			Set<String> keys = new HashSet<String>(map.keySet());
			for (String key : keys) {
				if (key.startsWith(str1 + '#') || key.endsWith('#' + str2)) {
					map.remove(key);
				}
			}
			count++;
		}
		return result / averageLen;
	}

	private double simNgram(Ngram NGram1, Ngram NGram2) {
		if (NGram1.getN() != NGram2.getN()) {
			throw new IllegalArgumentException(
					"can't calculate similarity between different order.");
		}

		double result = 1.0d;
		List<Word> list1 = NGram1.getGrams();
		List<Word> list2 = NGram2.getGrams();

		for (int i = 0; i < NGram1.getN(); i++) {
			double sim = getWordSim(list1.get(i), list2.get(i));
			sim = (sim == 0) ? epsilon : sim;
			result *= sim;
		}
		return result;
	}

	private double getWordSim(Word w1, Word w2) {
		List<String> words = Lists.newArrayList(w1.getWord(), w2.getWord());
		Collections.sort(words);
		String key = Joiner.on("#-#").join(words);
		if (WordSims.containsKey(key)) {
			return WordSims.get(key);
		}
		double sim = simCalculator.simWord(w1.getWord(), w2.getWord());
		WordSims.put(key, sim);
		return sim;
	}

	public List<Word> genWords(String sentence) {
		List<Word> words = Lists.newArrayList();
		List<String> wordStrs = this.zhAnalyzer.analyze(sentence);
		Class<? extends IZHAnalyzer> clazz = this.zhAnalyzer.getClass();
		if (clazz.equals(MMSegAnalyzer.class)) {
			for (String str : wordStrs) {
				if (STOPWORDS.contains(str))
					continue;
				Word word = new Word();
				word.setWord(str);
				words.add(word);
			}
			return words;
		} else if (clazz.equals(ICTCLASAnalyzer.class)) {
			for (String str : wordStrs) {
				int pos = str.lastIndexOf("/");
				if (pos > 0) {
					String str1 = str.substring(0, pos).trim();
					if (str1.contains(".") || str1.contains("$"))
						continue;
					final String type = str.substring(pos + 1).trim();
					if (STOPWORDS.contains(str1))
						continue;
					Boolean attriPassed = Iterables.any(ADOPTEDWORDATTRs,
							new Predicate<String>() {
								@Override
								public boolean apply(String attri) {
									if (type.startsWith(attri))
										return true;
									return false;
								}
							});
					if (attriPassed) {
						words.add(new Word(str1, type));
					}
				}
			}
			return words;
		} else {
			logger.error("ERROR in GenWords.");
			return null;
		}
	}

	@Override
	public void analyzeMdsDocument(MdsDocument doc) {

		List<String> sentenceStrs = this.nlpHelper.splitSentences(doc.getContent());
		List<Sentence> sentences = new ArrayList<Sentence>();
		int wordCount = 0;
		for (int i = 0; i < sentenceStrs.size(); i++) {
			String sentenceStr = sentenceStrs.get(i);
			Sentence sent = new Sentence();
			sent.setContent(sentenceStr);
			List<Word> words = this.genWords(sentenceStr);
			sent.setWords(words);
			sentences.add(sent);
			wordCount += words.size();
			for (Word w : words) {
				Integer cur = doc.getTermFreqs().get(w.getWord());
				cur = (cur == null) ? 1 : cur + 1;
				doc.getTermFreqs().put(w.getWord(), cur);
			}
		}
		doc.setSentences(sentences);
		doc.setWordCount(wordCount);
	}
}
