package edu.thepower.u3comunicacionred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class U3P05ClientDiccionario {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4000);
             BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

            String msg;
            do {
                System.out.print("Introduzca un comando (trd <palabra>/inc <palabra> <traducción>/lis/sal(bye): ");
                msg = sc.nextLine().trim();
                pw.println(msg);
                String respuesta = bf.readLine();
                System.out.println("Respuesta del servidor: " + respuesta);
            } while (!msg.equalsIgnoreCase("bye") || !msg.equalsIgnoreCase("sal"));

        } catch (IOException e) {
            System.err.println("Error en cliente: " + e.getMessage());
        }
        System.out.println("Comunicación finalizada");
    }
}
