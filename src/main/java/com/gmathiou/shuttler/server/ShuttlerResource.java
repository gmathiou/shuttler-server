package com.gmathiou.shuttler.server;

import static com.gmathiou.shuttler.server.DataHandler.getPassengerViewsMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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

    @POST
    @Path("hopon")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userHopOn(String msg) {
        dataInit();
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        String password = objMsg.get("password").toString();
        double lat = Double.valueOf(objMsg.get("latitude").toString());
        double lon = Double.valueOf(objMsg.get("longitude").toString());
        int line = Integer.valueOf(objMsg.get("lineid").toString());

        //Check if user is far away from Paris
        double ParisCenterLat = 48.856614;
        double ParisCenterLon = 2.352222;
        double distanceFromParis = LocationHelpers.distFrom(lat, lon, ParisCenterLat, ParisCenterLon);
        if (distanceFromParis > 100000) {
            return Response.serverError().build();
        }

        //Check if user is in the registered users list in DB
        if (DataHandler.getDbHandler().authenticateUser(email, password) == false) {
            return Response.serverError().build();
        }

        if (DataHandler.getPassengerToBusMap().containsKey(email)) {
            //User is already onboard a bus
            return Response.serverError().build();
        }

        boolean busFoundFlag = false;

        //Some buses exist. Check if the current passenger is onboard a bus that is alredy there
        for (Bus bus : DataHandler.getBuses()) {
            if (LocationHelpers.distFrom(lat, lon, bus.getLat(), bus.getLon()) < 40) {
                //Bus already registered. Just add a passenger
                bus.getPassengers().add(email);
                DataHandler.getPassengerToBusMap().put(email, bus);
                if(!getPassengerViewsMap().containsKey(email)){
                    getPassengerViewsMap().put(email, new HashSet<String>());
                }
                busFoundFlag = true;
                break;
            }
        }

        Line newLine = null;

        for (Line registeredLine : DataHandler.getLines()) {
            if (registeredLine.getID() == line) {
                newLine = registeredLine;
            }
        }

        if (newLine == null) {
            return Response.serverError().build();
        }

        //Check if there are any buses registered
        if (DataHandler.getBuses().size() < 1 || busFoundFlag == false) {
            Date now = new Date();
            Bus newBus = new Bus(lat, lon, newLine, now);
            DataHandler.getBuses().add(newBus);
            newBus.getPassengers().add(email);
            DataHandler.getPassengerToBusMap().put(email, newBus);
            if(!getPassengerViewsMap().containsKey(email)){
                getPassengerViewsMap().put(email, new HashSet<String>());
            }
        }
        return Response.ok().build();
    }

    @GET
    @Path("stops")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStations() {
        dataInit();
        String reply = DataHandler.getStopsJSON().toJSONString();
        return Response.ok().entity(reply).build();
    }

    @GET
    @Path("lines")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLines() {
        dataInit();
        String reply = DataHandler.getLinesJSON().toJSONString();
        return Response.ok().entity(reply).build();
    }

    @POST
    @Path("updatelocation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLocation(String msg) {
        dataInit();
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        double lat = Double.valueOf(objMsg.get("latitude").toString());
        double lon = Double.valueOf(objMsg.get("longitude").toString());
        int lastSeenStopID = Integer.valueOf(objMsg.get("lastseenstopid").toString());
        if (DataHandler.getPassengerToBusMap().containsKey(email)) {
            DataHandler.getPassengerToBusMap().get(email).updateLocation(lat, lon, lastSeenStopID);
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
    }

    @GET
    @Path("profile/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfileInfo(@PathParam("email") String email) {
        dataInit();
        JSONObject profile = DataHandler.getDbHandler().getUserStats(email);
        String reply = profile.toJSONString();
        if (reply != null) {
            return Response.ok().entity(reply).build();
        } else {
            return Response.serverError().build();
        }
    }

    @POST
    @Path("authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(String msg) {
        dataInit();
        if (msg == null) {
            return Response.serverError().build();
        }
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        String password = objMsg.get("password").toString();
        if (email == null || password == null) {
            return Response.serverError().build();
        }
        Boolean reply;
        reply = DataHandler.getDbHandler().authenticateUser(email, password);
        if (reply == true) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }

    @POST
    @Path("registration")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registration(String msg) {
        dataInit();
        if (msg == null) {
            return Response.serverError().build();
        }
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        String password = objMsg.get("password").toString();
        if (email == null || password == null) {
            return Response.serverError().build();
        }
        Boolean reply = DataHandler.getDbHandler().registerUser(email, password);
        if (reply == true) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("busesforline/{email}/{lineid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBusesForLine(@PathParam("email") String email, @PathParam("lineid") String lineString) {
        dataInit();
        int line;

        JSONArray buses = new JSONArray();
        try {
            line = Integer.valueOf(lineString);
            if (DataHandler.getBuses().size() > 0) {
                for (Bus b : DataHandler.getBuses()) {
                    if (b.getLine().getID() == line) {

                        //Calculate the interval between the start of the bus and now
                        Date now = new Date();
                        double intervalMil = now.getTime() - b.getStartTime().getTime();
                        double hours = intervalMil / (double) 3600000;

                        //Check for inactive buses
                        if (hours > 3) {
                            DataHandler.getBuses().remove(b);
                            continue;
                        }

                        // Update the views Map
                        for (String passenger : b.getPassengers()) {
                            DataHandler.updateViews(passenger, email);
                        }

                        //Start creating the response
                        JSONObject busObject = new JSONObject();
                        busObject.put("lineid", b.getLine().getID());
                        busObject.put("latitude", b.getLat());
                        busObject.put("longitude", b.getLon());
                        busObject.put("lastseenstopid", b.getLastSeenStopID());
                        buses.add(busObject);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println("Exception: " + ex.getMessage());
            return Response.serverError().build();
        }
        JSONObject reply = new JSONObject();
        reply.put("buses", buses);

        return Response.ok().entity(reply.toJSONString()).build();
    }

    @POST
    @Path("hopoff")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userHopOff(String msg) {
        dataInit();
        JSONObject objMsg = (JSONObject) JSONValue.parse(msg);
        String email = objMsg.get("email").toString();
        String password = objMsg.get("password").toString();
        double distanceTravelled = Double.valueOf(objMsg.get("kilometers").toString());

        //Check if user is in the registered users list in DB
        if (DataHandler.getDbHandler().authenticateUser(email, password) == false) {
            return Response.serverError().build();
        }

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
