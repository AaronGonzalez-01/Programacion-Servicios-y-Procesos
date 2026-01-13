package edu.thepower.u5seguridad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class U5E03ControlExcepcionesSegura {

    private static final Logger LOG = Logger.getLogger(U5E03ControlExcepcionesSegura.class.getName());
    static Scanner sc;

    public static void main(String[] args) {

        sc = new Scanner(System.in);

        System.out.println("=== U5E03 INSEGURO: API con fugas de info ===");
        System.out.println("1) Leer fichero config.txt");
        System.out.println("2) Parsear JSON (muy simple)");
        System.out.println("3) Conectar a BD (simulada)");
        System.out.print("> ");

        int option = 0;
        try {
            option = Integer.parseInt(sc.nextLine());
            System.out.println("Ok" + procesarOpcion(option));
        } catch (IOException e) {
            System.err.println("ERROR [ERR_ES] Recursos no existe");
            LOG.log(Level.WARNING, "Error al localizar recursos " + option + e.getMessage());
        } catch (Exception e) {
            System.err.println("opcion no valida");
            LOG.log(Level.INFO, "Error en la solicitud " + option + e.getMessage());
        }

        sc.close();
    }

    private static String readConfigInsecure(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            return br.readLine();
        }
    }

    private static String procesarOpcion(int opcion) throws IOException {
        String respuesta;
        if (opcion == 1) {
            String content = readConfigInsecure("config.txt");
            if (content == null || content.isBlank()) {
                respuesta = "Archivo vacio";
            } else {
                respuesta = "Configuracion leida";
            }
        } else if (opcion == 2) {
            System.out.print("JSON: ");
            String json = sc.nextLine();
            if (!json.trim().startsWith("{")) {
                throw new IllegalArgumentException("JSON inválido: debe empezar con '{'");
            }
            respuesta = "JSON OK (simulado)";
        } else if (opcion == 3) {
            throw new IOException("No se puede conectar a la BD en localhost:5432");
        } else {
            throw new IllegalArgumentException("Opción inválida");
        }
        return respuesta;
    }
}
