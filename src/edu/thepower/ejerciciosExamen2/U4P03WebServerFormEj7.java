package edu.thepower.ejerciciosExamen2;
/*
Ejercicio 7 – WebServer: procesar formulario POST (eco de datos)

Enunciado:
- Implementar un servidor HTTP que:
  * En GET "/" devuelva un formulario HTML con método POST hacia "/form".
  * En POST "/form" lea el cuerpo de la petición usando la cabecera Content-Length.
  * Suponer formato application/x-www-form-urlencoded, p.ej.: nombre=Juan&edad=20
  * Parsear los parámetros y devolver una página HTML listando los pares clave=valor.
- Para otros métodos o rutas, responder con el estado adecuado (405 o 404).
*/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class U4P03WebServerFormEj7 {

    private static final int PORT = 8080;
    private static final String HTTP_ESTADO_OK = "200 OK";
    private static final String HTTP_ESTADO_NOT_FOUND = "404 Not Found";
    private static final String HTTP_ESTADO_METHOD_NOT_ALLOWED = "405 Method Not Allowed";

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

            int contentLength = 0;
            String headerLine;
            while ((headerLine = bf.readLine()) != null && !headerLine.isBlank()) {
                System.out.println(headerLine);
                String lower = headerLine.toLowerCase();
                if (lower.startsWith("content-length:")) {
                    String valor = headerLine.substring("Content-Length:".length()).trim();
                    try {
                        contentLength = Integer.parseInt(valor);
                    } catch (NumberFormatException e) {
                        contentLength = 0;
                    }
                }
            }

            String estado;
            byte[] cuerpo;
            String contentType = "text/html; charset=UTF-8";

            if (metodo.equalsIgnoreCase("GET") && ruta.equals("/")) {
                estado = HTTP_ESTADO_OK;
                String html = """
                        <html>
                        <body>
                        <h1>Formulario</h1>
                        <form method="POST" action="/form">
                          Nombre: <input type="text" name="nombre"><br>
                          Edad: <input type="number" name="edad"><br>
                          <input type="submit" value="Enviar">
                        </form>
                        </body>
                        </html>
                        """;
                cuerpo = html.getBytes();
            } else if (metodo.equalsIgnoreCase("POST") && ruta.equals("/form")) {
                char[] buf = new char[contentLength];
                int leidos = 0;
                while (leidos < contentLength) {
                    int n = bf.read(buf, leidos, contentLength - leidos);
                    if (n == -1) break;
                    leidos += n;
                }
                String body = new String(buf, 0, leidos);
                System.out.println("Cuerpo recibido: " + body);

                Map<String, String> params = parseFormUrlencoded(body);
                StringBuilder sb = new StringBuilder();
                sb.append("<html><body>");
                sb.append("<h1>Datos recibidos</h1>");
                sb.append("<ul>");
                for (Map.Entry<String, String> e : params.entrySet()) {
                    sb.append("<li>").append(e.getKey()).append(" = ").append(e.getValue()).append("</li>");
                }
                sb.append("</ul>");
                sb.append("<a href=\"/\">Volver al formulario</a>");
                sb.append("</body></html>");

                estado = HTTP_ESTADO_OK;
                cuerpo = sb.toString().getBytes();
            } else {
                estado = ruta.equals("/") || ruta.equals("/form") ? HTTP_ESTADO_METHOD_NOT_ALLOWED : HTTP_ESTADO_NOT_FOUND;
                String html = "<html><body><h1>" + estado + "</h1></body></html>";
                cuerpo = html.getBytes();
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

    private static Map<String, String> parseFormUrlencoded(String body) {
        Map<String, String> map = new HashMap<>();
        String[] pares = body.split("&");
        for (String par : pares) {
            String[] kv = par.split("=", 2);
            String k = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String v = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            map.put(k, v);
        }
        return map;
    }
}
