package fr.inria.shuttler.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

@Path("/")
public class ShuttlerResource extends ServerResource {

    /**
     * Use to initialize the database handler
     */
    public void checkDB() {
        if (super._dbHandler == null) {
            super._dbHandler = new DBHandler(this);
        }
    }

    /**
     *
     * @return
     */
    @POST
    @Path("userHopOn")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userHopOn(String msg) {
        checkDB();
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        String lat = objMsg.get("lat").toString();
        String lon = objMsg.get("lon").toString();
        return Response.ok().entity(objMsg.toJSONString()).build();
    }

    @GET
    @Path("getStations")
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_PLAIN)
    public String getStations() {
        checkDB();

        return "Got it!";
    }

    @GET
    @Path("updateLocation")
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateLocation() {
        checkDB();

        return "Got it!";
    }

    @GET
    @Path("getProfileInfo")
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_PLAIN)
    public String getProfileInfo() {
        checkDB();

        return "Got it!";
    }

    @GET
    @Path("getBusesForLine")
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_PLAIN)
    public String getBusesForLine() {
        checkDB();

        return "Got it!";
    }

    @GET
    @Path("userHopOff")
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_PLAIN)
    public String userHopOff() {
        checkDB();

        return "Got it!";
    }
}
