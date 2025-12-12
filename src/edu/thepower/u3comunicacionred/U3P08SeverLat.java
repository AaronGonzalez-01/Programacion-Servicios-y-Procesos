package edu.thepower.u3comunicacionred;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class U3P08SeverLat {
    public static void main(String[] args) throws Exception {
        try(DatagramSocket ds = new DatagramSocket(2300)){
            System.out.println("Iniciando servidor en puerto 2300");
            byte[] data = new byte[1024];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            while(true){
                dp = new DatagramPacket(data, data.length);
                ds.receive(dp);
                String mensaje  = new String(dp.getData(), 0, dp.getLength());
                System.out.println("Recivido: " + mensaje);
                String respuesta = "pong";
                byte[] dataRes =  respuesta.getBytes();
                InetAddress inetAddress = dp.getAddress();
                int port = dp.getPort();
                DatagramPacket dp2 = new DatagramPacket(dataRes, dataRes.length, inetAddress, port);
                ds.send(dp2);
            }
        }catch(IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}
