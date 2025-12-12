package edu.thepower.u3comunicacionred;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class U3P00EchoClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8080)){
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            PrintWriter pw = new PrintWriter(out, true);
            Scanner sc = new Scanner(System.in);
            String msg;
            do{
                System.out.println("Intruzca un texto: ");
                msg = sc.nextLine().trim();
                pw.println(msg);
                System.out.println("Devuelto por el servidor: " + bf.readLine());
            }while(!msg.equalsIgnoreCase("/salir"));

        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        System.out.println("Comunicación finalizada");
    }
}
