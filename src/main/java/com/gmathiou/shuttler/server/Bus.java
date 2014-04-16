/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmathiou.shuttler.server;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author mathioud
 */
public class Bus {

    private Line _line;
    private double _lat;
    private double _lon;
    private ArrayList<String> passengers;
    private int _lastSeenStopID;
    private Date _startTime;

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

    public Bus(double lat, double lon, Line line, Date startTime) {
        this._lat = lat;
        this._lon = lon;
        this._line = line;
        this._startTime = startTime;
        passengers = new ArrayList<String>();
    }

//    public void hopOn(double lat, double lon, String email) {
//        this._lat = lat;
//        this._lon = lon;
//        passengers = new ArrayList<String>();
//        if (!passengers.contains(email)) {
//            passengers.add(email);
//        }
//    }

    public void updateLocation(double lat, double lon, int lastSeenStopID) {
        setLat(lat);
        setLon(lon);
        setLastSeenStopID(lastSeenStopID);
    }

    /**
     * @return the _lastSeenStopID
     */
    public int getLastSeenStopID() {
        return _lastSeenStopID;
    }

    /**
     * @param _lastSeenStopID the _lastSeenStopID to set
     */
    public void setLastSeenStopID(int _lastSeenStopID) {
        this._lastSeenStopID = _lastSeenStopID;
    }

    /**
     * @return the _startTime
     */
    public Date getStartTime() {
        return _startTime;
    }

    /**
     * @param _startTime the _startTime to set
     */
    public void setStartTime(Date _startTime) {
        this._startTime = _startTime;
    }
}
