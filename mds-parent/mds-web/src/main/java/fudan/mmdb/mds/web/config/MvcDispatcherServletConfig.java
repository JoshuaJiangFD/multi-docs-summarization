package fudan.mmdb.mds.web.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * This is the spring container on web layer. An alternative of
 * <b>dispatcher-context.xml</b> file.</br> this spring container can get the
 * beans from it's parent container, namely {@link RootAppConfig}
 * 
 * @author Joshua Jiang
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "fudan.mmdb.mds.web.controllers",
"fudan.mmdb.mds.web.controllers.rest",
		"fudan.mmdb.mds.web.services" })
public class MvcDispatcherServletConfig extends WebMvcConfigurerAdapter {

	/**
	 * static resource handlers,like css, js, etc.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/resources/");
	}

	/**
	 * customize the predefined content negotiation manager via its configurer
	 * note: favor doesn't change the order to check the content type, it will always be "path extension", "parameter","accept header"
	 */
	@Override
	public void configureContentNegotiation(
			ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false).favorParameter(true)
				.parameterName("mediaType").ignoreAcceptHeader(false)
				.useJaf(false).defaultContentType(MediaType.TEXT_HTML)
				.mediaType("xml", MediaType.APPLICATION_XML)
				.mediaType("json", MediaType.APPLICATION_JSON);
	}

	/**
	 * content negotiating view resolver
	 */
	@Bean
	public ContentNegotiatingViewResolver setUpContentNegotiatingViewResolver(
			ContentNegotiationManager configurer) {

		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setContentNegotiationManager(configurer);
		// set the default views
		List<View> defaultViews = new ArrayList<View>();
		defaultViews.add(new MappingJackson2JsonView());
		resolver.setDefaultViews(defaultViews);
		// set the order as the first view resolver to pick up
		resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return resolver;
	}

	/**
	 * internal resource view resolver, always the last on the view resolver
	 * chain.
	 * 
	 * @return
	 */
	@Bean
	public InternalResourceViewResolver setupViewResolver() {

		InternalResourceViewResolver resolver = new InternalResourceViewResolver();

		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");

		resolver.setOrder(Ordered.LOWEST_PRECEDENCE);
		return resolver;
	}
}
