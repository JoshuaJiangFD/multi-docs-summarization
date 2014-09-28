package fudan.mmdb.mds.web.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * The java based way to register the root web application context 
 * and dispatcherServlet to the web container(tomcat, or jetty.etc.)
 * An alternative of <b>web.xml.</b>
 * 
 * @author Joshua Jiang
 *
 */
public class WebInit implements WebApplicationInitializer {

    
    public void onStartup(ServletContext servletContext) throws ServletException {
        //1. create the root application context
        AnnotationConfigWebApplicationContext rootContext=new AnnotationConfigWebApplicationContext();
        //registers the application configuration with the root context
        rootContext.register(RootAppConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));
        
        /*
         *2. create the dispatcher servlet context, using "AnnotationConfigWebApplicationContext"
         *    an alternative of "XmlWebApplicationContext". 
        */
        AnnotationConfigWebApplicationContext sevletContext=new AnnotationConfigWebApplicationContext();
        //registers the servlet configuration with the dispatcher servlet context
        sevletContext.register(MvcDispatcherServletConfig.class);
        //further configures the servlet context
        ServletRegistration.Dynamic registration=servletContext.addServlet("dispatcher", new DispatcherServlet(sevletContext));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
        
    }

}
