package fudan.mmdb.mds.web.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;


@Configuration
@EnableSolrRepositories("fudan.mmdb.mds.web.repository.solr")
@PropertySource("classpath:application.properties")
public class HttpSolrConfig {

    private static final String PROPERTY_NAME_SOLR_SERVER_URL = "solr.server.url";
    
    @Resource
    private Environment environment;
    
    @Bean
    public HttpSolrServerFactoryBean solrServerFactoryBean() {
        HttpSolrServerFactoryBean factory = new HttpSolrServerFactoryBean();

        factory.setUrl(environment.getRequiredProperty(PROPERTY_NAME_SOLR_SERVER_URL));

        return factory;
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        return new SolrTemplate(solrServerFactoryBean().getObject());
    }
}
