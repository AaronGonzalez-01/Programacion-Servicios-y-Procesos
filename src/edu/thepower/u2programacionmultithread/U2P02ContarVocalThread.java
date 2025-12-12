package edu.thepower.u2programacionmultithread;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class U2P02ContarVocalThread implements Runnable {

    private char vocal;
    private String archivo;
    private static final String[] VOCALES = {"a","e","i","o","u"};
    private static String salida = "salida";
    private static final String EXTENCIONTXT = ".txt";
    private static final Map<Character, Character> VOCALESTILDE;

    static {
        VOCALESTILDE = new HashMap<>();
        VOCALESTILDE.put('a','á');
        VOCALESTILDE.put('e','é');
        VOCALESTILDE.put('i','í');
        VOCALESTILDE.put('o','ó');
        VOCALESTILDE.put('u','ú');
    }

    public U2P02ContarVocalThread(char vocal, String archivo, String salida) {
        this.vocal = vocal;
        this.archivo = archivo;
        this.salida = salida;
    }
    @Override
    public void run() {
        int contador = 0;

        System.out.println("[" + Thread.currentThread().getName() + "] Iniciando thread "  + vocal);

        try (BufferedReader in = new BufferedReader(new FileReader(archivo))) {

            String line;
            while ((line = in.readLine()) != null) {
                line = line.toLowerCase();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == vocal || line.charAt(i) == VOCALESTILDE.get(vocal)) {
                        contador++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No existe el archivo: " + archivo);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error en lectuera del archivo: " + archivo);
            throw new RuntimeException(e);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(salida + vocal + EXTENCIONTXT))){
            bw.write(String.valueOf(contador));
        } catch (IOException e) {
            System.err.println("Error en lectuera del archivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        List<Thread> hilos = new ArrayList();

        final String ARCHIVO = "./resources/vocales.txt";
        final String SALIDA = "./salida/";

        File f = new File(salida);
        if (f.mkdir()) {
            System.out.println("Directorio creado correctamente");

        } else {
            System.out.println("Directorio ya existe");
            for(File a :  f.listFiles()) {
                a.delete();
            }

        }

        for (char v : VOCALESTILDE.keySet()){
            Thread t = new Thread(new U2P02ContarVocalThread(v, ARCHIVO, SALIDA));
            hilos.add(t);
            t.start();
        }

        for (Thread hilo : hilos){
            try {
                hilo.join();
            } catch (InterruptedException e) {
                System.err.println("Error en lectuera del archivo: " + e.getMessage());
            }
        }

        int contadorTotal = 0;

        for (int i = 0; i < VOCALES.length; i++) {

            try {
                BufferedReader br = new BufferedReader(new FileReader(salida + VOCALES[i] + EXTENCIONTXT));
                int numero = Integer.parseInt(br.readLine());
                contadorTotal += numero;
                System.out.println(VOCALES[i] + ": " + numero);
                br.close();
            }catch (NumberFormatException e) {
                System.out.println("El archivo" + salida + VOCALES[i] + EXTENCIONTXT+ " no contiene un número: " + e.getMessage());
            }catch (FileNotFoundException e) {
                System.err.println("Error al encontrar el archivo " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error de lectura " + e.getMessage());
            }
        }
        System.out.println("Total de vocales contadas "+contadorTotal);
    }
}
