package spilserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import services.CORSResponseFilter;

import java.io.IOException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class StartSpilServer {

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://130.225.170.204:5107/galgespil/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("spilserver", "com.fasterxml.jackson.jaxrs.base");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        rc.register(new CORSResponseFilter());

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] arg) throws IOException {
        //RMI
        System.setProperty("java.rmi.server.hostname", "130.225.170.204");
        LocateRegistry.createRegistry(4107);
        SpilServerImpl serv = new SpilServerImpl();
        Naming.rebind("rmi://localhost:4107/galgespillet", serv);
        System.out.println("SpilServer oprettet");

        //Rest
        final HttpServer server = startServer();
        server.start();

        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl", BASE_URI));


    }


}
