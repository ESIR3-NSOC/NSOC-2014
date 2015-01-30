package rest.dimmableSwitchResource;

import ContextUtility.ContextMethod;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;



/**
 * Created by mathi_000 on 21/01/2015.
 */

@Path("/model")
public class DimmableSwitchResource {

    private context.Context ctx;

    @Context
    UriInfo uriInfo;

    @Context
    Request request;


    @Path("/Ready")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String Ready(){
        System.out.println("plop ! ");
        return "Server tomcat ready !";
    }


    @Path("vote/{user}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String setVote(@PathParam("user") String userVote){
        System.out.println("Vote ! => " + userVote);
        return "User : " + userVote.split("=")[0] + " vote : " + userVote.split("=")[1];
    }

    @Path("/Start")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String start(){
        if(ctx!=null){
        ctx = ContextMethod.initContext("Salle 928");
        return "Context started ! \n"  +  ctx.getKnxManager().getGroups();
        }else{
            return "Context has already started !" ;
        }
    }

    @Path("/Stop")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String Stop(){
        if(ctx.isRunning()){
            ctx.stopContext();
            return "context stopped !";
        }else{
            return "context already stopped";
        }
    }
}
