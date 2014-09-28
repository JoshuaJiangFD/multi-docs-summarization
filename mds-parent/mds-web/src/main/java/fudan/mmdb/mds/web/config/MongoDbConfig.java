package fudan.mmdb.mds.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(basePackages={
        "fudan.mmdb.mds.web.repository.mongo"
})
public class MongoDbConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "local";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient("127.0.0.1");
    }

    
    /**
     *  tells the converter where to scan for classes 
     *  annotated with the @org.springframework.data.mongodb.core.mapping.Document annotation.
     */
    @Override
    protected String getMappingBasePackage() {
        return "fudan.mmdb.mds.core.model";
        
    }

}
