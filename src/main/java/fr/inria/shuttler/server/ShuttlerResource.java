package fr.inria.shuttler.server;

import java.util.ArrayList;
import java.util.List;
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
public class ShuttlerResource {

    /**
     * Database communication methods
     */
    protected List _listeners = new ArrayList();

    protected synchronized void addEventListener(DBUpdateEventListener listener) {
        _listeners.add(listener);
    }

    protected synchronized void removeEventListener(DBUpdateEventListener listener) {
        _listeners.remove(listener);
    }

    protected synchronized void updateRouteSessionViews() {
        for (Object listener : _listeners) {
            ((DBUpdateEventListener) listener).updateRouteSessionViews();
        }
    }

    protected synchronized void getUserStats() {
        for (Object listener : _listeners) {
            ((DBUpdateEventListener) listener).getUserStats();
        }
    }

    protected synchronized void updateUserRankings() {
        for (Object listener : _listeners) {
            ((DBUpdateEventListener) listener).updateUserRankings();
        }
    }

    /**
     * Use to initialize the database handler
     */
    private void checkDB() {
        if (DataHandler.getDbHandler() == null) {
            DataHandler.setDbHandler(new DBHandler());
        }
        if (!_listeners.contains(DataHandler.getDbHandler())) {
            this.addEventListener(DataHandler.getDbHandler());
        }
    }

    /**
     *
     * @return
     */
    @POST
    @Path("userHopOn")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userHopOn(String msg) {
        checkDB();
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        double lat = Double.valueOf(objMsg.get("lat").toString());
        double lon = Double.valueOf(objMsg.get("lon").toString());
        Bus newBus = new Bus(lat, lon, email);

        //Check if there are any buses registered
        if (DataHandler.getBuses().size() < 1) {
            DataHandler.getBuses().add(newBus);
            DataHandler.getPassengerToBusMap().put(email, newBus);
        } else {
            //Find if any bus near ==> Already registered bus
            for (Bus bus : DataHandler.getBuses()) {
                if (LocationHelpers.distFrom(lat, lon, bus.getLat(), bus.getLon()) < 100) {
                    //Bus already registered. Just add a passenger
                    bus.getPassengers().add(email);
                    DataHandler.getPassengerToBusMap().put(email, bus);
                } else {
                    //Bus is not registered. Add it
                    DataHandler.getBuses().add(newBus);
                    DataHandler.getPassengerToBusMap().put(email, newBus);
                }
            }
        }
        return Response.ok().build();
    }

    @GET
    @Path("getStops")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStations(String msg) {
        checkDB();
        String reply = DataHandler.getStopsJSON().toJSONString();
        return Response.ok().entity(reply).build();
    }

    @POST
    @Path("updateLocation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLocation(String msg) {
        checkDB();
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        double lat = Double.valueOf(objMsg.get("lat").toString());
        double lon = Double.valueOf(objMsg.get("lon").toString());
        if (DataHandler.getPassengerToBusMap().containsKey(email)) {
            DataHandler.getPassengerToBusMap().get(email).updateLocation(lat, lon);
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
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
