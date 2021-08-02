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
    public int buggyEndpoint(PrivatePayload params) {
        System.out.println("key from payload: " + params.key);
        return params.key;
    }

    private static class PrivatePayload {
        int key;
    }


    @POST
    @Path("/good-endpoint")
    public int goodEndpoint(NonPrivatePayload params) {
        System.out.println("key from payload: " + params.key);
        return params.key;
    }

    static class NonPrivatePayload {
        int key;
    }
}