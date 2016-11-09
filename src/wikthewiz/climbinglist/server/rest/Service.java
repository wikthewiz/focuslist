package wikthewiz.climbinglist.server.rest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import wikthewiz.climbinglist.server.rest.dao.ClimbingList;
import wikthewiz.climbinglist.server.rest.dao.Person;

@Path("/")
public class Service {

    private static final Logger logger = LogManager.getLogger(Service.class.getName());
    private final Gson g = new GsonBuilder().create();
    private final String storagePath;
    
    @Inject
    public Service(Properties properties) {
        this.storagePath = properties.getProperty("storagePath");
    }
    
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(Person person, @Context HttpServletRequest httpRequest) throws Exception {
        Principal user = httpRequest.getUserPrincipal();
        logger.debug("Start saving " + user.getName());
        String fileName = user.getName() + ".json";
        try {
            File f = new File(getStoragePath() + fileName);
            ArrayList<String> list = new ArrayList<String>();
            String jsonPerson = g.toJson(person);
            logger.debug(user.getName() + " wants to save: " + jsonPerson);
            list.add(jsonPerson);
            Files.write(f.toPath(), list, Charset.forName("UTF-8"), (OpenOption) StandardOpenOption.TRUNCATE_EXISTING,
                    (OpenOption) StandardOpenOption.CREATE, (OpenOption) StandardOpenOption.WRITE);
            logger.debug("Done saving " + user.getName());
        } catch (IOException e) {
            logger.error("Failed to save: " + user.getName(), e);
            throw new Exception("Failed to save your list");
        }
    }
    

    @POST
    @Path("logout")
    @Produces(MediaType.APPLICATION_JSON)
    public void logout(@Context HttpServletRequest httpRequest){
         logger.debug("logout called ");
         httpRequest.getSession().invalidate();
    }
    
    @POST
    @Path("authenticated")
    @Produces(MediaType.APPLICATION_JSON)
    public void isAuthenticated(@Context HttpServletRequest httpRequest, @Context HttpServletResponse response) throws ServletException, IOException {
        logger.debug("authenticated called");
        RequestDispatcher view = httpRequest.getRequestDispatcher("edit.html");
        view.forward(httpRequest, response);
    }

    @GET
    @Path("load")
    @Produces(MediaType.APPLICATION_JSON)
    public String load() throws Exception {
        logger.debug("load called ");
        try {
            Person martin = loadPerson("martin.json");
            Person daniel = loadPerson("daniel.json");
            Person patrik = loadPerson("patrik.json");
            return g.toJson(new ClimbingList(martin, daniel, patrik));
        } catch (IOException e) {
            logger.error("When loading: ", e);
            throw new Exception("Failed to load list");
        }
    }
        
    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser(@Context HttpServletRequest httpRequest) throws Exception {
        Principal user = httpRequest.getUserPrincipal();
        logger.debug("user called");
        if (user == null){
            return g.toJson("");
        }
        else {
            return g.toJson(user.getName());
        }
    }

    private Person loadPerson(String name) throws IOException {
        File f = new File(getStoragePath() + name);
        if (!f.exists()) {
            f.createNewFile();
            Person p = new Person();
            ArrayList<String> list = new ArrayList<String>();
            list.add(g.toJson(p));
            Files.write(
                    f.toPath(), 
                    list, 
                    Charset.forName("UTF-8"), 
                    (OpenOption)StandardOpenOption.TRUNCATE_EXISTING,
                    (OpenOption)StandardOpenOption.CREATE_NEW,
                    (OpenOption)StandardOpenOption.WRITE);
        }

        StringBuilder builder = new StringBuilder();
        Files.lines(f.toPath(), Charset.forName("UTF-8") ).forEach(l -> {
            builder.append(l);
        });

        return g.fromJson(builder.toString(), Person.class);
    }

    private String getStoragePath() {
        return storagePath;
    }
}
