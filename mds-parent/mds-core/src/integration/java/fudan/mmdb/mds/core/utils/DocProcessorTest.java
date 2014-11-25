package fudan.mmdb.mds.core.utils;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.model.Sentence;
import fudan.mmdb.mds.wordsimilarity.model.Word;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:test-context.xml" })
public class DocProcessorTest {

	@Autowired
	private DocProcessor docProcessor;

	private String docContent;

	private static Logger logger = Logger.getLogger(DocProcessorTest.class);
	
    private String example1="中国首都附近发生了强烈海啸";
    
    private String example2="民国首都附近发生了强烈地震";
    
    private Sentence sentence1;
    
    private Sentence sentence2;

	@Before
	public void Setup() {
		try {
			String filePath = "corpus-utf8/阿富汗发生地震/1.txt";
			List<String> allLines = Files.readAllLines(Paths.get(filePath),Charsets.UTF_8);
			docContent = Joiner.on(" ").skipNulls().join(allLines);
			
	        sentence1 = new Sentence();
	        sentence1.setContent(example1);
	        List<Word> words = docProcessor.genWords(example1);
	        sentence1.setWords(words);
	        
	        sentence2 = new Sentence();
	        sentence2.setContent(example2);
	        List<Word> words2 = docProcessor.genWords(example2);
	        sentence2.setWords(words2);
		} catch (Exception ex) {
		    logger.error(ex);
		    ex.printStackTrace();
		}
	}

	@Test 
	public void Test_Status(){
	    Assert.notEmpty(docProcessor.STOPWORDS);
	}
	
	@Test
	@Ignore
	public void testSplitContent() throws Exception {
		List<String> sentences = docProcessor.splitContent(docContent);
		for (String s : sentences) {
			logger.info(s);
		}
		Assert.notEmpty(sentences);
	}
	
	@Test
	@Ignore
	public void testSimSentence_1(){
	    double sim=docProcessor.simSentence(sentence1, sentence2,1);
	    System.out.println(sim);
	}

	@Test
	@Ignore
    public void testSimSentence(){
        double sim=docProcessor.simSentence(sentence1, sentence2);
        System.out.println(sim);
    }

	
	@Test
	public void testGenDocument() {
		MdsDocument document = docProcessor.genDocument(docContent);
		for (Sentence sent : document.getSentences()) {
			System.out.println(sent.getContent());
			List<String> wordStrs = FluentIterable.from(sent.getWords())
					.transform(new Function<Word, String>() {
						@Override
						public String apply(Word w) {
							return w.getWord();
						}

					}).toList();
			System.out.println(Joiner.on("|").join(wordStrs));
		}
        Map<String,Integer> tf=document.getTermFreqs();
        for (String  key : tf.keySet()) {
            System.out.printf("[key:%s,freq:%d]\n",key,tf.get(key));
        }
	}

}
