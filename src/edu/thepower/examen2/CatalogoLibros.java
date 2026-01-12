package edu.thepower.examen2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CatalogoLibros {

    private static final String HTML_PRINCIPAL = """
                    <!DOCTYPE html>
                        <html lang="es">
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
                         </body>
                    </html>
            """;

    private static final String HTML_LISTA_LIBROS = """
                    <!DOCTYPE html>
                            <html lang="es">
                            <head>
                             <meta charset="UTF-8">
                             <title>Lista de libros</title>
                            </head>
                            <body>
                             <h1>Lista de libros</h1>
                             <ul>
                             <li>El Quijote -  Miguel de Cervantes</li>
                             <li>Cien años de soledad - Gabriel García Márquez</li>
                             <li>1984 - George Orwell</li>
                             <li>Pantaleón y las visitadora -  Mario Vargas Llosa</li>
                             <li>Dune -  Frank Herbert</li>
                             </ul>
                             <p><a href="/">Volver al inicio</a></p>
                            </body>
                            </html>
            
            """;
    private static final String HTML_LIBROS_TOTAL = """
                    <!DOCTYPE html>
                            <html lang="es">
                            <head>
                             <meta charset="UTF-8">
                             <title>TOTAL DE LIBROS</title>
                            </head>
                            <body>
                             <h1>Total de libros:</h1>
                            <p>%s</p>
                            </body>
                            </html>
                            """;
            
            
    private static final int PORT = 8080;
    private static final String NOMBRE_SERVIDOR = "Servidor web sencillo";
    private static final String HTTP_ESTADO_OK = "200 OK";
    private static final String HTTP_ESTADO_NOT_FOUND = "404 Not Found";
    private static final String HTTP_ESTADO_METHOD_NOT_ALLOWED = "405 Method Not Allowed";
    static String metodo;
    static String ruta;
    static String estado;

    public static void main() {
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
                metodo = partes[0].trim();
                ruta = (partes.length > 1 ? partes[1] : "/").trim();
                while ((line = bf.readLine()) != null && !line.isBlank()) {
                    System.out.println(line);
                }
                estado = HTTP_ESTADO_OK;
                

                if (metodo.equalsIgnoreCase("get")) {
                    String respuesta = "";
                    System.out.println("Devolviendo html");
                    if (ruta.equals("/libros") || ruta.equals("/libros/")) {
                        devolverRespuesta2(pw, estado, respuesta);
                    } else if (ruta.equals("/")) {
                        devolverRespuesta1(pw, estado, respuesta);
                    } //else if (ruta.equals("/libros/total") || ruta.equals("/libros/total/")) {
                        devolverRespuesta3(pw, estado, respuesta);
                    //} //else {
                        estado = HTTP_ESTADO_METHOD_NOT_ALLOWED;
                        respuesta = "Método no permitido";
                    }
                    
                }
                ;
            } catch (IOException e) {
            throw new RuntimeException(e);
        }
            if(metodo.equalsIgnoreCase("get")){
                String respuesta = "";
                System.out.println("Devolviendo html");
                respuesta = switch(ruta){
                    case "//libros_total", "/libros_total/" -> "El total de los libros es: " + sumaLibros();
                    default -> {
                        estado = HTTP_ESTADO_NOT_FOUND;
                        yield "Ruta no encontrada";
                    }
                };
            }else {
                estado = HTTP_ESTADO_METHOD_NOT_ALLOWED;
                String respuesta = "Método no permitido";
            }

    }

    private static String sumaLibros() {
        String libros[] = new String[5];
        libros[0] = "El Quijote - Miguel de Cervantes";
        libros[1] = "Cien años de soledad - Gabriel García Márquez";
        libros[2] = "1984 - George Orwell";
        libros[3] = "Pantaleón y las visitadoras – Mario Vargas Llosa";
        libros[4] = "Dune – Frank Herbert";
        int contador = 0;
        for (int i = 0; i < libros.length; i++) {
            libros[i] = libros[contador];
            contador++;
        }
        return contador + "";
    }

    private static void devolverRespuesta1(PrintWriter pw, String estado, String mensaje) {
        StringBuilder respuesta = new StringBuilder();
        respuesta.append(String.format(HTML_PRINCIPAL, mensaje));
        pw.println("HTTP/1.1 " + estado);
        pw.println("Content-Type: text/html;charset=UTF-8");
        pw.println("Content-Length: " + respuesta.toString().getBytes().length);
        pw.println();
        pw.print(respuesta);
        pw.flush();
    }

    private static void devolverRespuesta2(PrintWriter pw, String estado, String mensaje) {
        StringBuilder respuesta = new StringBuilder();
        respuesta.append(String.format(HTML_LISTA_LIBROS, mensaje));
        pw.println("HTTP/1.1 " + estado);
        pw.println("Content-Type: text/html;charset=UTF-8");
        pw.println("Content-Length: " + respuesta.toString().getBytes().length);
        pw.println();
        pw.print(respuesta);
        pw.flush();


    }
    private static void devolverRespuesta3(PrintWriter pw, String estado, String mensaje) {
        StringBuilder respuesta = new StringBuilder();
        respuesta.append(String.format(HTML_LIBROS_TOTAL, mensaje));
        pw.println("HTTP/1.1 " + estado);
        pw.println("Content-Type: text/html;charset=UTF-8");
        pw.println("Content-Length: " + respuesta.toString().getBytes().length);
        pw.println();
        pw.print(respuesta);
        pw.flush();


    }

}

