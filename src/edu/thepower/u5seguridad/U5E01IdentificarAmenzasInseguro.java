package edu.thepower.u5seguridad;

import java.util.Scanner;



/**

 * OBJETIVO (INSEGURO):

 *  - Simular un "servicio" que recibe una petición por consola.

 *  - Mostrar errores típicos: no valida comando, no limita longitud, no controla rutas,

 *    y registra en logs tal cual la entrada del usuario.

 *

 * CÓMO SE HA DESARROLLADO:

 *  - Se lee una línea y se "interpreta" como: comando + argumento.

 *  - Se imprime y se actúa sin validación (esto sería peligroso si se llamara a Runtime.exec()).

 *

 * IMPORTANTE:

 *  - No ejecutamos comandos reales por seguridad, pero este patrón es el que lleva a inyecciones.

 */

public class U5E01IdentificarAmenzasInseguro {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);



        System.out.println("=== CE1 INSEGURO: Servicio de eco (sin validación) ===");

        System.out.println("Escribe una petición tipo: list  | read <ruta>  | (cualquier cosa)");

        System.out.print("> ");



        String request = sc.nextLine();



        // MAL: log directo; permite log forging (saltos de línea, etc.)

        System.out.println("[LOG] Request recibida: " + request);



        // MAL: sin control de longitud: entrada enorme puede afectar rendimiento/logs.

        String[] parts = request.trim().split("\\s+", 2);

        String cmd = parts.length > 0 ? parts[0] : "";

        String arg = parts.length == 2 ? parts[1] : "";



        // MAL: cmd puede ser cualquier cosa; en un sistema real se acabaría en exec()

        if ("list".equalsIgnoreCase(cmd)) {

            System.out.println("OK: listando recursos... (simulado)");

        } else if ("read".equalsIgnoreCase(cmd)) {

            // MAL: se acepta cualquier ruta (path traversal)

            System.out.println("OK: leyendo fichero: " + arg + " (simulado)");

        } else {

            // MAL: se procesan comandos desconocidos

            System.out.println("OK: ejecutando comando: " + cmd + " con arg: " + arg + " (simulado)");

        }



        sc.close();

    }

}
