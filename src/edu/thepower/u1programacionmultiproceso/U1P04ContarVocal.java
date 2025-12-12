package edu.thepower.u1programacionmultiproceso;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class U1P04ContarVocal {

    private static final Map<Character, Character> VOCALES;

    static {
        VOCALES = new HashMap();
        VOCALES.put('a','á');
        VOCALES.put('e','é');
        VOCALES.put('i','í');
        VOCALES.put('o','ó');
        VOCALES.put('u','ú');
    }

    public static void main(String[] args) {
        U1P04ContarVocal v= new U1P04ContarVocal();
        v.contarVocales(args[0].charAt(0), args[1]);

    }

    private void contarVocales(char letra, String archivo) {
        int contador = 0;


        try (BufferedReader in = new BufferedReader(new FileReader(archivo))) {

            String line;
            while ((line = in.readLine()) != null) {
                line = line.toLowerCase();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == letra || line.charAt(i) == VOCALES.get(letra)) {
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

        System.out.println(contador);
    }
}