package fudan.mmdb.mds.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * This is the spring container on web layer.
 * An alternative of <b>dispatcher-context.xml</b>  file.</br>
 * this spring container can get the beans from it's parent container, namely {@link RootAppConfig}
 * 
 * @author Joshua Jiang
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages={
        "fudan.mmdb.mds.web.controllers",
        "fudan.mmdb.mds.web.services"
})
public class MvcDispatcherServletConfig extends WebMvcConfigurerAdapter {
    
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/*.html").addResourceLocations("/");
        registry.addResourceHandler("/**").addResourceLocations("/resources/");
    }
    
    
}
