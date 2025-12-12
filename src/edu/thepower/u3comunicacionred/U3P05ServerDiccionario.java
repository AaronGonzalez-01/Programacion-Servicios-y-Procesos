package edu.thepower.u3comunicacionred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class U3P05ServerDiccionario {
    private static Map<String,String> diccionario = Collections.synchronizedMap(new TreeMap<String,String>());

    static{
        String[] claves = {"house", "happy", "red", "monkey", "hello"};
        String[] valores = {"casa", "feliz", "rojo", "mono", "hola"};
        for(int i=0; i<claves.length; i++){
            diccionario.put(claves[i],valores[i]);
        }
    }

    public void main(String[] args) {
        try(ServerSocket server = new ServerSocket(4000)){
            System.out.println("Servidor esperando conexiones: " + server.getLocalPort());
            while(true){
                Socket socket = server.accept();
                Thread hilo = new Thread(() -> gestionarCliente(socket));
                hilo.start();
            }
        }catch (IOException e){
            System.err.println("Error en el servidor" +  e.getMessage());
        }
    }

    private static void gestionarCliente(Socket socket){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true)){

            String comando;
            String respuesta;
            while((comando = br.readLine()) != null){
                String[] partes = comando.split("\\s+",3);
                respuesta = switch (partes[0].trim().toLowerCase()){
                    case "trd" -> {
                        if (partes.length > 1){
                            yield diccionario.getOrDefault(partes[1], "No existe en el diccionario");
                        }else{
                            yield "Uso: trd <palabra>";
                        }
                    }
                    case "inc" -> {
                        if (partes.length > 2){
                            diccionario.put(partes[1], partes[2]);
                            yield "Palabra insertada en el diccionario";
                        }else {
                            yield "Uso: inc <palabra> <traducción>";
                        }
                    }
                    case "lis" -> {
                        StringBuffer sb = new StringBuffer();
                        for (Map.Entry entrada :diccionario.entrySet()){
                            sb.append(entrada.getKey()).append(": ").append(entrada.getValue()).append(", ");
                        }
                        if(!sb.isEmpty())
                            sb.setLength(sb.length() - 2);
                        yield sb.toString();

                        //diccionario.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining(", "));
                    }
                    case "sal" -> "Bye";
                    default -> "Comando invalido";
                };
                pw.println(respuesta);
            }

        }catch (IOException e){
            System.err.println("Error en la conexión" +  e.getMessage());
        }
        System.out.println("El cliente ha salido");
    }

}
