package edu.thepower.examen2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CalculadoraClient {
    public static void main(String[] args) {
        try(
                Socket socket = new Socket("127.0.0.1", 6000);
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                Scanner sc = new Scanner(System.in)
        ) {
            String msg;
            do{
                System.out.println("Ingrese un comando: SUM, RES, MUL, DIV, FIN");
                msg = sc.nextLine().trim();
                pw.println(msg);
                String respuesta = br.readLine();
                System.out.println("Respuesta del servidor: " + respuesta);
            }while(!msg.equalsIgnoreCase("fin"));
        }catch (IOException e){
            System.err.println("No se pudo establecer conexión con el servidor" + e.getMessage());
        }
        System.out.println("Conexión finalizada con el servidor");
    }
}
