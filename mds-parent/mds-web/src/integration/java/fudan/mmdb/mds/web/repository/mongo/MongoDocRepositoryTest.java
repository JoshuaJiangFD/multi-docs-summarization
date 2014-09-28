package fudan.mmdb.mds.web.repository.mongo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;
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
import fudan.mmdb.mds.core.utils.DocProcessor;
import fudan.mmdb.mds.web.config.MongoDbConfig;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={
        TestConfig.class,
        MongoDbConfig.class
})
public class MongoDocRepositoryTest {

    private static Logger logger = Logger.getLogger(MongoDocRepositoryTest.class);
    
    @Autowired
    private MongoDocRepository mongoRepository;
    
    @Autowired
    private DocProcessor docProcessor;
    
    private MdsDocument exampleDoc;
    
    @Before
    public void Setup() {
        try {
            String filePath = "corpus-utf8/阿富汗发生地震/1.txt";
            List<String> allLines = Files.readAllLines(Paths.get(filePath),Charsets.UTF_8);
            String docContent = Joiner.on(" ").skipNulls().join(allLines);
            exampleDoc = docProcessor.genDocument(docContent);
        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
    }
    
    @Test
    @Ignore
    public void testAddDocs(){
        mongoRepository.save(exampleDoc);
        mongoRepository.deleteAll();
    }
    
    @Test
    public void testDeleteAllDocs(){
        mongoRepository.deleteAll();
    }
    
    @Test
    @Ignore
    public void testQuery(){
        List<MdsDocument> docs=Lists.newArrayList(mongoRepository.findAll());
        System.out.printf("docs size in MongoDB:%d",docs.size());
        
    }

}
