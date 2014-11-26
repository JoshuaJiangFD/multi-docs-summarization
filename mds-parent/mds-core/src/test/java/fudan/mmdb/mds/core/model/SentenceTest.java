package fudan.mmdb.mds.core.model;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Joiner;

import fudan.mmdb.mds.core.utils.DocProcessor;
import fudan.mmdb.mds.wordsimilarity.model.Word;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:mdscore-context.xml" })
public class SentenceTest {

    private String example1="中国首都附近发生了强烈海啸";
    
    private String example2="民国首都附近发生了强烈地震";
    
    private Sentence sentence1;
    
    private Sentence sentence2;
    
    @Autowired
    private DocProcessor docProcessor;
    
    @Before
    public void setUp(){
        sentence1 = new Sentence();
        sentence1.setContent(example1);
        List<Word> words = docProcessor.genWords(example1);
        sentence1.setWords(words);
        
        sentence2 = new Sentence();
        sentence2.setContent(example1);
        List<Word> words2 = docProcessor.genWords(example2);
        sentence1.setWords(words2);
    }
    
    @Test
    public void testGetNgrams() {
        List<Ngram> grams=sentence1.getNgrams(3);
        System.out.println(Joiner.on("|").join(sentence1.getWords()));
        System.out.println(Joiner.on("|").join(grams));
    }

}
