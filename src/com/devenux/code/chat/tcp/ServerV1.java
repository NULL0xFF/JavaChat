package com.devenux.code.chat.tcp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @deprecated
 */
public class ServerV1 {

    // Instance Variables
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Thread sendThread;
    private Thread recvThread;

    /**
     * Server setting
     */
    private void serverSetting() {
        // Server socket port 9000
        // Wait for client socket to be connected;
        try {
            this.serverSocket = new ServerSocket(9000);
            System.out.println("Waiting for client...");
            this.clientSocket = this.serverSocket.accept();
            System.out.println("Client connected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close all sockets and streams
     */
    private void closeAll() {
        try {
            this.dataInputStream.close();
            this.dataOutputStream.close();
            this.serverSocket.close();
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stream setting
     */
    private void streamSetting() {
        // Connect data input/output stream to socket
        try {
            this.dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());
            this.dataInputStream = new DataInputStream(this.clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dataSend() {
        // Create thread and keep send message to client using loop
        // Exit when input is "/quit"
        this.sendThread = new Thread(new Runnable() {
            private BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(System.in));

            @Override
            public void run() {
                while (true) {
                    try {
                        String inputString = inputStreamReader.readLine();
                        if (inputString.compareTo("!quit") == 0) {
                            System.err.println("!quit detected. Exiting server...");
                            closeAll();
                            break;
                        } else
                            dataOutputStream.writeUTF(inputString);
                    } catch (IOException e) {
                        System.err.println("IOException in sendThread. Exiting server...");
                        closeAll();
                        break;
                    }
                }
            }
        });
        this.sendThread.start();
    }

    private void dataRecv() {
        // Create thread and keep print out client's message using loop
        // Exit when received the message "/quit"
        this.recvThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String outputString = dataInputStream.readUTF();
                        if (outputString.compareTo("!quit") == 0) {
                            System.err.println("Client sent !quit. Exiting server...");
                            closeAll();
                            break;
                        } else
                            System.out.println("client: " + outputString);
                    } catch (EOFException e) {
                        System.err.println("EOFException in recvThread. Exiting server...");
                        closeAll();
                        break;
                    } catch (IOException e) {
                        System.err.println("IOException in recvThread. Exiting server...");
                        closeAll();
                        break;
                    }
                }
            }
        });
        this.recvThread.start();
    }

    public void overwatch() {
        if (this.sendThread.isAlive())
            try {
                this.sendThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        if (this.recvThread.isAlive())
            try {
                this.recvThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        this.closeAll();
    }

    public ServerV1() {
        this.serverSetting();
        this.streamSetting();
        this.dataSend();
        this.dataRecv();
        // this.overwatch();
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        new ServerV1();
    }

}