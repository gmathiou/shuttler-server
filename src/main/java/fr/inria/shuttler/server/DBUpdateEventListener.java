/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.inria.shuttler.server;

/**
 *
 * @author mathioud
 */
public interface DBUpdateEventListener {
    void updateRouteSessionViews(String email, int views, double kilometers);
    void newUserRegistration(String email);
}
