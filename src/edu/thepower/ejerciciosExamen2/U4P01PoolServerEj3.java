package edu.thepower.ejerciciosExamen2;


/*
Ejercicio 3 – PoolServer con comando FIN para cerrar solo el cliente

Enunciado:
- Partiendo de un servidor con ServerSocket y un pool de hilos (ExecutorService),
  añade la lógica para que, si el cliente envía la línea "FIN", se cierre
  solo la conexión de ese cliente.
- El servidor debe seguir escuchando nuevas conexiones de otros clientes.
- Mientras no se envíe "FIN", el servidor devuelve las líneas recibidas en minúsculas.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class U4P01PoolServerEj3 {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        try (ServerSocket serverSocket = new ServerSocket(2777)) {
            System.out.println("Escuchando en el puerto: " + serverSocket.getLocalPort());
            while (true) {
                Socket socket = serverSocket.accept();
                pool.submit(() -> {
                    try (BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
                        String line;
                        while ((line = bf.readLine()) != null) {
                            System.out.println("Recibido del cliente: " + line);
                            if ("FIN".equalsIgnoreCase(line.trim())) {
                                pw.println("Conexion cerrada por el servidor");
                                break;
                            }
                            pw.println(line.toLowerCase());
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            // ignorar
                        }
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor" + e.getMessage());
        }
    }
}

