package com.devenux.code.chat.tcp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basic Socket Server
 * 
 * @author NoPlayer40600@gmail.com
 */
public class ChatServer {

    // Instance Variables
    private ServerSocket serverSocket;
    private HashMap<Integer, Socket> clientMap;
    private DataInputStream dataInputStream;

    // Logging
    private final static Logger LOG = Logger.getGlobal();

    /**
     * Server Setting
     */
    private void serverSetting() {
        try {
            this.serverSocket = new ServerSocket(9000); // Open server at port 9000
            Thread.sleep(10000);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
            return;
        } catch (InterruptedException e) {
            LOG.warning(e.getMessage());
            return;
        }
    }

    public ChatServer() {
        this.serverSetting();
    }

    /**
     * Main Static Method
     */
    public static void main(String[] args) {
        LOG.setLevel(Level.INFO);

        new ChatServer();
    }
}