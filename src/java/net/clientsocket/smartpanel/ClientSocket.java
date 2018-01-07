/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clientsocket.smartpanel;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author hfischer
 */
@ServerEndpoint("/endpoint")
public class ClientSocket {

    @OnMessage
    public String onMessage(String message) {
        return null;
    }
    
}
