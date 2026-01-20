package edu.thepower.u5seguridad;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class U5E04AutenticacionAutorizacionSegura {

    enum Rol {
        USER, ADMIN
    }

    static class User {
        private static final Random RANDOM = new Random();
        String username;
        int passwordValue;
        int salt;
        Rol rol;          // "ADMIN" o "USER"
        boolean blocked;
        int contador = 0;

        public User(String username, String password, Rol rol) {
            this.username = username;
            this.rol = rol;
            this.salt = getSalt();
            this.passwordValue = getPasswordValue(password, salt);
            this.blocked = false;
        }

        private static int getSalt() {
            return RANDOM.nextInt(10_000);
        }

        private static int getPasswordValue(String password, int salt) {
            return (password + salt).hashCode();
        }

        private boolean checkPassword(String password) {
            return passwordValue == getPasswordValue(password, salt);
        }
    }

    static class Sesion {
        String username;
        Rol rol;

        public Sesion(String username, Rol rol) {
            this.username = username;
            this.rol = rol;
        }
    }

    static class AutenticacionYValidacion {
        private final String CREDENCIALES_INCORRECTAS = "Credenciales incorrectas";
        Map<String, User> users;

        public AutenticacionYValidacion(Map<String, User> users) {
            this.users = users;
        }

        public Sesion login(String username, String password) {
            final int MAX_INTENTOS = 2;
            Sesion sesion = null;
            User user = users.get(username);
            if (user != null) {
                if (!user.blocked) {
                    if (user.checkPassword(password)) {
                        user.contador = 0;
                        sesion = new Sesion(username, user.rol);
                    } else {
                        user.contador++;
                        if (user.contador > MAX_INTENTOS) {
                            user.blocked = true;
                            System.out.println("Usuario bloqueado");
                        }
                        System.out.println(CREDENCIALES_INCORRECTAS);
                    }
                } else{
                    System.out.println("El usuario esta bloqueado. Contacta con un administrador");
                }
            } else {
                System.out.println(CREDENCIALES_INCORRECTAS);
            }
            return sesion;
        }

        public boolean validarPermisos(Sesion sesion, Rol rolRequerido) {
            boolean validado = false;
            if (rolRequerido.equals(Rol.USER)) {
                validado = true;
            } else {
                validado = sesion.rol.equals(Rol.ADMIN);
            }
            return validado;
        }
    }


    public static void main(String[] args) {

        Map<String, User> users = new HashMap<>();

        users.put("admin", new User("admin", "Admin123!", Rol.ADMIN));
        users.put("ana", new User("ana", "Ana123!!aa", Rol.USER));

        Scanner sc = new Scanner(System.in);

        AutenticacionYValidacion autenticacion = new AutenticacionYValidacion(users);

        Sesion sesion = null;

        System.out.println("=== U5E04 SEGURO: Login + Roles ===");

        while (sesion == null) {
            System.out.print("Usuario: ");

            String u = sc.nextLine();

            System.out.print("Password: ");

            String p = sc.nextLine();

            sesion = autenticacion.login(u, p);
        }


        System.out.println("Login OK. Rol=" + sesion.rol);

        System.out.println("1) Ver perfil");

        System.out.println("2) Ver lista de usuarios (debería ser ADMIN)");

        System.out.println("3) Apagar servicio (debería ser ADMIN)");

        System.out.print("> ");

        try {
            int opt = Integer.parseInt(sc.nextLine());

            if (opt == 1) {

                System.out.println("Perfil de " + sesion.username + " (rol=" + sesion.rol + ")");

            } else if (opt == 2) {

                if (autenticacion.validarPermisos(sesion, Rol.ADMIN)) {
                    System.out.println("Usuarios: " + users.keySet());
                } else {
                    System.out.println("No tienes los permisos para ejecutar esta operación");
                }

            } else if (opt == 3) {

                if (autenticacion.validarPermisos(sesion, Rol.ADMIN)) {
                    System.out.println("Servicio apagado (simulado).");
                } else {
                    System.out.println("No tienes los permisos para ejecutar esta operación");
                }

            } else {

                System.out.println("Opción inválida.");

            }
        } catch (NumberFormatException e) {
            System.out.println("Opción no valida");
        }
        sc.close();
    }
}

