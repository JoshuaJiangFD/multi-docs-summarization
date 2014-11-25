package fudan.mmdb.mds.analyzer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Configuration
@PropertySource("classpath:core.properties")
public class AnalyzerTestConfig {

	@Bean
	public ICTCLASAnalyzer getAnalyzer(){
		return new ICTCLASAnalyzer();
	}
	
	/**
	 * the Java @PropertySource annotation does not automatically register a PropertySourcesPlaceholderConfigurer with Spring. 
	 * Instead, the bean must be explicitly defined in the configuration 
	 * to get the property resolution mechanism working.</br>
	 * @see <a href="http://www.baeldung.com/2012/02/06/properties-with-spring/#java">http://www.baeldung.com/2012/02/06/properties-with-spring/#java</a>
	 * 
	 * @return
	 */
	 @Bean
     public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
     }
}
