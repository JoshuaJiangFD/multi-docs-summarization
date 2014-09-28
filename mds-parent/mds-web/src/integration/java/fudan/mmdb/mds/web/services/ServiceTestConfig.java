package fudan.mmdb.mds.web.services;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fudan.mmdb.mds.web.repository.mongo.TestConfig;


@Import(TestConfig.class)
@ComponentScan(basePackages="fudan.mmdb.mds.web.services")
@Configuration
public class ServiceTestConfig {

   
}
