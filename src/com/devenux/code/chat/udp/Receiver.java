package com.devenux.code.chat.udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.net.DatagramPacket;

public class Receiver {

    public static void main(String[] args) throws IOException {

        byte[] buffer = new byte[256];

        DatagramSocket socket = new DatagramSocket(5000);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            socket.receive(packet);
            String string = new String(buffer);
            if (string.compareTo("!quit") == 0)
                break;
            System.out.println(new String(buffer));
            Arrays.fill(buffer, (byte) 0);
        }

        socket.close();
    }
}