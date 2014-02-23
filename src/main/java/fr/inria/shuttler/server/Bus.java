/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.inria.shuttler.server;

import java.util.ArrayList;

/**
 *
 * @author mathioud
 */
public class Bus {

    private Line _line;
    private double _lat;
    private double _lon;
    private ArrayList<String> passengers;

    /**
     * @return the _line
     */
    public Line getLine() {
        return _line;
    }

    /**
     * @param _line the _line to set
     */
    public void setLine(Line _line) {
        this._line = _line;
    }

    /**
     * @return the _lat
     */
    public double getLat() {
        return _lat;
    }

    /**
     * @param _lat the _lat to set
     */
    public void setLat(double _lat) {
        this._lat = _lat;
    }

    /**
     * @return the _lon
     */
    public double getLon() {
        return _lon;
    }

    /**
     * @param _lon the _lon to set
     */
    public void setLon(double _lon) {
        this._lon = _lon;
    }

    /**
     * @return the passengers
     */
    public ArrayList<String> getPassengers() {
        return passengers;
    }

    /**
     * @param passengers the passengers to set
     */
    public void setPassengers(ArrayList<String> passengers) {
        this.passengers = passengers;
    }

    public Bus(double lat, double lon, int line) {
        this._lat = lat;
        this._lon = lon;
        for (Line registeredLine : DataHandler.getLines()) {
            if (registeredLine.getID() == line) {
                this._line = registeredLine;
            }
        }
        passengers = new ArrayList<String>();
    }

    public void hopOn(double lat, double lon, String email) {
        this._lat = lat;
        this._lon = lon;
        passengers = new ArrayList<String>();
        if (!passengers.contains(email)) {
            passengers.add(email);
        }
    }

    public void updateLocation(double lat, double lon){
        setLat(lat);
        setLon(lon);
    }
}
