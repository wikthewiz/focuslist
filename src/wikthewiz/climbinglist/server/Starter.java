package wikthewiz.climbinglist.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.glassfish.jersey.servlet.ServletContainer;

public class Starter {

    private static final Logger logger = LogManager.getLogger(Starter.class);
    
    private static final Properties Properties = new XProperties();

    public static void main(String[] args) {
        try {
            logger.info("Server_code started");
            logger.info("Current path: " + new File("./").getAbsolutePath());

            loadProperties();
            int port = Integer.parseInt(Properties.getProperty("port", "8080"));
            Server server = new Server(port);
            logger.debug("listening to: " + port);
            ServletContextHandler context = new ServletContextHandler(server, "/",
                    ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);

            logger.debug("created ServletContextHandler for /");
            addRootResource(context);
            logger.debug("Addint root resource");

            Constraint constraint = new Constraint();
            constraint.setName(Constraint.__FORM_AUTH);
            constraint.setRoles(new String[] { "user" });
            constraint.setAuthenticate(true);

            ConstraintMapping constraintMapping = new ConstraintMapping();
            constraintMapping.setConstraint(constraint);
            constraintMapping.setPathSpec("/edit");
            constraintMapping.setPathSpec("/edit/*");
            constraintMapping.setPathSpec("/edit.html");
            constraintMapping.setPathSpec("*edit.html*");
            constraintMapping.setPathSpec("/service/save");
            constraintMapping.setPathSpec("/service/authenticated");
            constraintMapping.setPathSpec("/dragula/dragula.min.css");
            constraintMapping.setPathSpec("/dragula/dragula.min.js");

            ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
            securityHandler.addConstraintMapping(constraintMapping);
            
            asserFileExists(Properties.getProperty("passwordfile"));
            LoginService loginService = new HashLoginService("server_code", Properties.getProperty("passwordfile"));
            securityHandler.setLoginService(loginService);
            addServiceServlet(context, loginService);

            FormAuthenticator authenticator = new FormAuthenticator("/login.html", "/login.html", false);
            authenticator.setAlwaysSaveUri(false);
            securityHandler.setAuthenticator(authenticator);

            context.setSecurityHandler(securityHandler);
            server.start();
            server.join();
        } catch (Throwable t) {
            StringWriter stacktrace = new StringWriter();
            t.printStackTrace(new PrintWriter(stacktrace));
            System.err.println("ERROR:" + stacktrace.toString());
            logger.error("Crash during start up", t);
        }
    }
    
    private static void loadProperties() throws IOException{
        InputStream resourceStream = Starter.class.getClass().getResourceAsStream("/server_code.properties");
        
        if(resourceStream == null){
            throw new FileNotFoundException("server_code.properties not found");
        }
        Properties.load(resourceStream);
    }

    private static void addServiceServlet(ServletContextHandler serviceContext, LoginService loginService) {
        ServletHolder ss = new ServletHolder(new ServletContainer(new Config(loginService, Properties)));
        ss.setInitOrder(0);
        ss.setInitParameter("jersey.config.server.provider.packages", "wikthewiz.climbinglist.server.rest");
        serviceContext.addServlet(ss, "/service/*");
    }

    private static void addRootResource(ServletContextHandler context) throws FileNotFoundException {
        asserFileExists(Properties.getProperty("resourceBasePath"));
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[] { "index.html" });
        resourceHandler.setResourceBase(Properties.getProperty("resourceBasePath"));
        resourceHandler.setCacheControl("Cache-Control: max-age=31536000");
        context.insertHandler(resourceHandler);
    }

    private static void asserFileExists(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        if (!f.exists()) {
            StringBuilder context = new StringBuilder();
            context.append("Current path: " + new File("./").getAbsolutePath() + "\n");
            throw new FileNotFoundException("The file file can not be found:" + fileName + context.toString());
        }
    }
}
