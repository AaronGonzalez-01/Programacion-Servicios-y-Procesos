package edu.thepower.u3comunicacionred;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class U3P06ClientUDP {
    static void main(String[] args) {

        try(DatagramSocket ds = new DatagramSocket()){
            String mensaje = "Aaron es malo en valorant";
            byte[] data = mensaje.getBytes();
            InetAddress host = InetAddress.getByName("localhost");
            int port = 2100;
            DatagramPacket dp = new DatagramPacket(data, data.length, host, port);
            ds.send(dp);

            //Recogida de la ACK del servidor
            byte[] repuesta = new byte[1024];
            DatagramPacket dp2 = new DatagramPacket(repuesta, repuesta.length);
            ds.receive(dp2);
            String mensajeServer = new String(dp2.getData(), 0 , dp2.getLength());
            System.out.println("Mensaje recibido: " + mensajeServer);

        }catch (IOException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}