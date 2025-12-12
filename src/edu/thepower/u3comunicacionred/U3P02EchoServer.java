package edu.thepower.u3comunicacionred;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class U3P02EchoServer {
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    public static String getFecha(){
        return sdf.format(System.currentTimeMillis());
    }

    public static void main(String[] args) {
        Thread demonio = new Thread(() -> {
            while (true) {
                System.out.println(getFecha() + " Servidor activo");
                try {
                    Thread.sleep(5000);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        });
        try (ServerSocket server = new ServerSocket(1025)){
            demonio.start();
            System.out.println(getFecha() + " Servidor escuchando en puerto 1025");
            Socket socket = server.accept();
            System.out.println(getFecha() + " Recibido solicitud de comunicación");
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println("Recibido del cliente: " + line);
                pw.println(line.toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error al arrancar el servidor " + e.getMessage());
        }
    }
}