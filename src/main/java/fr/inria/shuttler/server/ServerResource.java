/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.inria.shuttler.server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mathioud
 */
public class ServerResource {
    
    protected DBHandler _dbHandler;
    protected List _listeners = new ArrayList();
    protected synchronized void addEventListener(DBUpdateEventListener listener) {
        _listeners.add(listener);
    }
    protected synchronized void removeEventListener(DBUpdateEventListener listener) {
        _listeners.remove(listener);
    }
    protected synchronized void updateRouteSessionViews() {
        for(Object listener: _listeners){
            ((DBUpdateEventListener) listener).updateRouteSessionViews();
        }
    }
    
    protected synchronized void getUserStats() {
        for(Object listener: _listeners){
            ((DBUpdateEventListener) listener).getUserStats();
        }
    }
    
    protected synchronized void updateUserRankings() {
        for(Object listener: _listeners){
            ((DBUpdateEventListener) listener).updateUserRankings();
        }
    }
}
