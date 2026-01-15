package edu.thepower.u5seguridad;

import java.util.HashMap;

import java.util.Map;

import java.util.Scanner;



/**

 * OBJETIVO (INSEGURO):

 *  - Mostrar un login/roles MAL implementado:

 *    - Passwords en texto plano.

 *    - Mensajes que revelan si el usuario existe.

 *    - Autorización inexistente o débil.

 *

 * CÓMO SE HA DESARROLLADO:

 *  - Base de usuarios en memoria con password en claro.

 *  - Menú que deja ejecutar acciones sin comprobar rol correctamente.

 */

public class U5E04AutenticacionAutorizacionInseguro {



    static class User {

        String username;

        String passwordPlain; // MAL: en claro

        String role;          // "ADMIN" o "USER"

        User(String u, String p, String r) { username=u; passwordPlain=p; role=r; }

    }



    public static void main(String[] args) {

        Map<String, User> users = new HashMap<>();

        users.put("admin", new User("admin", "Admin123!", "ADMIN"));

        users.put("ana", new User("ana", "Ana123!!aa", "USER"));



        Scanner sc = new Scanner(System.in);



        System.out.println("=== CE4 INSEGURO: Login + Roles (mal) ===");

        System.out.print("Usuario: ");

        String u = sc.nextLine();

        System.out.print("Password: ");

        String p = sc.nextLine();



        // MAL: revela existencia del usuario

        if (!users.containsKey(u)) {

            System.out.println("El usuario NO existe.");

            sc.close();

            return;

        }



        User user = users.get(u);



        // MAL: compara password en claro

        if (!user.passwordPlain.equals(p)) {

            System.out.println("Password incorrecta.");

            sc.close();

            return;

        }



        System.out.println("Login OK. Rol=" + user.role);



        // MAL: menú sin autorización real

        System.out.println("1) Ver perfil");

        System.out.println("2) Ver lista de usuarios (debería ser ADMIN)");

        System.out.println("3) Apagar servicio (debería ser ADMIN)");

        System.out.print("> ");

        int opt = Integer.parseInt(sc.nextLine());



        if (opt == 1) {

            System.out.println("Perfil de " + user.username + " (rol=" + user.role + ")");

        } else if (opt == 2) {

            // MAL: no comprueba rol

            System.out.println("Usuarios: " + users.keySet());

        } else if (opt == 3) {

            // MAL: no comprueba rol

            System.out.println("Servicio apagado (simulado).");

        } else {

            System.out.println("Opción inválida.");

        }



        sc.close();

    }

}
