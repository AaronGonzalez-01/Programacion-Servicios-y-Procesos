package edu.thepower.u3comunicacionred;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class U3P07BroadcastingClient {
    public static void main(String[] args) {
        int puertoServer = 8453;
        try (DatagramSocket dgs = new DatagramSocket()) {
            String mensaje = "Mensaje de broadcast";
            byte[] data = mensaje.getBytes();

            InetAddress broadcastIP = InetAddress.getByName("10.255.255.255");
            DatagramPacket dgp = new DatagramPacket(data, data.length, broadcastIP, puertoServer);

            dgs.setBroadcast(true);
            dgs.send(dgp);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
