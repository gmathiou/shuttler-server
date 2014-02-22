/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.inria.shuttler.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathioud
 */
public class DBHandler implements DBUpdateEventListener {
    private Connection DBconnection = null;
    
    public DBHandler() {
        initializeDB();
        loadStops();
        loadLines();
    }

    private void initializeDB() {
        System.out.println("-------- MySQL JDBC Connection ------------");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found");
        }
        
        try {
            DBconnection = DriverManager.getConnection("jdbc:mysql://localhost:8889/shuttlerDB", "root", "root");
        } catch (SQLException e) {
            System.err.println("Connection to DB Failed!");
        }

        if (DBconnection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = DBconnection.prepareStatement("insert into  stops values (?)");
            preparedStatement.setInt(1, 1);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ShuttlerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadStops() {
        if (DBconnection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = DBconnection.prepareStatement("SELECT * FROM `stops` WHERE 1");
            ResultSet results = preparedStatement.executeQuery();
            while(results.next()){
                int ID = results.getInt("id");
                String shortName = results.getString("shortname");
                String name = results.getString("name");
                double lat = results.getDouble("latitude");
                double lon = results.getDouble("longitude");
                Stop newStop = new Stop(ID, shortName, name, lat, lon);
                DataHandler.getStops().add(newStop);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShuttlerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        private void loadLines() {
        if (DBconnection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = DBconnection.prepareStatement("SELECT * FROM `lines` WHERE 1");
            ResultSet results = preparedStatement.executeQuery();
            while(results.next()){
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
    public void updateRouteSessionViews() {
        System.out.println("Listened to event");
    }

    @Override
    public void getUserStats() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateUserRankings() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
