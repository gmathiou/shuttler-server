package com.gmathiou.shuttler.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author mathioud
 */
public class DBHandler implements DBUpdateEventListener {

    private Connection DBconnection = null;

    public DBHandler() {
        DBconnection = initializeDB();
        loadLines();
        loadStops();
    }

    private Connection initializeDB() {
        System.out.println("-------- MySQL JDBC Connection ------------");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found");
        }

        try {
            setDBconnection(DriverManager.getConnection("jdbc:mysql://localhost:8889/shuttlerDB?useUnicode=true&characterEncoding=UTF-8", "root", "root"));
        } catch (SQLException e) {
            System.err.println("Connection to DB Failed!");
        }
        return getDBconnection();
    }

    private void loadStops() {
        if (getDBconnection() == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = getDBconnection().prepareStatement("SELECT * FROM `stops` WHERE 1");
            ResultSet results = preparedStatement.executeQuery();
            DataHandler.getStops().clear();
            while (results.next()) {
                int ID = results.getInt("id");
                String shortName = results.getString("shortname");
                String name = results.getString("name");
                double lat = results.getDouble("latitude");
                double lon = results.getDouble("longitude");
                int lineId = results.getInt("line");
                Stop newStop = new Stop(ID, shortName, name, lat, lon, lineId);
                DataHandler.getStops().add(newStop);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShuttlerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadLines() {
        if (getDBconnection() == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = getDBconnection().prepareStatement("SELECT * FROM `lines` WHERE 1");
            ResultSet results = preparedStatement.executeQuery();
            DataHandler.getLines().clear();
            while (results.next()) {
                int ID = results.getInt("id");
                String name = results.getString("name");
                String stopSequence = results.getString("stopID_sequence");
                Line newLine = new Line(ID, name, stopSequence);
                DataHandler.getLines().add(newLine);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShuttlerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateRouteSessionViews(String email, int views, double kilometers) {
        if (getDBconnection() == null) {
            return;
        }

        try {
            PreparedStatement selectStatement = getDBconnection().prepareStatement("SELECT `views`,`kilometers`  FROM `profiles` WHERE `email` = ?");
            selectStatement.setString(1, email);
            ResultSet resultSet = selectStatement.executeQuery();
            int currentViews = 0;
            double currentKilometers = 0.0;
            while (resultSet.next()) {
                currentViews = resultSet.getInt("views");
                currentKilometers = resultSet.getDouble("kilometers");
            }
            int updatedViews = currentViews + views;
            double updatedKilometers = currentKilometers + kilometers;
            PreparedStatement updateStatement = getDBconnection().prepareStatement("UPDATE `profiles` SET `views`= ? ,`kilometers`= ? WHERE `email` = ?");
            updateStatement.setInt(1, updatedViews);
            updateStatement.setDouble(2, updatedKilometers);
            updateStatement.setString(3, email);
            updateStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ShuttlerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JSONObject getUserStats(String email) {
        if (getDBconnection() == null) {
            return null;
        }

        JSONObject reply = new JSONObject();
        try {
            PreparedStatement preparedStatement = getDBconnection().prepareStatement("SELECT * FROM `profiles` WHERE `email` = ? ORDER BY `views` DESC");
            preparedStatement.setString(1, email);
            ResultSet results = preparedStatement.executeQuery();
            if (!results.isBeforeFirst()) {
                reply.put("email", email);
                reply.put("views", "0");
                reply.put("kilometers", "0");
                reply.put("rank", "0");
                return reply;
            }
            while (results.next()) {
                if (results.getString("email").equals(email)) {
                    reply.put("email", results.getString("email"));
                    reply.put("views", results.getInt("views"));
                    reply.put("kilometers", results.getInt("kilometers"));
                    reply.put("rank", results.getRow());
                    return reply;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShuttlerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reply;
    }

    public JSONObject authenticateUser(String email, String pass) {
        if (getDBconnection() == null) {
            return null;
        }
        JSONObject reply = new JSONObject();

        PreparedStatement selectStatement;
        try {
            selectStatement = getDBconnection().prepareStatement("SELECT *  FROM `profiles` WHERE `email` = ? AND `password` = ?");
            selectStatement.setString(1, email);
            selectStatement.setString(2, pass);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                reply.put("authentication", 1);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reply;
    }

    public Boolean registerUser(String email, String pass) {
        if (getDBconnection() == null) {
            return null;
        }
        PreparedStatement selectStatement;
        try {
            selectStatement = getDBconnection().prepareStatement("SELECT *  FROM `profiles` WHERE `email` = ?");
            selectStatement.setString(1, email);
            ResultSet resultSet = selectStatement.executeQuery();
            if (!resultSet.next()) {
                PreparedStatement insertStatement = getDBconnection().prepareStatement("INSERT INTO `profiles`(`email`, `password`, `views`, `kilometers`) VALUES (?,?,0,0)");
                insertStatement.setString(1, email);
                insertStatement.setString(2, pass);
                insertStatement.execute();
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * @return the DBconnection
     */
    public Connection getDBconnection() {
        return DBconnection;
    }

    /**
     * @param DBconnection the DBconnection to set
     */
    public void setDBconnection(Connection DBconnection) {
        this.DBconnection = DBconnection;
    }
}
