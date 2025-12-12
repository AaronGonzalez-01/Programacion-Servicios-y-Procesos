package edu.thepower.u3comunicacionred;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class GestorCliente implements Runnable{

    private Socket socket;

    public GestorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String nombre = "[" + Thread.currentThread().getName() + "]";
        System.out.println(nombre + " Cliente conectado:" + socket.getInetAddress() + ":" + socket.getPort());
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            PrintWriter pw = new PrintWriter(out, true);


            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println(nombre +" Recibido de cliente: " + line);
                pw.println(line.toLowerCase());
            }
        }catch (IOException e){
            System.err.println(nombre + " Error en la conección del servidor:" + e.getMessage());
        }
        System.out.println("Cliente desconectado:" + socket.getInetAddress() + ":" + socket.getPort());
    }
}

public class U3P00MultiClientEchoServer {
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
            while(true) {
                Socket socket = servidor.accept();

                Thread t = new Thread(new GestorCliente(socket));
                t.start();
            }
        }catch (IOException e){
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
