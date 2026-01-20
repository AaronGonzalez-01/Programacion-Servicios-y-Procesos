package edu.thepower.ejerciciosExamen2;


/*
Ejercicio 2 – WebServer con contadores por ruta

Enunciado:
- A partir del WebServer sencillo con contador global, añade contadores separados para las rutas "/", "/fecha" y "/nombre".
- Cada vez que se llame a una ruta, aumenta su contador correspondiente.
- La respuesta HTML debe indicar cuántas veces se ha visitado esa ruta y el total de peticiones procesadas por el servidor.
- Seguir ignorando las peticiones a /favicon.ico en el contador global.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class U4P03WebServerEj2 {

    private static AtomicInteger contadorGeneral = new AtomicInteger(0);
    private static AtomicInteger contadorRoot = new AtomicInteger(0);
    private static AtomicInteger contadorFecha = new AtomicInteger(0);
    private static AtomicInteger contadorNombre = new AtomicInteger(0);

    private static final String HTML = """
            <html>
                <head>
                    <title>Servidor web sencillo</title>
                </head>
                <body>
                    <h1>Servidor web ejercicio 2</h1>
                    <p>%s</p>
                </body>
            </html>
            """;
    private static final int PORT = 8080;
    private static final String NOMBRE_SERVIDOR = "Servidor web sencillo";
    private static final String HTTP_ESTADO_OK = "200 OK";
    private static final String HTTP_ESTADO_NOT_FOUND = "404 Not Found";
    private static final String HTTP_ESTADO_METHOD_NOT_ALLOWED = "405 Method Not Allowed";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en el puerto:" + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(() -> atenderSolicitud(socket));
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor");
        }
    }

    private static void atenderSolicitud(Socket socket) {
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            String line = bf.readLine();
            if (line != null && !line.isBlank()) {
                System.out.println(line);
                String[] partes = line.split("\\s+");
                String metodo = partes[0].trim();
                String ruta = (partes.length > 1 ? partes[1] : "/").trim();

                while ((line = bf.readLine()) != null && !line.isBlank()) {
                    System.out.println(line);
                }

                String estado = HTTP_ESTADO_OK;
                String respuesta;

                if (metodo.equalsIgnoreCase("GET")) {
                    if (!ruta.contains("favicon.ico")) {
                        contadorGeneral.incrementAndGet();
                    }
                    System.out.println("Devolviendo html");
                    respuesta = switch (ruta) {
                        case "/" -> {
                            int num = contadorRoot.incrementAndGet();
                            yield "Has visitado '/' " + num + " veces. Total peticiones: " + contadorGeneral.get();
                        }
                        case "/fecha", "/fecha/" -> {
                            int num = contadorFecha.incrementAndGet();
                            String fecha = FORMATO_FECHA.format(LocalDateTime.now());
                            yield "La fecha y hora actuales son: " + fecha +
                                    "<br>Has visitado '/fecha' " + num + " veces.";
                        }
                        case "/nombre", "/nombre/" -> {
                            int num = contadorNombre.incrementAndGet();
                            yield "El nombre del servidor es: " + NOMBRE_SERVIDOR +
                                    "<br>Has visitado '/nombre' " + num + " veces.";
                        }
                        default -> {
                            estado = HTTP_ESTADO_NOT_FOUND;
                            yield "Ruta no encontrada";
                        }
                    };
                } else {
                    estado = HTTP_ESTADO_METHOD_NOT_ALLOWED;
                    respuesta = "Método no permitido";
                }

                devolverRespuesta(pw, estado, respuesta);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void devolverRespuesta(PrintWriter pw, String estado, String mensaje) {
        StringBuilder respuesta = new StringBuilder();
        respuesta.append(String.format(HTML, mensaje));
        byte[] bytes = respuesta.toString().getBytes();
        pw.println("HTTP/1.1 " + estado);
        pw.println("Content-Type: text/html;charset=UTF-8");
        pw.println("Content-Length: " + bytes.length);
        pw.println();
        pw.print(respuesta);
        pw.flush();
    }
}

