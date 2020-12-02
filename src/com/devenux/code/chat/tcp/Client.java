package com.devenux.code.chat.tcp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    // Instance Variables
    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private BufferedReader inputStreamReader;

    /**
     * Connect client to server
     */
    private void connect() {
        try {
            System.out.println("Trying to connect...");
            this.clientSocket = new Socket("127.0.0.1", 9000);
            System.out.println("Connected to Server...");
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
        // Create thread and keep send message to server using loop
        // Exit when input is "!quit"
        new Thread(new Runnable() {

            @Override
            public void run() {
                inputStreamReader = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    try {
                        String inputString = inputStreamReader.readLine();
                        if (inputString.compareTo("!quit") == 0) {
                            System.err.println("!quit detected. Exiting client...");
                            closeAll();
                            break;
                        } else
                            dataOutputStream.writeUTF(inputString);
                    } catch (IOException e) {
                        System.err.println("IOException in sendThread. Exiting client...");
                        closeAll();
                        break;
                    }
                }
            }
        }).start();
    }

    private void dataRecv() {
        // Create thread and keep print out server's message using loop
        // Exit when received the message "!quit"
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String outputString = dataInputStream.readUTF();
                        System.out.println("client: " + outputString);
                    } catch (EOFException e) {
                        System.err.println("EOFException in recvThread. Exiting client...");
                        System.err.printf("Press enter to exit.");
                        closeAll();
                        break;
                    } catch (IOException e) {
                        System.err.println("IOException in recvThread. Exiting client...");
                        closeAll();
                        break;
                    }
                }
            }
        }).start();
    }

    public Client() {
        this.connect();
        this.streamSetting();
        this.dataSend();
        this.dataRecv();
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        new Client();
    }
}