package edu.thepower.u3comunicacionred;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class U3P06ServerUDP {
    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket(2100)){
            System.out.println("Servidor escuchando en el puerto " + ds.getPort());
            byte[] data = new byte[1024];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            ds.receive(dp);
            String mensaje = new String(dp.getData(), 0, dp.getLength());
            System.out.println("Mensaje recibido: " + mensaje);

            //Respuesta al mensaje recibido
            String ack = "ACK " + mensaje;
            byte[] dataACK = ack.getBytes();
            InetAddress host = dp.getAddress();
            int port = dp.getPort();
            DatagramPacket dtp  =  new DatagramPacket(dataACK, dataACK.length, host, port);
            ds.send(dtp);
        } catch (IOException e){
            System.err.println("Error en el servidor " + e.getMessage());
        }
    }
}
