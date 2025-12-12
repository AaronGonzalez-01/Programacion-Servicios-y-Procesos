package edu.thepower.u3comunicacionred;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class U3P08ClientLat {
    public static void main(String[] args){
        try(DatagramSocket ds = new DatagramSocket()){
            String ping = "ping";
            byte[] pingBytes = ping.getBytes();
            InetAddress inetAddress = InetAddress.getLocalHost();
            int port = 2300;
            DatagramPacket dp =  new DatagramPacket(pingBytes, pingBytes.length, inetAddress, port);
            for (int i = 0; i < 10; i++){
                long tiempo = System.nanoTime();
                try {
                    ds.send(dp);
                    byte[] dataACK = new byte[1024];
                    DatagramPacket dpAck = new DatagramPacket(dataACK, dataACK.length);
                    ds.receive(dpAck);
                    long tiempoAck = System.nanoTime();
                    System.out.println("Numeró de ping: " + (i + 1) + " - Latencia: " + String.format("%.2f", ((tiempoAck - tiempo) / 1_000_000.0)) + "ms");
                }catch (SocketTimeoutException e){
                    System.err.println("Posible perdida de paquete: " + e.getMessage());
                }
            }
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
}
