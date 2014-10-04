package fudan.mmdb.mds.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
@Import({HttpSolrConfig.class,MongoDbConfig.class})
public class RootAppConfig {

}
