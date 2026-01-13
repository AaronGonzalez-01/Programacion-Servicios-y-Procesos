package edu.thepower.u5seguridad;

import java.util.Scanner;
import java.util.regex.Pattern;

public class U5E02ValidacionEntradaDatosSeguro {
    private static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {

        System.out.println("=== U5E02 SEGURO: Registro con validación ===");

        String username = prompt("Username:", 50);
        if(!Validador.validarUsername(username)){
            System.out.println("Usuario no valido");
            sc.close();
            System.exit(1);
        }

        String age = prompt("Edad: ", 2);
        if(Validador.validarEdad(age) == 0){
            System.out.println("Edad no valida");
            sc.close();
            System.exit(1);
        }

        String email = prompt("Email: ", 320);
        if(!Validador.validarCorreo(email)){
            System.out.println("Email no valido");
            sc.close();
            System.exit(1);
        }

        String password = prompt("Password: ", 20);
        if(Validador.validarPassword(password)){
            System.out.println("Password no valido");
            sc.close();
            System.exit(1);
        }

        System.out.println("\n[REGISTRO OK - SEGURO]");

        System.out.println("username=" + username);

        System.out.println("edad=" + age);

        System.out.println("email=" + email);

        //No imprimimos datos sensibles como la contraseña
        //System.out.println("password=" + password);



        sc.close();

    }

    private static String prompt(String texto, int longitud){
        System.out.print(texto);
        String respuesta = sc.nextLine();
        if(respuesta.length() > longitud){
            respuesta = respuesta.substring(0, longitud);
        }
        return respuesta;
    }

    static class Validador{
        private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_]{3,50}$");
        private static final Pattern EMAIL = Pattern.compile("^[^\\s@]{2,64}@[A-Za-z0-9.-]{1,68}\\.[A-Za-z0-9]{2,187}$");

        public static boolean validarUsername(String username){
            return USERNAME.matcher(username).matches();
        }

        public static int validarEdad(String edad){
            int edadInt = 0;
            try{
                edadInt = Integer.parseInt(edad);
                if(edadInt < 18 || edadInt > 80){
                    edadInt = 0;
                }
            }catch(NumberFormatException e){
                System.out.println("[LOG] Edad no valida");
            }
            return edadInt;
        }

        public static boolean validarCorreo(String email){
            return EMAIL.matcher(email).matches();
        }

        public static boolean validarPassword(String password){
            if(password.length() < 8){
                return false;
            }
            char[] letras = password.toCharArray();
            boolean validarMayuscula = false;
            boolean validarMinuscula = false;
            boolean validarNumero = false;
            boolean validarSimbolo = false;
            for(int i = 0; i < letras.length && !(validarMayuscula && validarMinuscula && validarNumero && validarSimbolo); i++){
                char letra = letras[i];
                if (Character.isLowerCase(letra)) {
                    validarMinuscula = true;
                }else if (Character.isUpperCase(letra)) {
                    validarMayuscula = true;
                }else if (Character.isDigit(letra)) {
                    validarNumero = true;
                }else {
                    validarSimbolo = true;
                }
            }
            return (validarMayuscula && validarMinuscula && validarNumero && validarSimbolo);
        }
    }
}