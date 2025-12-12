package edu.thepower.extras;

import java.lang.reflect.Array;
import java.util.*;

public class ReasignacionPuestos {
    private static String[] nombres = {"Genesis", "Pablo", "Luisa", "Alex",
            "Sergio. G", "Mario", "Astrid", "Esteban", "Victor", "Claudia", "Sergio. M", "Marcos",
            "David", "Sebastián", "Aarón", "Johan"};
    private static List<String> alumnos = Arrays.asList(nombres);
    private static List<Integer> puestos = new ArrayList<>();
    private static Map<Integer, String> alumnosPuestos = new TreeMap<>();
    private static final int MAXALUMNOS = 16;


    public static void main(String[] args) {
        for (int i = 1; i <= MAXALUMNOS; i++) {
            puestos.add(i);
        }
        System.out.println("Reasignando puestos");
        Collections.shuffle(alumnos);
        Collections.shuffle(puestos);
        System.out.println("Resultados: ");

        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < MAXALUMNOS; i++) {
            System.out.print("El puesto para el alumno " + alumnos.get(i) + " es... (pulse enter)");
            sc.nextLine();
            System.out.println(puestos.get(i));
            alumnosPuestos.put(puestos.get(i), alumnos.get(i));
        }
        System.out.println("Sorteo finalizado");
        System.out.println("Resultados finales: ");
        for (Map.Entry<Integer, String> entry : alumnosPuestos.entrySet()) {
            System.out.println( entry.getValue() + ": " + entry.getKey());
        }
    }
}
