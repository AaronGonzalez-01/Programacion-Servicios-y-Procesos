package edu.thepower.ejerciciosExamen2;


/*
Ejercicio 5 – PoolServer con autenticación simple y límite de clientes

Enunciado:
- Implementar un servidor con ServerSocket y ExecutorService (pool de hilos).
- Máximo 3 clientes concurrentes; si se supera, responder "Servidor ocupado"
  y cerrar esa conexión.
- Cada cliente debe autenticarse primero con: LOGIN user pass
  (user = admin, pass = 1234).
- Si falla 3 veces el login, se cierra la conexión.
- Si el login es correcto, puede usar los comandos:
  ECHO, UPPER, LOWER, TIME, QUIT.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class U4P01PoolServerAuthEj5 {

    private static final int PORT = 2777;
    private static final int MAX_CLIENTES = 3;
    private static final DateTimeFormatter FORMAT_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final AtomicInteger clientesActivos = new AtomicInteger(0);

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Escuchando en el puerto: " + serverSocket.getLocalPort());
            while (true) {
                Socket socket = serverSocket.accept();
                int activos = clientesActivos.incrementAndGet();
                if (activos > MAX_CLIENTES) {
                    System.out.println("Rechazando cliente: máximo alcanzado");
                    try (PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
                        pw.println("Servidor ocupado, intenta de nuevo más tarde");
                    } catch (IOException e) {
                        // ignorar
                    }
                    clientesActivos.decrementAndGet();
                    socket.close();
                    continue;
                }
                pool.submit(() -> atenderCliente(socket));
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void atenderCliente(Socket socket) {
        System.out.println("Cliente conectado: " + socket.getRemoteSocketAddress());
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {

            boolean autenticado = false;
            int intentos = 0;
            pw.println("Bienvenido. Debes autenticarte con: LOGIN user pass");

            while (!autenticado) {
                String line = bf.readLine();
                if (line == null) {
                    return;
                }
                String[] partes = line.trim().split("\\s+");
                if (partes.length == 3 && partes[0].equalsIgnoreCase("LOGIN")) {
                    String user = partes[1];
                    String pass = partes[2];
                    if ("admin".equals(user) && "1234".equals(pass)) {
                        autenticado = true;
                        pw.println("Autenticación correcta. Ya puedes usar comandos.");
                    } else {
                        intentos++;
                        pw.println("Login incorrecto (" + intentos + "/3)");
                        if (intentos >= 3) {
                            pw.println("Demasiados intentos. Cerrando conexión.");
                            return;
                        }
                    }
                } else {
                    pw.println("Debes usar: LOGIN user pass");
                }
            }

            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println("Comando de cliente autenticado: " + line);
                String respuesta = procesarComando(line);
                pw.println(respuesta);
                if ("QUIT".equalsIgnoreCase(obtenerComando(line))) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // ignorar
            }
            int activos = clientesActivos.decrementAndGet();
            System.out.println("Cliente desconectado. Activos: " + activos);
        }
    }

    private static String obtenerComando(String line) {
        String[] partes = line.trim().split("\\s+", 2);
        return partes[0].toUpperCase();
    }

    private static String procesarComando(String line) {
        String[] partes = line.trim().split("\\s+", 2);
        String comando = partes[0].toUpperCase();
        String cuerpo = partes.length > 1 ? partes[1] : "";

        return switch (comando) {
            case "ECHO" -> cuerpo;
            case "UPPER" -> cuerpo.toUpperCase();
            case "LOWER" -> cuerpo.toLowerCase();
            case "TIME" -> "Hora del servidor: " + LocalTime.now().format(FORMAT_HORA);
            case "QUIT" -> "Cerrando conexión. Adiós.";
            default -> "Comando no reconocido. Usa ECHO, UPPER, LOWER, TIME, QUIT.";
        };
    }
}

