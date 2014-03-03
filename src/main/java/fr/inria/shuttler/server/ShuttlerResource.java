package fr.inria.shuttler.server;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

@Path("/")
public class ShuttlerResource {

    /**
     * Database communication methods
     */
    protected List _DB_EventListeners = new ArrayList();

    protected synchronized void addEventListener(DBUpdateEventListener listener) {
        _DB_EventListeners.add(listener);
    }

    protected synchronized void removeEventListener(DBUpdateEventListener listener) {
        _DB_EventListeners.remove(listener);
    }

    protected synchronized void updateRouteSessionViews(String email, int views, double kilometers) {
        for (Object listener : _DB_EventListeners) {
            ((DBUpdateEventListener) listener).updateRouteSessionViews(email, views, kilometers);
        }
    }

    protected synchronized void newUserRegistration(String email) {
        for (Object listener : _DB_EventListeners) {
            ((DBUpdateEventListener) listener).newUserRegistration(email);
        }
    }

    /**
     * Used to initialize the database handler and the event listeners
     */
    private void dataInit() {
        if (DataHandler.getDbHandler() == null) {
            DataHandler.setDbHandler(new DBHandler());
        }
        if (!_DB_EventListeners.contains(DataHandler.getDbHandler())) {
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
        dataInit();
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        double lat = Double.valueOf(objMsg.get("lat").toString());
        double lon = Double.valueOf(objMsg.get("lon").toString());
        int line = Integer.valueOf(objMsg.get("line").toString());

        if (DataHandler.getPassengerToBusMap().containsKey(email)) {
            //User is already onboard a bus
            return Response.serverError().build();
        }

        //Check if user exists in the profile table in DB
        this.newUserRegistration(email);

        boolean busFoundFlag = false;

        //Some buses exist. Check if the current passenger is onboard a bus that is alredy there
        for (int i = 0; i < DataHandler.getBuses().size(); i++) {
            Bus bus = DataHandler.getBuses().get(i);
            if (LocationHelpers.distFrom(lat, lon, bus.getLat(), bus.getLon()) < 0.2) {
                //Bus already registered. Just add a passenger
                bus.getPassengers().add(email);
                DataHandler.getPassengerToBusMap().put(email, DataHandler.getBuses().get(i));
                busFoundFlag = true;
                break;
            }
        }

        //Check if there are any buses registered
        if (DataHandler.getBuses().size() < 1 || busFoundFlag == false) {
            Bus newBus = new Bus(lat, lon, line);
            DataHandler.getBuses().add(newBus);
            newBus.getPassengers().add(email);
            DataHandler.getPassengerToBusMap().put(email, newBus);
        }
        return Response.ok().build();
    }

    @GET
    @Path("getStops")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStations() {
        dataInit();
        String reply = DataHandler.getStopsJSON().toJSONString();
        return Response.ok().entity(reply).build();
    }

    @POST
    @Path("updateLocation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLocation(String msg) {
        dataInit();
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        double lat = Double.valueOf(objMsg.get("lat").toString());
        double lon = Double.valueOf(objMsg.get("lon").toString());
        int lastSeenStopID = Integer.valueOf(objMsg.get("lastSeenStopID").toString());
        if (DataHandler.getPassengerToBusMap().containsKey(email)) {
            DataHandler.getPassengerToBusMap().get(email).updateLocation(lat, lon, lastSeenStopID);
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
    }

    @GET
    @Path("getProfile/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfileInfo(@PathParam("email") String email) {
        dataInit();
        JSONObject profile = DataHandler.getDbHandler().getUserStats(email);
        String reply = profile.toJSONString();
        return Response.ok().entity(reply).build();
    }

    @GET
    @Path("getBusesForLine/{email}/{line}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBusesForLine(@PathParam("email") String email, @PathParam("line") String lineString) {
        dataInit();
        int line;

        JSONArray buses = new JSONArray();
        try {
            line = Integer.valueOf(lineString);
            if (DataHandler.getBuses().size() < 1) {
                return Response.ok().entity("{}").build();
            }

            for (Bus b : DataHandler.getBuses()) {
                if (b.getLine().getID() == line) {

                    // Update the views Map
                    for (String passenger : b.getPassengers()) {
                        DataHandler.updateViews(passenger, email);
                    }

                    //Start creating the response
                    JSONObject busObject = new JSONObject();
                    busObject.put("latitude", b.getLat());
                    busObject.put("longitude", b.getLon());
                    busObject.put("lineid", b.getLine().getID());
                    busObject.put("linename", b.getLine().getName());
                    busObject.put("lastSeenStopID", b.getLastSeenStopID());
                    buses.add(busObject);
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return Response.serverError().build();
        }
        JSONObject reply = new JSONObject();
        reply.put("buses", buses);

        return Response.ok().entity(reply.toJSONString()).build();
    }

    @POST
    @Path("userHopOff")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userHopOff(String msg) {
        dataInit();
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        double distanceTravelled = Double.valueOf(objMsg.get("kilometers").toString());

        if (DataHandler.getPassengerToBusMap().containsKey(email)) {
            Bus bus = DataHandler.getPassengerToBusMap().get(email);

            //Remove current passenger from the views map
            if (DataHandler.getPassengerViewsMap().containsKey(email)) {
                int totalViews = DataHandler.getPassengerViewsMap().get(email).size();
                updateRouteSessionViews(email, totalViews, distanceTravelled);
                DataHandler.getPassengerViewsMap().remove(email);
            }

            if (bus.getPassengers().contains(email)) {
                if (bus.getPassengers().size() > 1) {
                    //There are more passengers onboard
                    bus.getPassengers().remove(email);
                } else {
                    //This passenger was the only one in the bus
                    DataHandler.getBuses().remove(bus);
                }
            }
            DataHandler.getPassengerToBusMap().remove(email);
        }
        return Response.ok().build();
    }
}
