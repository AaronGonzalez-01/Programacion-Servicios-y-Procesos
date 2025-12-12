package edu.thepower.u3comunicacionred;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class U3P04ServerContadorAgain {

    private static AtomicInteger contador = new AtomicInteger(0);

    static class GestorClientesContador implements Runnable{
        private Socket socket;
        private String cliente;
        private static final String CONTADOR_ACTUALIZADO = "Contador actualizado. ";
        private static final String VALOR_CONTADOR = "Valor: ";
        public GestorClientesContador(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            cliente = socket.getInetAddress() + ":" + socket.getPort();
            System.out.println("[" + Thread.currentThread().getName() + "] IP: " + cliente);
            try(
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)){
                String comando;
                String respuesta;
                boolean continuar = true;
                while (continuar && (comando = br.readLine()) != null){
                    respuesta = switch (comando.trim().toLowerCase()){
                        case "inc" -> CONTADOR_ACTUALIZADO + VALOR_CONTADOR + incrementarContador();
                        case "dec" -> CONTADOR_ACTUALIZADO + VALOR_CONTADOR + decrementarContador();
                        case "get" -> VALOR_CONTADOR + getContador();
                        case "bye" -> {
                            continuar = false;
                            yield "Bye";
                        }
                        default -> "Comando no válido";
                    };
                    pw.println(respuesta);
                }


            }catch (IOException e){
                System.err.println("Error en la conexión: " + e.getMessage());
            }
            System.out.println("Conexión finalizada de " + cliente);
        }
    }

    public static int getContador(){
        return contador.get();
    }

    public static int incrementarContador(){
        return contador.incrementAndGet();
    }

    public static int decrementarContador(){
        return contador.decrementAndGet();
    }

    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(4000)){
            System.out.println("Servidor escuchando peticiones en el puerto: 4000");
            //Blucle while para atender todas las solicitudes a un thread
            while (true) {
                Socket socket = serverSocket.accept();
                //Instanciamos un thread utilizando la clase GestorClientesContador a la que le pasamos el socket como argumento
                Thread thread = new Thread(new GestorClientesContador(socket));
                thread.start();
            }

        }catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }



}
