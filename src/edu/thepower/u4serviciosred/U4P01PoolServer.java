package edu.thepower.u4serviciosred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class U4P01PoolServer {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        try(ServerSocket serverSocket = new ServerSocket(2777);){
            System.out.println("Escuchando en el puerto: " + serverSocket.getLocalPort());
            while(true){
                Socket socket = serverSocket.accept();
                pool.submit(() -> {
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
                });
            }
        }catch (IOException e){
            System.err.println("Error en el servidor" + e.getMessage());
        }
    }
}
