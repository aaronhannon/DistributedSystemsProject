package ie.gmit.ds;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserApiResource {

    private HashMap<Integer, User> usersMap = new HashMap<>();

    public UserApiResource() {
        User testUser = new User(1222, "aaron", "aaronchannon1@gmail.com", "lalala");
        usersMap.put(testUser.getUserId(), testUser);
    }


    @GET
    @Path("get")
    public Collection<User> getArtists() {

        return usersMap.values();
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(User newUser) {

        //System.out.println("NEW" + newUser.getUserId());
        usersMap.put(newUser.getUserId(), newUser);

        String result = "Track saved : " + newUser.getUserId();
        return Response.status(201).entity(result).build();
    }
}
