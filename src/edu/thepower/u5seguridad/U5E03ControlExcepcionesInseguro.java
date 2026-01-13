package edu.thepower.u5seguridad;
import java.io.BufferedReader;

import java.io.FileReader;

import java.io.IOException;

import java.util.Scanner;



/**

 * CE3 (INSEGURO) - Control de errores con fugas de información

 * -----------------------------------------------------------

 * OBJETIVO:

 *  Simular una "API" por consola con operaciones que pueden fallar:

 *   1) Leer config.txt

 *   2) Parsear JSON (simulado)

 *   3) Conectar a BD (simulado)

 *

 * QUÉ PROBLEMA DE SEGURIDAD MUESTRA:

 *  - Ante errores, se imprime el stack trace (e.printStackTrace()).

 *  - Esto filtra información interna: rutas, clases, estructura del proyecto, etc.

 *

 * CÓMO SE HA DESARROLLADO:

 *  - main lee una opción y ejecuta la operación dentro de try/catch genérico.

 *  - En caso de excepción, se imprime el stack trace al usuario (MAL).

 */

public class U5E03ControlExcepcionesInseguro {



    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);



        System.out.println("=== U5E03 INSEGURO: API con fugas de info ===");

        System.out.println("1) Leer fichero config.txt");

        System.out.println("2) Parsear JSON (muy simple)");

        System.out.println("3) Conectar a BD (simulada)");

        System.out.print("> ");



        try {

            int option = Integer.parseInt(sc.nextLine().trim());



            // En inseguro, hacemos todo aquí directamente (y con mala gestión de errores)

            if (option == 1) {

                // Provoca error si config.txt no existe

                System.out.println("Leyendo config.txt...");

                String content = readConfigInsecure("config.txt");

                System.out.println("CONFIG: " + content);



            } else if (option == 2) {

                System.out.print("JSON: ");

                String json = sc.nextLine();



                // Parse "falso": si no empieza por { -> excepción

                if (!json.trim().startsWith("{")) {

                    throw new IllegalArgumentException("JSON inválido: debe empezar con '{'");

                }

                System.out.println("JSON OK (simulado)");



            } else if (option == 3) {

                // Simula BD caída

                throw new IOException("No se puede conectar a la BD en localhost:5432");



            } else {

                throw new IllegalArgumentException("Opción inválida");

            }



        } catch (Exception e) {

            // MAL: fuga total de información interna

            e.printStackTrace();

        } finally {

            sc.close();

        }

    }



    private static String readConfigInsecure(String filename) throws IOException {

        // Nota: aunque usemos try-with-resources correctamente,

        // el problema de seguridad del ejercicio es el stack trace al usuario.

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            return br.readLine(); // leemos solo primera línea para simplificar

        }

    }

}
