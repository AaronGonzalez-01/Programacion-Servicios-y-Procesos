package edu.thepower.u3comunicacionred;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class U3P00EchoServer {
    public static void main(String[] args) {
        int puerto = 0;
        try {
            puerto = Validacion.validarPuerto(args);
        }catch (Exception e){
            System.err.println("Error en el formato del puerto: " + e.getMessage());
            System.exit(1);
        }
        try(ServerSocket servidor = new ServerSocket(puerto);){
            System.out.println("Servidor Iniciado; esperando connection con el puerto: " + puerto);
            Socket socket = servidor.accept();
            System.out.println("Cliente conectado:" +  socket.getInetAddress() + ":" + socket.getPort());

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            PrintWriter pw = new PrintWriter(out, true);


            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println("Recibido de cliente: " + line);
                pw.println(line.toLowerCase());
            }
        }catch (IOException e){
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
