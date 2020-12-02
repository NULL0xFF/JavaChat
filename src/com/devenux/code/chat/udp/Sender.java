package com.devenux.code.chat.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        String string = "";
        if (args.length == 1)
            string = args[0];
        else
            string = "A message from a sender.";
        byte[] buffer = string.getBytes();

        InetAddress address = InetAddress.getByName("127.0.0.1");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 5000);
        socket.send(packet);
        socket.close();
    }
}