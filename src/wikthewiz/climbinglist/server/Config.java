package wikthewiz.climbinglist.server;

import java.security.Provider.Service;
import java.util.Properties;

import org.eclipse.jetty.security.LoginService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wikthewiz.climbinglist.server.rest.GsonJerseyProvider;

public class Config extends ResourceConfig {
    public Config(LoginService loginService, Properties properties) 
    { 
        
       register( new AbstractBinder(){
            @Override
            protected void configure() {
               bind(loginService).to(LoginService.class);
            }});

       register(loginService);
        packages("wikthewiz.climbinglist.server.rest");
        register(new LoggingFilter(new JulFacade(), true));
        register(GsonJerseyProvider.class);
        
        register( new AbstractBinder(){
            @Override
            protected void configure() {
               bind(properties).to(Properties.class);
            }});
    }
    
    private static Logger LOG = LoggerFactory.getLogger(Service.class); //slf4j logger
    private static class JulFacade extends java.util.logging.Logger {
        JulFacade() { super("Jersey", null); }
        @Override public void info(String msg) { LOG.info(msg); }
      }
}
