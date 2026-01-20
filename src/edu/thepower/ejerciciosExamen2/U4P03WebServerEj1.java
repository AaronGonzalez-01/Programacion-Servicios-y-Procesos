package edu.thepower.ejerciciosExamen2;

/*
Ejercicio 1 – WebServer con ruta /saludo y query string

Enunciado:
- A partir de un WebServer sencillo con ServerSocket, añade la ruta /saludo.
- La ruta /saludo debe aceptar un parámetro en la query string, por ejemplo: /saludo?nombre=Pepe
- Si se recibe nombre, la respuesta HTML debe mostrar "Hola, Pepe".
- Si no se recibe nombre, la respuesta debe ser "Hola, desconocido".
- Mantener el contador de visitas general y no contar las peticiones a /favicon.ico.
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

public class U4P03WebServerEj1 {

    private static AtomicInteger contador = new AtomicInteger(0);
    private static final String HTML = """
            <html>
                <head>
                    <title>Servidor web sencillo</title>
                </head>
                <body>
                    <h1>Servidor web ejercicio 1</h1>
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
                String rutaCompleta = (partes.length > 1 ? partes[1] : "/").trim();

                // Separar ruta y query string
                String ruta;
                String query = null;
                int posInterrogacion = rutaCompleta.indexOf("?");
                if (posInterrogacion >= 0) {
                    ruta = rutaCompleta.substring(0, posInterrogacion);
                    query = rutaCompleta.substring(posInterrogacion + 1);
                } else {
                    ruta = rutaCompleta;
                }

                while ((line = bf.readLine()) != null && !line.isBlank()) {
                    System.out.println(line);
                }

                String estado = HTTP_ESTADO_OK;
                String respuesta;

                if (metodo.equalsIgnoreCase("GET")) {
                    if (!ruta.contains("favicon.ico")) {
                        contador.incrementAndGet();
                    }
                    System.out.println("Devolviendo html");
                    respuesta = switch (ruta) {
                        case "/" -> "Eres el visitante número: " + contador.get();
                        case "/fecha", "/fecha/" ->
                                "La fecha y hora actuales son: " + FORMATO_FECHA.format(LocalDateTime.now());
                        case "/nombre", "/nombre/" ->
                                "El nombre del servidor es: " + NOMBRE_SERVIDOR;
                        case "/saludo", "/saludo/" -> {
                            String nombre = "desconocido";
                            if (query != null) {
                                String[] pares = query.split("&");
                                for (String par : pares) {
                                    String[] kv = par.split("=", 2);
                                    if (kv.length == 2 && kv[0].equals("nombre") && !kv[1].isBlank()) {
                                        nombre = kv[1];
                                        break;
                                    }
                                }
                            }
                            yield "Hola, " + nombre;
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
