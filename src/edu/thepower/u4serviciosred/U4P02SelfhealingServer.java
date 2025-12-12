package edu.thepower.u4serviciosred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class U4P02SelfhealingServer {
    public static void main(String[] args) {
        while(true){
            try {
                arrancarServidor();
            }catch (Exception e){
                System.out.println("Servidor fuera de servicio");
                System.out.println("Reiniciando en 2 segundos");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {

                }
            }
        }
    }

    private static void arrancarServidor() {
        try(ServerSocket serverSocket = new ServerSocket(2777);){
            System.out.println("Escuchando en el puerto: " + serverSocket.getLocalPort());
            Thread killer = new Thread(() -> {
               System.out.println("Thread killer iniciándose");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            });
            killer.start();

            while(true){
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try(BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
                        PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
                        String line;
                        while((line = bf.readLine())!=null){
                            System.out.println("Recibido del cliente: " + line);
                            pw.println(line.toLowerCase());
                        }
                    }catch (IOException e){
                        System.out.println(e.getMessage());
                    }
                }).start();
            }
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
