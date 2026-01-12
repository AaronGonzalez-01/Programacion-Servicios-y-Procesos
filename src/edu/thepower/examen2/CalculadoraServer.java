package edu.thepower.examen2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculadoraServer {

    private static double num1;
    private static double num2;

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(6000)){
            System.out.println("Servidor esperando conexiones: " + serverSocket.getLocalPort());
            while(true){
                Socket socket = serverSocket.accept();
                Thread hilo = new Thread(() -> gestionarCliente(socket));
                hilo.start();
            }
        }catch (IOException e){
            System.err.println("Error en el servidor" + e.getMessage());
        }
    }

    private static void gestionarCliente(Socket socket){
        try(
                BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)
        ){
            String comando;
            String respuesta;

            while((comando = bf.readLine()) != null){
                String[] comandoDividido =  comando.split("\\s+", 3);
                respuesta = devolverRespuesta(comandoDividido);
                pw.println(respuesta);
                if (comandoDividido[0].equalsIgnoreCase("exit")) {
                    break;
                }
            }

        }catch (IOException e){
            System.err.println("Error al gestionar el cliente" + e.getMessage());
        }
    }

    private static String devolverRespuesta(String[] comando){
        if(comando[0].equalsIgnoreCase("help")){
            return "comando <número1> <número2>";
        }
        if(comando.length < 3){
            return "Formato del comando incorrecto, introduzca HELP para mas información";
        }
        try {
            num1 = Integer.parseInt(comando[1]);
            num2 = Integer.parseInt(comando[2]);
        }catch (NumberFormatException e){
            return "Solo se permiten números reales";
        }
        return switch (comando[0].trim().toLowerCase()){
            case "sum" -> {
                double resultado = num1 + num2;
                yield "Resultado de la suma: " + resultado;

            }
            case "res" -> {
                double resultado = num1 - num2;
                yield "Resultado de la resta: " + resultado;

            }
            case "mul" -> {
                double resultado = num1 * num2;
                yield "Resultado de la multiplicación: " + resultado;

            }
            case "div" -> {
                if (num2 == 0){
                    yield "No se puede dividir por cero";
                }else{
                    double resultado = num1 / num2;
                    yield "Resultado de la division: " + resultado;
                }
            }

            default -> "Comando invalido";
        };
    }
}
