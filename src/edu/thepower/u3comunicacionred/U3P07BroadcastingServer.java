package edu.thepower.u3comunicacionred;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class U3P07BroadcastingServer {
    public static void main(String[] args) {
        try(DatagramSocket dgs = new DatagramSocket(8453)){
            System.out.println("Servidor escuchando en el puerto " + dgs.getLocalPort());
            byte[] data = new byte[1024];
            DatagramPacket dgp = new DatagramPacket(data, data.length);
            dgs.receive(dgp);
            String mensaje = new String(dgp.getData(), 0, dgp.getLength());
            System.out.println("Mensaje recibido: " + mensaje);
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}
