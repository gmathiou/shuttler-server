/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmathiou.shuttler.server;

/**
 *
 * @author mathioud
 */
public class Stop {
    private int _ID;
    private String _shortName;
    private String _name;
    private double _lat;
    private double _lon;
    private int _line;

    public Stop(int _ID, String _shortName, String _name, double _lat, double _lon, int lineId) {
        this._ID = _ID;
        this._shortName = _shortName;
        this._name = _name;
        this._lat = _lat;
        this._lon = _lon;
        this._line = lineId;
    }

    /**
     * @return the _ID
     */
    public int getID() {
        return _ID;
    }

    /**
     * @param _ID the _ID to set
     */
    public void setID(int _ID) {
        this._ID = _ID;
    }

    /**
     * @return the _shortName
     */
    public String getShortName() {
        return _shortName;
    }

    /**
     * @param _shortName the _shortName to set
     */
    public void setShortName(String _shortName) {
        this._shortName = _shortName;
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
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param _name the _name to set
     */
    public void setName(String _name) {
        this._name = _name;
    }

    /**
     * @return the _line
     */
    public int getLine() {
        return _line;
    }

    /**
     * @param _lineId the _line to set
     */
    public void setLine(int _lineId) {
        this._line = _lineId;
    }
}
