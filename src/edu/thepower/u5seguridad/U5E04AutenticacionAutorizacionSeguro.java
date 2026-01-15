package edu.thepower.u5seguridad;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

import java.util.Random;
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

public class U5E04AutenticacionAutorizacionSeguro {

    enum Rol {
        USER,ADMIN
    }

    static class User {

        private static final Random RANDOM = new Random();

        String username;

        int passwordValor;

        int salt;

        Rol role;          // "ADMIN" o "USER"

        public User(String username, String password, Rol role) {
            this.username = username;
            this.role = role;
            this.salt = getSalt();
            this.passwordValor = getPasswordValor(password, salt);
        }

        private static int getSalt(){
            return RANDOM.nextInt(10000);
        }
        private static int getPasswordValor(String password,  int salt) {
            return (password + salt).hashCode();
        }
        private boolean checkPassword(String password) {
            return passwordValor == getPasswordValor(password, salt);
        }

    }

    static class Sesion{
        String username;
        Rol role;

        public Sesion(String username, Rol role) {
            this.username = username;
            this.role = role;
        }
    }

    static class AutenticacionYValidacion{
        private final String CREDENCIALES_INCORRECTAS = "Credenciales incorrectas";
        Map<String, User> users;
        public AutenticacionYValidacion(Map<String, User> users) {
            this.users = users;
        }
        public Sesion login(String username, String password){
            Sesion sesion = null;
            User user = users.get(username);
            if(user != null){
                if(user.checkPassword(password)){
                    sesion = new Sesion(username, user.role);
                } else{
                    System.out.println(CREDENCIALES_INCORRECTAS);
                }
            } else {
                System.out.println(CREDENCIALES_INCORRECTAS);
            }
            return sesion;
        }
        public boolean validarPermisos(Sesion sesion, Rol rolRequerido){
            boolean permitido = false;
            if(rolRequerido == Rol.USER){
                permitido = true;
            } else{
                permitido = sesion.role == Rol.ADMIN;
            }
            return permitido;
        }
    }

    public static void main(String[] args) {

        Map<String, User> users = new HashMap<>();

        users.put("admin", new User("admin", "Admin123!", Rol.ADMIN));

        users.put("ana", new User("ana", "Ana123!!aa", Rol.USER));

        AutenticacionYValidacion autenticacion = new AutenticacionYValidacion(users);

        Scanner sc = new Scanner(System.in);

        Sesion sesion = null;

        System.out.println("=== SEGURO: Login + Roles ===");

        while(sesion == null){
            System.out.print("Usuario: ");

            String u = sc.nextLine();

            System.out.print("Password: ");

            String p = sc.nextLine();

            sesion = autenticacion.login(u, p);


        }

        System.out.println("Login OK. Rol=" + sesion.role);


        // MAL: menú sin autorización real

        System.out.println("1) Ver perfil");

        System.out.println("2) Ver lista de usuarios (debería ser ADMIN)");

        System.out.println("3) Apagar servicio (debería ser ADMIN)");

        System.out.print("> ");

        try {
            int opt = Integer.parseInt(sc.nextLine());
            if (opt == 1) {

                System.out.println("Perfil de " + sesion.username + " (rol=" + sesion.role + ")");

            } else if (opt == 2) {

                if(autenticacion.validarPermisos(sesion, Rol.ADMIN)){
                    System.out.println("Usuarios: " + users.keySet());
                } else{
                    System.out.println("No tienes permisos para ejecutar esta operacion");
                }



            } else if (opt == 3) {

                if(autenticacion.validarPermisos(sesion, Rol.ADMIN)){
                    System.out.println("Servicio apagado (simulado).");
                } else{
                    System.out.println("No tienes permisos para ejecutar esta operacion");
                }


            } else {

                System.out.println("Opción inválida.");

            }
        } catch (NumberFormatException e) {
            System.err.println("Introduzca una opcion valida");
        }

        sc.close();

    }
    // Numero de intentos para el login Max = 3, si falla bloquea el usuario y no puede acceder
    // en el susuario la marca si esta bloqueado o no
    //
}
