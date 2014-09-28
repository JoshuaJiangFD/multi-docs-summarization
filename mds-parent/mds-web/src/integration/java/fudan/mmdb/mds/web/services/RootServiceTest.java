package fudan.mmdb.mds.web.services;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.utils.IDocProcessor;
import fudan.mmdb.mds.web.config.HttpSolrConfig;
import fudan.mmdb.mds.web.config.MongoDbConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceTestConfig.class, MongoDbConfig.class,
        HttpSolrConfig.class })
public class RootServiceTest {

    @Autowired
    RootService rootService;

    @Autowired
    IDocProcessor docProcessor;

    private List<MdsDocument> docs = Lists.newArrayList();

    @Before
    public void setUp() {
        genAllDocs();
    }

    private void genAllDocs() {
        String rootPath = "corpus-utf8";
        File rootFile = new File(rootPath);
        for(File file:rootFile.listFiles()){
            docs.addAll(getDocs(file));
        }
        
    }

    private List<MdsDocument> getDocs(File dir) {
        try {
            List<MdsDocument> docs = Lists.newArrayList();
            for (File file : dir.listFiles()) {
                List<String> allLines = Files.readAllLines(file.toPath(),
                        Charsets.UTF_8);
                String docContent = Joiner.on(" ").skipNulls().join(allLines);
                MdsDocument doc = docProcessor.genDocument(docContent);
                //doc.setTitle(file.getName());
                docs.add(doc);
            }
            return docs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Test
    public void testAddDocs() {

        List<MdsDocument> resDocs = rootService.addDocs(docs);
        int i = 0;
        for (MdsDocument doc : resDocs) {
            i++;
            System.out.printf("[id: %s]", doc.getId());
            if (i == 5) {
                i = 0;
                System.out.println();
            }
        }
    }

}
