package edu.thepower.u5seguridad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class U5E03ControlExcepcionesSegura {

    private static final Logger LOG = Logger.getLogger(U5E03ControlExcepcionesSegura.class.getName());

    private static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        System.out.println("=== U5E03 SEGURO: API con fugas de info ===");
        System.out.println("1) Leer fichero config.txt");
        System.out.println("2) Parsear JSON (muy simple)");
        System.out.println("3) Conectar a BD (simulada)");
        System.out.print("> ");

        int option = 0;

        try {
            option = Integer.parseInt(sc.nextLine().trim());
            System.out.println("OK " + procesarOpcion(option));

        } catch (NumberFormatException e) {
            System.err.println("Opción no permitida");
        } catch (IOException e) {
            System.err.println("ERROR [ERR_ES] No se puede acceder al recurso");
            LOG.log(Level.WARNING, "Error al localizar el recurso " + option + ": " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR [ERR_INPUT] Solicitud invalida");
            LOG.log(Level.INFO, "Error al localizar recurso " + option + ": " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("ERROR [ERR_UNKNOWN] Error desconocido");
            LOG.log(Level.SEVERE, "Error inesperado en la ejecucion " + option + ": " + e.getMessage(), e );
        }
        sc.close();
    }

    private static String readConfigInsecure(String filename) throws IOException {

        // Nota: aunque usemos try-with-resources correctamente,

        // el problema de seguridad del ejercicio es el stack trace al usuario.

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            return br.readLine(); // leemos solo primera línea para simplificar

        }

    }

    private static String procesarOpcion(int option) throws IOException {
        String respuesta;
        if (option == 1) {

            String content = readConfigInsecure("config.txt");
            if  (content == null || content.isBlank()) {
                respuesta = "Archivo vacío";
            } else {
                respuesta = "Archivo leído";
            }


        } else if (option == 2) {

            System.out.print("JSON: ");

            String json = sc.nextLine();

            // Parse "falso": si no empieza por { -> excepción

            if (!json.trim().startsWith("{")) {

                throw new IllegalArgumentException("JSON inválido: debe empezar con '{'");

            }

            respuesta = "JSON OK (simulado)";



        } else if (option == 3) {

            // Simula BD caída

            throw new IOException("No se puede conectar a la BD");



        } else {

            throw new IllegalArgumentException("Opción inválida");

        }
        return respuesta;
    }
}
