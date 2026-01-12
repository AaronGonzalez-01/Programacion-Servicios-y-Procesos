package edu.thepower.ejerciciosExamen2;

/*
Ejercicio 6 – WebServer con archivos estáticos y 404 personalizado

Enunciado:
- Implementar un servidor HTTP con ServerSocket que sirva archivos estáticos desde la carpeta local "www".
- Si la ruta es "/", se debe servir "www/index.html".
- Para una ruta "/algo", se intentará servir "www/algo.html".
- Si el archivo no existe, devolver un 404 con una página HTML personalizada que indique
  la ruta pedida y cuántos 404 se han producido.
- Añadir la ruta "/stats404" para mostrar el número de errores 404 generados.
*/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class U4P03WebServerArchivosEj6 {

    private static final int PORT = 8080;
    private static final String HTTP_ESTADO_OK = "200 OK";
    private static final String HTTP_ESTADO_NOT_FOUND = "404 Not Found";
    private static final String HTTP_ESTADO_METHOD_NOT_ALLOWED = "405 Method Not Allowed";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final AtomicInteger contador404 = new AtomicInteger(0);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en el puerto:" + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                Thread t = new Thread(() -> atenderSolicitud(socket));
                t.start();
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor");
        }
    }

    private static void atenderSolicitud(Socket socket) {
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream();
             PrintWriter pw = new PrintWriter(out)) {

            String line = bf.readLine();
            if (line == null || line.isBlank()) {
                return;
            }

            System.out.println(line);
            String[] partes = line.split("\\s+");
            String metodo = partes[0].trim();
            String ruta = (partes.length > 1 ? partes[1] : "/").trim();

            while ((line = bf.readLine()) != null && !line.isBlank()) {
                System.out.println(line);
            }

            String estado;
            byte[] cuerpo;
            String contentType = "text/html; charset=UTF-8";

            if (!metodo.equalsIgnoreCase("GET")) {
                estado = HTTP_ESTADO_METHOD_NOT_ALLOWED;
                String html = "<html><body><h1>405 Method Not Allowed</h1></body></html>";
                cuerpo = html.getBytes();
            } else if (ruta.equals("/stats404")) {
                estado = HTTP_ESTADO_OK;
                String html = """
                        <html>
                        <head><title>Estadísticas 404</title></head>
                        <body>
                        <h1>Estadísticas de errores 404</h1>
                        <p>Errores 404 producidos: %d</p>
                        <p>Fecha: %s</p>
                        </body>
                        </html>
                        """.formatted(contador404.get(), FORMATO_FECHA.format(LocalDateTime.now()));
                cuerpo = html.getBytes();
            } else {
                String nombre;
                if (ruta.equals("/")) {
                    nombre = "index.html";
                } else {
                    String limpio = ruta.startsWith("/") ? ruta.substring(1) : ruta;
                    if (limpio.isBlank()) {
                        limpio = "index";
                    }
                    if (!limpio.endsWith(".html")) {
                        limpio = limpio + ".html";
                    }
                    nombre = limpio;
                }

                Path path = Path.of("www", nombre);
                if (Files.exists(path) && Files.isRegularFile(path)) {
                    estado = HTTP_ESTADO_OK;
                    cuerpo = Files.readAllBytes(path);
                    contentType = detectarContentType(path);
                } else {
                    estado = HTTP_ESTADO_NOT_FOUND;
                    contador404.incrementAndGet();
                    String html404 = """
                            <html>
                            <head><title>404 - No encontrado</title></head>
                            <body>
                            <h1>404 - Recurso no encontrado</h1>
                            <p>La ruta solicitada no existe: %s</p>
                            <p>Errores 404 acumulados: %d</p>
                            </body>
                            </html>
                            """.formatted(ruta, contador404.get());
                    cuerpo = html404.getBytes();
                }
            }

            pw.println("HTTP/1.1 " + estado);
            pw.println("Content-Type: " + contentType);
            pw.println("Content-Length: " + cuerpo.length);
            pw.println();
            pw.flush();
            out.write(cuerpo);
            out.flush();

        } catch (IOException e) {
            System.out.println("Error en solicitud: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // ignorar
            }
        }
    }

    private static String detectarContentType(Path path) {
        String nombre = path.getFileName().toString().toLowerCase();
        if (nombre.endsWith(".html") || nombre.endsWith(".htm")) {
            return "text/html; charset=UTF-8";
        } else if (nombre.endsWith(".css")) {
            return "text/css; charset=UTF-8";
        } else if (nombre.endsWith(".js")) {
            return "application/javascript; charset=UTF-8";
        } else {
            return "application/octet-stream";
        }
    }
}

