package fudan.mmdb.mds.web.services;


import javax.xml.bind.JAXBException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverter;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverterImpl;
import fudan.mmdb.mds.web.repository.mongo.TestConfig;


@Import(TestConfig.class)
@ComponentScan(basePackages="fudan.mmdb.mds.web.services")
@Configuration
public class ServiceTestConfig {

   @Bean
   public XmlDocConverter getConverter() throws JAXBException{
	   return new XmlDocConverterImpl();
   }
}
