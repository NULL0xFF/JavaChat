package com.devenux.code.chat.tcp;

import java.io.DataInputStream;
import java.io.EOFException;
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
    private Socket clientSocket = null;
    private Thread serverThread;
    private HashMap<Integer, Socket> clientMap = new HashMap<>();

    // Logging
    private final static Logger LOG = Logger.getGlobal();

    /**
     * Server Setting
     */
    private void serverSetting() {
        try {
            this.serverSocket = new ServerSocket(9000); // Bind address to ServerSocket
            LOG.info("Server Address Bind Successful");
        } catch (IOException e) {
            LOG.severe(e.getMessage());
            return;
        }
    }

    /**
     * Wait for clients to be connected and save data with HashMap
     */
    private void clientsAccept() {
        this.serverThread = new Thread(new Runnable() {
            private boolean isCopied = false;

            @Override
            public void run() {
                try {
                    while (true) {
                        LOG.info("Waiting for Client");
                        clientSocket = serverSocket.accept();
                        LOG.info("Client Connected!");
                        clientMap.put(clientSocket.hashCode(), clientSocket);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Socket runSocket = clientSocket;
                                isCopied = true;
                                try {
                                    DataInputStream dataInputStream = new DataInputStream(runSocket.getInputStream());
                                    while (true) {
                                        String clientString = dataInputStream.readUTF();
                                        LOG.info(clientString);
                                    }
                                } catch (Exception e) {
                                    LOG.severe(e.getMessage());
                                } finally {
                                    clientMap.remove(runSocket.hashCode());
                                }
                            }
                        }).start();
                        while (isCopied == false) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                LOG.severe(e.getMessage());
                            }
                        }
                        isCopied = false;
                        clientSocket = null;
                    }
                } catch (IOException e) {
                    LOG.severe(e.getMessage());
                }
            }
        });
        this.serverThread.start();
    }

    /**
     * Wait until threads stop
     */
    private void waitForClose() {
        try {
            if (serverThread.isAlive())
                serverThread.join();
        } catch (InterruptedException e) {
            LOG.severe(e.getMessage());
        }
    }

    public ChatServer() {
        this.serverSetting();
        this.clientsAccept();
        this.waitForClose();
    }

    /**
     * Main Static Method
     */
    public static void main(String[] args) {
        LOG.setLevel(Level.INFO);

        new ChatServer();
    }
}