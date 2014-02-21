/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.inria.shuttler.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathioud
 */
public class DBHandler implements DBUpdateEventListener{

    public DBHandler(ShuttlerResource resource){
       resource.addEventListener(this);
       initializeDB();
    }
    
    private void initializeDB(){
        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
        }

        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:8889/shuttlerDB", "root", "root");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into  stops values (?)");
                preparedStatement.setInt(1, 1);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(ShuttlerResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Failed to make connection!");
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
