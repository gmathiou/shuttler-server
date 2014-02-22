package fr.inria.shuttler.server;

import java.util.ArrayList;

/**
 *
 * @author mathioud
 */
public class Line {

    private int _ID;
    private String _name;
    private ArrayList<Integer> _stops;

    public Line(int _ID, String _name, ArrayList<Integer> _stops) {
        this._ID = _ID;
        this._name = _name;
        this._stops = _stops;
    }

    public Line(int _ID, String _name, String _stopsString) {
        this._ID = _ID;
        this._name = _name;
        this._stops = new ArrayList<Integer>();
        String[] stopStringParts = _stopsString.split("-");
        for(String stop : stopStringParts)
            this._stops.add(Integer.valueOf(stop));
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
     * @return the _stops
     */
    public ArrayList<Integer> getStops() {
        return _stops;
    }

    /**
     * @param _stops the _stops to set
     */
    public void setStops(ArrayList<Integer> _stops) {
        this._stops = _stops;
    }
}
