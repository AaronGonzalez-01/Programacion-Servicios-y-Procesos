package edu.thepower.u3comunicacionred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class U3P04ClientContadorAgain {
    private static final String HOST = "localhost";
    private static final int PORT = 4000;

    public static void main(String[] args) {
        try(Socket socket = new Socket(HOST, PORT);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)){

            System.out.println("Conectado con el servidor");

            Scanner sc = new Scanner(System.in);
            String comando;
            do{
                System.out.print("Introduzca un comando (inc/dec/get/bye): ");
                comando = sc.nextLine();
                pw.println(comando);
                System.out.println(br.readLine());
            }while (!comando.trim().equalsIgnoreCase("bye"));
        }catch (IOException e){
            System.err.println("Error en la conexión: " + e.getMessage());
        }

    }
}
