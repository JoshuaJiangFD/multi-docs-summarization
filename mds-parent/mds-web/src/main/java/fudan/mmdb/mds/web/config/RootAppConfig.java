package fudan.mmdb.mds.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * This is the root applicationContext.</br>
 * mainly includes the configure of  the persistent layer and business layer.</br>
 * this spring context container will be loaded by 
 * the {@link org.springframework.web.context.ContextLoaderListener}</br> 
 * whilst the initialization of web container.
 * @author Joshua Jiang
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
@Import({HttpSolrConfig.class,MongoDbConfig.class})
@ImportResource("classpath:mdscore-context.xml")
public class RootAppConfig {

	
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
