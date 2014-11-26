package fudan.mmdb.mds.core;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.model.Sentence;
import fudan.mmdb.mds.core.utils.DocProcessor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml",
        "classpath:mdscore-context.xml" })
public class SummarizerTest {

    private static Logger logger = Logger.getLogger(SummarizerTest.class);

    @Autowired
    private DocProcessor docProcessor;

    private List<MdsDocument> documents = new ArrayList<MdsDocument>();

    private Summarizer summarizer;

    @Before
    public void setUp() throws Exception {
        try {
            String filePath = "corpus-utf8/巴以形势";
            File dir = new File(filePath);
            for (File file : dir.listFiles()) {
                List<String> allLines = Files.readAllLines(file.toPath(),
                        Charsets.UTF_8);
                String docContent = Joiner.on(" ").skipNulls().join(allLines);
                documents.add(docProcessor.genDocument(docContent));
            }
            // assign ID
            for (int i = 0; i < documents.size(); i++) {
                MdsDocument doc = documents.get(i);
                doc.setId(String.valueOf(i));
                for (int j = 0; j < doc.getSentences().size(); j++) {
                    Sentence sent = doc.getSentences().get(j);
                    String senId = String.format("%d#%d", i, j);
                    sent.setId(senId);
                    sent.setDocId(String.valueOf(i));
                    sent.setDocPos(j * 1.0 / doc.getSentences().size());
                }
            }
            summarizer = new Summarizer(documents);
            summarizer.setDocProcessor(docProcessor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void testCalculateDocFreqs() {
        summarizer.calculateDocFreqs();
        Map<String, Integer> df = this.summarizer.getDocFrequencies();
        int i = 0;
        for (String key : df.keySet()) {
            System.out.printf("[key:%s,freq:%d]  ", key, df.get(key));
            i++;
            if (i == 5) {
                System.out.println();
                i = 0;
            }
        }
        System.out.println();
        summarizer.calculateWordTFIDF();
        Map<String, Map<String, Double>> wordWeights = this.summarizer
                .getWordWeights();
        for (String key : wordWeights.keySet()) {
            System.out.printf("[document key:%s]\n", key);
            int j = 0;
            Map<String, Double> value = wordWeights.get(key);
            for (String word : value.keySet()) {
                System.out.printf("[word:%s,weight:%f]", word, value.get(word));
                j++;
                if (j == 5) {
                    System.out.println();
                    j = 0;
                }
            }
            System.out.println("\n************");
        }
        summarizer.CalcuateSentenceWeights();
        int k=0;
        Map<String,Double> sentW=summarizer.getSentenceWeights();
        List<String> keys=Lists.newArrayList(sentW.keySet());
        Collections.sort(keys);
        for(String key:keys){
            k++;
            System.out.printf("[sent:%s,weight:%f]", key, sentW.get(key));
            if (k == 5) {
                System.out.println();
                k = 0;
            }
        }
        System.out.println();
    }

    @Test
    public void testSummarizeDocs() {
        String result = summarizer.summarizeDocs();
        System.out.println(result);
    }
}
