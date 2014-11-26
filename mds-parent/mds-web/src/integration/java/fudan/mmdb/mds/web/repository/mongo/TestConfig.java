package fudan.mmdb.mds.web.repository.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import fudan.mmdb.mds.analyzer.ICTCLASAnalyzer;
import fudan.mmdb.mds.analyzer.IZHAnalyzer;
import fudan.mmdb.mds.core.utils.DocProcessor;
import fudan.mmdb.mds.core.utils.IDocProcessor;
import fudan.mmdb.mds.wordsimilarity.ISimilarityCalculator;
import fudan.mmdb.mds.wordsimilarity.SimilarityCalculatorImpl;

@Configuration
@PropertySource("classpath:core.properties")
public class TestConfig {

    
    @Bean
    public IZHAnalyzer getIZHAnalyzer(){
        return new ICTCLASAnalyzer();
    }
    
    @Bean
    public ISimilarityCalculator getSimilarityCalculator(){
        return new SimilarityCalculatorImpl();
    }
    
    @Bean IDocProcessor getDocProcessor(){
        return new DocProcessor();
    }
    
	 @Bean
     public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
     }
    
}
