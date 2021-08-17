package quarkus;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@RequestScoped
@Path("/hello")
@RolesAllowed("admin")
@Produces("application/json")
@Consumes("application/json")
public class HelloResource {

    @POST
    @Path("/buggy-endpoint")
    public String buggyEndpoint(PrivatePayload params) {
        System.out.println("buggy-endpoint: key from payload: " + params.key);
        return "Result: " + params.key;
    }

    private static class PrivatePayload {
        public int key;
    }


    @POST
    @Path("/good-endpoint")
    public String goodEndpoint(NonPrivatePayload params) {
        System.out.println("good-endpoint: key from payload: " + params.key);
        return "Result: " + params.key;
    }

    static class NonPrivatePayload {
        public String key;
    }
}