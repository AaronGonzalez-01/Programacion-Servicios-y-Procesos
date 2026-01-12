package edu.thepower.u5seguridad;

import java.util.Scanner;



/**

 * OBJETIVO (INSEGURO):

 *  - Registrar un usuario sin validar nada.

 *  - Mostrar el patrón peligroso: aceptar cualquier entrada y "confiar" en ella.

 *

 * CÓMO SE HA DESARROLLADO:

 *  - Se piden 4 campos y se "guardan" sin restricciones.

 *  - Esto facilita inyecciones, datos basura, DoS por tamaño y contraseñas débiles.

 */

public class U5E02ValidacionEntradaDatosInseguro {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);



        System.out.println("=== CE2 INSEGURO: Registro sin validación ===");



        System.out.print("Username: ");

        String username = sc.nextLine();



        System.out.print("Edad: ");

        String age = sc.nextLine();



        System.out.print("Email: ");

        String email = sc.nextLine();



        System.out.print("Password: ");

        String password = sc.nextLine();



        // MAL: imprime datos sensibles y acepta cualquier cosa

        System.out.println("\n[REGISTRO OK - INSEGURO]");

        System.out.println("username=" + username);

        System.out.println("edad=" + age);

        System.out.println("email=" + email);

        System.out.println("password=" + password);



        sc.close();

    }

}
