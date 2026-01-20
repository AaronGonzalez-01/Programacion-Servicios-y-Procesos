package edu.thepower.ejerciciosExamen2;


/*
Ejercicio 4 – Self-healing server con comando STOP que cancela el killer

Enunciado:
- Partiendo de un servidor "self-healing" que se reinicia cuando falla,
  existe un hilo "killer" que cierra el ServerSocket tras 5 segundos.
- Modifica el servidor para que si un cliente envía el texto "STOP",
  se cierre el ServerSocket por petición del cliente y NO por el killer.
- El bucle principal de self-healing debe seguir activo, reiniciando el servidor
  después de que se cierre.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class U4P02SelfhealingServerEj4 {

    private static volatile boolean cerrarPorCliente = false;

    public static void main(String[] args) {
        while (true) {
            try {
                arrancarServidor();
            } catch (Exception e) {
                System.out.println("Servidor fuera de servicio");
                System.out.println("Reiniciando en 2 segundos");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    // ignorar
                }
            }
        }
    }

    private static void arrancarServidor() {
        cerrarPorCliente = false;

        try (ServerSocket serverSocket = new ServerSocket(2777)) {
            System.out.println("Escuchando en el puerto: " + serverSocket.getLocalPort());

            Thread killer = new Thread(() -> {
                System.out.println("Thread killer iniciándose");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // ignorar
                }
                if (!cerrarPorCliente) {
                    try {
                        System.out.println("Killer cerrando el servidor");
                        serverSocket.close();
                    } catch (IOException e) {
                        // ignorar
                    }
                } else {
                    System.out.println("Killer cancelado, servidor cerrado por cliente");
                }
            });
            killer.start();

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try (BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {

                        String line;
                        while ((line = bf.readLine()) != null) {
                            System.out.println("Recibido del cliente: " + line);
                            if ("STOP".equalsIgnoreCase(line.trim())) {
                                pw.println("Servidor se cerrará por petición del cliente");
                                cerrarPorCliente = true;
                                serverSocket.close();
                                break;
                            }
                            pw.println(line.toLowerCase());
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
