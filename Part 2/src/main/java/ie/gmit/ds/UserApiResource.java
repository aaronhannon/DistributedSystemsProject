package ie.gmit.ds;

import com.google.protobuf.ByteString;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

@Path("/")
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
public class UserApiResource {

    private HashMap<Integer, UserHashed> usersMap = new HashMap<>();
    private Client client;
    private Scanner console;
    private int port;

    public UserApiResource() throws UnsupportedEncodingException {

        console = new Scanner(System.in);
        System.out.print("Please enter server port number: ");
        port = console.nextInt();

        client = new Client("localhost",port);
        client.hashMethod(22,"lala");
        String hash = new String(client.getExpectedHash().toByteArray(), "ISO-8859-1");
        String salt = new String(client.getSalt().toByteArray(), "ISO-8859-1");
        UserHashed testUser = new UserHashed(22, "aaron", "aaronchannon1@gmail.com", hash,salt);
        usersMap.put(testUser.getUserId(), testUser);


    }

    //Gets all users
    @GET
    @Path("users")
    public Collection<UserHashed> getUsers() {

        return usersMap.values();
    }

    //Gets one user from an id
    @GET
    @Path("users/{id}")
    public UserHashed getUser(@PathParam("id") int id) {

        return usersMap.get(id);
    }

    //Updates a user
    @PUT
    @Path("update")
    public Response updateUser(User newUser) throws UnsupportedEncodingException {

        if(usersMap.containsKey(newUser.getUserId())){
            usersMap.remove(newUser.getUserId());

            client.hashMethod(newUser.getUserId(),newUser.getPassword());

            String hash = new String(client.getExpectedHash().toByteArray(), "ISO-8859-1");
            String salt = new String(client.getSalt().toByteArray(), "ISO-8859-1");

            UserHashed hashedUser = new UserHashed(newUser.getUserId(),newUser.getUsername(),newUser.getEmail(),hash,salt);
            usersMap.put(hashedUser.getUserId(), hashedUser);

            String result = "User Updated : " + newUser.getUserId();
            return Response.status(201).entity(result).build();
        }else{
            String result = "User Does not exist : " + newUser.getUserId();
            return Response.status(404).entity(result).build();
        }

    }

    //Deletes a user via an ID
    @DELETE
    @Path("delete/{id}")
    public Response deleteOrderById(@PathParam("id") int id) {
        if(usersMap.containsKey(id)){
            usersMap.remove(id);
            String result = "User Removed : " + id;
            return Response.status(200).entity(result).build();
        }else{
            String result = "User Does not exist : " + id;
            return Response.status(404).entity(result).build();
        }
    }

    //Adds a user
    @POST
    @Path("add")
    public Response createUser(User newUser) throws UnsupportedEncodingException {

        client.hashMethod(newUser.getUserId(),newUser.getPassword());
        System.out.println("SALT RECEIVED" + client.getSalt().toByteArray());
        System.out.println("HASH RECEIVED" + client.getExpectedHash().toByteArray());

        String hash = new String(client.getExpectedHash().toByteArray(), "ISO-8859-1");
        String salt = new String(client.getSalt().toByteArray(), "ISO-8859-1");

        UserHashed hashedUser = new UserHashed(newUser.getUserId(),newUser.getUsername(),newUser.getEmail(),hash,salt);
        usersMap.put(hashedUser.getUserId(), hashedUser);

        String result = "User added : " + newUser.getUserId();
        return Response.status(201).entity(result).build();
    }

    //Validates a users login
    @POST
    @Path("login")
    public Response login(UserValidate userLogin) throws UnsupportedEncodingException {

        if(usersMap.containsKey(userLogin.getId())){
            UserHashed user = usersMap.get(userLogin.getId());

            byte[] hashedArray = user.getHashedPassword().getBytes("ISO-8859-1");
            byte[] salt = user.getSalt().getBytes("ISO-8859-1");

            ByteString userHashedP = ByteString.copyFrom(hashedArray);
            ByteString userSalt = ByteString.copyFrom(salt);



            System.out.println("HASHEDARRAY: " + hashedArray);
            System.out.println("SALT: " + salt);

            boolean login = client.validateMethod(userHashedP,userSalt,userLogin.getPassword());

            String result = "Login : " + login;
            return Response.status(201).entity(result).build();
        }else {
            String result = "User does not exit : " + userLogin.getId();
            return Response.status(404).entity(result).build();
        }

    }

}
