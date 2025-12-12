package edu.thepower.u3comunicacionred;

import java.io.*;
import java.net.Socket;

public class U3P01EjemploSoket {
    public static void main(String[] args) {
        try(Socket socket = new Socket("whois.internic.net", 43)){
            //Definimos el stream de salida hacia el whois
            OutputStream os = socket.getOutputStream();
            //Clase que nos ayuda a escribir caracteres
            PrintWriter pw = new PrintWriter(os, true);
            //Stream de entrada
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //Enviamos del dominio
            pw.println("miservidorprivado.com");
            String line = null;
            //Leer información
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error al conectarse con el servidor");
        }
    }
}
