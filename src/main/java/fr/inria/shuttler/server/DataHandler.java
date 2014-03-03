package fr.inria.shuttler.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataHandler {

    private static DBHandler _dbHandler = null;
    private static ArrayList<Stop> _stops;
    private static ArrayList<Line> _lines;
    private static ArrayList<Bus> _buses;
    private static HashMap<String, Bus> _passengerToBusMap;
    private static HashMap<String, Set<String>> _passengerViewsMap;

    public static void initDataHandler() {
        //Check if something is initialized. If e.g. stops is initialized everything will be
        if (_stops != null) {
            return;
        }
        _stops = new ArrayList<Stop>();
        _lines = new ArrayList<Line>();
        _buses = new ArrayList<Bus>();
        _passengerToBusMap = new HashMap<String, Bus>();
        setPassengerViewsMap(new HashMap<String, Set<String>>());
    }

    /**
     * @return the _stops
     */
    public static ArrayList<Stop> getStops() {
        initDataHandler();
        return _stops;
    }

    /**
     * @return the _stops
     */
    public static JSONObject getStopsJSON() {
        initDataHandler();
        JSONArray stopsJSON = new JSONArray();
        for (Stop stop : _stops) {
            JSONObject stopJSONObject = new JSONObject();
            stopJSONObject.put("id", stop.getID());
            stopJSONObject.put("name", stop.getName());
            stopJSONObject.put("shortname", stop.getShortName());
            stopJSONObject.put("latitude", stop.getLat());
            stopJSONObject.put("longitude", stop.getLon());
            stopsJSON.add(stopJSONObject);
        }
        JSONObject reply = new JSONObject();
        reply.put("stops", stopsJSON);
        return reply;
    }

    /**
     * @param aStops the _stops to set
     */
    public static void setStops(ArrayList<Stop> aStops) {
        initDataHandler();
        _stops = aStops;
    }

    /**
     * @return the _lines
     */
    public static ArrayList<Line> getLines() {
        initDataHandler();
        return _lines;
    }

    /**
     * @param aLines the _lines to set
     */
    public static void setLines(ArrayList<Line> aLines) {
        initDataHandler();
        _lines = aLines;
    }

    /**
     * @return the _buses
     */
    public static ArrayList<Bus> getBuses() {
        return _buses;
    }

    /**
     * @param aBuses the _buses to set
     */
    public static void setBuses(ArrayList<Bus> aBuses) {
        initDataHandler();
        _buses = aBuses;
    }

    /**
     * @return the _passengerToBusMap
     */
    public static HashMap<String, Bus> getPassengerToBusMap() {
        return _passengerToBusMap;
    }

    /**
     * @param aPassengerToBusMap the _passengerToBusMap to set
     */
    public static void setPassengerToBusMap(HashMap<String, Bus> aPassengerToBusMap) {
        initDataHandler();
        _passengerToBusMap = aPassengerToBusMap;
    }

    /**
     * @return the _dbHandler
     */
    public static DBHandler getDbHandler() {
        return _dbHandler;
    }

    /**
     * @param dbHandler the _dbHandler to set
     */
    public static void setDbHandler(DBHandler dbHandler) {
        _dbHandler = dbHandler;
    }

    /**
     *
     * @param passengerEmail The email of the passenger to increase views
     * @param requestersEmail The email of the requester (used to be sure we
     * count each person once)
     */
    public static void updateViews(String passengerEmail, String requestersEmail) {
        if (getPassengerViewsMap().containsKey(passengerEmail)) {
            getPassengerViewsMap().get(passengerEmail).add(requestersEmail);
        } else {
            Set<String> viewerEmails = new HashSet<String>();
            viewerEmails.add(requestersEmail);
            getPassengerViewsMap().put(passengerEmail, viewerEmails);
        }
    }

    /**
     * @return the _passengerViewsMap
     */
    public static HashMap<String, Set<String>> getPassengerViewsMap() {
        return _passengerViewsMap;
    }

    /**
     * @param aPassengerViewsMap the _passengerViewsMap to set
     */
    public static void setPassengerViewsMap(HashMap<String, Set<String>> aPassengerViewsMap) {
        _passengerViewsMap = aPassengerViewsMap;
    }
}
