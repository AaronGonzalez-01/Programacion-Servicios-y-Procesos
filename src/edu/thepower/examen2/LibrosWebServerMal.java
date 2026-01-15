package edu.thepower.examen2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class LibrosWebServerMal {

    private static int contador = 0;

    private static Map<String, String> libros = new HashMap<String, String>();


    static {
        libros.put("El Quijote", "Miguel de Cervantes");
        libros.put("Cien años de soledad", "    Gabriel García Márquez");
        libros.put("1984", "George Orwell");
        libros.put("Pantaleón y las visitadoras", "Mario Vargas Llosa");
        libros.put("Dune", "Frank Herbert");
    }
    private static final String HTML_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="es">
            <html>
                %s
            </html>
            """;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Servidor Web escuchando en http://localhost:8080");

            while (true) {
                Socket socket = server.accept();
                new Thread(() -> atenderPeticion(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void atenderPeticion(Socket socket) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(socket.getOutputStream())) {

            String linea = br.readLine();
            if (linea != null && !linea.isEmpty()) {
                System.out.println("Petición recibida: " + linea);
                String[] partes = linea.split("\\s+");
                String metodo = partes[0];
                String ruta = partes[1];

                while (br.ready() && (linea = br.readLine()) != null && !linea.isEmpty()) {
                }

                String contenidoCuerpo = "";
                String estado = "200 OK";

                if (metodo.equals("GET")) {
                    switch (ruta) {
                        case "/":
                            contenidoCuerpo = """
                                    <head>
                                     <title>Catálogo de libros</title>
                                    </head>
                                    <body>
                                     <h1>Catálogo de libros</h1>
                                     <p>Opciones disponibles:</p>
                                     <ul>
                                     <li><a href="/libros">Ver lista completa de libros</a></li>
                                     <li><a href="/libros_total">Número total de libros</a></li>
                                     </ul>
                                    """;
                            break;
                        case "/libros":
                            StringBuffer sb = new StringBuffer();
                            for (Map.Entry<String, String> entry : libros.entrySet()) {
                                sb.append("<li><a>"+ entry.getKey()+ " - " + entry.getValue() + "</a></li>");
                            }
                            String contenidoAux = """
                                    <head>
                                     <meta charset="UTF-8">
                                     <title>Lista de libros</title>
                                    </head>
                                    <body>
                                     <h1>Lista de libros</h1>
                                     <ul>
                                     %s
                                     </ul>
                                     <p><a href="/">Volver al inicio</a></p>
                                """;
                            contenidoCuerpo = String.format(contenidoAux, sb.toString());
                            break;
                        case "/libros_total":
                            for (Map.Entry<String, String> entry : libros.entrySet()) {
                                contador++;
                            }
                            contenidoCuerpo = "<p>Número total de libros: " + contador + " <a href=\"/\">Volver al inicio</a></p>";
                            break;
                        default:
                            estado = "404 Not Found";
                            contenidoCuerpo = "<h2 style='color:red'>Error 404: Página no encontrada</h2>";
                    }
                } else {
                    estado = "405 Method Not Allowed";
                    contenidoCuerpo = "Método no permitido";
                }

                enviarRespuestaHTTP(pw, estado, contenidoCuerpo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void enviarRespuestaHTTP(PrintWriter pw, String estado, String cuerpoMensaje) {
        String htmlFinal = String.format(HTML_TEMPLATE, cuerpoMensaje);

        pw.println("HTTP/1.1 " + estado);
        pw.println("Content-Type: text/html; charset=UTF-8");
        pw.println("Content-Length: " + htmlFinal.getBytes().length);
        pw.println();
        pw.println(htmlFinal);
        pw.flush();
    }
}
