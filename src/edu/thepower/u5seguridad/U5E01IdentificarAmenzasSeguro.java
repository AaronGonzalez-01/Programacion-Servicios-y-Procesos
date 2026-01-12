package edu.thepower.u5seguridad;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class U5E01IdentificarAmenzasSeguro {

    private static final int LONGITUD_MAXIMA = 100;
    private static final Set<String> COMANDOS_PERMITIDOS = Set.of("list", "read");
    private static final Path DIRECTORIO_BASE = Path.of("resources").toAbsolutePath();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);


        System.out.println("=== U5E01 SEGURO: Servicio de eco (con validación) ===");

        System.out.println("Escribe una petición tipo: list  | read <ruta>");

        System.out.print("> ");


        String request = sc.nextLine();

        //1. Evitar ataques DoS limitando longitud de la solicitud recibida

        if (request.length() > LONGITUD_MAXIMA) {
            System.out.println("Longitud superada");
            sc.close();
            System.exit(1);
        }

        // 2. Evitamos logs falsos (log forging) sustituyendo caracteres de control por espacios en blanco

        System.out.println("[LOG] Request recibida: " + formatear(request));
        //Simulación de log forging
        //System.out.println("[LOG] Request recibida: " + "list\n [LOG] Request recibida: resultado OK");


        String[] parts = request.trim().split("\\s+", 2);

        String cmd = parts.length > 0 ? parts[0].toLowerCase() : "";

        String arg = parts.length == 2 ? parts[1] : "";


        // 3. Solo permitimos list y read

        if(!COMANDOS_PERMITIDOS.contains(cmd)) {
            System.out.println("[ERROR] Comando invalido");
            sc.close();
            System.exit(1);
        }

        if ("list".equalsIgnoreCase(cmd)) {

            System.out.println("OK: listando recursos..." + DIRECTORIO_BASE + " (simulado)");

        } else if ("read".equalsIgnoreCase(cmd)) {

            // 4. Corregimos el path traversal
            if (arg.isBlank()) {
                System.out.println("[ERROR] Argumento invalido");
            }else{
                Path ruta = DIRECTORIO_BASE.resolve(arg).normalize();
                if (!ruta.startsWith(DIRECTORIO_BASE)) {
                    System.out.println("[ERROR] Acceso no permitido");
                }else {
                    System.out.println("OK: leyendo fichero: " + arg + " (simulado)");
                }
            }


        }


        sc.close();

    }

    private static String formatear(String request) {
        return request.replaceAll("\\p{Cntrl}", " ");
    }

}
